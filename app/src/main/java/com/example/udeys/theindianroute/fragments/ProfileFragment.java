package com.example.udeys.theindianroute.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.R;
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

/**
 * Created by udeys on 6/17/2016.
 */
public class ProfileFragment extends Fragment {

    View view;
    TextView uname , posts;
    ImageView iv;
    SharedPreferences sp;
    String username;
    String user_id,userid;
    int no_of_post = 0;
    GridView gridView;
    ArrayList<String> imagePath;
    Button follow_status;
    int uid;
    boolean res = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profilefragment, container, false);

        try {
            username = getArguments().getString("user_name");
            user_id = String.valueOf(getArguments().getInt("user_id"));
            Log.e("Profile try", "success");
            res = true;
        } catch (Exception e) {
            Log.e("catch", e.toString());
        }
        if (res == false) {
            sp = this.getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
            username = sp.getString("username", null);
            user_id = sp.getString("user_id", null);
        }
        userid = "2";
        uname = (TextView)view.findViewById(R.id.username);
        posts = (TextView) view.findViewById(R.id.number_of_post);
        gridView = (GridView) view.findViewById(R.id.gallery_images);
        follow_status = (Button)view.findViewById(R.id.follow_stauts);
        imagePath = new ArrayList<>();
        iv = (ImageView) view.findViewById(R.id.PF);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //if(userid.contentEquals(user_id)){
        if (res == false) {
            initValue(user_id);    //fetch profile
            initValues();  //fetch posts
            follow_status.setText("Edit Your Profile");
        }
        else{
            initValue(user_id);    //fetch profile
            initValues();  //fetch posts
            follow_status.setText("Follow");
        }
    }

    private void initValue(String user_id) {
        try {
            RequestParams params = new RequestParams();
            params.put("user_id", user_id);
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

    private void initValues(){
        try {
            RequestParams params = new RequestParams();
            params.put("user_id",user_id);
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
            JSONArray jArr = new JSONArray(result);


            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                String path = obj.getString("picture");
                imagePath.add(path);
                //Log.e("path", path);
                //gridView.setAdapter(new ImageAdapter(getActivity(), imagePath));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        gridView.setAdapter(new ImageAdapter(getActivity(), imagePath));
    }

    private void decodeNewJson(String result) {
        try {
            JSONArray jArr = new JSONArray(result);
            String userprofilePicture;

            JSONObject obj = jArr.getJSONObject(0);
            userprofilePicture = obj.getString("userProfilePicture");
            no_of_post = Integer.valueOf(obj.getString("post_count"));
            uname.setText(username);
            posts.setText(String.valueOf(no_of_post));

            //may throw some error

            Picasso.with(getActivity().getApplicationContext()).load(userprofilePicture).resize(250, 300).centerCrop().into(iv);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        gridView.setAdapter(new ImageAdapter(getActivity(), imagePath));
    }
}
