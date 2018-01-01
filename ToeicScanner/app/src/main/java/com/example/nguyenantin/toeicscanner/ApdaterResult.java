package com.example.nguyenantin.toeicscanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ApdaterResult extends ArrayAdapter<Result> {
    private Activity context;
    private int resource;
    private List<Result> objects;
//    private LinearLayout ll_backgroundresult;
    public ApdaterResult(Activity context, int resource, List<Result> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
//    private static class ViewHolder {
//        TextView answer;
//    }

    public ApdaterResult(Context context, ArrayList<Result> notes) {
        super(context, R.layout.itemt_result, notes);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Result note = getItem(position);
//        ViewHolder viewHolder;
//
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.itemt_result, parent, false);
//
//            viewHolder.answer = (TextView) convertView.findViewById(R.id.txt_result);
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        viewHolder.answer.setText(note.getResult());
//
//        return convertView;
//    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = this.context.getLayoutInflater();
            View row = inflater.inflate(this.resource, null);
            TextView txtSTT = (TextView) row.findViewById(R.id.txt_stt);
            TextView txtResult = (TextView) row.findViewById(R.id.txt_result);
            TextView txtResult2 = (TextView) row.findViewById(R.id.txt_user);

            final Result result = this.objects.get(position);
            txtSTT.setText(result.getStt());
            txtResult.setText(result.getResult());
            txtResult2.setText(result.getResult2());
        try {
            if (Integer.parseInt(txtSTT.getText().toString()) >= 100 && !txtResult.getText().equals(txtResult2.getText().toString())) {
                txtResult.setBackgroundResource(R.drawable.textincorrect);
                txtResult2.setBackgroundResource(R.drawable.textincorrect);
                txtResult2.setTextColor(Color.WHITE);
                txtResult.setTextColor(Color.WHITE);

            } else if (Integer.parseInt(txtSTT.getText().toString()) < 100 && !txtResult.getText().equals(txtResult2.getText().toString())) {
                txtResult.setBackgroundResource(R.drawable.textincorrect);
                txtResult2.setBackgroundResource(R.drawable.textincorrect);
                txtResult2.setTextColor(Color.WHITE);
                txtResult.setTextColor(Color.WHITE);
            } else if (Integer.parseInt(txtSTT.getText().toString()) < 100 && txtResult.getText().equals(txtResult2.getText().toString())) {
                txtResult.setBackgroundResource(R.drawable.text);
                txtResult2.setBackgroundResource(R.drawable.text);
            } else if (Integer.parseInt(txtSTT.getText().toString()) >= 100 && txtResult.getText().equals(txtResult2.getText().toString())) {
                txtResult.setBackgroundResource(R.drawable.text);
                txtResult2.setBackgroundResource(R.drawable.text);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return row;
    }
}

