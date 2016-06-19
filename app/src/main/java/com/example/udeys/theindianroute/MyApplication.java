package com.example.udeys.theindianroute;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

/**
 * Created by Siddharth Malhotra on 5/4/2016.
 */

public class MyApplication extends AppCompatActivity{
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myapplication);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.three_buttons_activity);

        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.five_buttons_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.recent_item:
                        setContentView(R.layout.myapplication);
                        //Snackbar.make(coordinatorLayout, "Recent Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.search_item:
                        setContentView(R.layout.search);
                        //Snackbar.make(coordinatorLayout, "Search Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.favorite_item:
                        Snackbar.make(coordinatorLayout, "Favorite Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.location_item:
                        Snackbar.make(coordinatorLayout, "Location Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.user_item:
                        Snackbar.make(coordinatorLayout, "User Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        });



        /*try{
            Intent in = getIntent();
            Toast.makeText(getApplicationContext(),"Welcome: " +in.getStringExtra("NAME"),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error: " +e.getMessage(),Toast.LENGTH_SHORT).show();
        }*/

        /*

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myapplication);

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata"); // receiving data from  MainActivity.java

        JSONArray friendslist;
        ArrayList<String> friends = new ArrayList<>();
        try {
            friendslist = new JSONArray(jsondata);
            for (int l=0; l < friendslist.length(); l++) {
                friends.add(friendslist.getJSONObject(l).getString("name"));
                Toast.makeText(getApplicationContext(),"name: " + friends.get(l),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"Error: " +e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        // adapter which populate the friends in listview
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_listview, friends);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);*/

    }
}