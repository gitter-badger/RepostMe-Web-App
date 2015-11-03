package ru.intcode.repostme.webapp.controllers;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;
import com.showvars.fugaframework.templates.TemplateNotFoundException;
import com.showvars.fugaframework.templates.TemplateRenderException;

public class MainController extends Controller {

    public static Response index(Context ctx) throws TemplateNotFoundException, TemplateRenderException {
        return ok(view(ctx, "index.html"));
    }
}
