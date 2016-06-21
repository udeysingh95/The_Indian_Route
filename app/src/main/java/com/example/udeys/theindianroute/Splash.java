package com.example.udeys.theindianroute;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by udeys on 5/3/2016.
 */

public class Splash extends Activity {
    ImageView im1 , im2 , im3 , im4 , im5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("user_details", MODE_PRIVATE);
        if(sp.contains("user_id")){
            Intent i = new Intent(this , MenuActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }



        setContentView(R.layout.splash);

        im1 = (ImageView) findViewById(R.id.foot1);
        im2 = (ImageView) findViewById(R.id.foot2);
        im3 = (ImageView) findViewById(R.id.foot3);
        im4 = (ImageView) findViewById(R.id.foot4);
        im5 = (ImageView) findViewById(R.id.foot5);



        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade);
        a.setRepeatCount( 1000 );
        im1.startAnimation( a );



        //fade1();
        fade2();
        fade3();
        fade4();
        fade5();



    }

    public void fade1( ){
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        im1.startAnimation(animation1);
    }
    public void fade2( ){
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade1);
        im2.startAnimation(animation2);
    }
    public void fade3( ){
        Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade2);
        im3.startAnimation(animation3);
    }
    public void fade4( ){
        Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade3);
        im4.startAnimation(animation4);
    }
    public void fade5( ){
        Animation animation5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade4);
        im5.startAnimation(animation5);
    }



}
