package com.ultracolor.colordetection_app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.opencv.android.CameraActivity;

public class MainActivity extends CameraActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    private void requestPermission() {
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED)
            requestPermission();
    }



}





