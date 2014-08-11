package ca.legacy.firebasedemo.models;

/**
 * Created by matthewlagace on 14-08-05.
 */
public class User {
    private String username;
    private String deviceId;
    private boolean isAdmin = false;
    private boolean isOnline = false;

    public User(String username, String deviceId, boolean isAdmin) {
        this.username = username;
        this.deviceId = deviceId;
        this.isAdmin = isAdmin;
    }

    public User(String username) {
        this.username = username;
    }

    private User() {

    }

    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
    public boolean getIsAdmin() { return isAdmin; }
    public boolean getIsOnline() { return isOnline; }
    public String getUsername() { return username; }
    public String getDeviceId() {
        return deviceId;
    }
}
