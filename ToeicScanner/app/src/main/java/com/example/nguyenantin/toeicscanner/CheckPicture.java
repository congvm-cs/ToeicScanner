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
    char[] arrResultAnswer;
    boolean isProcessed = false;    // check align process
    private LinearLayout hide_nav;
    static{
        OpenCVLoader.initDebug();
    }

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

        getExtra();
        getPicture();

        hide_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSystemUI();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "btn_next: Clicked");
                if (isProcessed == true){
                    Intent checkTestIntent = new Intent(CheckPicture.this, CheckTest.class);
                    checkTestIntent.putExtra("arrResultAnswer", arrResultAnswer);
                    CheckPicture.this.startActivity(checkTestIntent);
                }
                else{
                    DFragment alertdFragment = new DFragment();
                    // Show Alert DialogFragment
                    alertdFragment.show(fm, "Image wasn't processed. Please, come back take picture!!");
                }
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
