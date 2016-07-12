package com.example.udeys.theindianroute.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.helperClasses.notification;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Gitesh on 30-06-2016.
 */
public class notificationAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public notificationAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final notificationHolder notificationHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_notification, parent, false);
            notificationHolder = new notificationHolder();
            notificationHolder.t1 = (TextView) row.findViewById(R.id.username);
            notificationHolder.t2 = (TextView) row.findViewById(R.id.notification_details);
            notificationHolder.t3 = (TextView) row.findViewById(R.id.time);
            notificationHolder.i1 = (ImageView) row.findViewById(R.id.notifyuserProfilePicture);
            notificationHolder.i2 = (ImageView) row.findViewById(R.id.notifypostPicture);
            row.setTag(notificationHolder);
        } else {
            notificationHolder = (notificationHolder) row.getTag();
        }

        final notification Notification = (notification) this.getItem(position);
        notificationHolder.t1.setText(Notification.getUsername());
        notificationHolder.t2.setText(Notification.getNotify());
        notificationHolder.t3.setText(post_time(Notification.getTimeStamp()));
        Picasso.with(getContext()).load(Notification.getPp()).into(notificationHolder.i1);
        Picasso.with(getContext()).load(Notification.getPost_pic()).into(notificationHolder.i2);
        return row;

    }

    static class notificationHolder {
        TextView t1, t2, t3;
        ImageView i1, i2;
    }

    public String post_time(String post_time) {
        String time;
        String start;
        start = post_time;
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
        Log.e("timestamp", timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Date d1 = null;
        Date d2 = null;
        long diffHours = 0;

        try {
            d1 = format.parse(start);
            d2 = format.parse(timeStamp);
        } catch (Exception e) {
            Log.e("date catch", e.toString());
        }
        // Get msec from each, and subtract.
        long diff = 0;
        try {
            diff = d2.getTime() - d1.getTime();
        } catch (Exception e) {
            Log.e("catch", e.toString());
        }

        if (diff < 60) {
            time = Long.toString(diff / 1000 % 60);  //<60 print this value.
            time += " seconds ago";
            Log.d("sec", "" + time);
        } else if (diff < 3600) {
            time = Long.toString(diff / (60 * 1000) % 60); // <3600 print this value
            time += " minutes ago";
            Log.d("min", "" + time);
        } else if (diff < 86400) {
            time = Long.toString(diff / (60 * 60 * 1000));
            time += " hours ago";
            Log.d("hr", "" + time);
        } else {
            time = Long.toString((diff / (60 * 60 * 1000)) / 24);
            time += " days ago";
            Log.d("day", "" + time);
        }

        return time;
    }

}
