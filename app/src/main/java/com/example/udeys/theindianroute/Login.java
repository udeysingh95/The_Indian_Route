package com.example.udeys.theindianroute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by udeys on 5/3/2016.
 */

public class Login extends Activity {

    private CallbackManager callbackManager = null;
    private FacebookCallback <LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if(profile != null){
                Toast.makeText(getApplicationContext(),profile.getName().toString(),Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(),MyApplication.class);
                String name = profile.getName().toString();
                in.putExtra("NAME",name);
                startActivity(in);
            }/*
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager,callback);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
