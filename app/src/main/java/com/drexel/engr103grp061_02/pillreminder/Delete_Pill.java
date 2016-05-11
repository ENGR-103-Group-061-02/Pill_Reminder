package com.drexel.engr103grp061_02.pillreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.drexel.engr103grp061_02.pillreminder.adapters.CustomCursorAdapter;
import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Pill;

import java.util.ArrayList;

/**
 * Created by Liston on 5/2/2016.
 */
public class Delete_Pill extends Activity implements AdapterView.OnItemSelectedListener {
    EditText search;
    SQLiteDatabase sql;
    FeedReaderContract.FeedReaderDbHelper feed;
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_pill_layout);
        feed = new FeedReaderContract().new FeedReaderDbHelper(this);
        sql = feed.getWritableDatabase();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Cursor cursor = feed.getInfo(sql);
        CustomCursorAdapter spin_adapt = new CustomCursorAdapter (getApplicationContext(), cursor,0);
        spinner.setAdapter(spin_adapt);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        String name = cursor.getString(1);
        feed.deleteData(sql, name);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void delete_bttn(View view)
    {
        Toast.makeText(getBaseContext(), "Medication Deleted", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}