package com.normall.mqttandroidclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    //Файл настроек
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_IP_ADD = "ip_add";
    public static final String APP_PREFERENCES_PORT_MQTT = "mqtt_port";

    //Инициализация элементов формы для доступа
    private EditText editTextIpAdd;
    private EditText editTextPortMQTT;
    private SharedPreferences mSettings;
    private Button buttonConnect;
    private Button buttonSendTest;
    private MqttAndroidClient mqttMyClient = null;

    //Внутрненние параметры
    private String ipAdd;
    private String mqttPort;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_connect:

                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Меню
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Файл настроек
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        //Инициализация эелементов
        editTextIpAdd = (EditText) findViewById(R.id.editTextIpAdd);
        editTextPortMQTT = (EditText) findViewById(R.id.editTextPortMQTT);
        buttonConnect = (Button) findViewById(R.id.buttonConnect);
        buttonSendTest = (Button) findViewById(R.id.buttonSendTest);

        //Инициализация переменных



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
                String topic = "test";
                String payload = "test";
                byte[] encodedPayload = new byte[0];
                try{
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    mqttMyClient.publish(topic,message);
                }catch (UnsupportedEncodingException | MqttException e){
                    e.printStackTrace();
                }



            }
        });
    }

    private void connect() {
        final String clientId = MqttClient.generateClientId();
        String serverURL = "tcp://" + ipAdd + ":" + mqttPort;
        mqttMyClient = new MqttAndroidClient(this.getApplicationContext(),
                serverURL, clientId);

        try {
            IMqttToken token = mqttMyClient.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getApplicationContext(),"Ура, работает\n"+clientId, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getApplicationContext(),"Блин, не работает", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(),"Ок, отсоединились", Toast.LENGTH_SHORT).show();
                    mqttMyClient = null;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getApplicationContext(),"Хм, что-то не так", Toast.LENGTH_SHORT).show();
                    mqttMyClient = null;
                }
            });
        } catch (MqttException e){
            e.printStackTrace();
        }

    }



    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }



    @Override
    protected void onPause(){
        super.onPause();
        ipAdd = editTextIpAdd.getText().toString();
        mqttPort = editTextPortMQTT.getText().toString();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_IP_ADD, ipAdd);
        editor.putString(APP_PREFERENCES_PORT_MQTT, mqttPort);
        editor.apply();
    }

    @Override
    protected void onResume(){
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
