package com.drexel.engr103grp061_02.pillreminder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.drexel.engr103grp061_02.pillreminder.R;
import com.drexel.engr103grp061_02.pillreminder.Time;

import java.util.ArrayList;

/**
 * Created by Matt on 5/14/2016.
 */
public class CustomArrayListAdapter extends ArrayAdapter<Time> {

    public CustomArrayListAdapter(Context ctx, ArrayList<Time> times) {
        super(ctx, 0, times);
    }
   //other constructors

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Time time = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.new_pill_time_list, parent, false);
        }

        TextView textHours = (TextView)convertView.findViewById(R.id.list_hours);
        TextView textMinutes = (TextView)convertView.findViewById(R.id.list_minutes);

        textHours.setText(Integer.toString(time.getHours()));
        textMinutes.setText(Integer.toString(time.getMinutes()));
        return convertView;
    }
}