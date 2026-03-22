package com.scouthub.models;

public class Story {
    private String id;
    private String username;
    private String imageUrl;
    private boolean viewed;

    public Story() {}

    public Story(String id, String username, String imageUrl, boolean viewed) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.viewed = viewed;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isViewed() { return viewed; }
    public void setViewed(boolean viewed) { this.viewed = viewed; }
}
