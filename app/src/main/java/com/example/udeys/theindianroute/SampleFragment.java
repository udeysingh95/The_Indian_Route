package com.example.udeys.theindianroute;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;

import Classes.posts;
import Database.DBController;

/**
 * Created by Malhotra G on 5/16/2016.
 */


public class SampleFragment extends Fragment {
    private static final String STARTING_TEXT = "Four Buttons Bottom Navigation";

    public SampleFragment() {
    }

    public static SampleFragment newInstance(String text) {
        Bundle args = new Bundle();

        args.putString(STARTING_TEXT, text);

        SampleFragment sampleFragment = new SampleFragment();
        sampleFragment.setArguments(args);

        return sampleFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DBController db = new DBController(this);
        posts obj = new posts();
        obj.setUsername("gitesh");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.marina_bay_singapore_panorama);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] post = bos.toByteArray();
        obj.setPostimage(post);
        Bitmap a = BitmapFactory.decodeResource(getResources(), R.drawable.marina_bay_singapore_panorama);
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        a.compress(Bitmap.CompressFormat.PNG, 100, bos1);
        byte[] profile = bos1.toByteArray();
        obj.setProfileimg(profile);
        db.addRecord(obj);





        RelativeLayout rl = null;
        //TextView textView = new TextView(getActivity());
        //textView.setText(getArguments().getString(STARTING_TEXT));
        if (getArguments().getString(STARTING_TEXT).equals("Content for food.")) {
            rl = (RelativeLayout) inflater.inflate(R.layout.search, container, false);
        } else if (getArguments().getString(STARTING_TEXT).equals("Content for favorites.")) {
            rl = (RelativeLayout) inflater.inflate(R.layout.favourite, container, false);
        } else if (getArguments().getString(STARTING_TEXT).equals("Content for locations.")) {
            rl = (RelativeLayout) inflater.inflate(R.layout.location_map, container, false);
        } else if (getArguments().getString(STARTING_TEXT).equals("Content for recent.")) {
            new Timeline();
            rl = (RelativeLayout) inflater.inflate(R.layout.timeline, container, false);
        } else if (getArguments().getString(STARTING_TEXT).equals("Content for user.")) {
            rl = (RelativeLayout) inflater.inflate(R.layout.profile, container, false);
        }

        return rl;
        //return null;
    }
}