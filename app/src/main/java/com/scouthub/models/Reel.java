package com.scouthub.models;

public class Reel {
    private String id;
    private String caption;
    private String authorName;
    private String videoUrl;
    private int duration; // in seconds
    private boolean liked;

    public Reel() {}

    public Reel(String id, String caption, String authorName, String videoUrl, int duration, boolean liked) {
        this.id = id;
        this.caption = caption;
        this.authorName = authorName;
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.liked = liked;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public boolean isLiked() { return liked; }
    public void setLiked(boolean liked) { this.liked = liked; }
}
