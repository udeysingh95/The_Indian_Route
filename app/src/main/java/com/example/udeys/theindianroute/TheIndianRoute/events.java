package com.example.udeys.theindianroute.TheIndianRoute;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.udeys.theindianroute.R;

/**
 * Created by Gitesh on 24-06-2016.
 */
public class Events extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.events, container, false);
        return  view;
    }


}
