package com.normall.mqttandroidclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MQTTService extends Service {
    public MqttAndroidClient mqttMyClient = null;


    public MQTTService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        String ipAdd = intent.getStringExtra("ipAdd") ;
        String mqttPort = intent.getStringExtra("mqttPort");
        connect(ipAdd, mqttPort);

       return super.onStartCommand(intent, flags, startId);
    }



    private void connect(String ipAdd, String mqttPort) {
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
                    String topic = "test";
                    String payload = "Hello, I am " + clientId  ;
                    byte[] encodedPayload = new byte[0];
                    try{
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        mqttMyClient.publish(topic,message);
                    }catch (UnsupportedEncodingException | MqttException e){
                        e.printStackTrace();
                    }

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


    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
