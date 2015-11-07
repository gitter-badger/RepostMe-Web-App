package ru.intcode.repostme.webapp.services;

import com.showvars.fugaframework.FugaApp;
import com.showvars.fugaframework.services.Service;
import java.sql.Timestamp;
import java.util.List;
import ru.intcode.repostme.webapp.logic.Database;
import ru.intcode.repostme.webapp.logic.Repost;

public class RepostCheckService extends Service {

    private final FugaApp app;
    private final Database db;
    
    public RepostCheckService(FugaApp app) {
        this.app = app;
        this.db = (Database) app.getObject("db");
    }

    @Override
    public void run() {
        List<Repost> reposts = Repost.selectAll(db);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        for(Repost repost : reposts) {
            if(repost.getVerified() == 0) {
                
            } else {
                if(repost.getDate().after(now)) {
                    Repost.delete(db, repost.getId());
                    // Create kupon and notify user!
                }
            }
            
        }
    }

    
    private void checkRepost() {
        
    }
}
