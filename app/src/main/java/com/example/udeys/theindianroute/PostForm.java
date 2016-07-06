package com.example.udeys.theindianroute;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class PostForm extends Activity implements View.OnClickListener {

    String story, checkin;
    EditText s, c;
    String username;
    byte[] byteArray;
    Bitmap bmp;
    String filename, oldFile;
    File i;
    Double Latitude, Longitude;
    private boolean valid = false;

    public static void copyExif(String oldPath, String newPath) throws IOException {
        ExifInterface oldExif = new ExifInterface(oldPath);

        String[] attributes = new String[]
                {
                        ExifInterface.TAG_APERTURE,
                        ExifInterface.TAG_DATETIME,
                        ExifInterface.TAG_EXPOSURE_TIME,
                        ExifInterface.TAG_FLASH,
                        ExifInterface.TAG_FOCAL_LENGTH,
                        ExifInterface.TAG_GPS_ALTITUDE,
                        ExifInterface.TAG_GPS_ALTITUDE_REF,
                        ExifInterface.TAG_GPS_DATESTAMP,
                        ExifInterface.TAG_GPS_LATITUDE,
                        ExifInterface.TAG_GPS_LATITUDE_REF,
                        ExifInterface.TAG_GPS_LONGITUDE,
                        ExifInterface.TAG_GPS_LONGITUDE_REF,
                        ExifInterface.TAG_GPS_PROCESSING_METHOD,
                        ExifInterface.TAG_GPS_TIMESTAMP,
                        ExifInterface.TAG_IMAGE_LENGTH,
                        ExifInterface.TAG_IMAGE_WIDTH,
                        ExifInterface.TAG_ISO,
                        ExifInterface.TAG_MAKE,
                        ExifInterface.TAG_MODEL,
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.TAG_WHITE_BALANCE
                };

        ExifInterface newExif = new ExifInterface(newPath);
        for (int i = 0; i < attributes.length; i++) {
            String value = oldExif.getAttribute(attributes[i]);
            if (value != null)
                newExif.setAttribute(attributes[i], value);
        }
        newExif.saveAttributes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("user_details", MODE_PRIVATE);
        username = sp.getString("username", "udeysingh95");
        setContentView(R.layout.activity_post_form);
        s = (EditText) findViewById(R.id.post_story);
        c = (EditText) findViewById(R.id.post_checkin);
        Button push_post = (Button) findViewById(R.id.push_post);
        filename = getIntent().getStringExtra("post_image");
        oldFile = getIntent().getStringExtra("old_image");
        /*try {
            copyExif(oldFile,filename);
        } catch (IOException e) {
            Log.e("exif catch",e.toString());
        }*/
        push_post.setOnClickListener(this);
        try {
            c.setText(ReadExif(filename));
        } catch (Exception e) {
            Log.e("catch", e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        story = s.getText().toString();
        checkin = c.getText().toString();

        i = get();
        pushPost();
    }

    public void pushPost() {
        try {
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            /*
            * Bind parameters here
            * */
            RequestParams params = new RequestParams();
            try {
                params.put("username", username);
                params.put("check_in", checkin);
                params.put("story", story);
                params.put("picture", i);
            } catch (Exception e) {
                //Toast.makeText(this, "1" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            client.post("http://indianroute.roms4all.com/post.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            Log.d("error", res);
                            //Toast.makeText(PostForm.this, "2" + res, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplication(), MenuActivity.class);
                            startActivity(i);
                            finish();
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

    private File get() {

        File sd = Environment.getExternalStorageDirectory();
        File location = new File(sd.getAbsolutePath() + "/TheIndianRoute");
        location.mkdir();
        return new File(location, filename);
    }

    /*
    * Read Location from Image
    * */

    public String ReadExif(String file) {
        try {
            ExifInterface exif = new ExifInterface(file);
            String attrLATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String attrLATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String attrLONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String attrLONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

            if ((attrLATITUDE != null) && (attrLATITUDE_REF != null) && (attrLONGITUDE != null) && (attrLONGITUDE_REF != null)) {
                valid = true;

                if (attrLATITUDE_REF.equals("N")) {
                    Latitude = convertToDegree(attrLATITUDE);
                } else {
                    Latitude = 0 - convertToDegree(attrLATITUDE);
                }

                if (attrLONGITUDE_REF.equals("E")) {
                    Longitude = convertToDegree(attrLONGITUDE);
                } else {
                    Longitude = 0 - convertToDegree(attrLONGITUDE);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        Geocoder gcd = new Geocoder(PostForm.this, Locale.getDefault());
        List<Address> addresses = null;
        String location = null;
        try {
            addresses = gcd.getFromLocation(Latitude, Longitude, 1);
            if (addresses.size() > 0)
                location = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return location;
    }

    private Double convertToDegree(String stringDMS) {
        Double result;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Double(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;
    }

}
