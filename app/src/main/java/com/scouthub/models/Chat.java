package com.scouthub.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Chat implements Parcelable {
    private String id;
    private String name;
    private String lastMessage;
    private String time;
    private String type; // "player", "scout", "club"
    private String profileImageUrl;

    public Chat() {}

    public Chat(String id, String name, String lastMessage, String time, String type, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.type = type;
        this.profileImageUrl = profileImageUrl;
    }

    // Parcelable constructor
    protected Chat(Parcel in) {
        id = in.readString();
        name = in.readString();
        lastMessage = in.readString();
        time = in.readString();
        type = in.readString();
        profileImageUrl = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(lastMessage);
        dest.writeString(time);
        dest.writeString(type);
        dest.writeString(profileImageUrl);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}
