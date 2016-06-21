package com.example.udeys.theindianroute;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;

import cz.msebera.android.httpclient.Header;

public class PostForm extends Activity implements View.OnClickListener {

    String story, checkin;
    EditText s, c;
    String username;
    byte[] byteArray;
    Bitmap bmp;
    String filename;
    File i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
        username = sp.getString("username", "udeysingh95");
        setContentView(R.layout.activity_post_form);
        s = (EditText) findViewById(R.id.post_story);
        c = (EditText) findViewById(R.id.post_checkin);
        Button push_post = (Button) findViewById(R.id.push_post);
        filename = getIntent().getStringExtra("post_image");
        push_post.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        story = s.getText().toString();
        checkin = c.getText().toString();
        Toast.makeText(this,username,Toast.LENGTH_LONG).show();
        Toast.makeText(this,story,Toast.LENGTH_LONG).show();
        Toast.makeText(this,checkin,Toast.LENGTH_LONG).show();
        i = get();
        Toast.makeText(this,"i'm here",Toast.LENGTH_LONG).show();
        pushPost();
    }

    public void pushPost(){
        try {
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            /*
            * Bind parameters here
            * */
            RequestParams params = new RequestParams();
            try {
                params.put("username",username);
                params.put("check_in",checkin);
                params.put("story", story);
                params.put("picture",i);
            } catch (Exception e) {
                Toast.makeText(this, "1" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            client.post("http://indianroute.roms4all.com/post.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            Toast.makeText(PostForm.this, "2" + res, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplication(), MenuActivity.class);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Toast.makeText(PostForm.this, "3" + statusCode + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );


        } catch (Exception e) {
            Toast.makeText(PostForm.this, "4" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private File get(){

        File sd = Environment.getExternalStorageDirectory();
        File location = new File(sd.getAbsolutePath() + "/TheIndianRoute");
        location.mkdir();
        return new File(location, filename);
    }

}
