package com.scouthub.models;

public class CalendarEvent {
    private String id;
    private String title;
    private String type; // "trial", "training", "match", "meeting"
    private String date;
    private String time;
    private String location;

    public CalendarEvent() {}

    public CalendarEvent(String id, String title, String type, String date, String time, String location) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
