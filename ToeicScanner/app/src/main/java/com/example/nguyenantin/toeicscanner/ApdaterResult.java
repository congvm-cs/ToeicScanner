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
}

