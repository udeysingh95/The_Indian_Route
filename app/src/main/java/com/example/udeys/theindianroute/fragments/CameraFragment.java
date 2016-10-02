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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.udeys.theindianroute.PostForm;
import com.example.udeys.theindianroute.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by udeys on 6/17/2016.
 */

public class CameraFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener {
    static final int FOTO_MODE = 0;
    private static final int TAKE_PICTURE = 1;
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
                    Log.e("camera", "starting intent");
                    Intent intent = new Intent(getActivity(), PostForm.class);
                    intent.putExtra("post_image", filename);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // TODO Auto-generated method stub

        locationListener = new LocationListener() {

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }

            public void onLocationChanged(Location location) {

                CameraFragment.this.gpsLocationReceived(location);

                try {
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
                } catch (Exception e) {
                    Log.e("try", e.toString());
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
            locationManager.requestLocationUpdates(providerName, 20000, 100, CameraFragment.this.locationListener);
        } else {
            // Provider not enabled, prompt user to enable it
            Toast.makeText(getActivity(), "please_turn_on_gps", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            CameraFragment.this.startActivity(myIntent);
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


        if (Build.VERSION.SDK_INT < 21) {
            view = inflater.inflate(R.layout.camera_fragment, container, false);
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
        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);
                        storeByteImage(bitmap);

                        //Toast.makeText(getActivity(), selectedImage.toString(),Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load" + e.toString(), Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }

    public boolean dir_exists(String dir_path) {
        boolean ret = false;
        File dir = new File(dir_path);
        if (dir.exists() && dir.isDirectory())
            ret = true;
        return ret;
    }


    public void storeByteImage(Bitmap bp) {

        String sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        String dir_path = Environment.getExternalStorageDirectory() + "/TheIndianRoute";

        if (!dir_exists(dir_path)) {
            File directory = new File(dir_path);
            directory.mkdir();
        }

        filename = Environment.getExternalStoragePublicDirectory("TheIndianRoute/TIR") + sdf + ".jpeg";

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            try {
                fileOutputStream = new FileOutputStream(filename);
                bp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent intent = new Intent(getActivity(), PostForm.class);
            intent.putExtra("post_image", filename);
            startActivity(intent);


        } catch (FileNotFoundException e) {
            Log.d("catch", e.toString());
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

        String sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String dir_path = Environment.getExternalStorageDirectory() + "/TheIndianRoute";

        if (!dir_exists(dir_path)) {
            File directory = new File(dir_path);
            if (directory.mkdir())
                Log.e("mkdir", String.valueOf(directory.mkdir()));
        }

        filename = Environment.getExternalStoragePublicDirectory("TheIndianRoute/TIR") + sdf + ".jpeg";

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            try {
                fileOutputStream.write(data);
            } catch (IOException e) {
                Log.e("TAG-byte-catch", "error " + e.toString());
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            //Toast.makeText(getActivity(), "lat: " + lat + " ,lng: " + lon, Toast.LENGTH_SHORT).show();

            ExifInterface exif = new ExifInterface(filename.toString());
            createExifData(exif, lat, lon);
            exif.saveAttributes();
            return true;

        } catch (FileNotFoundException e) {
            Log.e("TAG-byte-catch", "error " + e.toString());
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

        Log.d("TAG", "Information : lat =" + lattude + "  lon =" + longitude + "  make = " + make + "  model =" + model + "  imei=" + imei + " time =" + (new Date(System.currentTimeMillis())).toString());
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
            setCameraDisplayOrientation();
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

    public void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; //Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; //Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;//Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;//Landscape right
        }
        int rotate = (info.orientation - degrees + 360) % 360;

//STEP #2: Set the 'rotation' parameter
        Camera.Parameters params = camera.getParameters();
        params.setRotation(rotate);
        camera.setParameters(params);
    }


}
