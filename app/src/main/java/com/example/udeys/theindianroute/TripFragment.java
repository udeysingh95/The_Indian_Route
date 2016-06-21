package com.example.udeys.theindianroute;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.udeys.theindianroute.path.HttpConnection;
import com.example.udeys.theindianroute.path.PathJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by udeys on 6/18/2016.
 */

public class TripFragment extends Fragment implements OnMapReadyCallback {

    View view;

    MapView mMapView;
    LatLng CHANDIGARH = new LatLng(30.733315, 76.779418);
    LatLng DELHI = new LatLng(28.613939, 77.209021);
    LatLng KURUKSHETRA = new LatLng(29.969512, 76.878282);
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.tripfragment, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        addMarkers();
        // Add a marker in Elante and move the camera
        //LatLng sydney = new LatLng(30.7108825,76.766128);
        //googleMap.addMarker(new MarkerOptions().position(sydney).title("ELANTE"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
    }


    private void addMarkers() {
        //Toast.makeText(getActivity(), "addMarker", Toast.LENGTH_SHORT).show();
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(CHANDIGARH).title("First Point"));
            googleMap.addMarker(new MarkerOptions().position(DELHI).title("Third Point"));
            googleMap.addMarker(new MarkerOptions().position(KURUKSHETRA).title("Second Point"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CHANDIGARH, 7));
        }

        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);
    }

    private String getMapsApiDirectionsUrl() {
        String waypoints = "waypoints=optimize:true|"
                + CHANDIGARH.latitude + "," + CHANDIGARH.longitude
                + "|" + "|" + DELHI.latitude + ","
                + DELHI.longitude + "|" + KURUKSHETRA.latitude + ","
                + KURUKSHETRA.longitude;

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + CHANDIGARH.latitude + "," + CHANDIGARH.longitude
                + "&destination=" + DELHI.latitude + "," + DELHI.longitude + "&waypoints=optimize:true|"
                + DELHI.latitude + "," + DELHI.longitude
                + "|" + KURUKSHETRA.latitude + "," + KURUKSHETRA.longitude
                + "&sensor=true";
        return url;
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes

            if (routes != null) {

                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(2);
                    polyLineOptions.color(Color.GRAY);
                }

                googleMap.addPolyline(polyLineOptions);
            }
        }
    }


}