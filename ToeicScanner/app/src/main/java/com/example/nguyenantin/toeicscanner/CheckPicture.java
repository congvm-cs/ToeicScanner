package com.example.nguyenantin.toeicscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CheckPicture extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;
    Button btn_cancel;
    Button btn_next;
    static {
        OpenCVLoader.initDebug();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_picture);

        imageView = (ImageView)findViewById(R.id.image_check);

        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_next = new Intent(CheckPicture.this, CheckTest.class);
                CheckPicture.this.startActivity(intent_next);
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                Mat I = new Mat();
                Mat I_pre = new Mat();
                String imageUri = "drawable://n3.jpg";
                I = Imgcodecs.imread(imageUri, 1);
                ToeicScanner scanner = new ToeicScanner();
                I_pre = scanner.Process(I);
                System.out.println(scanner.GetAnswers());
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent( CheckPicture.this, CameraToiec.class);
                CheckPicture.this.startActivity(intentMain);
            }
        });
    }
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS ) {
                // now we can call opencv code !
                helloworld();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    public void helloworld() {
        // make a mat and draw something
        Mat m = Mat.zeros(100,400, CvType.CV_8UC3);
        Imgproc.putText(m, "hi there ;)", new Point(30,80), Core.FONT_HERSHEY_SCRIPT_SIMPLEX, 2.2, new Scalar(200,200,0),2);

        // convert to bitmap:
        Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bm);
        // find the imageview and draw it!
        ImageView iv = (ImageView) findViewById(R.id.image_check);
        iv.setImageBitmap(bm);
    }
    @Override
    public void onResume() {;
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_5,this, mLoaderCallback);
        // you may be tempted, to do something here, but it's *async*, and may take some time,
        // so any opencv call here will lead to unresolved native errors.
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("Camera2Test");
            imageView.setImageBitmap(photo);
        }
    }
}
