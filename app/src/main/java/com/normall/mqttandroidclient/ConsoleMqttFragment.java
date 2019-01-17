package com.normall.mqttandroidclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class ConsoleMqttFragment extends Fragment {

    private ListView listViewConsole;
    private void ReloadList(){
        MessagesArray.setAdapter(getActivity());
        listViewConsole.setAdapter(MessagesArray.getAdapter());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_console_mqtt, null);
        listViewConsole = (ListView) v.findViewById(R.id.lv_console);
        ReloadList();


        return v;
    }




}
