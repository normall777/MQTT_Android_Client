package com.normall.mqttandroidclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

    public MainActivity mainActivity;
    public MqttAndroidClient mqttMyClient = null;
    IMqttToken tokenForMessages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, null);

        editTextIpAdd = (EditText) v.findViewById(R.id.editTextIpAdd);
        editTextPortMQTT = (EditText) v.findViewById(R.id.editTextPortMQTT);
        buttonConnect = (Button) v.findViewById(R.id.buttonConnect);
        buttonSendTest = (Button) v.findViewById(R.id.buttonSendTest);
        mainActivity = (MainActivity) getActivity();
        mqttMyClient = mainActivity.mqttMyClient;

        if (mqttMyClient == null){
            buttonConnect.setText(getText(R.string.btn_text_connect));
            buttonSendTest.setVisibility(View.INVISIBLE);
        } else if (mqttMyClient.isConnected()){
            buttonConnect.setText(getText(R.string.btn_text_disconnect));
            buttonSendTest.setVisibility(View.VISIBLE);
        }


        mSettings = getContext().getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipAdd = editTextIpAdd.getText().toString();
                mqttPort = editTextPortMQTT.getText().toString();
                if (mqttMyClient == null){
                    connect();
                    buttonConnect.setText(getText(R.string.btn_text_disconnect));
                    buttonSendTest.setVisibility(View.VISIBLE);

                } else if (mqttMyClient.isConnected()){
                    disconnect();
                    buttonConnect.setText(getText(R.string.btn_text_connect));
                    buttonSendTest.setVisibility(View.INVISIBLE);
                }

            }
        });

        buttonSendTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.sendMQTTMessage("test","test");
            }
        });


        return v;
    }





    private void subscribe(){
        String topic = "test";
        try {
            if (!mqttMyClient.isConnected()) return;
            IMqttToken subToken = mqttMyClient.subscribe(topic,0);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getActivity().getApplicationContext(),"Ставьте лайки, подписывайтесь на наш канал!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getActivity().getApplicationContext(),"Проблемы с подпиской", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    private void connect() {
        final String clientId = MqttClient.generateClientId();
        String serverURL = "tcp://" + ipAdd + ":" + mqttPort;
        mqttMyClient = new MqttAndroidClient(this.getActivity().getApplicationContext(),
                serverURL, clientId);

        try {
            IMqttToken token = mqttMyClient.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getActivity().getApplicationContext(),"Ура, работает\n"+clientId, Toast.LENGTH_LONG).show();
                    mainActivity.mqttMyClient = mqttMyClient;
                    mainActivity.sendMQTTMessage("test","Hello, I am "+clientId);
                    subscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getActivity().getApplicationContext(),"Блин, не работает", Toast.LENGTH_SHORT).show();
                    mqttMyClient = null;
                    mainActivity.mqttMyClient = null;

                }
            });
        } catch (MqttException e){
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try{
            IMqttToken disconToken = mqttMyClient.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getActivity().getApplicationContext(),"Ок, отсоединились", Toast.LENGTH_SHORT).show();
                    mqttMyClient = null;
                    mainActivity.mqttMyClient = null;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getActivity().getApplicationContext(),"Хм, что-то не так", Toast.LENGTH_SHORT).show();
                    mqttMyClient = null;
                    mainActivity.mqttMyClient = null;
                }
            });
        } catch (MqttException e){
            e.printStackTrace();
        }

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
