package com.example.udeys.theindianroute;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;
import com.roughike.bottombar.OnTabSelectedListener;

/**
 * Created by Siddharth Malhotra G on 5/16/2016.
 */

public class FiveButtonActivity extends AppCompatActivity {
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmenttry);

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

        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setFragmentItems(getFragmentManager(), R.id.fragment_container,
                new BottomBarFragment(SampleFragment.newInstance("Content for recent."), R.drawable.ic_update_white_24dp, "home"),
                new BottomBarFragment(SampleFragment.newInstance("Content for food."), R.drawable.ic_local_dining_white_24dp, "notification"),
                new BottomBarFragment(SampleFragment.newInstance("Content for favorites."), R.drawable.ic_favorite_white_24dp, "post"),
                new BottomBarFragment(SampleFragment.newInstance("Content for locations."), R.drawable.ic_location_on_white_24dp, "profile"),
                new BottomBarFragment(SampleFragment.newInstance("Content for user."), R.drawable.ic_favorite_white_24dp, "travel")
        );

        // Setting colors for different tabs when there's more than three of them.
        //bottomBar.mapColorForTab(0, "#3B494C");
        //bottomBar.mapColorForTab(1, "#00796B");
        //bottomBar.mapColorForTab(2, "#7B1FA2");
        //bottomBar.mapColorForTab(3, "#FF5252");

        bottomBar.setOnItemSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                switch (position) {
                    case 1:
                        //setContentView(R.layout.search);
                        break;
                    // Item 1 Selected
                }
            }
        });

        // Make a Badge for the first tab, with red background color and a value of "4".
        //BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(1, "#E91E63", 4);

        // Control the badge's visibility
        //unreadMessages.show();
        //unreadMessages.hide();

        // Change the displayed count for this badge.
        //unreadMessages.setCount(4);

        // Change the show / hide animation duration.
        //unreadMessages.setAnimationDuration(200);

        // If you want the badge be shown always after unselecting the tab that contains it.
        //unreadMessages.setAutoShowAfterUnSelection(true);
    }
}