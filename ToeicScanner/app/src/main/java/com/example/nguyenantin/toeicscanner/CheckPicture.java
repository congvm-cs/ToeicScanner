package com.example.nguyenantin.toeicscanner;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckPicture extends AppCompatActivity {

    private static ImageView imageView;
    private static CircleImageView btn_cancel;
    private static CircleImageView btn_next;
    private static final String TAG = "CheckPicture";

    char[] arrResultAnswer;
    boolean isProcessed = false;    // check align process

    static{
        OpenCVLoader.initDebug();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_picture);

        Bundle extras = getIntent().getExtras();

        imageView = (ImageView) findViewById(R.id.image_check);
        btn_cancel = (CircleImageView) findViewById(R.id.btn_cancel);
        btn_next = (CircleImageView) findViewById(R.id.btn_next);

        getExtra();
        getPicture();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "btn_next: Clicked");
                if (isProcessed == true){
                    Intent checkTestIntent = new Intent(CheckPicture.this, CheckTest.class);
                    checkTestIntent.putExtra("arrResultAnswer", arrResultAnswer);
                    CheckPicture.this.startActivity(checkTestIntent);
                }
//                else{
//                    // Show Popup / layout: CANNOT DETECT A TEST OR NOT A TEST
//                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(CheckPicture.this, CustomCamera.class);
                CheckPicture.this.startActivity(cameraIntent);
            }
        });
    }


    public void getExtra(){
        // get all value from previous activity
        arrResultAnswer = getIntent().getCharArrayExtra("arrResultAnswer");
        isProcessed = getIntent().getBooleanExtra("isProcessed", false);
    }

    public void getPicture(){
        Log.e(TAG,"getPicture");

        try {
            long addr = getIntent().getLongExtra("mat_image", 0);
            if(addr == 0) {
                return;
            }
            else {
                Mat temImg = new Mat(addr);
                Bitmap bmp = convertMattoBitmap(temImg);
                imageView.setImageBitmap(bmp);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            Log.e(TAG,"Da bam chup");
        }
        catch (IOError e)
        {
            Intent cameraIntent = new Intent(CheckPicture.this,SubmitIdTest.class);
            startActivity(cameraIntent);
        }
    }


    //Convert bitmap from mat
    public Bitmap convertMattoBitmap(Mat mat) {
        Log.e(TAG, "convertMattoBitmap");
        Bitmap temp1 = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, temp1);
        return temp1;
    }
}
