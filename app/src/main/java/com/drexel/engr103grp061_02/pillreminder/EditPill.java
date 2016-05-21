package com.drexel.engr103grp061_02.pillreminder;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.drexel.engr103grp061_02.pillreminder.adapters.CustomArrayListAdapter;
import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Pill;

import java.util.Calendar;

public class EditPill extends AppCompatActivity {
    String name;
    int quantity;
    static Time t = new Time();
    static Time oldT = new Time();
    String instructions;
    TextView textName;
    TextView textQuantity;
    TextView textTime;
    TextView textInstructions;

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
        quantity = intent.getIntExtra("quantity", 0);
        oldT.setHours(intent.getIntExtra("hours", 0));
        oldT.setMinutes(intent.getIntExtra("minutes", 0));
        instructions = intent.getStringExtra("instructions");

        textName = (TextView) findViewById(R.id.pillName);
        textQuantity = (TextView) findViewById(R.id.pillQuantity);
        textTime = (TextView) findViewById(R.id.pillTime);
        textInstructions = (TextView) findViewById(R.id.pillInstructions);

        textName.setText(name);
        textQuantity.setText(Integer.toString(quantity));
        textTime.setText(oldT.getTimeFormattedString());
        textInstructions.setText(instructions);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        Context context;
        TextView textText;
        public TimePickerFragment(){

        }
        public TimePickerFragment(Context c, TextView _timeText){
            context = c;
            textText = _timeText;
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
            Time newTime = new Time(hourOfDay,minute);
            //TODO Check for equivalent times and prompt the user to enter a new time
            if(t.equals(newTime)) {
                Toast.makeText(context, "Time already added \n Enter a new time", Toast.LENGTH_SHORT).show();
            }else{
                t = newTime;
                textText.setText("Time: " + t.getTimeFormattedString());
            }
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(this,textTime);
        newFragment.show(this.getFragmentManager(),"timePicker");
    }

    public void enter(View v){
        String newName = textName.getText().toString();
        int newQuantity = Integer.parseInt(textQuantity.getText().toString());
        String newInstructions = textInstructions.getText().toString();
        Pill updatePill = new Pill(newName,newQuantity,t.getHours(),t.getMinutes(),newInstructions);
        Pill oldPill = new Pill(name, quantity, oldT.getHours(), oldT.getMinutes(), instructions);

        FeedReaderContract.FeedReaderDbHelper feed = new FeedReaderContract().new FeedReaderDbHelper(this);
        SQLiteDatabase sql = feed.getWritableDatabase();
        feed.updatePill(sql,oldPill,updatePill);

        //NOTIFICATIONS
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,t.getHours());
        c.set(Calendar.MINUTE,t.getMinutes());
        // Fix so it says pill when quantity is one
        Time newTime = new Time(t.getHours(),t.getMinutes());
        alarmIntent.putExtra("title", "Take your " + name + " medication.");
        if(quantity == 1){
            alarmIntent.putExtra("detail","Take " + quantity +" pill.\n"+ "Additional Instructions: "+ instructions);
        }else{
            alarmIntent.putExtra("detail","Take " + quantity +" pills.\n"+ "Additional Instructions: "+ instructions);
        }
        alarmIntent.putExtra("id",feed.getIdByNameAndTime(sql, name, newTime));
        alarmIntent.putExtra("name",name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, feed.getIdByNameAndTime(sql, name, newTime),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 24*60*60*1000, pendingIntent);

        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

}
