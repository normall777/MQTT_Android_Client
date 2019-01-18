package com.normall.mqttandroidclient;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class MyNotification {
    private static NotificationCompat.Builder builder;
    private static Notification notification;
    private static NotificationManager notificationManager;
    private static int counter = 1;


    public static void showNotification(Activity activity, String message){
        MyNotification.builder = new NotificationCompat.Builder(activity).setSmallIcon(R.drawable.ic_email_green_700_24dp)
                .setContentTitle(activity.getText(R.string.app_name))
                .setContentText(message).setColor(activity.getResources().getColor(R.color.colorWhite));
        notification = builder.build();
        notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(counter,notification);
        counter++;
    }
}
