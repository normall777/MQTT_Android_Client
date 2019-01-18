package com.normall.mqttandroidclient;

import android.app.Fragment;
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
    public static boolean workModeOfDivice=false;

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
        findViewById(R.id.navigation_control).setEnabled(!workModeOfDivice);
        if (MqttConnection.getClient()==null){
            findViewById(R.id.navigation_console).setEnabled(false);
            findViewById(R.id.navigation_control).setEnabled(false);
        } else {
            findViewById(R.id.navigation_console).setEnabled(true);
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

        ChangeVisualInterface();
        findViewById(R.id.navigation_control).setEnabled(!workModeOfDivice);
        //Фонарик
        Torch.Initialize(this);
    }


    public void readMessage(String topic, String message){
        if (setingsFragment.getWorkMode()){
            if (topic.equals(getString(R.string.topic_slave_commands_torch))) {
                switch (message) {
                    case StringCommands.COMMAND_LIGHT_ON:
                        Torch.turnOnFlash();
                        MqttConnection.sendMQTTMessage(getString(R.string.topic_slave_response), StringCommands.RESPONSE_LIGHT_ON_OK);
                        break;
                    case StringCommands.COMMAND_LIGHT_OFF:
                        Torch.turnOffFlash();
                        MqttConnection.sendMQTTMessage(getString(R.string.topic_slave_response), StringCommands.RESPONSE_LIGHT_OFF_OK);
                        break;
                }
            } else if (topic.equals(getString(R.string.topic_slave_commands_notify))) {
                MyNotification.showNotification(this, message);
            } else if (topic.equals(getString(R.string.topic_slave_commands_phone))) {
                MyDial.StartDial(this, message);
            }
        } else {
            if (topic.equals(getString(R.string.topic_slave_response))) {
                switch (message) {
                    case StringCommands.RESPONSE_LIGHT_ON_OK:
                        controlFragment.SetButtonLightOn(true);
                        break;
                    case StringCommands.RESPONSE_LIGHT_OFF_OK:
                        controlFragment.SetButtonLightOn(false);
                        break;
                }
            }
        }
    }



    public void Connect(String ipAdd, String mqttPort, boolean workMode){
        if (MqttConnection.getClient()==null){
            MqttConnection.connect(this, ipAdd, mqttPort, workMode);
            workModeOfDivice = workMode;
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
