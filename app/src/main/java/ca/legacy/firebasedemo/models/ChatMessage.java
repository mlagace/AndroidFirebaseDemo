package ca.legacy.firebasedemo.models;
/**
 * Created by matthewlagace on 14-08-08.
 */
public class ChatMessage {
    private String message;
    private String user;
    private String createdAt;

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

    public String getCreatedAt() {
        return createdAt;
    }
}
