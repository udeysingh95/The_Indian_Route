package com.example.udeys.theindianroute.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.helperClasses.Event;
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
        final EventsHolder eventsHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_notification, parent, false);
            eventsHolder = new EventsHolder();
            eventsHolder.t1 = (TextView) row.findViewById(R.id.username);
            eventsHolder.t2 = (TextView) row.findViewById(R.id.notification_details);
            eventsHolder.t3 = (TextView) row.findViewById(R.id.time);
            eventsHolder.i1 = (ImageView) row.findViewById(R.id.notifyuserProfilePicture);
            eventsHolder.i2 = (ImageView) row.findViewById(R.id.notifypostPicture);
            row.setTag(eventsHolder);
        } else {
            eventsHolder = (EventsHolder) row.getTag();
        }

        final Event event =  (Event)this.getItem(position);
        eventsHolder.t1.setText(event.getHeading());
        Picasso.with(getContext()).load(event.getPic()).into(eventsHolder.i2);
        return row;

    }

    static class EventsHolder {
        TextView t1, t2, t3;
        ImageView i1, i2;
    }
}
