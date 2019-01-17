package com.normall.mqttandroidclient;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends FragmentActivity {

    //Работа с фрагментами
    private Fragment activityFragment = getFragmentManager().findFragmentById(R.id.fragment_on_activity);
    private FragmentTransaction fragmTrans;
    public SettingsFragment setingsFrag = new SettingsFragment();
    public ConsoleMqttFragment consoleMqttFragment = new ConsoleMqttFragment();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startService(new Intent(this, MqttService.class));
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

    public void Connect(String ipAdd, String mqttPort){
        MqttConnection.connect(ipAdd, mqttPort, getApplicationContext(), this);
    }
    public void Disconnect(){
        if (MqttConnection.isConnected()){
            MqttConnection.disconnect(this);
        }

    }
    
    @Override
    protected void onDestroy() {
        if (MqttConnection.isConnected()){
            try {
                MqttConnection.getClient().disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();

    }
}
