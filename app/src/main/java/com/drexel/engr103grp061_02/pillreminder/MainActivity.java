package com.drexel.engr103grp061_02.pillreminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startAct(View view){
        Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void startAddAct(View view){
        Intent myIntent = new Intent(MainActivity.this, AddPill.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void startDeleteAct(View view){
        Intent myIntent = new Intent(MainActivity.this, Delete_Pill.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void startEditAct(View view){
        Intent myIntent = new Intent(MainActivity.this, AddPill.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void clearNotifications(View view){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        mNotificationManager.cancel(1);
    }


    public void testNotify(View view){
        notify("Test","Test detail");
    }

    public void notify(String title, String detail){
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,17);
        c.set(Calendar.MINUTE,38);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 5000, pendingIntent);
    }
}
