package com.example.udeys.theindianroute.fragments;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.helperClasses.GooglePlacesReadTask;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by udeys on 6/18/2016.
 */

public class TripFragment extends Fragment implements View.OnClickListener {

    private static final String GOOGLE_API_KEY = "AIzaSyBU5MYsR4tlII9AWFRuKI12gEcJ6Ve9n64";
    View view;
    Button btnStart, btnData;
    EditText destPlace;
    GoogleMap googleMap = null;
    double latitude = 0;
    double longitude = 0;
    String place;
    String type;
    private SQLiteDatabase db = null;
    private int PROXIMITY_RADIUS = 20000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tripfragment, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnStart = (Button) view.findViewById(R.id.trpStart);
        btnData = (Button) view.findViewById(R.id.btnData);
        destPlace = (EditText) view.findViewById(R.id.trpDest);

        btnData.setOnClickListener(this);
        btnStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.trpStart:
                destPlace.setVisibility(View.VISIBLE);
                btnData.setVisibility(View.VISIBLE);
                break;
            case R.id.btnData:
                getNearbyPlaces();

        }

    }

    private void getNearbyPlaces() {
        String ddest = destPlace.getText().toString();
        StringBuilder table_query = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        table_query.append("NEARBY");
        table_query.append(" (Latitude double,Longitude double,Place_Name text,Vicinity text,Type text)");
        Log.e("tb", table_query.toString());

        try {
            db = SQLiteDatabase.openOrCreateDatabase("MapData.db", null);
            db.execSQL(table_query.toString());
        } catch (Exception e) {
            Log.e("db", e.toString());
        }

        ArrayList<Double> latlng = getLocationFromAddress(getActivity(), ddest);
        latitude = latlng.get(0);
        longitude = latlng.get(1);
        type = "hospital|gas_station";
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
        Log.e("url", googlePlacesUrl.toString());
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);
    }

    public ArrayList<Double> getLocationFromAddress(Context context, String strAddress) {

        ArrayList<Double> latlng = new ArrayList<>();

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            latlng.add(location.getLatitude());
            latlng.add(location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return latlng;
    }
}