package com.drexel.engr103grp061_02.pillreminder;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Pill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Pill> pills = new ArrayList<Pill>();
    ArrayList<String> last_taken = new ArrayList<String>();
    Cursor cursor;
    SQLiteDatabase sql;
    FeedReaderContract.FeedReaderDbHelper feed;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        flag = retrievePills();
        //
        //HOME PAGE TIME TILL NEXT PILL TRACKER START---------------------
        //
        if (pills.size()>0) {
            Calendar Now = Calendar.getInstance();
            int hour = Now.get(Calendar.HOUR_OF_DAY);
            int minute = Now.get(Calendar.MINUTE);
            int Total = minute + (hour * 60);
            //difference initialized as one more minute than 24 hours worth
            int tempH, tempM, tempTotal, difference = 1441;
            for (int c = 0; c < pills.size(); c++) {
                tempH = pills.get(c).getHours();
                tempM = pills.get(c).getMinutes();
                tempTotal = tempM + (tempH * 60);
                if (tempTotal > Total) {
                    if ((tempTotal - Total) < difference) {
                        difference = (tempTotal - Total);
                    }
                } else {
                    tempTotal = tempTotal + (1439 - Total);
                    if ((tempTotal - Total) < difference) {
                        difference = (tempTotal - Total);
                    }
                }
            }
            //HOURS AND MINUTES TILL NEXT PILL TO TAKE
            hour = difference / 60;
            minute = difference % 60;
        }
        else
        {
            //make text set to no pills inputted
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addPill) {
            Intent myIntent = new Intent(this, AddPill.class);
            this.startActivity(myIntent);
        } else if (id == R.id.nav_editPill) {
            Intent myIntent = new Intent(this, SelectPill.class);
            this.startActivity(myIntent);
        } else if (id == R.id.nav_deletePill) {
            Intent myIntent = new Intent(this, Delete_Pill.class);
            this.startActivity(myIntent);
        } else if (id == R.id.nav_clearPills){
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            mNotificationManager.cancelAll();
            feed.deleteTable(sql);
            feed.onCreate(sql);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public ArrayList findLast()
    {



        return null;
    }


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

}
