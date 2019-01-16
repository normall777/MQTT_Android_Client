package com.normall.mqttandroidclient;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class MqttConnection {
    private static MqttAndroidClient client;

    public static MqttAndroidClient getClient() {
        return client;
    }

    public static void setClient(MqttAndroidClient client) {
        MqttConnection.client = client;
    }
}