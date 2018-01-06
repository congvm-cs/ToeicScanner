package com.example.nguyenantin.toeicscanner;

import java.io.IOException;
import java.io.InputStream;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
<<<<<<< Updated upstream
=======
import android.os.Build;
>>>>>>> Stashed changes
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.support.v7.app.AppCompatActivity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

public class CustomCamera extends AppCompatActivity {

    private Button btn_back;
    private Button captureButton;
    Camera mCamera;
    private FragmentManager fm = getSupportFragmentManager();
    CameraPreview mPreview;
    private static final String TAG = "CustomCamera";
    private static AssetManager assetManager;

    private LinearLayout hide_nav;
    // use OpenCv in component

    static{
        OpenCVLoader.initDebug();
    }

    private long mLastClickTime = 0;
    private ProgressDialog progressDialog;
    private boolean click = true;
    private boolean mFlashMode = false;
    //
    private Intent intentCheckPicture;
    private Mat inputImg;
    private Mat processedImage;
    private boolean processing = false;
    private ToeicScanner scanner = new ToeicScanner();
    private char[] arrResultAnswer;
    private boolean isProcessed = false;    // check align process

<<<<<<< Updated upstream

=======
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
>>>>>>> Stashed changes
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        hideSystemUI();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
<<<<<<< Updated upstream

