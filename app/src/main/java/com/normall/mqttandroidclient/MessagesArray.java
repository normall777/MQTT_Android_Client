package com.normall.mqttandroidclient;

import android.app.Activity;
import android.widget.ArrayAdapter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.EventListener;

public class MessagesArray {
    private static ArrayList<String> messages = new ArrayList<String>();
    private static ArrayAdapter<String> adapter;

    public static void setAdapter(Activity activity) {
        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, MessagesArray.getMessages());
    }

    public static ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    public static void addMessage(String s){
        messages.add(s);
    }

    public static ArrayList<String> getMessages() {
        return messages;
    }

}
