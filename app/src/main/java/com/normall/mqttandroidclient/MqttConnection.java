package com.normall.mqttandroidclient;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MqttConnection {
    private static MqttAndroidClient client;

    public static MqttAndroidClient getClient() {
        return client;
    }

    public static void setClient(MqttAndroidClient client) {
        MqttConnection.client = client;
    }

    public static boolean disconnect() {
        try{
            IMqttToken disconToken = MqttConnection.getClient().disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MqttConnection.setClient(null);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    MqttConnection.setClient(null);
                }
            });
            return true;
        } catch (MqttException e){
            e.printStackTrace();
            return false;
        }

    }


    public static boolean connect(String ipAdd, String mqttPort, final Context context, final SettingsFragment fragment) {
        final String clientId = MqttClient.generateClientId();
        String serverURL = "tcp://" + ipAdd + ":" + mqttPort;
        MqttConnection.setClient(new MqttAndroidClient(context, //this.getActivity().getApplicationContext()
                serverURL, clientId));

        try {
            IMqttToken token = MqttConnection.getClient().connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MqttConnection.sendMQTTMessage("test","Hello, I am "+clientId);
                    Toast.makeText(context,"Успешно! Тебя зовут\n"+clientId, Toast.LENGTH_SHORT).show();
                    MqttConnection.subscribe();
                    fragment.ChangeVisualInterface();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context,"Блин, не работает", Toast.LENGTH_SHORT).show();
                    MqttConnection.setClient(null);
                    fragment.ChangeVisualInterface();
                }
            });
            return true;
        } catch (MqttException e){
            e.printStackTrace();
            return false;
        }
    }


    public static boolean subscribe(){
        String topic = "test";
        try {
            if (!MqttConnection.getClient().isConnected()) return false;
            IMqttToken subToken = MqttConnection.getClient().subscribe(topic,0);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                }
            });
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static boolean sendMQTTMessage(String topic, String message) {
        if (MqttConnection.getClient()==null){
            return false;
        }

        byte[] encodedPayload = new byte[0];
        try{
            encodedPayload = message.getBytes("UTF-8");
            MqttMessage mqttMessage = new MqttMessage(encodedPayload);
            MqttConnection.getClient().publish(topic,mqttMessage);
            return true;
        }catch (UnsupportedEncodingException | MqttException e){
            e.printStackTrace();
            return false;
        }
    }


}