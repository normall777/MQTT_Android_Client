package com.normall.mqttandroidclient;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends FragmentActivity {
    public static MqttAndroidClient mqttMyClient = null;

    //Работа с фрагментами
    private Fragment activityFragment = getFragmentManager().findFragmentById(R.id.fragment_on_activity);
    private FragmentTransaction fragmTrans;
    private SettingsFragment setingsFrag = new SettingsFragment();
    private ConsoleMqttFragment consoleMqttFragment = new ConsoleMqttFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmTrans = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_connect:
                    Bundle bundle = new Bundle();
                    fragmTrans.replace(R.id.fragment_on_activity, setingsFrag);
                    fragmTrans.commit();
                    return true;
                case R.id.navigation_control:

                    return true;
                case R.id.navigation_console:
                    fragmTrans.replace(R.id.fragment_on_activity, consoleMqttFragment);
                    fragmTrans.commit();
                    return true;
            }
            return false;
        }
    };


    public void sendMQTTMessage(String topic, String message) {
        if (mqttMyClient==null){
            Toast.makeText(getApplicationContext(),"Блин, не работает", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] encodedPayload = new byte[0];
        try{
            encodedPayload = message.getBytes("UTF-8");
            MqttMessage mqttMessage = new MqttMessage(encodedPayload);
            mqttMyClient.publish(topic,mqttMessage);
        }catch (UnsupportedEncodingException | MqttException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, MqttService.class));
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            fragmTrans = getSupportFragmentManager().beginTransaction();
            fragmTrans.replace(R.id.fragment_on_activity, setingsFrag);
            fragmTrans.commit();
        }


        //Меню
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }




}
