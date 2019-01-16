package com.normall.mqttandroidclient;

import android.arch.lifecycle.AndroidViewModel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.eclipse.paho.android.service.MqttAndroidClient;


public class ConsoleMqttFragment extends Fragment {

    public MainActivity mainActivity;
    public MqttAndroidClient mqttMyClient = null;
    private ArrayAdapter<String> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_console_mqtt, null);
        mqttMyClient = MqttConnection.getClient();
        String[] catNamesArray = new String[] { "Рыжик", "Барсик", "Мурзик",
                "Мурка", "Васька", "Томасина", "Бобик", "Кристина", "Пушок",
                "Дымка", "Кузя", "Китти", "Барбос", "Масяня", "Симба" };
        ListView listViewConsole = (ListView) v.findViewById(R.id.lv_console);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, catNamesArray);
        listViewConsole.setAdapter(adapter);


        return v;
    }


}
