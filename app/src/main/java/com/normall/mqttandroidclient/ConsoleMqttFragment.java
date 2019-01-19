package com.normall.mqttandroidclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class ConsoleMqttFragment extends Fragment {

    public ListView listViewConsole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_console_mqtt, null);
        listViewConsole = (ListView) v.findViewById(R.id.lv_console);
        listViewConsole.setAdapter(MessagesArray.getAdapter());
        Log.i("MyApp", "onCreateView");
        ((MainActivity) getActivity()).ChangeVisualInterface();
        return v;
    }
}
