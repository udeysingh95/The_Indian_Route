package com.example.udeys.theindianroute.helperClasses;

/**
 * Created by Gitesh on 05-07-2016.
 */
public class notification {
    String username;
    String pp;
    String post_pic;
    String notify;
    String post_id;
    String action;
    String follower_id;
    String timeStamp;


    public notification(String username, String pp, String post_pic, String notify, String post_id, String action, String follower_id, String timeStamp) {
        this.setUsername(username);
        this.setPp(pp);
        this.setPost_pic(post_pic);
        this.setNotify(notify);
        this.setPost_id(post_id);
        this.setAction(action);
        this.setFollower_id(follower_id);
        this.setTimeStamp(timeStamp);


    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getPost_pic() {
        return post_pic;
    }

    public void setPost_pic(String post_pic) {
        this.post_pic = post_pic;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(String follower_id) {
        this.follower_id = follower_id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

