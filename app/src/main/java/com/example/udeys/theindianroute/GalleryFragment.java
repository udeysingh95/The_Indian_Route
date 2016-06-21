package com.example.udeys.theindianroute;

/**
 * Created by udeys on 6/17/2016.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class GalleryFragment extends Fragment {

    View view;
    GridLayout gl;
    String username , user_id;
    SharedPreferences sp;
    ImageView iv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.galleryfragment, container, false);
        sp = this.getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        username = sp.getString("username", null);
        user_id = sp.getString("user_id", null);
        gl = (GridLayout) view.findViewById(R.id.gallery_container);
        initGrid();
        return view;
    }

    private void initGrid(){
        try {
            RequestParams params = new RequestParams();
            params.put("user_id",user_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/fetch_gallery.php",params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
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
        String picture_path;
        try {
            JSONArray jArr = new JSONArray(result);



            for(int count = 0 ; count < jArr.length() ; count++) {
                JSONObject obj = jArr.getJSONObject(count);
                picture_path = obj.getString("picture");

                iv = new ImageView(getActivity().getApplicationContext());

                iv.setMaxHeight(100);
                iv.setMaxWidth(100);

                Picasso.with(getActivity().getApplicationContext())
                        .load(picture_path).resize(100, 100).centerCrop()
                        .into(iv);
                gl.addView(iv);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
