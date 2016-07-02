package com.example.udeys.theindianroute.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.udeys.theindianroute.CompressFilter;
import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.filters.EffectsFilterFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by udeys on 6/17/2016.
 */

public class PostFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener {
    static final int FOTO_MODE = 0;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    View view;
    double lat;
    double lon;
    Button bt;
    FragmentTransaction ft;
    String file;
    Uri imageUri;
    private LocationManager locationManager;
    private SurfaceView surefaceView;
    private SurfaceHolder surefaceHolder;
    private LocationListener locationListener;
    private Camera camera;
    private String make;
    private String model;
    private String imei;
    private String filename = "";
    Camera.PictureCallback pictureCallBack = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                Intent imgIntent = new Intent();
                if (storeByteImage(data)) {
                    camera.startPreview();
                    getActivity().setResult(FOTO_MODE, imgIntent);
                    Intent intent = new Intent(getActivity(), CompressFilter.class);
                    intent.putExtra("path", filename);
                    startActivity(intent);
                    //getActivity().finish();
                }

            }

        }
    };
    private Location thislocation;
    private boolean previewRunning = false;

    private static String formatLatLongString(double d) {
        StringBuilder b = new StringBuilder();
        b.append((int) d);
        b.append("/1,");
        d = (d - (int) d) * 60;
        b.append((int) d);
        b.append("/1,");
        d = (d - (int) d) * 60000;
        b.append((int) d);
        b.append("/1000");
        return b.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Toast.makeText(getActivity(), "GRANTED", Toast.LENGTH_LONG).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        // TODO Auto-generated method stub

        locationListener = new LocationListener() {

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }

            public void onLocationChanged(Location location) {

                PostFragment.this.gpsLocationReceived(location);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_COARSE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(true);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
        String providerName = locationManager.getBestProvider(locationCritera, true);

        if (providerName != null && locationManager.isProviderEnabled(providerName)) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            locationManager.requestLocationUpdates(providerName, 20000, 100, PostFragment.this.locationListener);
        } else {
            // Provider not enabled, prompt user to enable it
            Toast.makeText(getActivity(), "please_turn_on_gps", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            PostFragment.this.startActivity(myIntent);
        }

        if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {

            lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            lon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        } else if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
            Log.e("TAG", "Inside NETWORK");

            lat = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
            lon = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();

        } else {

            Log.e("TAG", "else +++++++ ");
            lat = -1;
            lon = -1;
        }


        if (Build.VERSION.SDK_INT < 22) {
            view = inflater.inflate(R.layout.postfragment, container, false);
            try {
                if (camera != null)
                    camera.release();

                surefaceView = (SurfaceView) view.findViewById(R.id.surface_camera);
                surefaceView.setOnClickListener(this);
                surefaceHolder = surefaceView.getHolder();
                surefaceHolder.addCallback(this);
                surefaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

                bt = (Button) view.findViewById(R.id.btncapture);
                bt.setOnClickListener(this);

            } catch (Exception e) {
                Toast.makeText(getActivity(), "catch: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            takePhoto();
            //view = inflater.inflate(R.layout.activity_compress, container, false);
        }
        return view;
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = String.format("/TIR%d.jpeg", System.currentTimeMillis());
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), file);
        filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + file;
        Log.e("path", filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);

        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getActivity(), "req"+requestCode, Toast.LENGTH_SHORT).show();
        //Bitmap bp = (Bitmap) data.getExtras().get("data");
        //storeByteImage(bp);


        switch (requestCode) {
            case 100:
                Toast.makeText(getActivity(), "saved", Toast.LENGTH_SHORT).show();
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;

                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        //filename = "";
                        storeByteImage(bitmap);

                        //viewHolder.imageView.setImageBitmap(bitmap);
                        //Toast.makeText(getActivity(), selectedImage.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
            default:
                //Toast.makeText(getActivity(), "Filename" +filename, Toast.LENGTH_SHORT).show();
                Log.e("filename", filename);
                File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + file);
                if (file1.delete())
                    Log.e("TAG", "Deleted");
                Intent intent = new Intent(getActivity(), EffectsFilterFragment.class);
                intent.putExtra("path", filename);
                startActivity(intent);

        }

    }

    public void storeByteImage(Bitmap bp) {

        /*String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        File filename = new File(myDir, "man.jpeg");*/

        filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + String.format("/TIR%d.jpeg", System.currentTimeMillis());
        Log.e("TAG", "filename = " + filename);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            try {
                fileOutputStream = new FileOutputStream(filename);
                bp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            Toast.makeText(getActivity(), "saved", Toast.LENGTH_SHORT).show();
            ExifInterface exif = new ExifInterface(filename.toString());
            createExifData(exif, lat, lon);
            exif.saveAttributes();


            /*Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String imagefilename = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Long latitide = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                Long longitude = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));

                Log.e("TAG", "filepath: " + imagefilename + " latitude = " + latitide + "  longitude = " + longitude);
            }*/

            //return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return false;
    }

    private void initFragments() {
        Bundle bundl = new Bundle();
        bundl.putString("filename", filename); // send imagePath

        /*
        * Resolve issue
        * */

    }

    protected void gpsLocationReceived(Location location) {

        thislocation = location;
    }

    public boolean storeByteImage(byte[] data) {

        /*String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        File filename = new File(myDir, "man.jpeg");*/


        filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + String.format("/TIR%d.jpeg", System.currentTimeMillis());
        Log.e("TAG", "filename = " + filename);


        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            try {
                fileOutputStream.write(data);
                Log.e("TAG", "Image file created, size in bytes = " + data.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            //Toast.makeText(getActivity(), "lat: " + lat + " ,lng: " + lon, Toast.LENGTH_SHORT).show();

            Log.e("TAG", "lat =" + lat + "  lon :" + lon);
            ExifInterface exif = new ExifInterface(filename.toString());
            createExifData(exif, lat, lon);
            exif.saveAttributes();

            /*Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String imagefilename = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Long latitide = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                Long longitude = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));

                Log.e("TAG", "filepath: " + imagefilename + " latitude = " + latitide + "  longitude = " + longitude);
            }*/

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createExifData(ExifInterface exif, double lattude, double longitude) {

        if (lattude < 0) {
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            lattude = -lattude;
        } else {
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
        }

        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
                formatLatLongString(lattude));

        if (longitude < 0) {
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            longitude = -longitude;
        } else {
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
        }
        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
                formatLatLongString(longitude));

        try {
            exif.saveAttributes();
        } catch (IOException e) {

            e.printStackTrace();
        }
        make = android.os.Build.MANUFACTURER; // get the make of the device
        model = android.os.Build.MODEL; // get the model of the divice

        exif.setAttribute(ExifInterface.TAG_MAKE, make);
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        exif.setAttribute(ExifInterface.TAG_MODEL, model);

        exif.setAttribute(ExifInterface.TAG_DATETIME, (new Date(System.currentTimeMillis())).toString()); // set the date & time

        Log.e("TAG", "Information : lat =" + lattude + "  lon =" + longitude + "  make = " + make + "  model =" + model + "  imei=" + imei + " time =" + (new Date(System.currentTimeMillis())).toString());
    }

    protected boolean isRouteDisplayed() {
        return false;
    }


    @Override
    public void onClick(View v) {

        camera.takePicture(null, pictureCallBack, pictureCallBack);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            //camera.setPreviewDisplay(holder);
        } catch (RuntimeException e) {
            Toast.makeText(getActivity(), "error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        //camera.startPreview();
        //previewRunning = true;

        //Camera.Parameters parameters = camera.getParameters();


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewRunning) {
            camera.stopPreview();
        }

        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        previewRunning = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            camera.stopPreview();
        } catch (Exception e) {

        }
        previewRunning = false;
        camera.release();

    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cameramenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item01:
                Toast.makeText(this, "Pressed !", Toast.LENGTH_LONG).show();
                break;
            case R.id.item03:
                System.exit(0);
                break;
        }
        return true;
    }*/

    @Override
    public void onPause() {
        super.onPause();
        if (camera != null)
            camera.release();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            locationManager.removeUpdates(this.locationListener);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (camera != null)
            camera.release();

    }


}
