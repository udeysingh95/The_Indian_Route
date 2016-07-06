package com.example.udeys.theindianroute.helperClasses;

/**
 * Created by Gitesh on 05-07-2016.
 */
public class notification {
    String username;
    String pp;
    String post_pic;
    String notify;
    String time;
    String post_id;


    public notification(String username, String pp, String post_pic, String notify,String post_id) {
        this.setUsername(username);
        this.setPp(pp);
        this.setPost_pic(post_pic);
        this.setNotify(notify);
        this.setPost_id(post_id);

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

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

}

