package com.example.nguyenantin.toeicscanner;

import android.app.Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.JavaCameraView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomCamera extends Activity implements CvCameraViewListener2 {

    CircleImageView btn_back;
    CircleImageView captureButton;

    Intent intentView;
    private static final String TAG = "CustomCamera";
    private static AssetManager assetManager;
    // Loads camera view of OpenCV for us to use. This lets us see using OpenCV
    private CameraBridgeViewBase mOpenCvCameraView;

    // Used in Camera selection from menu (when implemented)
    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    static{
        OpenCVLoader.initDebug();
    }
    Intent intentCheckPicture;
//    Intent intentSubmitIdTest;

    Mat inputImage;
    Mat processedImage;
    ToeicScanner scanner;
    char[] arrResultAnswer;
    boolean isProcessed = false;    // check align process

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_camera);
        LoadTemplate();

        // Controller
        captureButton = (CircleImageView)findViewById(R.id.btn_takepicture);
        btn_back = (CircleImageView)findViewById(R.id.btn_back);

        // Toeic Scanner
        scanner = new ToeicScanner();

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