package com.example.udeys.theindianroute;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by udeys on 5/3/2016.
 */

public class Login extends Activity implements View.OnClickListener{
    Button log , facebook_dummy;
    EditText username, passsword;
    String user, pass;
    LoginButton loginButton;
    TextView create_acc, f_pass;


    private CallbackManager callbackManager = null;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                Toast.makeText(getApplicationContext(), profile.getName().toString(), Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(), MenuActivity.class);
                String name = profile.getName().toString();
                String userId = profile.getId().toString();
                in.putExtra("NAME", userId);
                startActivity(in);
            }

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);
        username = (EditText) findViewById(R.id.username);
        passsword = (EditText) findViewById(R.id.password);
        facebook_dummy = (Button) findViewById(R.id.facebook);
        log = (Button) findViewById(R.id.login);
        create_acc = (TextView) findViewById(R.id.create);
        f_pass = (TextView) findViewById(R.id.pass);



        log.setOnClickListener(this);
        create_acc.setOnClickListener(this);
        f_pass.setOnClickListener(this);



        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);

        facebook_dummy.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Making Json Array request
     */
    private void makeJsonArrayReq() {

        try {
            /*
            * Asynchttpclient library
            * */
            AsyncHttpClient client = new AsyncHttpClient();
            /*
            * Bind Parameters
            * */
            RequestParams params = new RequestParams();
            try {
                params.put("username", user);
                params.put("password", pass);

            } catch (Exception e) {
                Toast.makeText(Login.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            client.post("http://indianroute.roms4all.com/login.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            //called when response HTTP status is "200"
                            Toast.makeText(Login.this, "success", Toast.LENGTH_SHORT).show();
                            Log.e("success", res);
                            
                            decodeJson(res);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Toast.makeText(Login.this, res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            Toast.makeText(Login.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * decode json array here which is coming as response from server
    * */
    private void decodeJson(String result) {
        String name = null;
        String user_id = null;
        try {
            JSONArray jArr = new JSONArray(result);
            JSONObject obj = jArr.getJSONObject(0);
            name = obj.getString("username");
            user_id = obj.getString("user_id");
            if (user_id == null) {
                Toast.makeText(Login.this, "user not found", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.clear();
                    ed.putString("user_id", user_id);
                    ed.putString("username", name);
                    ed.commit();

                    Intent i = new Intent(this, MenuActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "sp failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login:
                user = username.getText().toString().trim();
                pass = passsword.getText().toString().trim();
                makeJsonArrayReq();
                break;
            case R.id.facebook:
                loginButton.performClick();
                break;
            case R.id.create:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
                break;
            case R.id.pass:
                break;
            default:
        }
    }
}
