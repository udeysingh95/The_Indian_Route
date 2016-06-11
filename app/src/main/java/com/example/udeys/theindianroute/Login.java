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
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

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
        /*
         * Bind Parameters
         * */
        final List<Pair<String, String>> params = new ArrayList<Pair<String, String>>() {{
            add(new Pair<>("username", user));
            add(new Pair<>("password", pass));
        }};

        /**
        * fuel library is used for sending and receiving response from server
        * */
        Fuel.post("http://indianroute.roms4all.com/login.php", params).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {
                updateUI(null, s);
            }

            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {

                updateUI(fuelError, null);
            }

        });

    }


    private void decodeJson(String result) {
        try {
            JSONArray jArr = new JSONArray(result);
            String name = null,image=null;
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                name = obj.getString("username");
                image = obj.getString("image");
            }
            /*
            * Toast for testing response from server
            * Remove to manipulate JSON here
            * */
            Toast.makeText(Login.this, "username : " + name+" image: "+image, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void updateUI(final FuelError error, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (error == null) {
                    decodeJson(result);
                } else {
                    Log.e("Error", "error: " + error.getException().getMessage());
                    Toast.makeText(Login.this, "" + error.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
