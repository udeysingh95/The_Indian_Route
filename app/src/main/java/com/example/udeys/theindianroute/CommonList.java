package com.example.udeys.theindianroute;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.udeys.theindianroute.adapters.CommonListAdapter;
import com.example.udeys.theindianroute.helperClasses.CommonListClass;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CommonList extends Activity {
    String listType;
    ListView listView;
    TextView title;
    String user_id, profile_id,post_id,postType;
    ImageButton btn_back;
    CommonListAdapter commonListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_list);
        profile_id = getIntent().getStringExtra("profile_id");
        listType = getIntent().getStringExtra("list_type");
        post_id = getIntent().getStringExtra("post_id");
        postType = getIntent().getStringExtra("type");
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
        user_id = sp.getString("user_id", null);
        listView = (ListView)findViewById(R.id.common_list);
        title = (TextView)findViewById(R.id.title);
        title.setText("FOLLOWERS");
        if (listType.contentEquals("likes")) {
            fetch_likers();
        } else {
            fetch_followers();
        }

        commonListAdapter = new CommonListAdapter(this, R.layout.commonlistlayout);
        listView.setAdapter(commonListAdapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void fetch_likers() {
        try {
            RequestParams params = new RequestParams();
            params.put("post_id", post_id);
            params.put("type", postType);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.post("http://indianroute.roms4all.com/fetch_reaction.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            fetch(res);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            //Toast.makeText(getApplicationContext(), "" + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetch_followers() {
        try {
            RequestParams params = new RequestParams();
            params.put("profile_id", profile_id);
            params.put("user_id", user_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.post("http://indianroute.roms4all.com/fetch_follower.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            fetch(res);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            //Toast.makeText(getApplicationContext(), "" + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetch(String res) {
        try {
            JSONArray jArr = new JSONArray(res);
            String userprofilePicture,username,status;
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                username = obj.getString("username");
                userprofilePicture = obj.getString("userProfilePicture");
                status = obj.getString("following");
                CommonListClass commonListClass = new CommonListClass(username,userprofilePicture,status,true);
                commonListAdapter.add(commonListClass);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
