package com.example.nguyenantin.toeicscanner;

import android.app.Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomCamera extends Activity {

    CircleImageView btnTake;
    CircleImageView btn_checkresult;
    CircleImageView btn_back;
    char [] cvchar;
    CircleImageView captureButton;

    Intent intentView;
    private static final String TAG = "CameraDemo";
    private Camera mCamera;
    boolean flag =false;
    byte [] image;
    private CameraPreview mPreview;
    Bitmap tempbitmap;
    public static final int MEDIA_TYPE_IMAGE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        if(flag==false) {
            // Create an instance of Camera
            mCamera = getCameraInstance();

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            // Add a listener to the Capture button
            captureButton = (CircleImageView) findViewById(R.id.btn_takepicture);

            captureButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // get an image from the camera
                            Log.e(TAG, "takePicture");
                            mCamera.takePicture(null, null, mPicture);
                            Log.e(TAG, "takePicture done");
                            return;
                        }
                    }
            );
//
            preview.addView(mPreview);
        }
        else{
            Intent intent = new Intent(CustomCamera.this,CustomCamera.class);
            startActivity(intent);
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };
    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ToiecScanner");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("ToiecScanner", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = "newsky";
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("ToiecScanner", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 10000, fos);
            Log.e(TAG,"da chay qua file output");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG,"da chay xong load anh");
        return directory.getAbsolutePath();
    }
    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.e(TAG, "Show now1");
            String filename;
            if (data == null) {
                Log.e(TAG, "Show now2");
                return;
            } else {
                Log.e(TAG, "Show now3");
                image = data;
                tempbitmap = convertBitmapfrombyte(data);
                filename = saveToInternalStorage(tempbitmap);
            }

            Log.e(TAG, "Show done: " + filename);

            intentView = new Intent(CustomCamera.this, CheckPicture.class);

            if (image == null) {
                Log.e(TAG, "image null");
            } else {
                //Bitmap abc = convertBitmapfrombyte(data);
                //
//                Bundle bundle = new Bundle();
//              String sendString = convertByteArrayToString(data);
                intentView = intentView.putExtra("filename",filename);
                flag=true;
                intentView = intentView.putExtra("flag",flag);
//                intentView.putExtra(bundle);
                //
                //intentView.putExtra("1", abc);
                startActivity(intentView);

                Log.e(TAG, "Transfer done");
            }
        }
    };
    public Bitmap convertBitmapfrombyte(byte [] data) {
        Log.e(TAG, "Successful convertion to bitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }
}