package com.example.nguyenantin.toeicscanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.nguyenantin.toeicscanner.R.drawable.text;
import static com.example.nguyenantin.toeicscanner.R.drawable.textincorrect;

/**
 * Created by NguyenBao on 12/25/2016.
 */

public class ApdaterResult extends ArrayAdapter<Result> {
    Activity context;
    int resource;
    List<Result> objects;
    LinearLayout ll_backgroundresult;

    public ApdaterResult(Activity context, int resource, List<Result> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
    private static class ViewHolder {
        TextView answer;
    }
    public ApdaterResult(Context context, ArrayList<Result> notes) {
        super(context, R.layout.itemt_result, notes);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Result note = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.itemt_result, parent, false);

            viewHolder.answer = (TextView) convertView.findViewById(R.id.txt_result);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.answer.setText(note.getResult());

        return convertView;
    }
//    @NonNull
//    @Override
//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = this.context.getLayoutInflater();
//        View row = inflater.inflate(this.resource,null);
//
//        ll_backgroundresult = (LinearLayout) row.findViewById(R.id.ll_backgroundresult);
//        TextView txt_result = (TextView) row.findViewById(R.id.txt_result);
//        TextView txt_user = (TextView) row.findViewById(R.id.txt_user);
//        TextView txt_stt = (TextView) row.findViewById(R.id.txt_stt);
//
//        final Result result = this.objects.get(position);
//        txt_result.setText(result.getResult());
//        txt_user.setText(result.getResult2());
//        txt_stt.setText(result.getStt());
//        if (!result.getResult().toString().equals(result.getResult2().toString())){
//            txt_user.setBackgroundResource(R.drawable.textincorrect);
//            txt_result.setBackgroundResource(R.drawable.textincorrect);
//            txt_stt.setBackgroundResource(R.drawable.textincorrect);
//            int white = context.getResources().getColor(R.color.colorWhite1);
//            txt_user.setTextColor(white);
//            txt_stt.setTextColor(white);
//            txt_result.setTextColor(white);
//        } else {
//            ll_backgroundresult.setBackgroundResource(R.drawable.listview);
//        }
//
////        btnLike.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                xuLyThich(result);
////            }
////        });
////
////        btnDisklike.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                xuLyBoThich(result);
////            }
////        });
//
//        return row;
//    }

//    private void xuLyBoThich(Result result) {
//        result.setThich(false);
//    }
//
//    private void xuLyThich(Result result) {
//        result.setThich(true);
//    }
}

