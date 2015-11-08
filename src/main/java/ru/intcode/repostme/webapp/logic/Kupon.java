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
    private int friends;

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

    public int getFriends() {
        return friends;
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

    public void setFriends(int friends) {
        this.friends = friends;
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

    public static final String selectByUidQuery
            = "SELECT kupon, name, discount, uses FROM (SELECT * FROM kupons WHERE uid = :uid) AS tmp LEFT JOIN offers ON offers.id = oid;";

    public static List<KuponTriple> selectByUid(Database db, long uid) {
        try (Connection c = db.getSql2o().open()) {
            return c.createQuery(selectByUidQuery)
                    .addParameter("uid", uid)
                    .executeAndFetch(KuponTriple.class);
        }
    }

    private static final String insertCuponSQL
            = "INSERT INTO kupons VALUES (:id, :uid, :oid, :kupon, :discount, :uses, :friends);";
    private static final String getDiscSQL
            = "SELECT discFrom, discTo, kuponAmount FROM offers WHERE id=:oid;";

    public static void insertKupon(Database db, long uid, long oid, float discPer, long friends) {
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
                    .addParameter("friends", friends)
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

    public static class KuponTriple {
        public String kupon;
        public String name;
        public int uses;
        public float discount;
        
    }
}
