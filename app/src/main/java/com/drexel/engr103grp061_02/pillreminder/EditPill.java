package com.drexel.engr103grp061_02.pillreminder;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.drexel.engr103grp061_02.pillreminder.adapters.CustomArrayListAdapter;

import java.util.Calendar;

public class EditPill extends AppCompatActivity {
    String name;
    int quantity;
    Time t = new Time();
    String instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //TODO assuming
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        quantity = intent.getIntExtra("quantity",0);
        t.setHours(intent.getIntExtra("hours",0));
        t.setMinutes(intent.getIntExtra("minutes",0));
        instructions = intent.getStringExtra("instructions");

        TextView textName = (TextView) findViewById(R.id.pillName);
        TextView textQuantity = (TextView) findViewById(R.id.pillQuantity);
        TextView textTime = (TextView) findViewById(R.id.pillTime);
        TextView textInstructions = (TextView) findViewById(R.id.pillInstructions);

        textName.setText(name);
        textQuantity.setText(Integer.toString(quantity));
        textTime.setText(t.getTimeFormattedString());
        textInstructions.setText(instructions);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        Context context;
        public TimePickerFragment(){

        }
        public TimePickerFragment(Context c){
            context = c;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Time addTime = new Time(hourOfDay,minute);
            boolean existingTime = false;
            //TODO Check for equivalent times and prompt the user to enter a new time

        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(this);
        newFragment.show(this.getFragmentManager(),"timePicker");
    }

}
