package com.example.udeys.theindianroute;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

        }
    }
}