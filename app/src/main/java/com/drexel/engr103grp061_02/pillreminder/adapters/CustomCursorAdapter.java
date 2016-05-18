package com.drexel.engr103grp061_02.pillreminder.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.drexel.engr103grp061_02.pillreminder.R;
import com.drexel.engr103grp061_02.pillreminder.Time;

import java.util.HashMap;

/**
 * Created by Matthew Rassmann on 5/10/16.
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
        TextView textViewName = (TextView) view.findViewById(R.id.pillName);
        TextView textViewTime = (TextView) view.findViewById(R.id.pillTime);
        Log.d("count",Integer.toString(cursor.getCount()));
        String pillName = cursor.getString(1);
        int pillHour = cursor.getInt(3);
        int  pillMinute = cursor.getInt(4);

        Time t = new Time(pillHour,pillMinute);

        String timeOutput = t.getTimeFormattedString();

        textViewName.setText(pillName);
        textViewTime.setText(timeOutput);


    }
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.pill_list, parent, false);
    }
}
