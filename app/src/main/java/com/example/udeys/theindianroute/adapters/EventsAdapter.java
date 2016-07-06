package com.example.udeys.theindianroute.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.helperClasses.notification;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gitesh on 06-07-2016.
 */
public class EventsAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public EventsAdapter(Context context, int resource) {
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
        Picasso.with(getContext()).load(Notification.getPp()).into(notificationHolder.i1);
        Picasso.with(getContext()).load(Notification.getPost_pic()).into(notificationHolder.i2);
        return row;

    }

    static class notificationHolder {
        TextView t1, t2, t3;
        ImageView i1, i2;
    }
}
