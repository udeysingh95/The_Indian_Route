package com.example.udeys.theindianroute.helperClasses;

/**
 * Created by Gitesh on 05-07-2016.
 */
public class Notification {
    String username;
    String pp;
    String post_pic;
    String notify;
    String time;


    public Notification(String username, String pp, String post_pic) {
        this.setUsername(username);
        this.setPp(pp);
        this.setPost_pic(post_pic);

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
