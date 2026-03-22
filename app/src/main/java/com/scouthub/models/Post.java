package com.scouthub.models;

public class Post {
    private String id;
    private String authorName;
    private String content;
    private String type; // "trial", "highlight", "recruitment"
    private String date;
    private String imageUrl;
    private String videoUrl;

    public Post() {}

    public Post(String id, String authorName, String content, String type, String date, String imageUrl) {
        this.id = id;
        this.authorName = authorName;
        this.content = content;
        this.type = type;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
}
