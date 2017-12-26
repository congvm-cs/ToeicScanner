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

    // Use http service
//    private SharedPreferences mSharedPreferences;
//    List<Character> resultAnswer;

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
        submit_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SubmitIdTest.this, CustomCamera.class);
                startActivity(intent);

                //Server custom class
//                setError();
//
//                String made = edt_submit_id.getText().toString();
//
//                int err = 0;
//
//                if (!validateMade(made)) {
//
//                    err++;
//                    edt_submit_id.setError("Ma De should be valid !");
//                }
//
//                if (err == 0) {
//
//                    passProcess(made);
//
//                } else {
//
//                    showSnackBarMessage("Enter Valid Details !");
//                }
            }
        });
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
    /// Method for httpservice get data base on server
//    private void setError() {
//        edt_submit_id.setError(null);
//    }
//    private void passProcess(String made) {
//
//        mSubscriptions.add(NetworkUtil.getRetrofitMade(made).pass()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::handleResponse,this::handleError));
//    }
//
//    private void handleError(Throwable error) {
//        if (error instanceof HttpException) {
//
//            Gson gson = new GsonBuilder().create();
//
//            try {
//
//                String errorBody = ((HttpException) error).response().errorBody().string();
//                Response response = gson.fromJson(errorBody,Response.class);
//                showSnackBarMessage(response.getMessage());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//
//            showSnackBarMessage("Network Error !");
//        }
//    }
//    private void showSnackBarMessage(String message) {
//
//        if (findViewById(R.id.activity_main) != null) {
//
//            Snackbar.make(findViewById(R.id.activity_main), message, Snackbar.LENGTH_SHORT).show();
//        }
//    }
//
//    private void handleResponse(Response response) {
//
//        SharedPreferences.Editor editor = mSharedPreferences.edit();
//        editor.putString(Constants.TOKEN,response.getToken());
//        editor.putString(Constants.MADE,response.getMessage());
//        editor.apply();
//
//        edt_submit_id.setText(null);
//
//        Intent intent = new Intent(SubmitIdTest.this, CustomCamera.class);
//        startActivity(intent);
//    }
}
