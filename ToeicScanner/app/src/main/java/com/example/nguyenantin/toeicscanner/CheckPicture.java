package com.example.nguyenantin.toeicscanner;

import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;


public class CheckPicture extends AppCompatActivity {

    private ImageView imageView;
    private Button btn_cancel;
    private Button btn_next;
    private static final String TAG = "CheckPicture";
    private FragmentManager fm = getSupportFragmentManager();
    private char[] arrResultAnswer = null;
    private boolean isProcessed = false;    // check align process
    private LinearLayout hide_nav;
    private Bundle extras = new Bundle();
    private long mLastClickTime = 0;
    static{
        OpenCVLoader.initDebug();
    }
    private Intent intent;
    private boolean click=true;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        hideSystemUI();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_picture);

        imageView = (ImageView) findViewById(R.id.image_check);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_next = (Button) findViewById(R.id.btn_next);
        hide_nav = (LinearLayout) findViewById(R.id.hide_nav);

        try {
            extras = getIntent().getExtras();
        }
        catch (Exception e){
            e.printStackTrace();
        }
//        LoadTemplate();
        if(getExtrafromCamera()) {
            getPicture();
        }
        else{
            DFragment alertdFragment = new DFragment();
            // Show Alert DialogFragment
            alertdFragment.show(fm, "Image wasn't processed. Please, come back take picture!!");
        }
        try {
            hide_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSystemUI();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.e(TAG, "btn_next: Clicked");
                        if (click == true) {
                            // mis-clicking prevention, using threshold of 1000 ms
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();
                            hide_nav.setVisibility(View.VISIBLE);
                            btn_next.setVisibility(View.VISIBLE);
                            hide_nav.setEnabled(false);
                            btn_next.setEnabled(false);
                            btn_cancel.setEnabled(false);
                            intent = new Intent();
                            if (getExtrafromCamera() == true) {
                                SendResult();
                            }
                        } else {
                            // mis-clicking prevention, using threshold of 1000 ms
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();
                            DFragment alertdFragment = new DFragment();
                            btn_next.setVisibility(View.VISIBLE);
                            // Show Alert DialogFragment
                            alertdFragment.show(fm, "Image wasn't processed. Please, come back take picture!!");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // mis-clicking prevention, using threshold of 1000 ms
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        if (click == true) {
                            hide_nav.setVisibility(View.VISIBLE);
                            hide_nav.setEnabled(false);
                            hide_nav.setVisibility(View.VISIBLE);
                            btn_next.setEnabled(false);
                            btn_cancel.setEnabled(false);
                            click = false;
                            Intent cameraIntent = new Intent(CheckPicture.this, CustomCamera.class);
                            startActivity(cameraIntent);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
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
        try {
            Log.e("Kiem tra gui", "Da gui thanh cong");
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //
    private boolean getExtrafromCamera(){
        try {
            isProcessed = getIntent().getBooleanExtra("isProcessed", false);
            // get all value from previous activity
            arrResultAnswer = getIntent().getCharArrayExtra("arrResultAnswer");
            intent = new Intent(CheckPicture.this, CheckTest.class);
            intent.putExtra("arrResultAnswer", arrResultAnswer);
            if (isProcessed == false) {
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private void getPicture(){
        Log.e(TAG,"getPicture");
        try {
            long addr = getIntent().getLongExtra("mat_image", 0);
            if (addr == 0) {
                return;
            } else {
                Mat temImg = new Mat(addr);
                Log.e(TAG, "Temp iamge size: " + temImg.size().toString());
                Bitmap bmp = convertMattoBitmap(temImg);
                imageView.setImageBitmap(bmp);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            Log.e(TAG, "Da bam chup");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    //Convert bitmap from mat
    private Bitmap convertMattoBitmap(Mat mat) {
        try {
            Bitmap temp1 = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, temp1);
            Log.e(TAG, "convert Matto Bitmap : " + temp1.getHeight() + "___" + temp1.getWidth());
            return temp1;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
