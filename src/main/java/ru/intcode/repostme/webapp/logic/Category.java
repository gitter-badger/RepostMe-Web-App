package ru.intcode.repostme.webapp.logic;

import java.util.List;
import org.sql2o.Connection;

public class Category {

    private long id;
    private String name;
    private String link;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static final String selectById
            = "SELECT * FROM categories WHERE id = :id;";
    
    public static final String selectAllQuery
            = "SELECT * FROM categories;";

    public static final String selectByLinkQuery
            = "SELECT * FROM categories WHERE link = :link;";

    public static Category selectById(Database db, long id) {
        try (Connection c = db.getSql2o().open()) {
            return c.createQuery(Category.selectById).addParameter("id", id).executeAndFetchFirst(Category.class);
        }
    }
    
    public static List<Category> selectAll(Database db) {
        try (Connection c = db.getSql2o().open()) {
            return c.createQuery(Category.selectAllQuery).executeAndFetch(Category.class);
        }
    }

    public static Category selectByLink(Database db, String link) {
        try (Connection c = db.getSql2o().open()) {
            return c.createQuery(Category.selectByLinkQuery).addParameter("link", link).executeAndFetchFirst(Category.class);
        }
    }

}
