package com.normall.mqttandroidclient;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MessagesArray {
    private static ArrayList<String> messages;
    private static ArrayAdapter<String> adapter;

    public static void setAdapter(ArrayAdapter<String> arrayAdapter) {
        adapter = arrayAdapter;
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

    public static void setMessages(ArrayList<String> messages) {
        MessagesArray.messages = messages;
    }
}
