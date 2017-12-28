package com.example.nguyenantin.toeicscanner;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import java.io.IOError;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckPicture extends AppCompatActivity {

    private static ImageView imageView;
    private static CircleImageView btn_cancel;
    private static CircleImageView btn_next;
    private static final String TAG = "CheckPicture";
    private  FragmentManager fm = getSupportFragmentManager();
    private char[] arrResultAnswer;
    private boolean isProcessed = false;    // check align process
    private LinearLayout hide_nav;
    static{
        OpenCVLoader.initDebug();
    }
    Intent intent;
    private boolean click=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideSystemUI();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_picture);

        Bundle extras = getIntent().getExtras();

        imageView = (ImageView) findViewById(R.id.image_check);
        btn_cancel = (CircleImageView) findViewById(R.id.btn_cancel);
        btn_next = (CircleImageView) findViewById(R.id.btn_next);
        hide_nav = (LinearLayout) findViewById(R.id.hide_nav);

//        LoadTemplate();
        if(getExtrafromCamera()) {
            getPicture();
        }
        else{
            DFragment alertdFragment = new DFragment();
            // Show Alert DialogFragment
            alertdFragment.show(fm, "Image wasn't processed. Please, come back take picture!!");
        }
        hide_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSystemUI();
            }
        });
        if(click==true) {
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "btn_next: Clicked");
                    if (click == true) {
                        hide_nav.setVisibility(View.VISIBLE);
                        click = false;
                        intent = new Intent();
                        if(getExtrafromCamera()==true) {
                            SendResult();
                        }
                    } else {
                        DFragment alertdFragment = new DFragment();
                        // Show Alert DialogFragment
                        alertdFragment.show(fm, "Image wasn't processed. Please, come back take picture!!");
                    }
                }
            });
        }
        if(click==true) {
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click == true) {
                        click = false;
                        hide_nav.setVisibility(View.VISIBLE);
                        Intent cameraIntent = new Intent(CheckPicture.this, CustomCamera.class);
                        startActivity(cameraIntent);
                    }
                }
            });
        }
    }
    //Create Arlet show check
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
    private void SendResult(){
        Log.e("Kiem tra gui", "Da gui thanh cong");
        startActivity(intent);
    }
    //
    private boolean getExtrafromCamera(){
        isProcessed = getIntent().getBooleanExtra("isProcessed", false);
        // get all value from previous activity
        arrResultAnswer = getIntent().getCharArrayExtra("arrResultAnswer");
        intent = new Intent(CheckPicture.this, CheckTest.class);
        intent.putExtra("arrResultAnswer", arrResultAnswer);
        if(isProcessed==false) {
            return false;
        }
        return true;
    }

    private void getPicture(){
        Log.e(TAG,"getPicture");

        long addr = getIntent().getLongExtra("mat_image", 0);
        if(addr == 0) {
            return;
        }
        else {
            Mat temImg = new Mat(addr);
            Log.e(TAG, "Temp iamge size: "+ temImg.size().toString()) ;
            Bitmap bmp = convertMattoBitmap(temImg);
            imageView.setImageBitmap(bmp);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        Log.e(TAG,"Da bam chup");
    }


    //Convert bitmap from mat
    private Bitmap convertMattoBitmap(Mat mat) {
        Bitmap temp1 = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, temp1);
        Log.e(TAG, "convert Matto Bitmap : " + temp1.getHeight() + "___" + temp1.getWidth());
        return temp1;
    }
}
