package ca.legacy.firebasedemo.models;

/**
 * Created by matthewlagace on 14-08-05.
 */
public class User {
    private String username;
    private String deviceId;

    public User(String username, String deviceId) {
        this.username = username;
        this.deviceId = deviceId;
    }

    public User(String username) {
        this.username = username;
    }

    private User() {

    }

    public String getUsername() { return username; }
    public String getDeviceId() {
        return deviceId;
    }
}
