package com.example.udeys.theindianroute;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Register extends AppCompatActivity implements View.OnClickListener {

    public static boolean flag = false;
    protected static int res = 0;
    EditText name, uname, passowrd, repassword, email;
    Button reg;
    Bitmap bp;
    String bas;

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
        device_token = FirebaseInstanceId.getInstance().getToken();

        //Log.e("token" , device_token);
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
            AsyncHttpClient client = new AsyncHttpClient();
            /*
            * Bind parameters here
            * */
            RequestParams params = new RequestParams();
            try {
                params.put("username", username);
                params.put("name", name);
                params.put("user_email", email);
                params.put("user_password", password);
                params.put("notification", "1");
                params.put("device_token", device_token);

            } catch (Exception e) {
                Toast.makeText(Register.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            client.post("http://indianroute.roms4all.com/register.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            Toast.makeText(Register.this, "" + res, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Toast.makeText(Register.this, "" + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            Toast.makeText(Register.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}



