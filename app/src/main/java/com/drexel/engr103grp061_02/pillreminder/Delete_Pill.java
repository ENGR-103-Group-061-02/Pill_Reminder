package com.drexel.engr103grp061_02.pillreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.drexel.engr103grp061_02.pillreminder.adapters.CustomCursorAdapter;
import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Time;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Liston on 5/2/2016.
 */
public class Delete_Pill extends Activity implements AdapterView.OnItemSelectedListener {
    SQLiteDatabase sql;
    String name;
    FeedReaderContract.FeedReaderDbHelper feed;
    Time t;
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_pill_layout);
        TextView defaultText = (TextView) findViewById(R.id.defaultText);
        feed = new FeedReaderContract().new FeedReaderDbHelper(this);
        sql = feed.getWritableDatabase();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Cursor cursor = feed.getInfo(sql);
        if(cursor.getCount()==0){
            defaultText.setText("No medications have been added");
        }else{
            defaultText.setText("");
        }
        CustomCursorAdapter spin_adapt = new CustomCursorAdapter (getApplicationContext(), cursor,0);
        spinner.setAdapter(spin_adapt);
        spinner.setOnItemSelectedListener(this);
        t = new Time();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(false).build();
        ImageView imgView =(ImageView) findViewById(R.id.deleteLogo);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().displayImage("assets://logo2.png", imgView);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        name = cursor.getString(1);
        t.setHours(cursor.getInt(3));
        t.setMinutes(cursor.getInt(4));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void delete_bttn(View view){
        int rowCount = feed.getRowCount(sql);
        if (rowCount==0){
            Toast.makeText(getBaseContext(), "No Medications to Delete", Toast.LENGTH_LONG).show();
        }else {
            int id = feed.getIdByNameAndTime(sql, name, t);
            feed.deleteData(sql, id);
            Toast.makeText(getBaseContext(), "Medication Deleted", Toast.LENGTH_LONG).show();
            //Removing the correct notification based off the unique ID given to the Pills in the database
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            alarmIntent.putExtra("id",id);
            alarmIntent.putExtra("name",name);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            mNotificationManager.cancel(id);
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
    }

}