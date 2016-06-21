package com.example.udeys.theindianroute.fragments;

/**
 * Created by udeys on 6/17/2016.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.utils.ImageAdapter;
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
    TextView uname , posts;
    ImageView iv;
    SharedPreferences sp;
    String username;
    String user_id;
    int no_of_post = 0;
    GridView gridView;
    ArrayList<String> imagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profilefragment, container, false);
        sp = this.getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        username = sp.getString("username", null);
        user_id = sp.getString("user_id", null);
        uname = (TextView)view.findViewById(R.id.username);
        posts = (TextView) view.findViewById(R.id.number_of_post);
        gridView = (GridView) view.findViewById(R.id.gallery_images);

        imagePath = new ArrayList<>();

        iv = (ImageView) view.findViewById(R.id.PF);

        initValue();    //fetch profile

        initValues();  //fetch posts



        return view;
    }

    private void initValue() {
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

            JSONObject obj = jArr.getJSONObject(0);

            String path = obj.getString("picture");
            imagePath.add(path);

            Log.e("path", path);


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
