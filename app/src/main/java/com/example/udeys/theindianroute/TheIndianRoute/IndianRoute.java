package com.example.udeys.theindianroute.TheIndianRoute;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.udeys.theindianroute.R;

public class IndianRoute extends AppCompatActivity implements View.OnClickListener {
    LinearLayout frag1 , frag2 , frag3 , nav;
    RelativeLayout parentLayout;
    FragmentTransaction ft;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indian_route);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.back_bar);
        setSupportActionBar(myToolbar);
        ImageButton back_btn = (ImageButton) findViewById(R.id.btn_back);

        title = (TextView) findViewById(R.id.title);
        title.setText("The Indian Route");

        back_btn.setOnClickListener(this);

        nav = (LinearLayout) findViewById(R.id.bottom_nav);
        parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);

        frag1 = (LinearLayout) findViewById(R.id.frag1);
        frag2 = (LinearLayout) findViewById(R.id.frag2);
        frag3 = (LinearLayout) findViewById(R.id.frag3);

        nav.bringToFront();
        parentLayout.invalidate();

        frag1.setOnClickListener(this);
        frag2.setOnClickListener(this);
        frag3.setOnClickListener(this);

        init();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.frag1:
                reduce_alpha();
                frag1.setAlpha(1.0f);
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.indian_route_fragment, new IndianRouteGallery());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.frag2:
                reduce_alpha();
                frag2.setAlpha(1.0f);
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.indian_route_fragment, new IndianRouteMap());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.frag3:
                reduce_alpha();
                frag3.setAlpha(1.0f);
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.indian_route_fragment, new Events());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void init(){
        frag1.setAlpha(1.0f);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.indian_route_fragment, new IndianRouteGallery());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void reduce_alpha() {
        frag1.setAlpha(0.5f);
        frag2.setAlpha(0.5f);
        frag3.setAlpha(0.5f);
    }
}
