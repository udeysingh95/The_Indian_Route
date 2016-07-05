package com.example.udeys.theindianroute.helperClasses;

/**
 * Created by Gitesh on 22-06-2016.
 */
public class comments {
    String username, comments;

    public comments(String comments) {
        this.setComments(comments);

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
