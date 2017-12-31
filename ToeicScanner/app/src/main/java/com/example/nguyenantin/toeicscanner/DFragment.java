package com.example.nguyenantin.toeicscanner;

/**
 * Created by nguyenantin on 12/26/17.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class DFragment extends DialogFragment {
    private boolean onClick = true;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        hideSystemUI();
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(R.drawable.logotoeicscanner)
                // Set Dialog Title
                .setTitle("Process Image Error!")
                // Set Dialog Message
                .setMessage("Cannot detect ROI or Not a TOEIC test")

                // Positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (onClick == true) {
                                // Do something else
                                onClick = false;
                                Intent intent = new Intent(getContext(), CustomCamera.class);
                                startActivity(intent);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).create();
    }

    //hide system navigation
    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}