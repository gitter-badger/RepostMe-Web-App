package ru.intcode.repostme.webapp.logic;

import org.sql2o.Connection;

public class User {

    private long id;
    private String email;
    private String vkId;
    private String vkToken;

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getVkId() {
        return vkId;
    }

    public String getVkToken() {
        return vkToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    public void setVkToken(String vkToken) {
        this.vkToken = vkToken;
    }

    public static final String selectUserByVkIdQuery
            = "SELECT * FROM users WHERE vkId = :vkId;";

    public static User selectUserByVkId(Database db, String vkId) {
        try (Connection c = db.getSql2o().open()) {
            return c.createQuery(User.selectUserByVkIdQuery).addParameter("vkId", vkId).executeAndFetchFirst(User.class);
        }
    }

    public static final String insertUserQuery
            = "INSERT INTO users VALUES (null, :email, :vkId, :vkToken);";

    public static User insertUser(Database db, User user) {
        try (Connection c = db.getSql2o().open()) {
            long id = c.createQuery(User.insertUserQuery).bind(user).executeUpdate().getKey(long.class);
            user.id = id;
            return user;
        }
    }
}
