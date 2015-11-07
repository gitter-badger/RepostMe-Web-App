package ru.intcode.repostme.webapp;

import com.showvars.fugaframework.FugaApp;
import java.util.concurrent.TimeUnit;
import ru.intcode.repostme.webapp.logic.Database;
import ru.intcode.repostme.webapp.services.QueueService;
import ru.intcode.repostme.webapp.services.RepostCheckService;

public class RepostMeWebApp extends FugaApp {

    @Override
    public void prepare() {
        getConfiguration().loadFromResources("config/default.config");
        getConfiguration().load("./app.properties");

        getRouter().loadFromResources("routes/main.routes");

        putObject("db", new Database(this));

        QueueService queueService = new QueueService();

        putObject("queue", queueService);

        getServiceManager().registerService(queueService, 0, 250, TimeUnit.MILLISECONDS);
        getServiceManager().registerService(new RepostCheckService(this), 0, 15, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws Exception {
        new RepostMeWebApp().start();
    }

}
