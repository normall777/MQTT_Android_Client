package com.normall.mqttandroidclient;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

public class MyDial {
    private static Intent intent;


    public static void StartDial(Activity activity, String phoneNumber){
        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
        if (intent.resolveActivity((activity.getPackageManager()))!= null){
            activity.startActivity(intent);
        }
    }
}
