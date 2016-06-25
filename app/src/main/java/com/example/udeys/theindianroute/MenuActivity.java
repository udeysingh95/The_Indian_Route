package com.example.udeys.theindianroute;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.udeys.theindianroute.TheIndianRoute.IndianRoute;
import com.example.udeys.theindianroute.fragments.HomeFragment;
import com.example.udeys.theindianroute.fragments.NotificationFragment;
import com.example.udeys.theindianroute.fragments.PostFragment;
import com.example.udeys.theindianroute.fragments.ProfileFragment;
import com.example.udeys.theindianroute.fragments.TripFragment;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {


    public String username , user_id;
    ImageButton hm, srch, pst, notif, prfl,ir;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
        String username = sp.getString("username", "udeysingh95");
        user_id = sp.getString("user_id", "22");
        initFragments();

        hm = (ImageButton) findViewById(R.id.home);
        srch = (ImageButton) findViewById(R.id.trip);
        pst = (ImageButton) findViewById(R.id.post);
        notif = (ImageButton) findViewById(R.id.notification);
        prfl = (ImageButton) findViewById(R.id.profile);
        ir = (ImageButton)findViewById(R.id.toolbar_logo);


        hm.setOnClickListener(this);
        srch.setOnClickListener(this);
        pst.setOnClickListener(this);
        notif.setOnClickListener(this);
        prfl.setOnClickListener(this);
        ir.setOnClickListener(this);

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
        }

    }

    private void initFragments() {
        ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_1, new HomeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}
