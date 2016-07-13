package com.example.udeys.theindianroute;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

public class Setting extends Activity  implements View.OnClickListener{

    ImageView cancel , save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cancel = (ImageView) findViewById(R.id.ic_cancel);
        save = (ImageView) findViewById(R.id.ic_save);

        cancel.setOnClickListener(this);
        save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.ic_cancel:
                Intent i = new Intent(this , MenuActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.ic_save:
                save_details();
                break;
        }
    }


    public void save_details(){

    }
}
