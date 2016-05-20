package com.drexel.engr103grp061_02.pillreminder;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Pill;

public class PillNotificationPage  extends Activity {
    TextView notifiedName, notifiedQuantity, notifiedInstrcutions, notifiedHours, notifiedMinutes;
    //Takes in pill's Feeder ID
    Pill pill_notified = new Pill();
    SQLiteDatabase sql;
    FeedReaderContract.FeedReaderDbHelper feed;
    public  void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pill_notification);
        int id = this.getIntent().getIntExtra("id",0);
        //declaring ID's
        notifiedName = (TextView) findViewById(R.id.notified_name);
        notifiedQuantity = (TextView) findViewById(R.id.notified_quantity);
        notifiedInstrcutions = (TextView) findViewById(R.id.notified_instruction);
        notifiedHours = (TextView) findViewById(R.id.notified_hour);
        notifiedMinutes = (TextView) findViewById(R.id.notified_minute);
        //Database with ID
        feed = new FeedReaderContract().new FeedReaderDbHelper(getApplicationContext());
        sql = feed.getReadableDatabase();
        pill_notified = feed.getInfoFromID(id, sql);
        //pill_notified contains pill with SPECIFIC time info
        //
        //Re-Set text Fields from specified pill_notified object
        notifiedName.setText(pill_notified.getName());
        notifiedQuantity.setText(pill_notified.getQuantity());
        notifiedInstrcutions.setText(pill_notified.getInstructions());
        notifiedHours.setText(pill_notified.getHours());
        notifiedMinutes.setText(pill_notified.getMinutes());
    }

}
