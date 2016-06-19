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
    String post_id;
    int reaction;
    int state;

    public posts(String username,
                 String story,
                 String pictue,
                 String check_in,
                 String userProfilePicture,
                 String post_id,
                 int reaction,
                 int state) {
        this.setUsername(username);
        this.setStory(story);
        this.setPictue(pictue);
        this.setCheck_in(check_in);
        this.setUserProfilePicture(userProfilePicture);
        this.setPost_id(post_id);
        this.setReaction(reaction);
        this.setstate(state);

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

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public int getReaction() {
        return reaction;
    }

    public void setReaction(int reaction) {
        this.reaction = reaction;
    }
    public int getstate() {
        return state;
    }

    public void setstate(int like_state) {
        this.state = like_state;
    }
}
