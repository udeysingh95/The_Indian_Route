package com.example.udeys.theindianroute;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

        RelativeLayout rl = null;
        //TextView textView = new TextView(getActivity());
        //textView.setText(getArguments().getString(STARTING_TEXT));
        if (getArguments().getString(STARTING_TEXT).equals("Content for food.")) {
            rl = (RelativeLayout) inflater.inflate(R.layout.search, container, false);
        } else if (getArguments().getString(STARTING_TEXT).equals("Content for favorites.")) {
            rl = (RelativeLayout) inflater.inflate(R.layout.favourite, container, false);
        } else if (getArguments().getString(STARTING_TEXT).equals("Content for locations.")) {
            rl = (RelativeLayout) inflater.inflate(R.layout.location_map, container, false);
        } else if (getArguments().getString(STARTING_TEXT).equals("Content for recents.")) {
            rl = (RelativeLayout) inflater.inflate(R.layout.timeline, container, false);
        } else if (getArguments().getString(STARTING_TEXT).equals("Content for user.")) {
            rl = (RelativeLayout) inflater.inflate(R.layout.profile, container, false);
        }

        return rl;
    }
}