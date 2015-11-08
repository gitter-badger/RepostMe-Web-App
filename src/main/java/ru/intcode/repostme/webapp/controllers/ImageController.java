package ru.intcode.repostme.webapp.controllers;

import com.showvars.fugaframework.configuration.Configuration;
import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;
import com.showvars.fugaframework.foundation.controllers.DefaultController;
import java.io.File;

public class ImageController extends Controller {

    public static Response get(Context ctx, String name) {

        Configuration config = ctx.getApp().getConfiguration();
        File img = new File(config.get("fuga.resources.path", "") + "/assets/offers/" + name + ".jpg");

        if (!img.isFile() || !img.canRead()) {
            return DefaultController.notFound(ctx);
        }

        return ok(img).setContentType("image/jpeg");

    }
}
