package ru.intcode.repostme.webapp.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.showvars.fugaframework.FugaApp;
import com.showvars.fugaframework.services.Service;
import java.sql.Timestamp;
import java.util.List;
import org.json.JSONArray;
import ru.intcode.repostme.webapp.controllers.MainController;
import ru.intcode.repostme.webapp.logic.Database;
import ru.intcode.repostme.webapp.logic.Kupon;
import ru.intcode.repostme.webapp.logic.Repost;
import ru.intcode.repostme.webapp.logic.User;

public class RepostCheckService extends Service {

    private final FugaApp app;
    private final Database db;
    private final QueueService queue;

    public RepostCheckService(FugaApp app) {
        this.app = app;
        this.db = (Database) app.getObject("db");
        this.queue = (QueueService) app.getObject("queue");
    }

    @Override
    public void run() {
        List<Repost> reposts = Repost.selectAll(db);
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (Repost repost : reposts) {
            if (repost.getVerified() == 0) {
                queue.addTask(new VerifyTask(app, repost));
            } else {
                if (repost.getDate().after(now)) {
                    try {
                        Repost.delete(db, repost.getId());
                        
                        User u = User.selectUserByUid(db, repost.getUid());
                        if (u == null) {
                            continue;
                        }
                        
                        HttpResponse<JsonNode> hr = Unirest.get("https://api.vk.com/method/friends.get")
                                .queryString("user_id", Integer.parseInt(u.getVkId())).asJson();
                        
                        int friends = hr.getBody().getObject().getJSONArray("response").length();
                        
                        Kupon.insertKupon(db, repost.getUid(), repost.getOid(), friends > 500 ? 1 : friends / 500, friends);
                        
                    } catch (Exception ex) {
                        
                    }
                }
            }

        }
    }

    public static class VerifyTask extends Task {

        private final Database db;
        private final Repost repost;

        public VerifyTask(FugaApp app, Repost repost) {
            super(app);
            this.db = (Database) app.getObject("db");
            this.repost = repost;
        }

        @Override
        public void run() {
            User u = User.selectUserByUid(db, repost.getUid());
            if (u == null) {
                return;
            }
            try {
                HttpResponse<JsonNode> hr = Unirest.get("https://api.vk.com/method/wall.search")
                        .queryString("owner_id", u.getVkId())
                        .queryString("query", "!" + MainController.rid.encode(repost.getId()))
                        //.queryString("owners_only", "1")
                        .queryString("count", 1).asJson();

                JSONArray obj = hr.getBody().getObject().getJSONArray("response");
                if (obj.getInt(0) > 0) {
                    Repost.updateVerified(db, repost.getId());
                } else {
                    if(repost.getVerifyCount() > 3) {
                        Repost.delete(db, repost.getId());
                    } else {
                        Repost.updateVerifyCount(db, repost.getId());
                    }
                }
            } catch (UnirestException ex) {
            }
        }

    }
}
