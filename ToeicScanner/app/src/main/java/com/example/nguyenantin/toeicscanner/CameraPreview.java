package com.example.nguyenantin.toeicscanner;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by nguyenantin on 12/28/17.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    private static SurfaceHolder mHolder;
    private static Context context = null;
    private static String TAG = "Show";
    private static Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        try {
            //=======================================
            //=======================================
            mCamera = camera;
//        camera.setZoomChangeListener(1,false,mCamera);
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            setFocusable(true);
            setFocusableInTouchMode(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //
    //
    public void surfaceCreated(SurfaceHolder holder) {
        try {
//            synchronized (holder){
//                Draw();
//            }
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

            startFaceDetection(); // start face detection feature
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }
    ////////////

    //////////////
    ////////////////////////////////////////////////////////////
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("down", "focusing now");
            try {
                mCamera.autoFocus(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        //================
        //=============
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

            startFaceDetection(); // re-start face detection feature

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    //=====================================

    //=====================================

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void startFaceDetection(){
        // Try starting Face Detection
        Camera.Parameters params = mCamera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0){
            // camera supports face detection, so can start it:
            mCamera.startFaceDetection();
        }
    }
    //Open Flash
    public boolean setFlash(PackageManager pm, boolean stateFlash) {
        try {
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Camera.Parameters par = mCamera.getParameters();
                par.setFlashMode(stateFlash ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(par);
                Log.d(TAG, "flash: " + (stateFlash ? "on" : "off"));
                return stateFlash;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
