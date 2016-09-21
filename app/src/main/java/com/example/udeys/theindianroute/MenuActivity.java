package com.example.udeys.theindianroute;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.TheIndianRoute.IndianRoute;
import com.example.udeys.theindianroute.fragments.HomeFragment;
import com.example.udeys.theindianroute.fragments.NotificationFragment;
import com.example.udeys.theindianroute.fragments.ProfileFragment;
import com.example.udeys.theindianroute.fragments.SearchFragment;
import com.example.udeys.theindianroute.fragments.TripFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {


    public String username, user_id;
    ImageButton hm, trp, pst, notif, prfl;      //menuBar
    ImageButton Logo, Search;
    EditText srch;
    TextView title;
    Boolean search_state = false;
    FragmentTransaction ft;
    Intent intent = null;
    LinearLayout home, trip, post, alert, passport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        srch = (EditText) findViewById(R.id.search_bar);
        Search = (ImageButton) findViewById(R.id.btn_search);
        Logo = (ImageButton) findViewById(R.id.toolbar_logo);
        home = (LinearLayout) findViewById(R.id.home_button);
        trip = (LinearLayout) findViewById(R.id.trip_button);
        post = (LinearLayout) findViewById(R.id.post_button);
        alert = (LinearLayout) findViewById(R.id.alert_button);
        passport = (LinearLayout) findViewById(R.id.passport_button);

        title = (TextView) findViewById(R.id.title);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
        String username = sp.getString("username", null);
        user_id = sp.getString("user_id", null);
        initFragments();

        hm = (ImageButton) findViewById(R.id.home);
        trp = (ImageButton) findViewById(R.id.trip);
        pst = (ImageButton) findViewById(R.id.post);
        notif = (ImageButton) findViewById(R.id.notification);
        prfl = (ImageButton) findViewById(R.id.profile);


        hm.setOnClickListener(this);
        trp.setOnClickListener(this);
        pst.setOnClickListener(this);
        notif.setOnClickListener(this);
        prfl.setOnClickListener(this);

        Logo.setOnClickListener(this);
        Search.setOnClickListener(this);


        srch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                changeUsername(cs);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.home:
                reduce_alpha();
                home.setAlpha(1f);
                title.setText("home");
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new HomeFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.trip:
                reduce_alpha();
                trip.setAlpha(1f);
                title.setText("trip mode");
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new TripFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.post:
                reduce_alpha();
                startActivity(new Intent(getApplicationContext(), PostActivity.class));
                /*ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new CameraFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();*/
                break;
            case R.id.notification:
                reduce_alpha();
                alert.setAlpha(1f);
                title.setText("alerts");
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new NotificationFragment());
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case R.id.profile:
                reduce_alpha();
                passport.setAlpha(1f);
                title.setText("passport");
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_1, new ProfileFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.toolbar_logo:
                Intent intent = new Intent(this, IndianRoute.class);
                startActivity(intent);
                break;
            case R.id.btn_search:
                if (search_state == false) {
                    Search.setImageResource(R.drawable.ic_action_back);
                    title.setVisibility(View.GONE);
                    srch.setVisibility(View.VISIBLE);
                    srch.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    search_state = true;
                } else {
                    Search.setImageResource(R.drawable.ic_action_search_dark);
                    srch.setText("");
                    srch.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    search_state = false;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    View view = getCurrentFocus();
                    imm.hideSoftInputFromWindow(srch.getWindowToken(), 0);
                    hm.performClick();
                }
                break;


        }

    }

    public void changeUsername(CharSequence cs) {
        try {
            RequestParams params = new RequestParams();
            params.put("search", cs.toString());
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://theindianroute.net/search_temp.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Toast.makeText(getApplicationContext(), "" + responseString, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            decodeJson(responseString);
                        }

                    }
            );
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void decodeJson(String result) {
        ArrayList<String> users_name;
        users_name = new ArrayList<>();
        ArrayList<String> users_id;
        users_id = new ArrayList<>();
        try {
            JSONArray jArr = new JSONArray(result);
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                String path = obj.getString("username");
                String id = obj.getString("user_id");
                users_name.add(path);
                users_id.add(id);
                //Toast.makeText(this,users.get(count),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Toast.makeText(this,users.size(),Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("user_name", users_name);
        bundle.putStringArrayList("user_id", users_id);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_1, searchFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void initFragments() {
        home.setAlpha(1f);
        ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_1, new HomeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void reduce_alpha() {
        home.setAlpha(0.5f);
        trip.setAlpha(0.5f);
        alert.setAlpha(0.5f);
        passport.setAlpha(0.5f);
    }


}
