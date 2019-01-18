package com.normall.mqttandroidclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ControlFragment extends Fragment {

    private Button buttonLight;
    private Button buttonNotification;
    private Button buttonDial;
    private boolean lightOn = false;
    private String lightMessage = StringCommands.COMMAND_LIGHT_ON;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_control, null);
        buttonLight = (Button) v.findViewById(R.id.button_light);
        SetButtonLightOn(lightOn);
        buttonNotification = (Button) v.findViewById(R.id.button_notification);
        buttonDial = (Button) v.findViewById(R.id.button_dial);

        buttonLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttConnection.sendMQTTMessage("phones/slave/execute", lightMessage);
                //MqttConnection.sendMQTTMessage("control","lightOn");
            }
        });

        buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttConnection.sendMQTTMessage("phones/slave/execute", StringCommands.COMMAND_NOTIFICATION);
            }
        });

        buttonDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttConnection.sendMQTTMessage("phones/slave/execute", StringCommands.COMMAND_DIAL);
            }
        });


        return v;
    }

    public void SetButtonLightOn(boolean sost){
        lightOn = sost;

        if (lightOn){
            buttonLight.setText(R.string.btn_text_off_light);
            lightMessage = StringCommands.COMMAND_LIGHT_OFF;
        }

        else {
            buttonLight.setText(R.string.btn_text_on_light);
            lightMessage = StringCommands.COMMAND_LIGHT_ON;
        }

    }

}
