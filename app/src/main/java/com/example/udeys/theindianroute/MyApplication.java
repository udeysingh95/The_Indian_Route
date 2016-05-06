package com.example.udeys.theindianroute;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Siddharth Malhotra on 5/4/2016.
 */

public class MyApplication extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myapplication);

        try{
            Intent in = getIntent();
            Toast.makeText(getApplicationContext(),"Welcome: " +in.getStringExtra("NAME"),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error: " +e.getMessage(),Toast.LENGTH_SHORT).show();
        }/*

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