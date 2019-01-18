package com.normall.mqttandroidclient;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

public class MyDial {
    private static Intent intent;

    public static void Initialize(){
        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1234567"));
    }

    public static void StartDial(Activity activity){
        if (intent.resolveActivity((activity.getPackageManager()))!= null){
            activity.startActivity(intent);
        }
    }
}