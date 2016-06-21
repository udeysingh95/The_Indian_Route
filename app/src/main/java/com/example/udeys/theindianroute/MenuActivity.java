package com.example.udeys.theindianroute;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
    String username = sp.getString("username", null);
    String user_id = sp.getString("user_id", null);

    ImageButton hm, srch, pst, notif, prfl;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initFragments();

        hm = (ImageButton) findViewById(R.id.home);
        srch = (ImageButton) findViewById(R.id.trip);
        pst = (ImageButton) findViewById(R.id.post);
        notif = (ImageButton) findViewById(R.id.notification);
        prfl = (ImageButton) findViewById(R.id.profile);


        hm.setOnClickListener(this);
        srch.setOnClickListener(this);
        pst.setOnClickListener(this);
        notif.setOnClickListener(this);
        prfl.setOnClickListener(this);

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
        }

    }

    private void initFragments() {
        ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_1, new HomeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
