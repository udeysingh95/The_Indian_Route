package com.example.udeys.theindianroute.utils;

/**
 * Created by Gitesh on 23-05-2016.
 */
public class posts {
    String username;
    String story;
    String pictue;
    String check_in;
    String userProfilePicture;

    public posts(String username, String story, String pictue, String check_in, String userProfilePicture) {
        this.setUsername(username);
        this.setStory(story);
        this.setPictue(pictue);
        this.setCheck_in(check_in);
        this.setUserProfilePicture(userProfilePicture);
    }

    public String getUsername() {
        return username;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getPictue() {
        return pictue;
    }

    public void setPictue(String pictue) {
        this.pictue = pictue;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }
}
