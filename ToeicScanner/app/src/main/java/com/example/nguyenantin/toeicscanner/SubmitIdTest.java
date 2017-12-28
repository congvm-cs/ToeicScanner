package com.example.nguyenantin.toeicscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import rx.subscriptions.CompositeSubscription;

public class SubmitIdTest extends AppCompatActivity {

    private Button submit_id;
    private EditText edt_submit_id;
    private CompositeSubscription mSubscriptions;
    private LinearLayout hide_nav;
    private boolean click=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_submit_id_test);
        submit_id = (Button) findViewById(R.id.submit_id);
        edt_submit_id = (EditText)findViewById(R.id.edt_submit_id);
        mSubscriptions = new CompositeSubscription();
        hide_nav = (LinearLayout)findViewById(R.id.hide_nav);

        //Activity in component
        if(click==true) {
            submit_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click == true) {
                        hide_nav.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(SubmitIdTest.this, CustomCamera.class);
                        startActivity(intent);
                        click = false;
                    }
                }
            });
        }
        hide_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSystemUI();
            }
        });
    }
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
}
