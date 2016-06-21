package com.example.udeys.theindianroute.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.udeys.theindianroute.R;

/**
 * Created by udeys on 6/18/2016.
 */

public class EmptyFragment extends Fragment{

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.emptylayout, container, false);
        return view;
    }



}