package com.example.udeys.theindianroute;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText name, uname, passowrd, repassword, email;
    Button reg;
    protected static int res = 0;
    Bitmap bitmap;
    String base64;
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

        reg = (Button) findViewById(R.id.register);

        reg.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        String nm, unm, pass, eml, repass, device_token;

        nm = name.getText().toString();
        unm = uname.getText().toString();
        pass = passowrd.getText().toString();
        eml = email.getText().toString();
        repass = repassword.getText().toString();
        device_token = "hello";
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.facebook_button);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] b = os.toByteArray();
        base64 = Base64.encodeToString(b, Base64.DEFAULT);


        /*
        * validation goes here
        * */




        /*
        * validated data send to the server.
        * flag boolean varidable to check the response from the server.
        * */
        hit_data(nm, unm, pass, eml, device_token);

    }

    public void hit_data(final String name, final String username, final String email, final String password, final String device_token) {
        try {
       /*
         * Bind Parameters
         * */
            final List<Pair<String, String>> params = new ArrayList<Pair<String, String>>() {{
                add(new Pair<>("username", username));
                add(new Pair<>("name", name));
                add(new Pair<>("user_email", email));
                add(new Pair<>("user_password", password));
                add(new Pair<>("picture", base64));
                add(new Pair<>("device_token", device_token));
            }};

            /**
             * fuel library is used for sending and receiving response from server
             * */
            Fuel.post("http://indianroute.roms4all.com/register.php", params).responseString(new Handler<String>() {
                @Override
                public void success(@NotNull Request request, @NotNull Response response, String s) {
                    updateUI(null, s);
                }

                @Override
                public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                    updateUI(fuelError, null);
                }

            });
        }catch (Exception e){
            Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUI(final FuelError error, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (error == null) {
                    Toast.makeText(Register.this, ""+result, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Error", "error: " + error.getException().getMessage());
                    Toast.makeText(Register.this, "" + error.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
