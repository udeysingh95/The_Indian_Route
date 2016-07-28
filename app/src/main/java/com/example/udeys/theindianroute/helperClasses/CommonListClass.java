package com.example.udeys.theindianroute.helperClasses;

/**
 * Created by Gitesh on 22-06-2016.
 */
public class CommonListClass {
    String username, comments,profilepic,status;

    public CommonListClass(String comments, String profilepic, String username) {
        this.setComments(comments);
        this.setProfilepic(profilepic);
        this.setUsername(username);

    }

    public CommonListClass(String comments){
        this.setComments(comments);
    }

    public CommonListClass(String username,String profilepic,String status,boolean nothing){
        this.setUsername(username);
        this.setProfilepic(profilepic);
        this.setStatus(status);
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

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
