package com.example.udeys.theindianroute;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Setting extends Activity  implements View.OnClickListener{

    ImageButton cancel;
    EditText website, email, phone;
    SwitchCompat notification;
    LinearLayout l1;
    Button save;
    String id = "22" , gender;
    TextView title , name , username , male , female;
    SharedPreferences sp;
    RoundedImageView pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sp = this.getSharedPreferences("user_details", MODE_PRIVATE);
        id = sp.getString("user_id", null);

        title = (TextView) findViewById(R.id.title);
        title.setText("Settings");

        cancel = (ImageButton) findViewById(R.id.btn_back);
        save = (Button) findViewById(R.id.ic_save);

        pp = (RoundedImageView) findViewById(R.id.profile_picture);

        notification = (SwitchCompat) findViewById(R.id.not_button);
        name = (TextView)findViewById(R.id.name);
        username = (TextView)findViewById(R.id.username);

        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        male = (TextView) findViewById(R.id.male);
        female = (TextView) findViewById(R.id.female);
        website = (EditText)findViewById(R.id.website);
        l1 = (LinearLayout) findViewById(R.id.logout);

        getDetails();
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        l1.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_back:
                finish();
                break;
            case R.id.ic_save:
                save_details();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.male:
                male.setBackgroundResource(R.drawable.login_edittext);
                male.setTextColor(Color.parseColor("#ccffffff"));
                female.setBackgroundResource(0);
                female.setTextColor(Color.parseColor("#888888"));
                gender = "M";
                break;
            case R.id.female:
                male.setBackgroundResource(0);
                male.setTextColor(Color.parseColor("#888888"));
                female.setBackgroundResource(R.drawable.login_edittext);
                female.setTextColor(Color.parseColor("#ccffffff"));
                gender = "F";
                break;
        }
    }

    public void logout(){
        sp.edit().remove("user_id").apply();
        sp.edit().remove("username").apply();

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
        params.put("gender", gender);
        params.put("phone", phone.getText());
        params.put("notification", notifications);
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get("http://indianroute.roms4all.com/update_setting.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
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

            if(obj.getString("gender").equals("M")){
                male.setBackgroundResource(R.drawable.login_edittext);
                male.setTextColor(Color.parseColor("#ccffffff"));
                female.setBackgroundResource(0);
                female.setTextColor(Color.parseColor("#888888"));
                gender = "M";
            }
            else{
                gender = "F";
            }

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

            Picasso.with(getApplicationContext()).load(userprofilePicture).resize(300, 300).centerCrop().into(pp);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
