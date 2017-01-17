package com.xianxia.arbriberymoney.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by dmx on 2017/1/4.
 * 自定义实现摄像头预览
 */

public class CameraPreview extends SurfaceView implements Camera.AutoFocusCallback, Camera.PictureCallback, SurfaceHolder.Callback, SensorEventListener {
    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private static final int CAMERA_STATE_MOVING = 1;
    private static final int CAMERA_STATE_STATIC = 2;
    private static int CAMERA_STATE = CAMERA_STATE_STATIC;
    private boolean isFocused = false;
    private IOnCameraFocusListener mIOnCameraFocusListener;

    public void setIOnCameraFocusListener(IOnCameraFocusListener mIOnCameraFocusListener) {
        this.mIOnCameraFocusListener = mIOnCameraFocusListener;
    }

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSensorManager = (SensorManager) getContext().getSystemService(Activity.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        registerSensorListener();
    }

    public void unRegisterSensorListener() {
        mSensorManager.unregisterListener(this, mSensor);
    }

    public void registerSensorListener() {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.e(TAG, "surfaceCreated");
        try {
            if (mCamera != null) {
                Camera.Parameters parameters = mCamera.getParameters();
                if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    parameters.set("orientation", "portrait");
                    mCamera.setDisplayOrientation(90);

                    Camera.Size size = parameters.getSupportedPreviewSizes().get(0);
                    parameters.setPreviewSize(size.width, size.height);

                    List<Camera.Size> list = parameters.getSupportedPreviewSizes();
                    for (int i = 0; i < list.size(); i++) {
                        Log.d(TAG, "surfaceCreated: " + list.get(14).height + list.get(14).width);
                    }
                    Camera.Size picSize = parameters.getSupportedPictureSizes().get(0);
//                parameters.setPictureFormat(PixelFormat.JPEG);
//                parameters.set("jpeg-quality", 85);
                    getHolder().setFixedSize(picSize.width, picSize.height); // 设置分辨率
                    parameters.setRotation(90);

                }
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
                mCamera.autoFocus(this);
            }
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.e(TAG, "surfaceChanged");
        if (mHolder.getSurface() == null) {
            Log.e(TAG, "mHolder.getSurface() == null");
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.e(TAG, "stopPreview failed");
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mHolder.removeCallback(this);
            mCamera.setPreviewCallback(null);
            try {
                mCamera.setPreviewDisplay(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
        unRegisterSensorListener();
    }

    private int mX = 0, mY = 0, mZ = 0;
    private long lastTime = System.currentTimeMillis();
    private boolean switch_on = true;
    private boolean isFocusing = false;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null) {
            Log.e(TAG, "sensorevent is null");
        } else {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                int x = (int) event.values[0];
                int y = (int) event.values[1];
                int z = (int) event.values[2];
                int dx = mX - x;
                int dy = mY - y;
                int dz = mZ - z;
                double sqrt = Math.sqrt(dx * dx + dy * dy + dz * dz);
                Log.e(TAG,/*"x:"+x+"y:"+y+"z:"+z+"dx:"+dx+"dy:"+dy+"dz:"+dz+"mx:"+mX+"my:"+mY+"mz:"+mZ+*/"sqrt" + sqrt);
                if (sqrt > 1.4) {
                    CAMERA_STATE = CAMERA_STATE_MOVING;
                    mIOnCameraFocusListener.onMoving();
                    isFocused = false;
                    switch_on = true;
                    isFocusing = false;
                } else {
                    if (switch_on) {
                        lastTime = System.currentTimeMillis();
                        switch_on = false;
                    }
                    CAMERA_STATE = CAMERA_STATE_STATIC;
                    if (!isFocused && System.currentTimeMillis() - lastTime > 5000 && !isFocusing) {
                        isFocusing = true;
                        focus();
                    }
                }
                mX = x;
                mY = y;
                mZ = z;
            } else {
                Log.e(TAG, "none");
            }
        }

    }

    private void focus() {
        if (mCamera != null && !isFocusing) {
            mCamera.cancelAutoFocus();
            mCamera.autoFocus(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mIOnCameraFocusListener.onFocused(data, camera);
        unRegisterSensorListener();
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            Log.e(TAG, "focus success");
            isFocused = true;
            mCamera.takePicture(null, null, null, this);
        } else {
            Log.e(TAG, "focus failed");
            isFocused = false;
            mIOnCameraFocusListener.onFocusFailed();
        }
        isFocusing = false;
        switch_on = true;
    }


    public interface IOnCameraFocusListener {
        void onFocused(byte[] data, Camera camera);

        void onFocusFailed();

        void onMoving();
    }
}