        try {
            LoadTemplate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
=======
        //
>>>>>>> Stashed changes
        // permission camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);

        // Controller
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        captureButton = (Button)findViewById(R.id.btn_takepicture);
        btn_back = (Button)findViewById(R.id.btn_back);
        hide_nav = (LinearLayout) findViewById(R.id.hide_nav);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        if(mCamera == null){
            Log.e(TAG, "camera null");
        }
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview.addView(mPreview);

        Toast.makeText(CustomCamera.this, "Align Your Test Center", Toast.LENGTH_SHORT).show();

        final Button flashModeButton = (Button) findViewById(R.id.flashModeButton);

        try {
            flashModeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mFlashMode == false) {
                        PackageManager packageManager = getPackageManager();
                        mPreview.setFlash(packageManager, true);
                        flashModeButton.setBackgroundResource(R.drawable.ic_flash_on_24dp);
                        mFlashMode = true;
                    } else {
                        PackageManager packageManager = getPackageManager();
                        mPreview.setFlash(packageManager, false);
                        flashModeButton.setBackgroundResource(R.drawable.ic_flash_off_24dp);
                        mFlashMode = false;
                    }

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //Activity in component
        hide_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSystemUI();
            }
        });

        try {
            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (click == true) {
                            mCamera.takePicture(null, null, mPicture);
                            hide_nav.setVisibility(View.VISIBLE);
                            captureButton.setVisibility(View.VISIBLE);
                            // mis-clicking prevention, using threshold of 1000 ms
<<<<<<< Updated upstream
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 60000){
=======
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 3000){
>>>>>>> Stashed changes
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();
                            click = false;
<<<<<<< Updated upstream
                            progressDialog = new ProgressDialog(CustomCamera.this);
                            progressDialog.setMax(100);
                            progressDialog.setTitle("Image is processing, please wait!");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

=======
                            progressDoalog = new ProgressDialog(CustomCamera.this);
                            progressDoalog.setMax(100);
                            progressDoalog.setTitle("Image is processing, please wait!!!");
                            progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progressDoalog.setCancelable(false);
                            progressDoalog.setCanceledOnTouchOutside(false);
                            progressDoalog.show();
                            final PackageManager packageManager = getPackageManager();
>>>>>>> Stashed changes
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        hideSystemUI();
                                        PackageManager packageManager = getPackageManager();
                                        mPreview.setFlash(packageManager, false);
                                        flashModeButton.setBackgroundResource(R.drawable.ic_flash_off_24dp);
                                        while (processing == false) {
                                            try {
                                                Thread.sleep(10);

                                                mFlashMode = false;
                                                handle.sendMessage(handle.obtainMessage());
<<<<<<< Updated upstream
                                                if (processing == true) {
                                                    progressDialog.dismiss();
=======
                                                if (processing == true){
                                                    Thread.sleep(10);
                                                    progressDoalog.dismiss();
                                                }
                                                if (isProcessed==true) {
                                                    startActivity(intentCheckPicture);
>>>>>>> Stashed changes
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            finally {
                                                if (processing == true && isProcessed==true){
                                                    Thread.sleep(10);
                                                    startActivity(intentCheckPicture);
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }

                Handler handle = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        progressDialog.incrementProgressBy(1);
                    }
                };
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            btn_back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (click == true) {
                        // mis-clicking prevention, using threshold of 1000 ms
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        hide_nav.setVisibility(View.VISIBLE);
                        PackageManager packageManager = getPackageManager();
                        mPreview.setFlash(packageManager, false);
                        flashModeButton.setBackgroundResource(R.drawable.ic_flash_off_24dp);
                        mFlashMode = false;
                        click = false;
                        Intent intent = new Intent(CustomCamera.this, SubmitIdTest.class);
                        startActivity(intent);
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
<<<<<<< Updated upstream

=======
    //============
    //=========
>>>>>>> Stashed changes
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    ///=================================================
    private PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                android.hardware.Camera.Size pictureSize = camera.getParameters().getPictureSize();

                Log.e(TAG, "onPictureTaken - received image " + pictureSize.width + "x" + pictureSize.height);
                inputImg = new Mat(new Size(pictureSize.width, pictureSize.height), CvType.CV_8U);
                inputImg = Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
                Log.e(TAG, "inputImage size:" + inputImg.size().toString());

                try {
                    ButtonProcess();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    };


    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public void ButtonProcess() throws IOException {
        try {
            Log.e(TAG, "captureButton: Click");

            try {
                processedImage = Process(inputImg);
                Log.e("Size of processedImage:", processedImage.size().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // send processed Image, isProcessed, resultAnswer
            intentCheckPicture = new Intent(CustomCamera.this, CheckPicture.class);

            long Mat_Image = processedImage.getNativeObjAddr();
            Log.e(TAG, "mat img: Click" + Mat_Image);

            intentCheckPicture.putExtra("mat_image", Mat_Image);
            intentCheckPicture.putExtra("arrResultAnswer", arrResultAnswer);
            intentCheckPicture.putExtra("isProcessed", isProcessed);

            Log.e(TAG, "captureButton: Done");
            startActivity(intentCheckPicture);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Mat Process(Mat img) throws IOException {
        try {
            Log.e("Show input image size", img.size().toString());
            Mat I_temp = new Mat();

            // process image take to camera
            I_temp = scanner.DetectROI(img);

            isProcessed = scanner.AlignProcess();

            if (isProcessed == false) {
                DFragment alertdFragment = new DFragment();
                alertdFragment.show(fm, "Image wasn't processed. Please, capture again!");
            }

            Log.e(TAG, "isProcessed " + isProcessed);

            arrResultAnswer = scanner.GetAnswers().toString().toCharArray(); // String result frome array char
            Log.e(TAG, "resultAnswer " + arrResultAnswer);

            Log.e("Show size i_temp", I_temp.size().toString());
            Log.e("Show size item", scanner.GetResultAlign().size().toString());

            I_temp = scanner.GetResultAlign();

//            scanner.Reset();
            processing = true;
            return I_temp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    private void LoadTemplate(){
        Log.e(TAG, "Load Template");
        try {
            assetManager = getAssets();
            String filename = "image/template.jpg";
            InputStream img = assetManager.open(filename);
            Bitmap bitmap_templates = BitmapFactory.decodeStream(img);

//            if(bitmap_templates.toString()!=null ){
//                Log.e(TAG, "I_templates null, please extra!!! ");
//            }

            Mat I_templates = new Mat(bitmap_templates.getHeight(), bitmap_templates.getWidth(), CvType.CV_8UC3);
            I_templates.setTo(Scalar.all(0));
            Bitmap bmp32 = bitmap_templates.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bmp32, I_templates);

            if(I_templates.empty()){
                Log.e(TAG, "I_templates null, please extra!!! ");
            }

            Log.e(TAG, "I_templates size: " + I_templates.size().toString());

            scanner.LoadTemplate(I_templates);

            Log.e(TAG, "Finish");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
