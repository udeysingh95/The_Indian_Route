package com.example.udeys.theindianroute;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.udeys.theindianroute.TheIndianRoute.IndianRoute;
import com.example.udeys.theindianroute.fragments.HomeFragment;
import com.example.udeys.theindianroute.fragments.NotificationFragment;
import com.example.udeys.theindianroute.fragments.PostFragment;
import com.example.udeys.theindianroute.fragments.ProfileFragment;
import com.example.udeys.theindianroute.fragments.TripFragment;
import com.google.firebase.messaging.FirebaseMessaging;

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

        FirebaseMessaging.getInstance().subscribeToTopic("Notification");

        try {
            intent = getIntent();
            String open = intent.getStringExtra("notification");
            if (open != null) {
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
        Log.e("sharedpred", username + " " + user_id);
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
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new PostFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
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
                Intent i = new Intent(this, IndianRoute.class);
                startActivity(i);
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

    private void initFragments() {
        ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_1, new HomeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}
