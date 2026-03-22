package com.scouthub.models;

public class UserProfile {
    private String name;
    private String sport;
    private String position;
    private String age;
    private String height;
    private String weight;
    private String club;
    private String location;
    private String contact;
    private String profileImageUrl;

    public UserProfile() {}

    public UserProfile(String name, String sport, String position, String age, String height, String weight, String club, String location, String contact, String profileImageUrl) {
        this.name = name;
        this.sport = sport;
        this.position = position;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.club = club;
        this.location = location;
        this.contact = contact;
        this.profileImageUrl = profileImageUrl;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getClub() { return club; }
    public void setClub(String club) { this.club = club; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}
