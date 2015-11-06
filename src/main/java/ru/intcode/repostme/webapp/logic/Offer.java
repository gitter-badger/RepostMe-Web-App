package ru.intcode.repostme.webapp.logic;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sql2o.Connection;

public class Offer {

    private long id;
    private String name;
    private String description;
    private String fullDescription;
    private double discFrom;
    private double discTo;
    private long amount;
    private String pic;
    private int kuponAmount;
    private long aid;
    private Timestamp untill;
    private long catId;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public double getDiscFrom() {
        return discFrom;
    }

    public double getDiscTo() {
        return discTo;
    }

    public long getAmount() {
        return amount;
    }

    public String getPic() {
        return pic;
    }

    public int getKuponAmount() {
        return kuponAmount;
    }

    public long getAid() {
        return aid;
    }

    public Timestamp getUntill() {
        return untill;
    }

    public long getCatId() {
        return catId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public void setDiscFrom(double discFrom) {
        this.discFrom = discFrom;
    }

    public void setDiscTo(double discTo) {
        this.discTo = discTo;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setKuponAmount(int kuponAmount) {
        this.kuponAmount = kuponAmount;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public void setUntill(Timestamp untill) {
        this.untill = untill;
    }

    public void setCatId(long catId) {
        this.catId = catId;
    }

    public static final String selectAllQuery
            = "SELECT * FROM offers;";

    public static final String selectAllByCatQuery
            = "SELECT * FROM offers WHERE catId = :catId";

    public static Map<Category, List<Offer>> selectAll(Database db) {
        Map<Category, List<Offer>> map = new HashMap<>();

        try (Connection c = db.getSql2o().open()) {
            List<Category> cats = c.createQuery(Category.selectAllQuery).executeAndFetch(Category.class);

            for (Category cat : cats) {
                List<Offer> offers = c.createQuery(Offer.selectAllByCatQuery).addParameter("catId", cat.getId()).executeAndFetch(Offer.class);

                map.put(cat, offers);
            }
        }

        return map;
    }

    public static Map<Category, List<Offer>> selectAllByCat(Database db, Category category) {
        Map<Category, List<Offer>> map = new HashMap<>();

        try (Connection c = db.getSql2o().open()) {
            List<Category> cats = Arrays.asList(category);

            for (Category cat : cats) {
                List<Offer> offers = c.createQuery(Offer.selectAllByCatQuery).addParameter("catId", cat.getId()).executeAndFetch(Offer.class);

                map.put(cat, offers);
            }
        }
        return map;
    }
}
