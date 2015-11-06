package ru.intcode.repostme.webapp.controllers;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;
import com.showvars.fugaframework.templates.TemplateNotFoundException;
import com.showvars.fugaframework.templates.TemplateRenderException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import ru.intcode.repostme.webapp.logic.Category;
import ru.intcode.repostme.webapp.logic.Database;
import ru.intcode.repostme.webapp.logic.Offer;

public class MainController extends Controller {

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
            return notFound();
        }

        MainData data = new MainData();
        
        data.all = Offer.selectAllByCat(db, cat);
        data.curCat = cat.getId();
                
        return ok(view(ctx, "index.html", data));
    }

    public static class MainData {
        public Map<Category, List<Offer>> all;

        public long curCat = 0;
    }
}
