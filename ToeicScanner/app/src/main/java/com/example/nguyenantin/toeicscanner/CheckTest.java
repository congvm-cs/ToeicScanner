package com.example.nguyenantin.toeicscanner;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

import android.content.Intent;
import android.widget.TextView;

public class CheckTest extends AppCompatActivity {

    private ListView lvResult;
    private Button btn_ok;
    private ArrayList<Result> dsResult;
    private ApdaterResult adapterResult;

    private int count_correct_read = 0;
    private int count_correct_listen = 0;

    private TextView txt_crread;
    private TextView txt_total_read;
    private TextView txt_crlisten;
    private TextView txt_total_listen;
    private TextView  txt_sum;
    private LinearLayout hide_nav;
    private static  String TAG = "CheckTest";

    private char[] arrResultAnswer;
    private int[] standardReadingScore;
    private int[] standardListeningScore;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_check_test);

        txt_crread = (TextView) findViewById(R.id.txt_crread);
        txt_total_read = (TextView) findViewById(R.id.txt_icrread);
        txt_crlisten = (TextView) findViewById(R.id.txt_crlisten);
        txt_total_listen = (TextView) findViewById(R.id.txt_icrlisten);
        txt_sum = (TextView) findViewById(R.id.txt_sum);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        hide_nav = (LinearLayout) findViewById(R.id.hide_nav);
        // get data from camera
        try {
            getExtra();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        // Activity in component
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
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // mis-clicking prevention, using threshold of 1000 ms
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        btn_ok.setVisibility(View.VISIBLE);
                        hide_nav.setVisibility(View.VISIBLE);
                        hide_nav.setEnabled(false);
                        btn_ok.setEnabled(false);
                        Intent intentMain = new Intent(CheckTest.this, CustomCamera.class);
                        startActivity(intentMain);
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

        lvResult = (ListView) findViewById(R.id.ls_result);

        dsResult=new ArrayList<>();
        adapterResult=new ApdaterResult(CheckTest.this,R.layout.itemt_result,dsResult);

        lvResult.setAdapter(adapterResult);

        standardReadingScore = getStandardReadingScore();
        standardListeningScore = getStandardListeningScore();


        danhsachKetQua(arrResultAnswer);
        txt_crread.setText(String.valueOf(count_correct_read));
        txt_total_read.setText(String.valueOf(standardReadingScore[count_correct_read]));
        txt_crlisten.setText(String.valueOf(count_correct_listen));
        txt_total_listen.setText(String.valueOf(standardListeningScore[count_correct_listen]));
        txt_sum.setText(String.valueOf(standardReadingScore[count_correct_read] +
                standardListeningScore[count_correct_listen]));
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
    // extra results
    private void getExtra(){
        arrResultAnswer = getIntent().getCharArrayExtra("arrResultAnswer");
    }
    //Create result emty
    private void createArrEmpty(String [] temp){
        for(int i=0;i<temp.length;i++){
            temp[i]="X";
        }
    }
    ///====================================
    private char[] generateAnswerKey(){

        Random rand = new Random();
        char[] retAnswerKey = new char[200];
        char[] answerKey = {'A', 'B', 'C', 'D'};

        for(int index = 0; index < 200; index++){
            int i1 = rand.nextInt(4);
            retAnswerKey[index] = answerKey[i1];
        }
        return retAnswerKey;
    }

    private void danhsachKetQua(char [] abc) {
        char[] answerKey = generateAnswerKey();
        Log.e("arrResultAnswer", abc.toString());
        String[] strArrResultAnswer = null;
        strArrResultAnswer=Chuanhoachuoi(abc);
//        arrResultAnswer = generateAnswerKey();

        if(abc.length == 0){
            Log.e(TAG, "Null arrResultAnswer");
        }

        // Show on ListView
        for(int index = 0; index < 200; index++){
            String userAnswer = strArrResultAnswer[index];
            String question = Integer.toString(index+1);
            String key = Character.toString(answerKey[index]);
//            Log.e("Result Answer",strArrResultAnswer[index]);
            dsResult.add(new Result(question, userAnswer, key));

            // Show on Detail Table
//            // Listening Part
            if(index < 100){
                if(userAnswer.equals(key)==true){
                    count_correct_read++;
                }
            }
            // Reading Part
            if(index >= 100){
                if(userAnswer.equals(key)==true){
                    count_correct_listen++;
                }
            }
        }
        adapterResult.notifyDataSetChanged();
    }
    private String[] Chuanhoachuoi(char[] a){
        String [] temp = new String[a.length-1];
        createArrEmpty(temp);
        int index2 = 0;
        for(int i=0;i<a.length-1;i++){
            if(String.valueOf(a[i]).equals("A") || String.valueOf(a[i]).equals("B")
                    || String.valueOf(a[i]).equals("C") || String.valueOf(a[i]).equals("D")
                    || String.valueOf(a[i]).equals("X")){
                temp[index2] = String.valueOf(a[i]);
                index2++;
            }
        }
        Log.e("Results: ",temp.toString());
        return temp;
    }
    private int[] getStandardReadingScore(){
        int[] standardScore = new int[101];

        for(int index = 0; index < 101; index++) {
            if (index < 10) {
                standardScore[index] = 5;
            }

            if (index >= 10 && index < 25) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 25) {
                standardScore[index] = 90;
            }

            if (index > 25 && index < 28) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 28) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 28 && index < 39) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 39) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 39 && index < 43) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 43) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 43 && index < 47) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 47) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 47 && index < 52) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 52) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 52 && index < 55) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 55) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 55 && index < 64) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 64) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 64 && index < 82) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 82) {
                standardScore[index] = standardScore[index - 1];
            }

            if (index > 82 && index < 89) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 89) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 89 && index < 93) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 92) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index == 93) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 94) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 94 && index < 97) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index >= 97 && index < 101) {
                standardScore[index] = 495;
            }

        }
        return standardScore;
    }
    // Score Listening
    private int[] getStandardListeningScore(){
        int[] standardScore = new int[101];

        for(int index = 0; index < 101; index++) {
            if (index < 7) {
                standardScore[index] = 5;
            }

            if (index >= 7 && index < 31) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 31) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 31 && index < 39) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 39) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 39 && index < 44) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 44 || index == 45) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 45 && index < 54) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 54) {
                standardScore[index] = standardScore[index - 1] + 10;
            }
            if (index > 54 && index < 58) {
                standardScore[index] = standardScore[index - 1] + 5;
            }
            if (index == 58) {
                standardScore[index] = standardScore[index - 1] + 10;
            }
            if (index > 58 && index < 70) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 70) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 70 && index < 75) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 75) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 75 && index < 80) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 80) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 80 && index < 85) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 85) {
                standardScore[index] = standardScore[index - 1] + 10;
            }

            if (index > 85 && index < 88) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index == 88) {
                standardScore[index] = standardScore[index - 1]+10;
            }

            if (index > 88 && index < 93) {
                standardScore[index] = standardScore[index - 1] + 5;
            }

            if (index >= 93 && index < 101) {
                standardScore[index] = 495;
            }

        }
        return standardScore;
    }
}

