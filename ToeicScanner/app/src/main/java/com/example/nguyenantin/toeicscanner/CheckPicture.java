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
import java.io.IOException;
import java.io.InputStream;

public class CheckPicture extends AppCompatActivity {

    ImageView imageView;
    Button btn_cancel;
    Button btn_next;
    private static final String TAG = null;
    private static AssetManager assetManager;
    private static ToeicScanner scanner;
    static{
        OpenCVLoader.initDebug();
    }

    //=====================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_picture);

        imageView = (ImageView) findViewById(R.id.image_check);

        //Class B
        if(getIntent().hasExtra("newsky")) {
            imageView = new ImageView(CheckPicture.this);
            Bitmap b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("newsky"),0,getIntent().getByteArrayExtra("newsky").length);
            imageView.setImageBitmap(b);
        }

        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_next = (Button) findViewById(R.id.btn_next);

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

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread welcomeThread = new Thread() {

                    @Override
                    public void run() {
                        try {
                            String filename1 = "image/n3.jpg";
                            InputStream img1 = assetManager.open(filename1);
                            Bitmap bitmap1 = BitmapFactory.decodeStream(img1);
                            //
                            Mat I1 = new Mat(bitmap1.getHeight(), bitmap1.getWidth(), CvType.CV_8UC3);
                            Utils.bitmapToMat(bitmap1, I1);

                            scanner.DetectROI(I1);
                            scanner.AlignProcess();

                            //                    scanner.GetAnswers().toString();
                            Log.e(TAG, scanner.GetAnswers().toString());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                welcomeThread.start();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent( CheckPicture.this, CameraToiec.class);
//                CheckPicture.this.startActivity(intentMain);
            }
        });
    }

}
