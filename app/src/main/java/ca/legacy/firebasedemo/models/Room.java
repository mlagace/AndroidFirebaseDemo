package ca.legacy.firebasedemo.models;

import java.util.Map;

/**
 * Created by matthewlagace on 14-08-07.
 */
public class Room {
    private String name;
    private int userLimit = 0;
    private String createdBy;
    private boolean isPrivate = false;
    private String password;
    private Map<String, User> users;
    private Map<String, ChatMessage> messages;

    public Room(String name, int userLimit, String createdBy, boolean isPrivate, String password) {
        this.name      = name;
        this.userLimit = userLimit;
        this.createdBy = createdBy;
        this.isPrivate = isPrivate;
        this.password  = password;
    }

    public Room(String name, String createdBy) {
        this.name      = name;
        this.createdBy = createdBy;
    }

    private Room() {
    }


    public String getName() {
        return name;
    }

    public int getUserLimit() {
        return userLimit;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, User> getUsers() { return users; }

    public Map<String, ChatMessage> getMessages() { return messages; }
}
