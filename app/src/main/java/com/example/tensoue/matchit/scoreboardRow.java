package com.example.tensoue.matchit;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;

public class scoreboardRow {
    private String userUUID;
    private String username;
    private int highestScore;
    private String profilePicURL;

    public scoreboardRow(){}

    public scoreboardRow(String userUUID, String username, int highestScore, String profilePicURL) {
        this.userUUID = userUUID;
        this.username = username;
        this.highestScore = highestScore;
        this.profilePicURL = profilePicURL;
    }

    public scoreboardRow(String username, int highestScore, String profilePicURL) {
        this.username = username;
        this.highestScore = highestScore;
        this.profilePicURL = profilePicURL;
    }

    public scoreboardRow(String userUUID, String username, String profilePicURL) {
        this.userUUID = userUUID;
        this.username = username;
        this.profilePicURL = profilePicURL;
    }

    public void isHigherScore(int score){
        if (score  > this.highestScore )
            this.setHighestScore(score);
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }
}
