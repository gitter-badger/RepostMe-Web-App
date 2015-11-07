package ru.intcode.repostme.webapp.controllers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;
import com.showvars.fugaframework.sessions.Session;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ru.intcode.repostme.webapp.logic.Database;
import ru.intcode.repostme.webapp.logic.User;

public class AuthController extends Controller {

    private static final Logger log = LogManager.getLogger(AuthController.class);

    public static Response oauth(Context ctx) {
        String redirectUrl = Urls.that(ctx, "vk/oauthcallback");
        return redirect("https://oauth.vk.com/authorize?client_id=5136825&redirect_uri=" + redirectUrl + "&display=page&response_type=code&v=5.40&scope=friends,email,offline");
    }

    public static Response oauthCallBack(Context ctx) {
        List<String> codes = ctx.getRequest().getQuery().get("code");

        if (codes == null || codes.isEmpty()) {
            return ok("Ошибка авторизации");
        }

        HttpResponse<JsonNode> hr;
        try {
            hr = Unirest.get("https://oauth.vk.com/access_token")
                    .queryString("client_id", "5136825")
                    .queryString("client_secret", "XIfbjYsZz12i54OLmK3l")
                    .queryString("redirect_uri", Urls.that(ctx, "vk/oauthcallback"))
                    .queryString("code", codes.get(0)).asJson();
        } catch (UnirestException ex) {
            log.catching(ex);
            return ok("Ошибка авторизации");
        }

        JSONObject jso = hr.getBody().getObject();

        if(jso.has("error")) {
            return ok(jso.getString("error") + ": " + jso.getString("error_description"));
        }
        
        String vkId = jso.getInt("user_id") + "";

        Database db = (Database) ctx.getApp().getObject("db");
        User user = User.selectUserByVkId(db, vkId);

        if (user == null) {
            user = new User();
            user.setEmail(jso.getString("email"));
            user.setVkId(vkId);
            user.setVkToken(jso.getString("access_token"));
            
            User.insertUser(db, user);
        }

        Session session = ctx.getSession();
        
        session.put("user", user);
        
        return redirect(Urls.that(ctx));
    }
}
