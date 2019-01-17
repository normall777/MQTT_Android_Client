package com.normall.mqttandroidclient;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

    //Работа с фрагментами
    private Fragment activityFragment = getFragmentManager().findFragmentById(R.id.fragment_on_activity);
    public FragmentTransaction fragmTrans;
    public SettingsFragment setingsFragment = new SettingsFragment();
    public ConsoleMqttFragment consoleMqttFragment = new ConsoleMqttFragment();
    public ControlFragment controlFragment = new ControlFragment();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmTrans = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_connect:
                    Bundle bundle = new Bundle();
                    fragmTrans.replace(R.id.fragment_on_activity, setingsFragment);
                    fragmTrans.commit();
                    return true;
                case R.id.navigation_control:
                    fragmTrans.replace(R.id.fragment_on_activity, controlFragment);
                    fragmTrans.commit();
                    return true;
                case R.id.navigation_console:
                    fragmTrans.replace(R.id.fragment_on_activity, consoleMqttFragment);
                    fragmTrans.commit();
                    return true;
            }
            return false;
        }
    };


    public void ChangeVisualInterface(){//Для корректной перерисовки фрагмента, если он сейчас показан на экране
        if (setingsFragment.isVisible()){
            setingsFragment.ChangeVisualInterface();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startService(new Intent(this, MqttService.class));
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            fragmTrans = getSupportFragmentManager().beginTransaction();
            fragmTrans.replace(R.id.fragment_on_activity, setingsFragment);
            fragmTrans.commit();
        }

        //Меню
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Фонарик
        Torch.Initialize(this);
    }


    public void readMessage(String topic, String message){
        if (setingsFragment.getWorkMode()){
            if (topic.equals("phones/slave/execute")){
                switch (message){
                    case "lightOn":
                        Torch.turnOnFlash();
                        MqttConnection.sendMQTTMessage("phones/slave/result","LIGHT_ON_OK");
                        return;
                    case "lightOff":
                        Torch.turnOffFlash();
                        MqttConnection.sendMQTTMessage("phones/slave/result","LIGHT_OFF_OK");
                        return;
                }
            }
        } else {
            if (topic.equals("phones/slave/result")) {
                switch (message) {
                    case "LIGHT_ON_OK":
                        controlFragment.SetButtonLightOn(true);
                        return;
                    case "LIGHT_OFF_OK":
                        controlFragment.SetButtonLightOn(false);
                        return;
                }
            }
        }
    }



    public void Connect(String ipAdd, String mqttPort){
        if (MqttConnection.getClient()==null){
            MqttConnection.connect(ipAdd, mqttPort, getApplicationContext(), this);
        }
    }

    public void Disconnect(){
        if (MqttConnection.isConnected()){
            MqttConnection.disconnect(this);
        }

    }

    /*@Override
    protected void onDestroy() {
        if (MqttConnection.isConnected()){
            try {
                MqttConnection.getClient().disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();

    }*/
}
