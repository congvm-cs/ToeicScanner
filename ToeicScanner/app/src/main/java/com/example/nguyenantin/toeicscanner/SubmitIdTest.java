package com.example.nguyenantin.toeicscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SubmitIdTest extends AppCompatActivity {

    Button submit_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_submit_id_test);
        submit_id = (Button) findViewById(R.id.submit_id);
        submit_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( SubmitIdTest.this, CameraToiec.class);
                SubmitIdTest.this.startActivity(intent);
            }
        });
    }
}
