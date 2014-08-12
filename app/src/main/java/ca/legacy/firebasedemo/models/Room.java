package ca.legacy.firebasedemo.models;

/**
 * Created by matthewlagace on 14-08-07.
 */
public class Room {
    private String name;
    private String createdBy;
    private boolean isPrivate = false;
    private String originalName;

    public Room(String name, String createdBy, boolean isPrivate) {
        this.name      = name;
        this.createdBy = createdBy;
        this.isPrivate = isPrivate;
    }

    private Room() {
    }


    public String getName() {
        return name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getOriginalName() { return originalName; }

    public boolean getIsPrivate() { return isPrivate; }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginalName(String name) {
        this.originalName = name;
    }
}
