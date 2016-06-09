package com.example.udeys.theindianroute;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText name, uname, passowrd, repassword, email;
    Button register;
    protected static int res = 0;
    AsyncTask<Void, Void, Void> sender;
    public static boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        uname = (EditText) findViewById(R.id.username);
        passowrd = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        email = (EditText) findViewById(R.id.email);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);


    }




    @Override
    public void onClick(View v) {
        String nm, unm, pass, eml , repass ,device_token;

        nm = name.getText().toString();
        unm = uname.getText().toString();
        pass = passowrd.getText().toString();
        eml = email.getText().toString();
        repass = repassword.getText().toString();
        device_token = "hello";


        /*
        * validation goes here
        * */




        /*
        * validated data send to the server.
        * flag boolean varidable to check the response from the server.
        * */
        flag = hit_data(nm , unm , pass, eml , device_token);

        if(flag == true){
            /*
            * User is registered successfully.
            * now change the intent to the Main Activity.
            *
            * */
            Toast.makeText(getApplicationContext() , "done" , Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hit_data(final String name , final String username, final String email , final String password ,final String device_token){


        sender = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                Register(Register.this , username , name , email , password , device_token);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                sender = null;
            }
        };
        sender.execute(null, null, null);

        if(res == 1 )
            return true;
        else
            return  false;
    }


    void Register(Context c, String username, String name, String email, String passowrd , String device_token) {

        String serverURL = "http://indianroute.roms4all.com/register.php";
        Map<String, String> params = new HashMap<>();

        params.put("username", username);
        params.put("name", name);
        params.put("user_email", email);
        params.put("user_password", passowrd);
        params.put("picture", "images/1.jpg");
        params.put("device_token", device_token);

        post(serverURL, params, c);

        }

    private static int post(String serverurl, Map<String, String> params, Context c) {
        URL u = null;
        try {
            u = new URL(serverurl);
        } catch (MalformedURLException e) {
            //  e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> p = it.next();
            sb.append(p.getKey()).append("=").append(p.getValue());
            if (it.hasNext()) {
                sb.append("&");
            }
        }

        String body = sb.toString();
        byte[] bytes = body.getBytes();
        HttpURLConnection uc = null;
        try {
            uc = (HttpURLConnection) u.openConnection();
            uc.setDoOutput(true);
            uc.setUseCaches(false);
            uc.setFixedLengthStreamingMode(bytes.length);
            uc.setRequestMethod("POST");
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            OutputStream out = uc.getOutputStream();
            out.write(bytes);
            out.close();
            int status = uc.getResponseCode();
            if (status != 200) {
                Log.d("invalid request code", "status s" + status);
                res = 0;
            } else {
                res = 1;
            }

        } catch (IOException e) {
            Log.d("error", e.getMessage());
        }

        return res;

    }

}
