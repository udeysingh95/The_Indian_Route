package com.example.udeys.theindianroute;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.udeys.theindianroute.adapters.ImageAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Setting extends Activity  implements View.OnClickListener{

    ImageView cancel , save;
    ImageView pp;
    EditText name, username, website, email, gender, phone;
    Switch notification;
    LinearLayout l1;
    String id = "22";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sp = this.getSharedPreferences("user_details", MODE_PRIVATE);
        id = sp.getString("user_id", null);

        cancel = (ImageView) findViewById(R.id.ic_cancel);
        save = (ImageView) findViewById(R.id.ic_save);
        pp = (ImageView) findViewById(R.id.profile_picture);
        notification = (Switch) findViewById(R.id.not_button);
        name = (EditText)findViewById(R.id.name);
        username = (EditText)findViewById(R.id.username);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        gender = (EditText)findViewById(R.id.gender);
        website = (EditText)findViewById(R.id.website);
        l1 = (LinearLayout) findViewById(R.id.logout);

        getDetails();
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        l1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.ic_cancel:
                Intent i = new Intent(this , MenuActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.ic_save:
                save_details();
                break;
            case R.id.logout:
                logout();
                break;
        }
    }


    public void logout(){
        sp.edit().remove("user_id").commit();
        sp.edit().remove("username").commit();

        Intent i = new Intent(this , Splash.class);
        startActivity(i);
        finish();

    }

    public void save_details(){
        String notifications;
        if(notification.isChecked()){
            notifications = "1";
        }
        else {
            notifications = "0";
        }
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("name", name.getText());
        params.put("email", email.getText());
        params.put("website", website.getText());
        params.put("gender", gender.getText());
        params.put("phone", phone.getText());
        params.put("notification", notifications);
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get("http://indianroute.roms4all.com/update_setting.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        Intent i = new Intent(getApplicationContext() , MenuActivity.class);
                        startActivity(i);
                        finish();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        Toast.makeText(getApplicationContext(), "can not save!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
    public  void getDetails(){
                RequestParams params = new RequestParams();
                params.put("id", id);
                AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
                client.get("http://indianroute.roms4all.com/setting.php", params, new TextHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String res) {

                                decodeJson(res);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Toast.makeText(getApplicationContext() , "Failed initialization", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

    }

    private void decodeJson(String result) {
        try {
            JSONArray jArr = new JSONArray(result);
            String userprofilePicture;

            JSONObject obj = jArr.getJSONObject(0);
            userprofilePicture = obj.getString("userProfilePicture");


            name.setText(obj.getString("name"));
            email.setText(obj.getString("email"));
            phone.setText(obj.getString("phone"));
            gender.setText(obj.getString("gender"));
            username.setText(obj.getString("username"));
            website.setText(obj.getString("username"));
            int not = Integer.parseInt(obj.getString("notification"));
            if(not == 1){
                notification.setChecked(true);
            }
            else{
                notification.setChecked(false);
            }

            //may throw some error

            Picasso.with(getApplicationContext()).load(userprofilePicture).resize(250, 300).centerCrop().into(pp);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
