package com.drexel.engr103grp061_02.pillreminder;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Pill;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PillNotificationPage extends Activity {

    public  void onCreate(Bundle savedInstanceState ) {
        TextView notifiedName, notifiedQuantity, notifiedInstructions, noteTime;
        //Takes in pill's Feeder ID
        Pill pill_notified;
        SQLiteDatabase sql;
        FeedReaderContract.FeedReaderDbHelper feed;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pill_notification);
        int id = this.getIntent().getIntExtra("id", 0);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(false).build();
        ImageView imgView = (ImageView) findViewById(R.id.noteLogo);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().displayImage("assets://logo2.png", imgView);

        //declaring ID's
        Button close = (Button) findViewById(R.id.closeButton);
        notifiedName = (TextView) findViewById(R.id.noteName);
        notifiedQuantity = (TextView) findViewById(R.id.noteQuantity);
        notifiedInstructions = (TextView) findViewById(R.id.noteInstructions);
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
        if (pill_notified.getInstructions() == null) {
            notifiedInstructions.setText("No Additional Information");
        } else {
            if (pill_notified.getInstructions().equalsIgnoreCase("")) {
                notifiedInstructions.setText("No Additional Information");
            } else {
                notifiedInstructions.setText(pill_notified.getInstructions());
            }
            noteTime.setText(pill_notified.getTime().getTimeFormattedString());
        }
    }

    public void close(View view){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

}
