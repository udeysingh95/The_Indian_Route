package com.example.udeys.theindianroute;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.udeys.theindianroute.fragments.CameraFragment;
import com.example.udeys.theindianroute.fragments.HomeFragment;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{
    Button camera,gallery;
    FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        gallery = (Button)findViewById(R.id.gallery);
        camera  = (Button)findViewById(R.id.camera);

        gallery.setOnClickListener(this);
        camera.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.gallery:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.post_fragment, new HomeFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.camera:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.post_fragment, new CameraFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
        }
    }
}
