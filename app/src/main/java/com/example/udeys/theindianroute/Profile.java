package com.example.udeys.theindianroute;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;

import java.net.URL;

/**
 * Created by Siddharth Malhotra on 5/22/2016.
 */
public class Profile extends AppCompatActivity {
    ImageView imageView;
    Intent intent = null;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        ProfilePictureView profilePictureView;

        profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);


        try {
            intent = getIntent();
            String userId = intent.getStringExtra("NAME");
            profilePictureView.setProfileId(userId);

        } catch (Exception e) {

        }
        //imageView.setImageBitmap(bitmap);

    }

    public Bitmap getFacebookProfilePicture(String userID) {
        URL imageURL;
        Bitmap bitmap = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            Toast.makeText(getApplicationContext(), "image got", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

        }
        return bitmap;
    }
}
