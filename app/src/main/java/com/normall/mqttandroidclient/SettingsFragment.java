package com.normall.mqttandroidclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


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

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, null);

        editTextIpAdd = (EditText) v.findViewById(R.id.editTextIpAdd);
        editTextPortMQTT = (EditText) v.findViewById(R.id.editTextPortMQTT);
        buttonConnect = (Button) v.findViewById(R.id.buttonConnect);
        buttonSendTest = (Button) v.findViewById(R.id.buttonSendTest);
        mainActivity = (MainActivity) getActivity();

        ChangeVisualInterface();
        final SettingsFragment fragment = this;

        mSettings = getContext().getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipAdd = editTextIpAdd.getText().toString();
                mqttPort = editTextPortMQTT.getText().toString();
                if (MqttConnection.getClient() == null){
                    boolean status = MqttConnection.connect(ipAdd, mqttPort, getActivity().getApplicationContext(), fragment);
                    MqttConnection.getClient().setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {
                            Toast.makeText(getActivity().getApplicationContext(),"Sorry, the connection is lost! :c", Toast.LENGTH_LONG).show();
                            MqttConnection.setClient(null);
                            ChangeVisualInterface();
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            Toast.makeText(getActivity().getApplicationContext(),"ОООООО, СМСКА ПРИШЛА, ПРИКИИИНЬ?!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {
                            Toast.makeText(getActivity().getApplicationContext(),"Мур, доставка завершена вроде как, это кайф!", Toast.LENGTH_LONG).show();
                        }
                    });

                } else if (MqttConnection.getClient().isConnected()){
                    MqttConnection.disconnect();
                }
            }
        });

        buttonSendTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttConnection.sendMQTTMessage("test","test");
            }
        });
        return v;
    }

    public void ChangeVisualInterface() {
        if (MqttConnection.getClient() == null){
            buttonConnect.setText(getText(R.string.btn_text_connect));
            buttonSendTest.setVisibility(View.INVISIBLE);
        } else if (MqttConnection.getClient().isConnected()){
            buttonConnect.setText(getText(R.string.btn_text_disconnect));
            buttonSendTest.setVisibility(View.VISIBLE);
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
