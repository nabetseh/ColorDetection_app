package com.ultracolor.colordetection_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.compose.ui.geometry.Size;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import org.opencv.android.CameraActivity;
import org.opencv.android.OpenCVLoader;

import java.util.concurrent.ExecutionException;

public class MainActivity extends CameraActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        previewView = findViewById(R.id.cameraView);
        if (OpenCVLoader.initDebug())
            System.out.println("Arriba");
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        /*cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                //bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException ignored) {}
        }, ContextCompat.getMainExecutor(this));*/
    }

    private void getPermission() {
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED)
            getPermission();
    }

    /*private void bindPreview(@NonNull ProcessCameraProvider cameraProvider){
        Preview preview = new Preview.Builder()
                .build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        previewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }*/

}








/*camera = findViewById(R.id.cameraView);
        camera.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {}

            @Override
            public void onCameraViewStopped() {}

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                Mat mRgba = inputFrame.rgba();
                Mat mRgbaT = mRgba.t();
                Core.flip(mRgba.t(), mRgbaT, 1);
                Imgproc.resize(mRgbaT, mRgbaT, mRgba.size());
                return mRgbaT;
            }
        });
    //CameraBridgeViewBase camera;
            //camera.enableView();

       /*@Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(camera);
    }*/


