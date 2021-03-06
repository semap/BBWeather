package ca.aequilibrium.bbweather.utils;

public class Message {
    private String body;
    private MessageType type;

    public Message(String body, MessageType type) {
        this.body = body;
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public MessageType getType() {
        return type;
    }
}
