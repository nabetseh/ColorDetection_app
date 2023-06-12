package com.ultracolor.colordetection_app;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.TextureView.SurfaceTextureListener;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MyCameraPreview {

    private ImageReader.OnImageAvailableListener onImageAvailableListener;
    private SurfaceTextureListener surfaceTextureListener;
    private CameraDevice.StateCallback cameraCallback;
    private HandlerThread backgroundHandlerThread;
    private SurfaceTexture surfaceTexture;
    private CameraManager cameraManager;
    private final Context cameraContext;
    private String cameraId;
    private Handler backgroundHandler;



    public MyCameraPreview(Context context)  {
        cameraContext = context;
        surfaceTextureListener = new SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

            }
        };
        startBackgroundThread();
        cameraCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {

            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {

            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {

            }
        };
        initCameraManagement();
        cameraManager.openCamera(cameraId, cameraCallback, backgroundHandler);
    }

    private void initCameraManagement() {
        CameraManager cameraManager = (CameraManager) cameraContext.getSystemService(Context.CAMERA_SERVICE);
        String[] cameraIds;
        cameraId = "";

        try {
            cameraIds = cameraManager.getCameraIdList();
            for (String id : cameraIds) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                Size[] outputSizes = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
                Size optimalPreviewSize = Collections.max(Arrays.asList(outputSizes), Comparator.comparingInt(size -> size.getHeight() * size.getWidth()));
                ImageReader imageReader = ImageReader.newInstance(optimalPreviewSize.getWidth(), optimalPreviewSize.getHeight(), ImageFormat.JPEG, 1);
                imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler);
                cameraId = id;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread() {
        backgroundHandlerThread = new HandlerThread("CameraVideoThread");
        backgroundHandlerThread.start();
        backgroundHandler = new Handler(backgroundHandlerThread.getLooper());
    }

    private void stopBackgroundThread() {
        backgroundHandlerThread.quitSafely();
        try {
            backgroundHandlerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
