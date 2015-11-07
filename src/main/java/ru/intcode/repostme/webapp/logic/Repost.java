package ru.intcode.repostme.webapp.logic;

import java.sql.Timestamp;
import java.util.List;
import org.sql2o.Connection;

public class Repost {

    private long id;
    private long uid;
    private long oid;
    private Timestamp date;
    private int verified;
    private int verifyCount;

    public long getId() {
        return id;
    }

    public long getUid() {
        return uid;
    }

    public long getOid() {
        return oid;
    }

    public int getVerifyCount() {
        return verifyCount;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public Timestamp getDate() {
        return date;
    }

    public int getVerified() {
        return verified;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public void setVerifyCount(int verifyCount) {
        this.verifyCount = verifyCount;
    }

    public static final String insertQuery
            = "INSERT INTO reposts VALUES (null, :uid, :oid, NOW() + INTERVAL 1 DAY, 0, 0);";

    public static final String selectAllQuery
            = "SELECT * FROM reposts;";

    public static final String selectByUidAndOidQuery
            = "SELECT * FROM reposts WHERE uid = :uid AND oid = :oid;";

    public static final String deleteQuery
            = "DELETE FROM reposts WHERE id = :id;";

    public static final String updateVerifiedQuery
            = "UPDATE reposts SET verified = 1 WHERE id = :id;";

    public static final String updateVerifyCountQuery
            = "UPDATE reposts SET verifyCount = verifyCount + 1 WHERE id = :id; ";

    public static void updateVerifyCount(Database db, long rid) {
        try (Connection c = db.getSql2o().open()) {
            c.createQuery(updateVerifyCountQuery)
                    .addParameter("id", rid).executeUpdate();
        }
    }

    public static long insert(Database db, long uid, long oid) {
        try (Connection c = db.getSql2o().open()) {
            return c.createQuery(insertQuery)
                    .addParameter("uid", uid)
                    .addParameter("oid", oid)
                    .executeUpdate().getKey(long.class);
        }
    }

    public static List<Repost> selectAll(Database db) {
        try (Connection c = db.getSql2o().open()) {
            return c.createQuery(selectAllQuery)
                    .executeAndFetch(Repost.class);
        }
    }

    public static List<Repost> selectByUidAndOid(Database db, long uid, long oid) {
        try (Connection c = db.getSql2o().open()) {
            return c.createQuery(selectByUidAndOidQuery)
                    .addParameter("uid", uid)
                    .addParameter("oid", oid)
                    .executeAndFetch(Repost.class);
        }
    }

    public static void delete(Database db, long rid) {
        try (Connection c = db.getSql2o().open()) {
            c.createQuery(deleteQuery)
                    .addParameter("id", rid)
                    .executeUpdate();
        }
    }

    public static void updateVerified(Database db, long rid) {
        try (Connection c = db.getSql2o().open()) {
            c.createQuery(updateVerifiedQuery)
                    .addParameter("id", rid)
                    .executeUpdate();
        }
    }

}
