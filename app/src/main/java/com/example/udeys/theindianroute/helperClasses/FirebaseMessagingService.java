package com.example.udeys.theindianroute.helperClasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.example.udeys.theindianroute.MenuActivity;
import com.example.udeys.theindianroute.R;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Created by Malhotra G on 6/30/2016.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    int NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showNotification(remoteMessage.getData().get("message"));
    }

    public int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    private void showNotification(String message) {
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtra("notification", "openNF");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("The Indian Route")
                .setSmallIcon(R.drawable.icon)
                .setContentText(message)
                .setSound(alarmSound)
                .setLights(Color.BLUE, 500, 500)
                .setVibrate(new long[]{100, 250, 100, 250, 100, 250})
                .setContentIntent(pendingIntent);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn;
        if (Build.VERSION.SDK_INT <= 19) {
            isScreenOn = pm.isScreenOn();
        } else {
            isScreenOn = pm.isInteractive();
        }

        //Log.e("screen on...........", ""+isScreenOn);

        if (isScreenOn == false) {

            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");

            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");

            wl_cpu.acquire(10000);
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(generateRandom(), builder.build());

    }
}
