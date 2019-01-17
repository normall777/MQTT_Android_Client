package com.normall.mqttandroidclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class ConsoleMqttFragment extends Fragment {

    public ListView listViewConsole;
    private void ReloadList(){
        listViewConsole.setAdapter(MessagesArray.getAdapter());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_console_mqtt, null);
        listViewConsole = (ListView) v.findViewById(R.id.lv_console);
        ReloadList();
        Log.i("MyApp", "onCreateView");


        return v;
    }




}
