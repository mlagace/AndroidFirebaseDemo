package ca.legacy.firebasedemo.models;
/**
 * Created by matthewlagace on 14-08-08.
 */
public class ChatMessage {
    private String message;
    private String user;
    private String target;
    private String createdAt;

    public ChatMessage(String message, String user, String target, String createdAt, String room) {
        this.message = message;
        this.user = user;
        this.target = target;
        this.createdAt = createdAt;
    }

    public ChatMessage(String message, String user, String createdAt) {
        this.message = message;
        this.user = user;
        this.createdAt = createdAt;
    }

    private ChatMessage() {

    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public String getTarget() {
        return target;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
