package com.drexel.engr103grp061_02.pillreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.drexel.engr103grp061_02.pillreminder.adapters.CustomArrayListAdapter;
import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Pill;

import java.util.ArrayList;
import java.util.Calendar;

// TODO COMMENTS REALLY NEEDS COMMENTS, edit the xml to disable certain characters, move the list view to be centered.
// TODO Check the user actually inputted data and fix if they did not
// This Class was way too complicated and will be changed to be more readable and easier to understand the flow of data
// EDIT It is still complicated just because of the nested class. I will probably move the class down to the bottom
// so it is out of the way and it is easier to understand the rest of the activity.

public class AddPill extends Activity{
    EditText Pill_name, Pill_quantity, Pill_instructions;
    ArrayList<Pill> pills = new ArrayList<Pill>();
    Cursor cursor;
    SQLiteDatabase sql;
    FeedReaderContract.FeedReaderDbHelper feed;
    boolean emptyFlag;
    static ArrayList<Time> times;
    public static ListView timeList;

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
            for (Time p:times) {
                if (p.equals(addTime)){
                    existingTime = true;
                }
            }
            if(existingTime){
                Toast.makeText(context, "Time already added \n Enter a new time", Toast.LENGTH_LONG).show();
            }else{
                times.add(addTime);
                CustomArrayListAdapter adapt = new CustomArrayListAdapter(context,times);
                timeList.setAdapter(adapt);
            }

        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pill);
        Pill_name = (EditText) findViewById(R.id.Pill_name);
        Pill_quantity = (EditText) findViewById(R.id.Pill_quantity);
        Pill_instructions = (EditText) findViewById(R.id.Pill_instructions);
        emptyFlag = retrievePills();
        timeList = (ListView) findViewById(R.id.timeList);
        times = new ArrayList<Time>();
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(this);
        newFragment.show(this.getFragmentManager(),"timePicker");
    }

    //               BUTTON METHODS BELOW:

    // Button to add Pill with only one time
    public void add_pill(View view) {
        String name = Pill_name.getText().toString();
        int quantity = Integer.parseInt(Pill_quantity.getText().toString());
        String instructions = Pill_instructions.getText().toString();
        for(Time t: times){
            Pill newPill = new Pill(name,quantity,t.getHours(),t.getMinutes(),instructions);
            if(check_pill(newPill)){
                addToDataBase(newPill);
            }
        }
        Toast.makeText(getBaseContext(), "Medication Added", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    //returns true if the pills are not equal
    public boolean check_pill(Pill newPill){
        for (Pill p:pills) {
            if(p.getName().equalsIgnoreCase(newPill.getName())&& p.getHours() == newPill.getHours()
                    && p.getMinutes() == newPill.getMinutes()){
                return false;
            }
        }
        return true;
    }

    //USED ON CREATE OF PAGE, RE-establishes array list object of pills
    // The boolean return checks for an empty database and allows for the correct sequence of events
    // to occur in addPill
    public boolean retrievePills() {
        int counter = 0;
        feed = new FeedReaderContract().new FeedReaderDbHelper(getApplicationContext());
        sql = feed.getReadableDatabase();
        cursor = feed.getInfo(sql);
        if (cursor.getCount() == 0) {
           return false;
        } else {
            if (cursor.moveToFirst()) {
                do {
                    pills.add(new Pill());
                    pills.get(counter).setName(cursor.getString(1));
                    pills.get(counter).setQuantity(Integer.parseInt(cursor.getString(2)));
                    pills.get(counter).setTime(Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
                    pills.get(counter).setInstrutctions(cursor.getString(5));
                    counter++;
                } while (cursor.moveToNext());
            }
        }
        return true;
    }

    //SEND SINGLE INDEX OF new_pill OBJECT TO DATABASE, LOOP IF SEVERAL
    public void addToDataBase (Pill APill){
        //converting all inputs to readable vars
        boolean matches=false, allowed;
        String name = APill.getName();
        int quantity = APill.getQuantity();
        String instructions = APill.getInstructions();
        int hoursINT = APill.getHours();
        int minutesINT = APill.getMinutes();
        //THIS ELSE REPRESENTS IF EVERYTHING IS FINE AND IS PASSED INTO DATABASE
        //ADDING TO SQL
        feed = new FeedReaderContract().new FeedReaderDbHelper(this);
        sql = feed.getWritableDatabase();
        // feed.onCreate(sql);
        feed.addData(sql, name, quantity, hoursINT, minutesINT, instructions);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hoursINT);
        c.set(Calendar.MINUTE,minutesINT);
        // Fix so it says pill when quantity is one
        Time newTime = new Time(hoursINT,minutesINT);
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
        Log.d("ID", Integer.toString(feed.getIdByNameAndTime(sql, name, newTime)));
        feed.close();
    }
}
