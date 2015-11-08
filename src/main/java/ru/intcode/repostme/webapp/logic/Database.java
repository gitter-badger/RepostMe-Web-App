package ru.intcode.repostme.webapp.logic;

import com.showvars.fugaframework.FugaApp;
import com.showvars.fugaframework.configuration.Configuration;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class Database {

    private final Configuration config;
    private final Sql2o sql2o;

    public Database(FugaApp app) {
        this.config = app.getConfiguration();
        this.sql2o = new Sql2o(config.get("app.db.url", "jdbc:mysql://localhost:3306/repostme"), config.get("app.db.user", "user"), config.get("app.db.password", "user"));
    }

    protected Sql2o getSql2o() {
        return sql2o;
    }
    
    public static final String countKuponUsesQuery
            = "SELECT COUNT(*) AS count FROM kupons WHERE oid = :oid;";
    
    public SQLCount countKuponUses(Offer offer) {
        try (Connection c = sql2o.open()) {
            return c.createQuery(countKuponUsesQuery).addParameter("oid", offer.getId()).executeAndFetchFirst(SQLCount.class);
        }
    }
    
    public static class SQLCount {
        public long count;
    }
}
