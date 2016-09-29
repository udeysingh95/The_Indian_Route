package com.example.udeys.theindianroute;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.helperClasses.SpaceTokenizer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class PostForm extends AppCompatActivity implements View.OnClickListener {

    String story, checkin = "";
    EditText c;
    MultiAutoCompleteTextView sto;
    String id;
    String hash_tag;
    String filename;
    File i;
    Button push_post;
    double lat;
    double lon;
    ListView listView;
    ArrayList<String> hashtag;
    private Location thislocation;
    private boolean valid = false;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
        id = sp.getString("user_id", null);
        setContentView(R.layout.activity_post_form);

        sto = (MultiAutoCompleteTextView) findViewById(R.id.post_story);

        sto.setTokenizer(new SpaceTokenizer());
        c = (EditText) findViewById(R.id.post_checkin);
        listView = (ListView) findViewById(R.id.mini_list);

        sto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String hash = sto.getText().toString();
                hash_tag = hash.substring(hash.lastIndexOf("#") + 1);
                hashtag();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        push_post = (Button) findViewById(R.id.push_post);
        filename = getIntent().getStringExtra("post_image");
        Log.e("filename-postform", filename);
        push_post.setOnClickListener(this);

        try {
            getLocationFromGPS();
        } catch (Exception e) {
            //Log.e("post catch", e.toString());
        }
    }

    private void getLocationFromGPS() {
        locationListener = new LocationListener() {

            public void onStatusChanged(String provider, int status, Bundle extras) {            }
            public void onProviderEnabled(String provider) {            }
            public void onProviderDisabled(String provider) {            }
            public void onLocationChanged(Location location) {
                gpsLocationReceived(location);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    lat = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
                    lon = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
                }

            }
        };


        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_COARSE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(true);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
        String providerName = locationManager.getBestProvider(locationCritera, true);

        if (providerName != null && locationManager.isProviderEnabled(providerName)) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            locationManager.requestLocationUpdates(providerName, 20000, 100, locationListener);
        } else {
            // Provider not enabled, prompt user to enable it
            Toast.makeText(getApplicationContext(), "please_turn_on_gps", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);
        }

        if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {

            lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            lon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        } else if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
            //Log.e("TAG", "Inside NETWORK");

            lat = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
            lon = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();

        } else {

            //Log.e("TAG", "else +++++++ ");
            lat = -1;
            lon = -1;
        }
    }

    @Override
    public void onClick(View v) {
        story = sto.getText().toString();

        checkin = c.getText().toString();

        if (checkin.length() == 0)
            checkin = getLocation();

        c.setText(checkin);
        //i = get();
        //Log.e("file", "" + i);
        processImage(filename);
        pushPost();
    }

    private void processImage(String image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        String path = image;
        BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        /*
        Log.e("processImage","before");
        Log.e("imageHeight", String.valueOf(imageHeight));
        Log.e("imageWidth", String.valueOf(imageWidth));*/

        if (imageWidth < 320) {
            Bitmap newImage = BitmapFactory.decodeFile(path);
            newImage = Bitmap.createScaledBitmap(newImage, 320, imageHeight, true);
            path = saveBitmap(newImage);
        } else if (imageWidth > 1080) {
            Bitmap newImage = BitmapFactory.decodeFile(path);
            newImage = Bitmap.createScaledBitmap(newImage, 1920, 1080, true);
            path = saveBitmap(newImage);
        }

        BitmapFactory.decodeFile(path, options);

        imageHeight = options.outHeight;
        imageWidth = options.outWidth;
        /*
        Log.e("processImage","after");
        Log.e("imageHeight", String.valueOf(imageHeight));
        Log.e("imageWidth", String.valueOf(imageWidth));*/
    }

    private String saveBitmap(Bitmap bitmap) {
        String sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        String path = Environment.getExternalStoragePublicDirectory("TheIndianRoute/TIR") + sdf + ".jpeg";
        //Log.e("path",path);
        if (bitmap != null) {
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        //Log.e("finally-catch",e.toString());
                    }
                }
            } catch (Exception e) {
                // Log.e("outer-catch",e.toString());
            }
        }
        return path;
    }

    public void pushPost() {

       /* try {
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            /*
            * Bind parameters here
            * */

            /*
            RequestParams params = new RequestParams();
            Log.e("user_id: ", id);
            params.put("user_id" , id);
            params.put("check_in", checkin);
            params.put("story", story);
            params.put("image", i);

            client.post("http://theindianroute.net/post.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            Intent i = new Intent(getApplication(), MenuActivity.class);
                            startActivity(i);
                            finish();
                            Log.e("posted", res);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Log.d("error", res);
                        }
                    }
            );


        } catch (Exception e) {
            Toast.makeText(PostForm.this, "4" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }

    private File get() {

        File sd = Environment.getExternalStorageDirectory();
        File location = new File(sd.getAbsolutePath() + "/TheIndianRoute");
        return new File(location, filename);
    }

    protected void gpsLocationReceived(Location location) {
        thislocation = location;
    }
    /*
    * Read Location
    * */

    public String getLocation() {
        Geocoder gcd = new Geocoder(PostForm.this, Locale.getDefault());
        List<Address> addresses = null;
        String location = null;
        try {
            addresses = gcd.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                location = addresses.get(0).getLocality() + " ";
                location += addresses.get(0).getSubLocality() + " ";
                location += addresses.get(0).getAdminArea();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return location;
    }

    public void hashtag() {
        try {
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            /*
            * Bind parameters here
            * */
            RequestParams params = new RequestParams();
            try {
                // Log.e("hash_tag", hash_tag);
                params.put("hash", hash_tag);
            } catch (Exception e) {
                //Toast.makeText(this, "1" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            client.post("http://theindianroute.net/fetch_hash.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            // Log.e("succes", res);
                            decodeHashTag(res);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            //Toast.makeText(PostForm.this, "3" + statusCode + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );


        } catch (Exception e) {
            Toast.makeText(PostForm.this, "4" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void decodeHashTag(String result) {
        try {
            String hash = "";
            hashtag = new ArrayList<>();
            try {
                JSONArray jArr = new JSONArray(result);
                for (int count = 0; count < jArr.length(); count++) {
                    JSONObject obj = jArr.getJSONObject(count);
                    hash = obj.getString("hash_tag");
                    hashtag.add(hash);

                }

            } catch (JSONException e) {
                // Log.e("catch", e.toString());
            }
            ArrayAdapter adapter = new ArrayAdapter<>(PostForm.this, android.R.layout.simple_list_item_1, hashtag);
            sto.setAdapter(adapter);

        } catch (Exception e) {
            Log.d("Exception", e + "");
        }
    }

}
