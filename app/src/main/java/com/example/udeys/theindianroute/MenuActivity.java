package com.example.udeys.theindianroute;

import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.udeys.theindianroute.TheIndianRoute.IndianRoute;
import com.example.udeys.theindianroute.fragments.HomeFragment;
import com.example.udeys.theindianroute.fragments.NotificationFragment;
import com.example.udeys.theindianroute.fragments.ProfileFragment;
import com.example.udeys.theindianroute.fragments.SearchFragment;
import com.example.udeys.theindianroute.fragments.TripFragment;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {


    public String username , user_id;
    ImageButton hm, trp, pst, notif, prfl;      //menuBar
    ImageButton Logo, Search ;
    EditText srch;
    Boolean search_state = false;
    FragmentTransaction ft;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        srch = (EditText) findViewById(R.id.search_bar);
        Search = (ImageButton) findViewById(R.id.btn_search);
        Logo = (ImageButton)findViewById(R.id.toolbar_logo);

        srch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                changeUsername(cs);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("Notification");

        try {
            intent = getIntent();
            String open = intent.getStringExtra("Notification");
            if (open != null) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.cancelAll();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new NotificationFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            } else {
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new HomeFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }

        SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
        String username = sp.getString("username", null);
        user_id = sp.getString("user_id", null);
        //Log.e("sharedpred", username + " " + user_id);
        initFragments();

        hm = (ImageButton) findViewById(R.id.home);
        trp = (ImageButton) findViewById(R.id.trip);
        pst = (ImageButton) findViewById(R.id.post);
        notif = (ImageButton) findViewById(R.id.notification);
        prfl = (ImageButton) findViewById(R.id.profile);



        hm.setOnClickListener(this);
        trp.setOnClickListener(this);
        pst.setOnClickListener(this);
        notif.setOnClickListener(this);
        prfl.setOnClickListener(this);

        Logo.setOnClickListener(this);
        Search.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.home:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new HomeFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.trip:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new TripFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.post:
                /*ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new ViewPostFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();*/
                Intent i = new Intent(this, PostActivity.class);
                startActivity(i);
                break;
            case R.id.notification:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new NotificationFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.profile:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new ProfileFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.toolbar_logo:
                Intent intent = new Intent(this, IndianRoute.class);
                startActivity(intent);
                break;
            case R.id.btn_search:
                if(search_state == false){
                    Search.setImageResource(R.drawable.ic_action_back);
                    srch.setVisibility(View.VISIBLE);
                    search_state = true;
                }
                else{
                    Search.setImageResource(R.drawable.ic_action_search_dark);
                    srch.setVisibility(View.INVISIBLE);
                    search_state = false;
                }
                break;



        }

    }

    public void changeUsername(CharSequence cs) {
        try {
            RequestParams params = new RequestParams();
            params.put("search", cs.toString());
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/search_temp.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Toast.makeText(getApplicationContext(), "" + responseString, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            decodeJson(responseString);
                        }

                    }
            );
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void decodeJson(String result) {
        ArrayList<String> users_name;
        users_name = new ArrayList<>();
        ArrayList<String> users_id;
        users_id = new ArrayList<>();
        try {
            JSONArray jArr = new JSONArray(result);
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                String path = obj.getString("username");
                String id = obj.getString("user_id");
                users_name.add(path);
                users_id.add(id);
                //Toast.makeText(this,users.get(count),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Toast.makeText(this,users.size(),Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("user_name", users_name);
        bundle.putStringArrayList("user_id", users_id);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_1, searchFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    private void initFragments() {
        ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_1, new HomeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}
