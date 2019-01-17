package com.normall.mqttandroidclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.security.Policy;

public class ControlFragment extends Fragment {

    private Button buttonLightOn;
    private boolean isFlashOn=false;
    private Camera camera;
    private boolean hasFlash;
    Camera.Parameters params;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_control, null);
        buttonLightOn = (Button) v.findViewById(R.id.button_light);

        hasFlash = getActivity().getApplicationContext()
                .getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash){
            Toast.makeText(getActivity().getApplicationContext(),"Фонарика нет :с", Toast.LENGTH_SHORT).show();
        }

        getCamera();


        buttonLightOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    //Выключаем фонарик:
                    turnOffFlash();
                } else {
                    //Включаем фонарик:
                    turnOnFlash();
                }
            }
        });
        return v;
    }

    private void getCamera(){
        if (camera == null){
            try {
                camera = android.hardware.Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e){
                Log.e("Ошибка", e.getMessage());
            }
        }
    }

    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }
    }

    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
    }






}
