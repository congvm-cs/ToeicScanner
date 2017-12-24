package com.example.nguyenantin.toeicscanner;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

public class CheckPicture extends AppCompatActivity {

    ImageView imageView;
    Button btn_cancel;
    Button btn_next;
    private static final int CAMERA_REQUEST = 3200;
    private static final String TAG = "Cuc cong";
    private static AssetManager assetManager;
    private static ToeicScanner scanner;
    boolean flag =true;
    Bitmap temp;
    Mat I_temp;
    String result;
    static{
        OpenCVLoader.initDebug();
    }
    Bitmap photo;
    //=====================
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
        LoadTemplate();
        takeAPicture();
        if(flag) {
            XuLy();
        }
        imageView = (ImageView) findViewById(R.id.image_check);

        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_next = (Button) findViewById(R.id.btn_next);


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(CheckPicture.this, CheckTest.class);
                cameraIntent.putExtra("result",result);
                CheckPicture.this.startActivity(cameraIntent);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }
    public void takeAPicture(){
        try {
            flag = false;
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
            Log.e(TAG,"Da bam chup");
        }
        catch (IOError e)
        {
            Intent cameraIntent = new Intent(CheckPicture.this,SubmitIdTest.class);
            startActivity(cameraIntent);
        }
    }
    public void LoadTemplate(){

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

        } catch (IOException e) {
            e.printStackTrace();
        };

    }
    public void XuLy(){
        Log.e(TAG, "xu ly");
        Bitmap bitmap1 = photo;
        //
        Mat I1 = new Mat(bitmap1.getHeight(), bitmap1.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(bitmap1, I1);

        Log.e(TAG, "DetectROI");
        I_temp = scanner.DetectROI(I1);
        temp = convertMattoBitmap(I_temp);
        Log.e(TAG, "DetectROI done");
//                Log.e(TAG,String.valueOf(I_temp.size()));
//        Log.e(TAG, "dm Tin" + String.valueOf(I_temp.size()));
//        scanner.AlignProcess();
//                //                    scanner.GetAnswers().toString();
//        temp = Bitmap.createBitmap(scanner.DetectROI(I_temp).cols(),scanner.DetectROI(I_temp).rows(),Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(I_temp, temp);
//        result = scanner.GetAnswers().toString();
    }
    public Bitmap convertMattoBitmap(Mat mat) {
        Log.e(TAG, "convertMattoBitmap");
        Bitmap temp1 = Bitmap.createBitmap(scanner.DetectROI(mat).cols(), scanner.DetectROI(mat).rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, temp1);
        return temp1;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            photo = (Bitmap) data.getExtras().get("data");
            XuLy();
            imageView.setImageBitmap(temp);
        }
    }
}
