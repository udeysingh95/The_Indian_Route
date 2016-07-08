package com.example.udeys.theindianroute.helperClasses;

/**
 * Created by Gitesh on 07-07-2016.
 */
public class Event {
    String heading,pic;

    public Event(String heading, String pic) {
        this.setHeading(heading);
        this.setPic(pic);
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
