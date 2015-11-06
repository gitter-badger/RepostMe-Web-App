package ru.intcode.repostme.webapp.logic;

import com.showvars.fugaframework.FugaApp;
import com.showvars.fugaframework.configuration.Configuration;
import org.sql2o.Sql2o;

public class Database {

    private final Configuration config;
    private final Sql2o sql2o;

    public Database(FugaApp app) {
        this.config = app.getConfiguration();
        this.sql2o = new Sql2o(config.get("app.db.url", "jdbc:mysql://localhost:3306/repostme"), "user", "user");
    }

    protected Sql2o getSql2o() {
        return sql2o;
    }
}
