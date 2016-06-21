package com.example.udeys.theindianroute;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Siddharth Malhotra on 6/19/2016.
 */
public class CompressFilter extends AppCompatActivity {


    Intent intent;
    String imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress);
        //initFragments();

        try {
            intent = getIntent();
            imagePath = intent.getStringExtra("path");
            Log.e("ImagePath ", imagePath);
        } catch (Exception e) {
            Toast.makeText(CompressFilter.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        //newInstance(imagePath);

    }

    /*private void initFragments(Fragment targetFragment) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.image_filters, targetFragment);
        transaction.commit();
    }*/






}
