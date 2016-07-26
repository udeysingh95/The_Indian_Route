package com.example.udeys.theindianroute;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.udeys.theindianroute.adapters.commentsAdapter;
import com.example.udeys.theindianroute.fragments.HomeFragment;
import com.example.udeys.theindianroute.helperClasses.comments;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gitesh on 22-06-2016.
 */
public class CommentActivity extends AppCompatActivity {
    Button post_comment;
    EditText write_comment;
    TextView user;
    commentsAdapter commentsAdapter;
    ListView commnetslists;
    String post_id, username;
    ImageView post_image, pp;
    ImageButton btn_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        post_id = getIntent().getStringExtra("post_id");
        username = getIntent().getStringExtra("username");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.back_bar);
        setSupportActionBar(myToolbar);
        post_comment = (Button) findViewById(R.id.post_comment);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        write_comment = (EditText) findViewById(R.id.write_comment);
        requestComments();

        commnetslists = (ListView) findViewById(R.id.comments_lists);

        commentsAdapter = new commentsAdapter(getApplicationContext(), R.layout.commentrowlayout, post_id);

        commnetslists.setAdapter(commentsAdapter);
        commnetslists.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String co = write_comment.getText().toString();
                write_comment.setText("");
                comments comments = new comments(co);
                commentsAdapter.add(comments);
                commentsAdapter.notifyDataSetChanged();


                try {
                    RequestParams params = new RequestParams();
                    params.put("post_id", post_id);
                    params.put("user_id", HomeFragment.user_id);
                    params.put("comment", co);
                    AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
                    client.get("http://indianroute.roms4all.com/post_comment.php", params, new TextHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String res) {
                                    Log.d("success", "" + res);

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
                commentNotification(HomeFragment.user_id, post_id);

            }
        });
    }



    public void requestComments() {
        try {
            RequestParams params = new RequestParams();
            params.put("post_id", post_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.post("http://indianroute.roms4all.com/fetch_comment.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            decodeCommentsJSON(res);

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

    public void decodeCommentsJSON(String res) {
        try {
            JSONArray jArr = new JSONArray(res);
            String commentersUsername, comment;
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                comment = obj.getString("comment");
                comments Comments = new comments(comment);
                commentsAdapter.add(Comments);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void commentNotification(String user_id, String post_id) {
        try {
            RequestParams params = new RequestParams();
            params.put("user_id", user_id);
            params.put("post_id", post_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/comment_push_notification.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            Log.d("on success", "" + res);

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

}
