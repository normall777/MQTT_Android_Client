package com.normall.mqttandroidclient;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class Torch {

    static private boolean isFlashOn;
    static private Camera camera;
    static private boolean hasFlash;
    static private Camera.Parameters params;

    public static void Initialize(Activity activity){
        getTorch();
        hasFlash = activity.getApplicationContext()
                .getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash){
            Toast.makeText(activity.getApplicationContext(),"Фонарика нет :с", Toast.LENGTH_SHORT).show();
        }
    }


    private static void getTorch(){
        if (camera == null){
            try {
                camera = android.hardware.Camera.open();
                params = camera.getParameters();
                isFlashOn = false;
            } catch (RuntimeException e){
                Log.e("Ошибка", e.getMessage());
            }
        }
    }

    public static void turnOnFlash() {
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

    public static void turnOffFlash() {
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

    private static boolean isOn(){
        return isFlashOn;
    }


}
