package com.normall.mqttandroidclient;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MqttConnection {
    private static MqttAndroidClient client;

    public static MqttAndroidClient getClient() {
        return client;
    }

    public static void setClient(MqttAndroidClient client) {
        MqttConnection.client = client;
    }

    public static boolean isConnected(){
        return getClient().isConnected();
    }


    public static boolean disconnect(final MainActivity activity) {
        try{
            IMqttToken disconToken = MqttConnection.getClient().disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("MyApp", "Это мое сообщение о заходе в disconnect");
                    MqttConnection.setClient(null);
                    activity.ChangeVisualInterface();
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


    public static boolean connect(final MainActivity activity, String ipAdd, String mqttPort, final boolean workMode) {
        final String clientId = MqttClient.generateClientId();
        final Context context = activity.getApplicationContext();
        String serverURL = "tcp://" + ipAdd + ":" + mqttPort;
        MqttConnection.setClient(new MqttAndroidClient(context, //this.getActivity().getApplicationContext()
                serverURL, clientId));
        try {
            IMqttToken token = MqttConnection.getClient().connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MessagesArray.setMessages(new ArrayList<String>());
                    MessagesArray.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, MessagesArray.getMessages()));
                    MqttConnection.sendMQTTMessage("test","Hello, I am "+clientId);
                    Toast.makeText(context,"Успешно! Тебя зовут\n"+clientId, Toast.LENGTH_SHORT).show();
                    if (workMode) MqttConnection.subscribe(activity.getString(R.string.topic_slave_commands));
                    else MqttConnection.subscribe("#");
                    activity.ChangeVisualInterface();
                    MqttConnection.getClient().setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {
                            MqttConnection.setClient(null);
                            Toast.makeText(context.getApplicationContext(), "Sorry, the connection is lost! :c", Toast.LENGTH_LONG).show();
                            Log.i("MyApp", "Это мое сообщение о заходе в connectionLost");
                            activity.ChangeVisualInterface();
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            byte[] encodedPayload = message.getPayload();
                            String mes = new String(encodedPayload);
                            MessagesArray.addMessage(topic + ": " + mes);
                            MessagesArray.getAdapter().notifyDataSetChanged();
                            Toast.makeText(context.getApplicationContext(), mes, Toast.LENGTH_LONG).show();
                            activity.readMessage(topic,mes);
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context,"Подключение выполнить не удалось", Toast.LENGTH_SHORT).show();
                    MqttConnection.setClient(null);
                    activity.setingsFragment.ChangeVisualInterface();
                }
            });
            return true;
        } catch (MqttException e){
            e.printStackTrace();
            return false;
        }
    }


    public static boolean subscribe(String topic){
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