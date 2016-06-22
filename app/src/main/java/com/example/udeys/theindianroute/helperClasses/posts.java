package com.example.udeys.theindianroute.helperClasses;

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
    String user_id;
    int comment;
    int reaction;
    int state;

    public posts(String username,
                 String story,
                 String pictue,
                 String check_in,
                 String userProfilePicture,
                 String post_id,
                 int reaction,
                 int state, int comment, String user_id) {
        this.setUsername(username);
        this.setStory(story);
        this.setPictue(pictue);
        this.setCheck_in(check_in);
        this.setUserProfilePicture(userProfilePicture);
        this.setPost_id(post_id);
        this.setReaction(reaction);
        this.setstate(state);
        this.setComment(comment);
        this.setUser_id(user_id);

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

    public void setstate(int state) {
        this.state = state;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
