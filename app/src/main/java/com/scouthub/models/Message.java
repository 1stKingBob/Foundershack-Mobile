package com.scouthub.models;

public class Message {
    private String id;
    private String text;
    private String timestamp;
    private boolean isSent; // true if sent by current user, false if received
    private String senderId;
    private String senderName;

    public Message() {}

    public Message(String id, String text, String timestamp, boolean isSent, String senderId, String senderName) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.isSent = isSent;
        this.senderId = senderId;
        this.senderName = senderName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
