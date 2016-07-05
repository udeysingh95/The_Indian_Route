package com.example.udeys.theindianroute.TheIndianRoute;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.udeys.theindianroute.R;

public class IndianRoute extends AppCompatActivity implements View.OnClickListener {
    ImageButton frag1, frag2, frag3;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indian_route);
        frag1 = (ImageButton) findViewById(R.id.frag1);
        frag2 = (ImageButton) findViewById(R.id.frag2);
        frag3 = (ImageButton) findViewById(R.id.frag3);

        frag1.setOnClickListener(this);
        frag2.setOnClickListener(this);
        frag3.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.frag1:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.indian_route_fragment, new fragment1());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.frag2:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.indian_route_fragment, new MapFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.frag3:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.indian_route_fragment, new fragment3());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
        }
    }
}
