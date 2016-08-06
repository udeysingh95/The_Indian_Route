package com.example.udeys.theindianroute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Register extends AppCompatActivity implements View.OnClickListener {

    public static boolean flag = false;
    protected static int res = 0;
    EditText name, uname, passowrd, repassword, email;
    Button reg;
    TextView terms;
    TextView male, female;
    Bitmap bp;
    String bas;

    public final static boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        uname = (EditText) findViewById(R.id.username);
        passowrd = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        email = (EditText) findViewById(R.id.email);
        male = (TextView) findViewById(R.id.male);
        female = (TextView) findViewById(R.id.female);
        terms = (TextView) findViewById(R.id.terms);

        terms.setPaintFlags(terms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        reg = (Button) findViewById(R.id.register);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        terms.setOnClickListener(this);
        reg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.male:
                female.setBackgroundResource(0);
                female.setTextColor(Color.parseColor("#888888"));
                male.setBackgroundResource(R.drawable.login_edittext);
                male.setTextColor(Color.parseColor("#cccccc"));
                break;
            case R.id.female:
                male.setBackgroundResource(0);
                male.setTextColor(Color.parseColor("#888888"));
                female.setBackgroundResource(R.drawable.login_edittext);
                female.setTextColor(Color.parseColor("#cccccc"));
                break;
            case R.id.register:
                register_me();
                break;
            case R.id.terms:
                Toast.makeText(Register.this, "terms and conditions", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    private void register_me() {

        String nm, unm, pass, eml, repass, device_token;

        nm = name.getText().toString();
        unm = uname.getText().toString();
        pass = passowrd.getText().toString();
        eml = email.getText().toString();
        repass = repassword.getText().toString();
        device_token = FirebaseInstanceId.getInstance().getToken();
        boolean status = true;
        if (nm.length() == 0) {
            name.setError("You Forgot To Put Your Name");
            status = false;
        }
        if (uname.length() == 0) {
            uname.setError("You Forgot To Put Your Username");
            status = false;
        }
        if (!isValidEmail(eml)) {
            email.setError("Invalid Email");
            status = false;
        }
        if (pass.length() == 0) {
            passowrd.setError("You Forgot To Put Your Password");
            status = false;
        }
        if (!pass.contentEquals(repass)) {
            repassword.setError("Password doesn't match");
            status = false;
        }
        if (repassword.length() == 0) {
            repassword.setError("You Forgot To Put Your Re-Password");
            status = false;
        }
        if (status == true) {
            hit_data(nm, unm, pass, eml, device_token);
        }

        Log.e("token", device_token);


    }


    public void hit_data(final String name, final String username, final String password, final String email, final String device_token) {
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
                params.put("Notification", "1");
                params.put("device_token", device_token);

            } catch (Exception e) {
                Toast.makeText(Register.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            client.post("http://indianroute.roms4all.com/register.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            Log.d("log", res);
                            String username = "", user_id = "";
                            if (res.contentEquals("Username already exists"))
                                Toast.makeText(Register.this, "" + res, Toast.LENGTH_SHORT).show();
                            else {
                                try {
                                    JSONArray jArr = new JSONArray(res);

                                    JSONObject obj = jArr.getJSONObject(0);
                                    username = obj.getString("username");
                                    user_id = obj.getString("user_id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
                                SharedPreferences.Editor ed = sp.edit();
                                ed.clear();
                                ed.putString("user_id", user_id);
                                ed.putString("username", username);
                                ed.commit();
                                Intent intent = new Intent(getApplication(), MenuActivity.class);
                                startActivity(intent);
                                finish();

                            }


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



