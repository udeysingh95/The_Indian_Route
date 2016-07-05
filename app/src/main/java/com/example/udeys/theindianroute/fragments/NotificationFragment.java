package com.example.udeys.theindianroute.fragments;

/**
 * Created by udeys on 6/17/2016.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.adapters.notificationAdapter;
import com.example.udeys.theindianroute.helperClasses.notification;

public class NotificationFragment extends Fragment {

    View view;
    ListView notificationList;
    notificationAdapter notAdapter;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.notificationfragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notificationList = (ListView) view.findViewById(R.id.notification_list);
        notAdapter = new notificationAdapter(getActivity(), R.layout.single_notification);
        notificationList.setAdapter(notAdapter);
        String username,pp,post;
        username = "udeysingh95";
        pp = "http://indianroute.roms4all.com/uploads/users_profile_pictures/pp.jpg";
        post = "http://indianroute.roms4all.com/uploads/users_posts_images/1.jpg";
        notification notification = new notification(username,pp,post);
        notAdapter.add(notification);
    }


}
