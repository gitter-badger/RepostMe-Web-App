package ru.intcode.repostme.webapp;

import com.showvars.fugaframework.FugaApp;
import ru.intcode.repostme.webapp.logic.Database;

public class RepostMeWebApp extends FugaApp {

    @Override
    public void prepare() {        
        getConfiguration().loadFromResources("config/default.config");
        getConfiguration().load("./app.properties");
        
        getRouter().loadFromResources("routes/main.routes");
        
        putObject("db", new Database(this));
    }
    
    public static void main(String[] args) throws Exception {
        new RepostMeWebApp().start();
    }
    
}
