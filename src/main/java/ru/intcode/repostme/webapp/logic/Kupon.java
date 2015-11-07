package ru.intcode.repostme.webapp.logic;

import java.util.List;
import org.sql2o.Connection;
import ru.intcode.repostme.webapp.controllers.MainController;

public class Kupon {

    private long id;
    private long uid;
    private long oid;
    private String kupon;
    private float discount;
    private int uses;

    public long getId() {
        return id;
    }

    public long getUid() {
        return uid;
    }

    public long getOid() {
        return oid;
    }

    public String getKupon() {
        return kupon;
    }

    public float getDiscount() {
        return discount;
    }

    public int getUses() {
        return uses;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public void setKupon(String kupon) {
        this.kupon = kupon;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public static final String selectByUidAndOidQuery
            = "SELECT * FROM kupons WHERE uid = :uid AND oid = :oid;";

    public static List<Kupon> selectByUidAndOid(Database db, long uid, long oid) {
        try (Connection c = db.getSql2o().open()) {
            return c.createQuery(selectByUidAndOidQuery)
                    .addParameter("uid", uid)
                    .addParameter("oid", oid)
                    .executeAndFetch(Kupon.class);
        }
    }

    private static final String insertCuponSQL
            = "INSERT INTO kupons VALUES (:id, :uid, :oid, :kupon, :discount, :uses);";
    private static final String getDiscSQL
            = "SELECT discFrom, discTo, kuponAmount FROM offers WHERE id=:oid;";

    public static void insertKupon(Database db, long uid, long oid, float discPer) {
        try (Connection c = db.getSql2o().open()) {
            FloatPair pair = c.createQuery(getDiscSQL).addParameter("oid", oid).executeAndFetchFirst(FloatPair.class);
            float discount;
            if (pair.discFrom == 0) {
                discount = pair.discTo;
            } else {
                discount = pair.discFrom + (pair.discTo - pair.discFrom) * discPer;
            }
            long id = getNextKuponId(db);
            String kupon = MainController.kuponId.encode(id);
            c.createQuery(insertCuponSQL)
                    .addParameter("id", id)
                    .addParameter("uid", uid)
                    .addParameter("oid", oid)
                    .addParameter("kupon", kupon)
                    .addParameter("discount", discount)
                    .addParameter("uses", pair.kuponAmount)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static final String getNextKuponIdSQL
            = "SELECT MAX(id)+1 FROM kupons;";

    private static long getNextKuponId(Database db) {
        try (Connection c = db.getSql2o().open()) {
            return c.createQuery(getNextKuponIdSQL).executeAndFetchFirst(Long.class);
        }
    }
    
    static class FloatPair {
        float discFrom;
        float discTo;
        long kuponAmount;
    }
}
