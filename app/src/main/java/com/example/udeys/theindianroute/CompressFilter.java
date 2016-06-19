package com.example.udeys.theindianroute;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Malhotra G on 6/19/2016.
 */
public class CompressFilter extends AppCompatActivity {

    ImageView imageView;
    Intent intent;
    String imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress);

        imageView = (ImageView) findViewById(R.id.imageView3);

        try {
            intent = getIntent();
            imagePath = intent.getStringExtra("path");
            Log.e("ImagePath ", imagePath);
        } catch (Exception e) {
            Toast.makeText(CompressFilter.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
    }


}
