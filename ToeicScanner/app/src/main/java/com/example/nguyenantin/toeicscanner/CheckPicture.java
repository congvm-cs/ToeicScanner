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
    private static final String TAG = "Cuc cong";
    private static AssetManager assetManager;
    private static ToeicScanner scanner;
    private Bitmap temp;
    private Mat I_temp;
    private String result;
    static{
        OpenCVLoader.initDebug();
    }
    private Bitmap photo;
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
        Bundle extras = getIntent().getExtras();
        boolean flag = extras.getBoolean("flag");
        if(flag==true)
        {
            LoadTemplate();
            imageView = (ImageView) findViewById(R.id.image_check);
            btn_cancel = (CircleImageView) findViewById(R.id.btn_cancel);
            btn_next = (CircleImageView) findViewById(R.id.btn_next);
            getPicture();

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(CheckPicture.this, CheckTest.class);
                    cameraIntent.putExtra("result", result);
                    CheckPicture.this.startActivity(cameraIntent);
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
        else{
            Intent cameraIntent = new Intent(CheckPicture.this, CustomCamera.class);
            cameraIntent.putExtra("flag", flag);
            CheckPicture.this.startActivity(cameraIntent);
        }
    }
    public void getPicture(){
        try {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                return;
            }
            else {
                String filename = extras.getString("filename");
                Bitmap bmp = loadImageFromStorage(filename);
                photo = bmp;
                Process();
            }
            Log.e(TAG,"Da bam chup");
        }
        catch (IOError e)
        {
            Intent cameraIntent = new Intent(CheckPicture.this,SubmitIdTest.class);
            startActivity(cameraIntent);
        }
    }
    private Bitmap loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
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
    //
    public void Process(){
        try {
            AssetManager assetManager1;
            assetManager1 = getAssets();
            String filename1 = "image/n2.jpg";
            InputStream img1 = null;
            img1 = assetManager1.open(filename1);
            Bitmap bitmap1 = photo;
            //
            Mat I1 = new Mat(bitmap1.getHeight(), bitmap1.getWidth(), CvType.CV_8UC3);
            Utils.bitmapToMat(bitmap1, I1);

            // process image take to camera
            I_temp = scanner.DetectROI(I1);
            temp = convertMattoBitmap(I_temp);
            scanner.AlignProcess();
            Utils.matToBitmap(I_temp, temp);
            result = scanner.GetAnswers().toString(); // String result frome array char

            // set background image
            imageView.setImageBitmap(temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Convert bitmap from mat
    public Bitmap convertMattoBitmap(Mat mat) {
        Log.e(TAG, "convertMattoBitmap");
        Bitmap temp1 = Bitmap.createBitmap(scanner.DetectROI(mat).cols(), scanner.DetectROI(mat).rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, temp1);
        return temp1;
    }
}
