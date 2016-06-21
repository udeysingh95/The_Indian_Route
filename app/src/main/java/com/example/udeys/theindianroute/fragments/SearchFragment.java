package com.example.udeys.theindianroute.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.udeys.theindianroute.R;

/**
 * Created by udeys on 6/17/2016.
 */

public class SearchFragment extends Fragment{

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.searchfragment, container, false);
        return view;
    }

    public void setName(String name)
    {
        TextView txtName = (TextView) view.findViewById(R.id.txtViewResult);
        txtName.setText("Hi \n" + name);
    }

}
