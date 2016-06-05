package Classes;

/**
 * Created by Gitesh on 22-05-2016.
 */
public class posts {
    String username;
    byte[] postimage,profileimg;

    public byte[] getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(byte[] profileimg) {
        this.profileimg = profileimg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPostimage() {
        return postimage;
    }

    public void setPostimage(byte[] postimage) {
        this.postimage = postimage;
    }
}
