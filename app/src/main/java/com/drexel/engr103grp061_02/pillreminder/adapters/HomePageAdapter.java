package com.drexel.engr103grp061_02.pillreminder.adapters;

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

import java.util.Calendar;

/**
 * Created by matthewrassmann on 5/24/16.
 */
public class HomePageAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    public HomePageAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {

        TextView textViewName = (TextView) view.findViewById(R.id.pillName);
        TextView textViewTime = (TextView) view.findViewById(R.id.pillTime);
        Log.d("count", Integer.toString(cursor.getCount()));
        String pillName = cursor.getString(1);
        int pillHour = cursor.getInt(3);
        int  pillMinute = cursor.getInt(4);

        Time t = new Time(pillHour,pillMinute);

        Calendar Now = Calendar.getInstance();
        int hour = Now.get(Calendar.HOUR_OF_DAY);
        int minute = Now.get(Calendar.MINUTE);

        Time currTime = new Time(hour,minute);
        Time subT = t.subtract(currTime);
        textViewName.setText(pillName);
        textViewTime.setText(subT.getHours()+":"+subT.getMinutes());
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.pill_list, parent, false);
    }
}
