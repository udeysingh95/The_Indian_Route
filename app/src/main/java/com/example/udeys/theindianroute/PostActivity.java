package com.example.udeys.theindianroute;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.udeys.theindianroute.fragments.CameraFragment;
import com.example.udeys.theindianroute.fragments.GalleryFragment;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    Button camera, gallery;
    FragmentTransaction ft;
    ImageButton back_btn;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        gallery = (Button) findViewById(R.id.gallery);
        camera = (Button) findViewById(R.id.camera);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.back_bar);
        back_btn = (ImageButton) findViewById(R.id.btn_back);
        //setSupportActionBar(myToolbar);
        title = (TextView) findViewById(R.id.title);
        title.setText("Post");
        gallery.setOnClickListener(this);
        camera.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.gallery:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.post_fragment, new GalleryFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.camera:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.post_fragment, new CameraFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.btn_back:
                finish();
        }
    }
}
