package com.drexel.engr103grp061_02.pillreminder.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.drexel.engr103grp061_02.pillreminder.R;

import java.util.HashMap;

/**
 * Created by matthewrassmann on 5/10/16.
 */

public class CustomCursorAdapter extends CursorAdapter{
    private LayoutInflater cursorInflater;
    private String _columnName;

    private HashMap map;

    public CustomCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        String pillName = cursor.getString(1);
        int pillHour = cursor.getInt(3);
        int  pillMinute = cursor.getInt(4);
        String pillTime = Integer.toString(pillHour)+":"+Integer.toString(pillMinute);

        TextView textViewName = (TextView) view.findViewById(R.id.pillName);
        TextView textViewTime = (TextView) view.findViewById(R.id.pillTime);

        textViewName.setText(pillName);
        textViewTime.setText(pillTime);
    }
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.pill_list, parent, false);
    }
}
