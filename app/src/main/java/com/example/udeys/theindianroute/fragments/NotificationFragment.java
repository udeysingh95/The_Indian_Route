package com.example.udeys.theindianroute.fragments;

/**
 * Created by udeys on 6/17/2016.
 */

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.ViewPostActivity;
import com.example.udeys.theindianroute.adapters.notificationAdapter;
import com.example.udeys.theindianroute.helperClasses.notification;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NotificationFragment extends Fragment {
    FragmentTransaction ft;
    View view;
    ListView notificationList;
    notificationAdapter notAdapter;
    String user_id = "22";

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.notificationfragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetch_notification();
        notificationList = (ListView) view.findViewById(R.id.notification_list);
        notAdapter = new notificationAdapter(getActivity(), R.layout.single_notification);
        notificationList.setAdapter(notAdapter);
        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                notification obj = (notification) notificationList.getItemAtPosition(position);
                String post_id = obj.getPost_id();
                String action = obj.getAction();
                String user_name = obj.getUsername();
                String follower_id = obj.getFollower_id();
                Bundle bundle = new Bundle();
                bundle.putString("action", action);
                bundle.putString("user_id",follower_id);
                bundle.putString("post_id", post_id);
                bundle.putString("user_name", user_name);
                if (action.contentEquals("1") || action.contentEquals("2")) {
                    Intent intent = new Intent(getActivity(), ViewPostActivity.class);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                    /*ViewPostActivity ViewPostActivity = new ViewPostActivity();
                    ViewPostActivity.setArguments(bundle);
                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_1, ViewPostActivity);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();*/
                } else {
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setArguments(bundle);
                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_1, profileFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }

            }
        });
    }

    private void fetch_notification() {
        try {
            RequestParams params = new RequestParams();
            params.put("user_id", user_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.post("http://indianroute.roms4all.com/fetch_notification.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            extractNotifictaion(res);

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

    public void extractNotifictaion(String result) {
        try {
            JSONArray jArr = new JSONArray(result);
            String userprofilepic, username, message, picture, post_id, action, follower_id, timestamp;
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                userprofilepic = obj.getString("userProfilePicture");
                username = obj.getString("username");
                message = obj.getString("message");
                picture = obj.getString("picture");
                post_id = obj.getString("post_id");
                action = obj.getString("action");
                follower_id = obj.getString("user_id");
                timestamp = obj.getString("timestamp");
                notification notification = new notification(username, userprofilepic, picture, message, post_id, action, follower_id, timestamp);
                notAdapter.add(notification);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
