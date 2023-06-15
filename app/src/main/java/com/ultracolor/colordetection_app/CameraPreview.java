package com.ultracolor.colordetection_app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession;
    private CaptureRequest.Builder previewRequestBuilder;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;
    private Size previewSize;

    public CameraPreview(Context context) {
        super(context);
        getHolder().addCallback(this);
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // La superficie está creada y lista para ser utilizada
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // La configuración de la superficie ha cambiado
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // La superficie está siendo destruida
        closeCamera();
    }

    private void openCamera() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            return;
        }

        try {
            String cameraId = cameraManager.getCameraIdList()[0]; // Obtener el ID de la cámara trasera
            StreamConfigurationMap map = cameraManager.getCameraCharacteristics(cameraId)
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            previewSize = map.getOutputSizes(SurfaceHolder.class)[0];

            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    startPreview();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    cameraDevice.close();
                    cameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    cameraDevice.close();
                    cameraDevice = null;
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startPreview() {
        try {
            Surface surface = getHolder().getSurface();
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    captureSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    // Manejar el fallo de configuración
                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if (cameraDevice == null) return;

        try {
            captureSession.setRepeatingRequest(previewRequestBuilder.build(), null, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (captureSession != null) {
            captureSession.close();
            captureSession = null;
        }
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (backgroundThread != null) {
            backgroundThread.quitSafely();
            try {
                backgroundThread.join();
                backgroundThread = null;
                backgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}




