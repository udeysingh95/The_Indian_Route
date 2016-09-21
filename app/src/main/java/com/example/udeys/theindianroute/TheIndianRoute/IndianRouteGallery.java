package com.example.udeys.theindianroute.TheIndianRoute;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.ViewPostActivity;
import com.example.udeys.theindianroute.adapters.ImageAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gitesh on 24-06-2016.
 */
public class IndianRouteGallery extends Fragment {
    View view;
    GridView gridView;
    ArrayList<String> imagePath;
    ArrayList<Integer> post_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.indianroute_gallery, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String id = String.valueOf(post_id.get(position));

                Intent intent = new Intent(getActivity(), ViewPostActivity.class);
                intent.putExtra("post_id", id);
                startActivity(intent);
            }
        });
        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initValues();
    }

    private void initValues() {
        try {

            RequestParams params = new RequestParams();
            params.put("user_id", "22");
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://theindianroute.net/fetch_gallery.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            //Toast.makeText(getActivity(), "success" +res, Toast.LENGTH_LONG).show();
                            //Log.e("try","success");
                            decodeJson(res);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            //Log.e("fail",res);
                        }
                    }
            );
        } catch (Exception e) {
            Log.e("Catch", e.toString());
        }
    }

    private void decodeJson(String result) {
        try {
            //Toast.makeText(getActivity() , "decodeJson",Toast.LENGTH_SHORT).show();
            JSONArray jArr = new JSONArray(result);
            imagePath = new ArrayList<>();
            post_id = new ArrayList<>();
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                String path = obj.getString("picture");
                int id = obj.getInt("id");
                imagePath.add(path);
                post_id.add(id);
                //Log.e("path", path);
                //gridView.setAdapter(new ImageAdapter(getActivity(), imagePath));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e("catch decode", e.toString());
        }
        gridView.setAdapter(new ImageAdapter(getActivity(), imagePath));
    }
}
