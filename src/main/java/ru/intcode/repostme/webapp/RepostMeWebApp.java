package ru.intcode.repostme.webapp;

import com.showvars.fugaframework.FugaApp;

public class RepostMeWebApp extends FugaApp {

    @Override
    public void prepare() {        
        getConfiguration().loadFromResoures("config/default.config");
        
        getRouter().loadFromResources("routes/main.routes");
        
    }
    
    public static void main(String[] args) throws Exception {
        new RepostMeWebApp().start();
    }
    
}
