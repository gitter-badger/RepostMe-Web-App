package ru.intcode.repostme.webapp.logic;

import java.util.List;
import org.sql2o.Connection;

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
}
