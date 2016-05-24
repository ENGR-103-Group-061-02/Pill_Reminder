package com.drexel.engr103grp061_02.pillreminder;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Pill;

public class PillNotificationPage extends Activity {

    public  void onCreate(Bundle savedInstanceState ) {
        TextView notifiedName, notifiedQuantity, notifiedInstructions, noteTime;
        //Takes in pill's Feeder ID
        Pill pill_notified;
        SQLiteDatabase sql;
        FeedReaderContract.FeedReaderDbHelper feed;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pill_notification);
        int id = this.getIntent().getIntExtra("id",0);

        //declaring ID's
        Button close = (Button) findViewById(R.id.closeButton);
        notifiedName = (TextView) findViewById(R.id.notified_name);
        notifiedQuantity = (TextView) findViewById(R.id.notified_quantity);
        notifiedInstructions = (TextView) findViewById(R.id.notified_instruction);
        noteTime = (TextView) findViewById(R.id.noteTime);

        //Database with ID
        feed = new FeedReaderContract().new FeedReaderDbHelper(getApplicationContext());
        sql = feed.getReadableDatabase();
        pill_notified = feed.getInfoFromID(id, sql);

        //pill_notified contains pill with SPECIFIC time info
        //
        //Re-Set text Fields from specified pill_notified object
        notifiedName.setText(pill_notified.getName());
        notifiedQuantity.setText(Integer.toString(pill_notified.getQuantity()));
        notifiedInstructions.setText(pill_notified.getInstructions());
        noteTime.setText(pill_notified.getTime().getTimeFormattedString());
    }

    public void close(View view){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

}
