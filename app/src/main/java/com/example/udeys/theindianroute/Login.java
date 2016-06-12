package com.example.udeys.theindianroute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.appindexing.AppIndex;
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

public class Login extends Activity {
    Button log;
    EditText username, passsword;
    String user, pass;

    private CallbackManager callbackManager = null;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                Toast.makeText(getApplicationContext(), profile.getName().toString(), Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(), Profile.class);
                String name = profile.getName().toString();
                String userId = profile.getId().toString();
                in.putExtra("NAME", userId);
                startActivity(in);
            }
            /*
            GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                    loginResult.getAccessToken(),
                    //AccessToken.getCurrentAccessToken(),
                    "//friends",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Intent intent = new Intent(getApplicationContext(),MyApplication.class);
                            try {
                                //Toast.makeText(getApplicationContext(),"sending friend list",Toast.LENGTH_SHORT).show();
                                JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                intent.putExtra("jsondata", rawName.toString());
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();*/
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
        log = (Button) findViewById(R.id.login);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString().trim();
                pass = passsword.getText().toString().trim();
                makeJsonArrayReq();

            }
        });

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
            * Asynchttpclient libary
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
                            decodeJson(res);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Toast.makeText(Login.this, "" + res, Toast.LENGTH_SHORT).show();
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
        try {
            JSONArray jArr = new JSONArray(result);
            String name = null;
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                name = obj.getString("username");
            }
            Toast.makeText(Login.this, "username : " + name, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
