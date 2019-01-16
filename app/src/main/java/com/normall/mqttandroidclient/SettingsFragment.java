package com.normall.mqttandroidclient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


public class SettingsFragment extends Fragment {
    //Файл настроек
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_IP_ADD = "ip_add";
    public static final String APP_PREFERENCES_PORT_MQTT = "mqtt_port";

    //Внутрненние параметры
    private String ipAdd;
    private String mqttPort;

    //Инициализация элементов формы для доступа
    private EditText editTextIpAdd;
    private EditText editTextPortMQTT;
    private SharedPreferences mSettings;
    private Button buttonConnect;
    private Button buttonSendTest;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, null);

        editTextIpAdd = (EditText) v.findViewById(R.id.editTextIpAdd);
        editTextPortMQTT = (EditText) v.findViewById(R.id.editTextPortMQTT);
        buttonConnect = (Button) v.findViewById(R.id.buttonConnect);
        buttonSendTest = (Button) v.findViewById(R.id.buttonSendTest);

        mSettings = getContext().getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipAdd = editTextIpAdd.getText().toString();
                mqttPort = editTextPortMQTT.getText().toString();
                getActivity().startService(new Intent(getActivity(), MQTTService.class)
                        .putExtra("ipAdd",ipAdd)
                        .putExtra("mqttPort",mqttPort));
                /*if (mqttMyClient == null){

                    connect(); //При подключении отправляется сообщение в чат
                    buttonConnect.setText(getText(R.string.btn_text_disconnect));
                    buttonSendTest.setVisibility(View.VISIBLE);

                } else if (mqttMyClient.isConnected()){
                    disconnect();
                    buttonConnect.setText(getText(R.string.btn_text_connect));
                    buttonSendTest.setVisibility(View.INVISIBLE);
                }*/

            }
        });

        buttonSendTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String topic = "test";
                String payload = "test";
                byte[] encodedPayload = new byte[0];
                try{
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    mqttMyClient.publish(topic,message);
                }catch (UnsupportedEncodingException | MqttException e){
                    e.printStackTrace();
                }*/



            }
        });


        return v;
    }








    @Override
    public void onPause(){
        super.onPause();
        ipAdd = editTextIpAdd.getText().toString();
        mqttPort = editTextPortMQTT.getText().toString();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_IP_ADD, ipAdd);
        editor.putString(APP_PREFERENCES_PORT_MQTT, mqttPort);
        editor.apply();
    }

    @Override
    public void onResume(){
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_IP_ADD)){
            ipAdd = mSettings.getString(APP_PREFERENCES_IP_ADD, getString(R.string.default_ip_add));
            editTextIpAdd.setText(ipAdd);
        }

        if (mSettings.contains(APP_PREFERENCES_PORT_MQTT)) {
            mqttPort = mSettings.getString(APP_PREFERENCES_PORT_MQTT, getString(R.string.default_mqtt_port));
            editTextPortMQTT.setText(mqttPort);
        }
    }

}
