package com.example.udeys.theindianroute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

import java.net.URL;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gitesh on 04-07-2016.
 */
public class ViewPostActivity extends AppCompatActivity {
    static int state;
    public String user_id;
    String posts_id, username, profile_pic, post_pic, story, check_in, reaction,no_of_comments,pt;
    com.makeramen.roundedimageview.RoundedImageView pp;
    ImageView post;
    ImageButton back_btn;
    TextView user_name, post_story, post_like, post_comment,comment_no,post_time;
    ImageView like, comment;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpost);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
        user_id = sp.getString("user_id", null);
        posts_id = getIntent().getStringExtra("post_id");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.back_bar);
        setSupportActionBar(myToolbar);
        pp = (com.makeramen.roundedimageview.RoundedImageView) findViewById(R.id.vpuserProfilePicture);
        Typeface fa = Typeface.createFromAsset(getAssets(), "SAMARN__.TTF");
        back_btn = (ImageButton) findViewById(R.id.btn_back);
        post = (ImageView) findViewById(R.id.vpuserpostimage);
        user_name = (TextView) findViewById(R.id.username);
        post_time = (TextView)findViewById(R.id.post_time);
        user_name.setTypeface(fa);
        like = (ImageView) findViewById(R.id.icon_like);
        comment = (ImageView) findViewById(R.id.icon_comment);
        post_like = (TextView) findViewById(R.id.likes);
        comment_no = (TextView)findViewById(R.id.vpcomments);
        like.setImageResource(R.drawable.not_liked);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {
                    like.setImageResource(R.drawable.liked);
                    state = 1;
                } else {
                    like.setImageResource(R.drawable.not_liked);
                    state = 0;
                }
                post_reaction(state, user_id, posts_id);
            }
        });
        post_comment = (TextView) findViewById(R.id.comments);

        comment.setImageResource(R.drawable.comment);
        post_story = (TextView) findViewById(R.id.post_story);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                intent.putExtra("post_id", posts_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        extractData();

    }


    public void extractData() {
        try {
            RequestParams params = new RequestParams();
            params.put("post_id", posts_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.post("http://theindianroute.net/view_post.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            jsonExtract(res);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Toast.makeText(getApplicationContext(), "" + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void jsonExtract(String res) {
        try {
            JSONArray jArr = new JSONArray(res);
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                username = obj.getString("username");
                profile_pic = obj.getString("picture");
                post_pic = obj.getString("post_picture");
                check_in = obj.getString("check_in");
                story = obj.getString("story");
                reaction = obj.getString("reaction_no");
                no_of_comments = obj.getString("comment");
                Log.d("comment_no", "" + no_of_comments);
                Log.d("username", "" + username);
            }
            if (profile_pic.equals("http://theindianroute.net/uploads/users_profile_pictures/"))
                Picasso.with(getApplicationContext()).load(R.drawable.dummyprofile).placeholder(R.drawable.ppplaceholder)
                        .into(pp);


            else
                Picasso.with(getApplicationContext()).load(profile_pic).into(pp);
            try {
                Picasso.with(getApplicationContext()).load(post_pic).resize(320, 240).into(post);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }

            try {
                URL url = new URL(post_pic);

                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                int imageHeight = bmp.getHeight();
                int imageWidth = bmp.getWidth();

                Log.e("image-height", String.valueOf(imageHeight));
                Log.e("image-width", String.valueOf(imageWidth));
            } catch (Exception e) {
                Log.e("catch", e.toString());
            }

            //post_comment.setText();
            post_like.setText(reaction);
            comment_no.setText(no_of_comments);
            user_name.setText(username);
            post_story.setText(story);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//
    public void post_reaction(int setstate, String user_id, String post_id) {

        try {
            RequestParams params = new RequestParams();
            params.put("state", setstate);
            params.put("user_id", user_id);
            params.put("post_id", post_id);
            Log.d("post_id", "" + post_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://theindianroute.net/post_reaction.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            decodeJson(res);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Toast.makeText(getApplicationContext(), "" + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void decodeJson(String result) {
        try {
            JSONArray jArr = new JSONArray(result);
            JSONObject obj = jArr.getJSONObject(0);
            String num = obj.getString("reaction");
            post_like.setText(num);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
