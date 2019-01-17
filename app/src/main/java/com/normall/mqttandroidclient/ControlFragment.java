package com.normall.mqttandroidclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.security.Policy;

public class ControlFragment extends Fragment {

    private Button buttonLightOn;
    private boolean lightOn;
    private String lightMessage = "lightOn";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_control, null);
        buttonLightOn = (Button) v.findViewById(R.id.button_light);


        buttonLightOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttConnection.sendMQTTMessage("phones/slave/execute", lightMessage);
                //MqttConnection.sendMQTTMessage("control","lightOn");
            }
        });
        return v;
    }

    public void SetButtonLightOn(boolean sost){
        lightOn = sost;

        if (lightOn){
            buttonLightOn.setText(R.string.btn_text_off_light);
            lightMessage = "lightOff";
        }

        else {
            buttonLightOn.setText(R.string.btn_text_on_light);
            lightMessage = "lightOn";
        }

    }

}
