package com.example.udeys.theindianroute.TheIndianRoute;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.adapters.EventsAdapter;
import com.example.udeys.theindianroute.adapters.notificationAdapter;
import com.example.udeys.theindianroute.helperClasses.Event;
import com.example.udeys.theindianroute.helperClasses.notification;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gitesh on 24-06-2016.
 */
public class Events extends Fragment {
    View view;
    ListView notificationList;
    SharedPreferences sp;
   EventsAdapter eventsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notificationfragment, container, false);
        notificationList = (ListView) view.findViewById(R.id.notification_list);
        eventsAdapter = new EventsAdapter(getActivity(), R.layout.single_notification);
        notificationList.setAdapter(eventsAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetch_events();
    }

    private void fetch_events() {
        try {
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.post("http://indianroute.roms4all.com/fetch_event.php", new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            extractEvents(res);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Toast.makeText(getActivity(), "" + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void extractEvents(String result) {
        try {
            JSONArray jArr = new JSONArray(result);
            String pic,header;
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                pic = obj.getString("picture");
                header = obj.getString("header");
                Event event = new Event(header,pic);
                eventsAdapter.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
