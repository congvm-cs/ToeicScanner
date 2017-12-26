package com.example.nguyenantin.toeicscanner;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import org.opencv.android.JavaCameraView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomCamera extends Activity implements CvCameraViewListener2 {

    private CircleImageView btn_back;
    private CircleImageView captureButton;

    private static final String TAG = "CustomCamera";
    private static AssetManager assetManager;
    // Loads camera view of OpenCV for us to use. This lets us see using OpenCV
    private CameraBridgeViewBase mOpenCvCameraView;

    // Used in Camera selection from menu (when implemented)
    private LinearLayout hide_nav;
    // use OpenCv in component
    static{
        OpenCVLoader.initDebug();
    }
    //
    Intent intentCheckPicture;

    private Mat inputImage;
    private Mat processedImage;
    private ToeicScanner scanner;
    private char[] arrResultAnswer;
    private boolean isProcessed = false;    // check align process

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        hideSystemUI();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        LoadTemplate();
        // Controller
        captureButton = (CircleImageView)findViewById(R.id.btn_takepicture);
        btn_back = (CircleImageView)findViewById(R.id.btn_back);
        hide_nav = (LinearLayout) findViewById(R.id.hide_nav);
        // Toeic Scanner
        scanner = new ToeicScanner();

        //Activity in component
        hide_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSystemUI();
            }
        });
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonProcess();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomCamera.this,SubmitIdTest.class);
                startActivity(intent);
            }
        });
        // TO DO
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.camera_preview);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

    }
    //hide system navigation
    // This snippet hides the system bars.
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
    ///
    //    ========================
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onCameraViewStarted(int width, int height) {
        inputImage = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        inputImage.release();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        // TODO Auto-generated method stub
        inputImage = inputFrame.rgba();
        return inputImage; // This function must return
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }


    public void ButtonProcess(){
        Log.e(TAG,"captureButton: Click");
        processedImage = Process(inputImage);
        // send processed Image, isProcessed, resultAnswer
        intentCheckPicture = new Intent(CustomCamera.this, CheckPicture.class);

        long Mat_Image = processedImage.getNativeObjAddr();

        intentCheckPicture.putExtra("mat_image", Mat_Image);
        intentCheckPicture.putExtra("arrResultAnswer", arrResultAnswer);
        intentCheckPicture.putExtra("isProcessed", isProcessed);

        Log.e(TAG,"captureButton: Done");
        startActivity(intentCheckPicture);
    }

    public Mat Process(Mat img){
        Mat I_temp = new Mat();
        // process image take to camera
        I_temp = scanner.DetectROI(img);
        Log.e(TAG, "GetSquare " + scanner.GetSquare().toString());
        isProcessed = scanner.AlignProcess();

        Log.e(TAG, "isProcessed " + isProcessed);

        arrResultAnswer = scanner.GetAnswers().toString().toCharArray(); // String result frome array char
        Log.e(TAG, "resultAnswer " + arrResultAnswer);
        return I_temp;
    }

    private void LoadTemplate(){
        Log.e(TAG, "Load Template");
        try {
            assetManager = getAssets();
            String filename = "image/templates.jpg";
            InputStream img = assetManager.open(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(img);
            Mat I = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap, I);

            scanner = new ToeicScanner();
            scanner.LoadTemplate(I);
            Log.e(TAG, "Finish");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}