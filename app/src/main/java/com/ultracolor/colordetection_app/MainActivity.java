package com.ultracolor.colordetection_app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

import com.google.common.util.concurrent.ListenableFuture;

import org.opencv.android.CameraActivity;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends CameraActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        layout = findViewById(R.id.linearLayout);
        layout.addView(new CameraPreview(this));
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


