package ru.intcode.repostme.webapp.controllers;

import com.google.gson.Gson;
import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;
import com.showvars.fugaframework.foundation.controllers.DefaultController;
import com.showvars.fugaframework.sessions.Session;
import com.showvars.fugaframework.templates.TemplateNotFoundException;
import com.showvars.fugaframework.templates.TemplateRenderException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.hashids.Hashids;
import ru.intcode.repostme.webapp.logic.Category;
import ru.intcode.repostme.webapp.logic.Database;
import ru.intcode.repostme.webapp.logic.Kupon;
import ru.intcode.repostme.webapp.logic.KuponDescription;
import ru.intcode.repostme.webapp.logic.Offer;
import ru.intcode.repostme.webapp.logic.Repost;
import ru.intcode.repostme.webapp.logic.User;

public class MainController extends Controller {

    public static final Hashids hid = new Hashids("kSpdK0mG7lwVxOF1", 4);
    public static final Hashids rid = new Hashids("NRpgpmjwx0V4yQwJ", 6);
    public static final Hashids kuponId = new Hashids("NRFg66cwx05BiQwJ", 8, "0123456789abcdefghigklmnopqrstuvwxyz");

    private static final Gson gson = new Gson();

    public static Response index(Context ctx) throws TemplateNotFoundException, TemplateRenderException {
        Database db = (Database) ctx.getApp().getObject("db");

        MainData data = new MainData();

        data.all = Offer.selectAll(db);

        return ok(view(ctx, "index.html", data));
    }

    public static Response category(Context ctx, String category) throws TemplateNotFoundException, TemplateRenderException {
        Database db = (Database) ctx.getApp().getObject("db");

        Category cat = Category.selectByLink(db, category);

        if (cat == null) {
            return DefaultController.notFound(ctx);
        }

        MainData data = new MainData();

        data.all = Offer.selectAllByCat(db, cat);
        data.curCat = cat.getId();
        data.curCatName = cat.getName();

        return ok(view(ctx, "index.html", data));
    }

    public static Response offer(Context ctx, String hash) throws TemplateNotFoundException, TemplateRenderException {
        long id = hid.decode(hash)[0];

        Database db = (Database) ctx.getApp().getObject("db");

        Offer offer = Offer.selectById(db, id);

        if (offer == null) {
            return DefaultController.notFound(ctx);
        }

        MainData data = new MainData();

        data.curCat = offer.getCatId();
        data.offer = offer;

        Category cat = Category.selectById(db, offer.getCatId());

        data.curCatName = cat == null ? "" : cat.getName();
        data.kuponNumber = db.countKuponUses(offer).count;

        return ok(view(ctx, "offer.html", data));
    }

    public static Response getKupon(Context ctx, String hash) throws TemplateNotFoundException, TemplateRenderException {
        long id = hid.decode(hash)[0];

        Database db = (Database) ctx.getApp().getObject("db");

        Offer offer = Offer.selectById(db, id);

        if (offer == null) {
            return DefaultController.notFound(ctx);
        }

        MainData data = new MainData();

        data.curCat = offer.getCatId();
        data.offer = offer;

        Category cat = Category.selectById(db, offer.getCatId());

        data.curCatName = cat == null ? "" : cat.getName();
        data.kuponNumber = db.countKuponUses(offer).count;

        return ok(view(ctx, "kupon.html", data));
    }
    
    
    public static Response kupon(Context ctx, String hash) throws TemplateNotFoundException, TemplateRenderException {
        long id = kuponId.decode(hash)[0];
        Database db = (Database) ctx.getApp().getObject("db");
        KuponData data = new KuponData();
        User user = (User) ctx.getSession().get("user");
        if (user == null) {
            return DefaultController.notFound(ctx);
        }
        KuponDescription kd = KuponDescription.selectByUid(db, user.getId());
        data.hid = kd.kupon;
        data.discount = kd.discount;
        data.name = kd.name;
        data.description = kd.description;
        data.fullDescription = kd.fullDescription;
        data.b64qr = KuponDescription.getQR(kd.kupon);
        return ok(view(ctx, "printKupon.html", data));
    }
    

    public static Response repost(Context ctx) {
        Session session = ctx.getSession();

        User user = (User) session.get("user");

        if (user == null) {
            return ok("{\"success\": false, \"status\": 10}").setContentType("application/json");
        }

        Database db = (Database) ctx.getApp().getObject("db");

        String json = ctx.getRequest().getContent().toString(Charset.forName("UTF-8"));
        RepostRequest rr = gson.fromJson(json, RepostRequest.class);

        List<Kupon> kupons = Kupon.selectByUidAndOid(db, user.getId(), rr.oid);

        if (kupons != null && !kupons.isEmpty()) {
            return ok("{\"success\": false, \"status\": 20}").setContentType("application/json");
        }

        List<Repost> reposts = Repost.selectByUidAndOid(db, user.getId(), rr.oid);

        boolean flag = false;

        if (reposts != null && !reposts.isEmpty()) {
            for (Repost repost : reposts) {
                if (repost.getVerified() == 0) {
                    Repost.delete(db, repost.getId());
                } else {
                    flag = true;
                }
            }
        }

        if (flag) {
            return ok("{\"success\": false, \"status\": 30}").setContentType("application/json");
        }

        String repostId = "!" + rid.encode(Repost.insert(db, user.getId(), rr.oid));

        return ok("{\"success\": true, \"rid\": \"" + repostId + "\"}").setContentType("application/json");
    }

    public static Response mykupons(Context ctx) throws TemplateNotFoundException, TemplateRenderException {
        Session session = ctx.getSession();
        User user = (User) session.get("user");

        if (user == null) {
            return redirect(Urls.that(ctx));
        }

        Database db = (Database) ctx.getApp().getObject("db");

        MainData data = new MainData();

        data.curCat = -1;

        List<Kupon.KuponTriple> kupons = Kupon.selectByUid(db, user.getId());

        data.kupons = kupons;
        

        return ok(view(ctx, "mykupons.html", data));
    }

    public static class RepostRequest {

        public long oid;
    }

    public static class MainData {

        public Map<Category, List<Offer>> all;

        public List<Kupon.KuponTriple> kupons;

        public long curCat = 0;
        public String curCatName = "";

        public Offer offer;
        public long kuponNumber;

        public Hashids hid = MainController.hid;
    }
    
    public static class KuponData {
        public String hid;
        public float discount;
        public String name;
        public String description;
        public String fullDescription;
        public String b64qr;
    }

}
