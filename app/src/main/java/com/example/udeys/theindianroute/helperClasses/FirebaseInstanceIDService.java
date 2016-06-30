package com.example.udeys.theindianroute.helperClasses;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Malhotra G on 6/30/2016.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        //Log.e("token" , token);

        //registerToken(token);
    }

    /*private void registerToken(String token) {

        //Log.e("token" , token);

        try {
            SyncHttpClient client = new SyncHttpClient();
            /*
            * Bind parameters here
            * */
            /*RequestParams params = new RequestParams();
            try {
                params.put("device_token", token);
                Log.e("token" , token);

            } catch (Exception e) {
                Log.e("catch1" , e.toString());
            }
            client.post(this, "http://indianroute.roms4all.com/register_fcm.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.e("success", responseString);
                }
            });
        } catch (Exception e) {
            Log.e("catch2" , e.toString());
        }
    }*/
}
