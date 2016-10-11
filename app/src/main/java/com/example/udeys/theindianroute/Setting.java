package com.example.udeys.theindianroute;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Setting extends Activity  implements View.OnClickListener{

    ImageButton cancel;
    EditText website, email, phone;
    SwitchCompat notification;
    LinearLayout l1;
    Button save;
    String id = "22" , gender;
    TextView title, name, username, male, female, editBtn;
    SharedPreferences sp;
    RoundedImageView pp;
    File imageFile;
    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sp = this.getSharedPreferences("user_details", MODE_PRIVATE);
        id = sp.getString("user_id", null);

        title = (TextView) findViewById(R.id.title);
        title.setText("Settings");

        editBtn = (TextView) findViewById(R.id.editBtn);

        cancel = (ImageButton) findViewById(R.id.btn_back);
        save = (Button) findViewById(R.id.ic_save);

        pp = (RoundedImageView) findViewById(R.id.profile_picture);

        notification = (SwitchCompat) findViewById(R.id.not_button);
        name = (TextView)findViewById(R.id.name);
        username = (TextView)findViewById(R.id.username);

        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        male = (TextView) findViewById(R.id.male);
        female = (TextView) findViewById(R.id.female);
        website = (EditText)findViewById(R.id.website);
        l1 = (LinearLayout) findViewById(R.id.logout);

        getDetails();
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        l1.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        editBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_back:
                finish();
                break;
            case R.id.ic_save:
                save_details();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.male:
                male.setBackgroundResource(R.drawable.login_edittext);
                male.setTextColor(Color.parseColor("#ccffffff"));
                female.setBackgroundResource(0);
                female.setTextColor(Color.parseColor("#888888"));
                gender = "M";
                break;
            case R.id.female:
                male.setBackgroundResource(0);
                male.setTextColor(Color.parseColor("#888888"));
                female.setBackgroundResource(R.drawable.login_edittext);
                female.setTextColor(Color.parseColor("#ccffffff"));
                gender = "F";
                break;
            case R.id.editBtn:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    imageUri = imageReturnedIntent.getData();
                    //imageFile = new File(getRealPathFromURI(imageUri));
                    imageFile = new File(compressImage(getRealPathFromURI(imageUri)));
                    Log.e("path-settings", imageFile.toString());
                    uploadProfileImage();
                    //Log.e("path-settings",imageFile.toString());

                }
        }
    }

    public String compressImage(String filePath) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "TheIndianRoute");
        if (!file.exists()) {
            file.mkdirs();
        }
        String sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String uriSting = (file.getAbsolutePath() + "/" + "TIR" + sdf + ".jpeg");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private void uploadProfileImage() {

        try {
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            RequestParams params = new RequestParams();
            params.put("user_id", id);
            params.put("image", imageFile);

            client.post("http://theindianroute.net/post_profile_picture.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("fail-settings", responseString);
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String res) {
                    Log.e("result-settings", res);
                    Toast.makeText(getApplicationContext(), "success\n" + res, Toast.LENGTH_LONG).show();
                    Picasso.with(getApplicationContext()).load(imageUri).resize(300, 300).centerCrop().into(pp);
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    * Gives exact path in phone storage
    * */
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void logout(){
        sp.edit().remove("user_id").apply();
        sp.edit().remove("username").apply();

        Intent i = new Intent(this , Splash.class);
        startActivity(i);
        finish();

    }

    public void save_details(){
        String notifications;
        if(notification.isChecked()){
            notifications = "1";
        }
        else {
            notifications = "0";
        }
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("name", name.getText());
        params.put("email", email.getText());
        params.put("website", website.getText());
        params.put("gender", gender);
        params.put("phone", phone.getText());
        params.put("notification", notifications);
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get("http://theindianroute.net/update_setting.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                        finish();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        Toast.makeText(getApplicationContext(), "can not save!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    public  void getDetails(){
                RequestParams params = new RequestParams();
                params.put("id", id);
                AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get("http://theindianroute.net/setting.php", params, new TextHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String res) {

                                decodeJson(res);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Toast.makeText(getApplicationContext() , "Failed initialization", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

    }

    private void decodeJson(String result) {
        try {
            JSONArray jArr = new JSONArray(result);
            String userprofilePicture;

            JSONObject obj = jArr.getJSONObject(0);
            userprofilePicture = obj.getString("userProfilePicture");


            name.setText(obj.getString("name"));
            email.setText(obj.getString("email"));
            phone.setText(obj.getString("phone"));

            if(obj.getString("gender").equals("M")){
                male.setBackgroundResource(R.drawable.login_edittext);
                male.setTextColor(Color.parseColor("#ccffffff"));
                female.setBackgroundResource(0);
                female.setTextColor(Color.parseColor("#888888"));
                gender = "M";
            }
            else{
                gender = "F";
            }

            username.setText(obj.getString("username"));
            website.setText(obj.getString("username"));

            int not = Integer.parseInt(obj.getString("notification"));
            if(not == 1){
                notification.setChecked(true);
            }
            else{
                notification.setChecked(false);
            }

            //may throw some error

            if (userprofilePicture.equals("http://theindianroute.net/uploads/users_profile_pictures/"))
                Picasso.with(getApplicationContext()).load(R.drawable.dummyprofile).resize(300, 300).centerCrop().into(pp);
            else
                Picasso.with(getApplicationContext()).load(userprofilePicture).resize(300, 300).centerCrop().into(pp);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
