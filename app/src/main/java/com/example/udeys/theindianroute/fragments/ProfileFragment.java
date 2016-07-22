package com.example.udeys.theindianroute.fragments;

/**
 * Created by udeys on 6/17/2016.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.ViewPostActivity;
import com.example.udeys.theindianroute.adapters.ImageAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ProfileFragment extends Fragment {

    View view;
    TextView uname, posts;
    ImageView iv;
    SharedPreferences sp;
    String username;
    String user_id;
    int no_of_post = 0;
    GridView gridView;
    ArrayList<String> imagePath;
    Button follow_status;
    boolean res = false;
    String follow_s;
    String u_id;
    ArrayList<String> post_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profilefragment, container, false);
        sp = this.getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

        try {
            username = getArguments().getString("user_name");
            user_id = getArguments().getString("user_id");
            u_id = sp.getString("user_id", null);
            res = true;
        } catch (Exception e) {
            Log.e("catch", e.toString());
        }
        if (!res) {

            username = sp.getString("username", null);
            user_id = sp.getString("user_id", null);
        }

        uname = (TextView) view.findViewById(R.id.username);
        posts = (TextView) view.findViewById(R.id.number_of_post);
        gridView = (GridView) view.findViewById(R.id.gallery_images);
        follow_status = (Button) view.findViewById(R.id.follow_status);
        imagePath = new ArrayList<>();
        iv = (ImageView) view.findViewById(R.id.PF);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initValue(user_id);    //fetch profile
        initValues();  //fetch Posts

    }

    private void initValue(String user_id) {
        try {
            u_id = sp.getString("user_id", null);
            RequestParams params = new RequestParams();

            params.put("user_id", user_id);
            params.put("u_id", u_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/fetch_profile.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            //Toast.makeText(getActivity(), "success" +res, Toast.LENGTH_LONG).show();

                            decodeNewJson(res);
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

    private void initValues() {
        try {
            RequestParams params = new RequestParams();
            params.put("user_id", user_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/fetch_gallery.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            //Toast.makeText(getActivity(), "success" +res, Toast.LENGTH_LONG).show();

                            decodeJson(res);
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

    private void decodeJson(String result) {
        try {
            post_id = new ArrayList<>();
            JSONArray jArr = new JSONArray(result);


            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                String path = obj.getString("picture");
                String id = obj.getString("id");
                imagePath.add(path);
                post_id.add(id);

                // Log.e("path", id);
                //gridView.setAdapter(new ImageAdapter(getActivity(), imagePath));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e("error", e.toString());
        }
        gridView.setAdapter(new ImageAdapter(getActivity(), imagePath));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("position", String.valueOf(i));
                Intent intent = new Intent(getActivity(), ViewPostActivity.class);
                intent.putExtra("post_id", post_id.get(i));
                startActivity(intent);
            }
        });
    }

    private void decodeNewJson(String result) {
        try {
            JSONArray jArr = new JSONArray(result);
            String userprofilePicture;

            JSONObject obj = jArr.getJSONObject(0);
            userprofilePicture = obj.getString("userProfilePicture");
            no_of_post = Integer.valueOf(obj.getString("post_count"));
            follow_s = obj.getString("following");

            if (user_id.matches(sp.getString("user_id", null))) {
                follow_status.setText("edit your profile");

            } else if (follow_s.contentEquals("1")) {
                follow_status.setText("following");
            } else {
                follow_status.setText("follow");

            }
            uname.setText(username);
            posts.setText(String.valueOf(no_of_post));

            follow_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    follow_button();
                }
            });

            Picasso.with(getActivity().getApplicationContext()).load(userprofilePicture).resize(250, 300).centerCrop().into(iv);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        gridView.setAdapter(new ImageAdapter(getActivity(), imagePath));
    }

    private void follow_button() {
        try {

            RequestParams params = new RequestParams();
            String status = follow_status.getText().toString();
            params.put("user_id", u_id);
            params.put("profile_id", user_id);
            params.put("status", status);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/follow_unfollow.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            decode_status(res);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Toast.makeText(getActivity(), "button crash" + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void decode_status(String result) {
        try {
            JSONArray jArr = new JSONArray(result);
            JSONObject obj = jArr.getJSONObject(0);
            follow_status.setText(obj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
