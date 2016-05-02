package com.drexel.engr103grp061_02.pillreminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Pill;

import java.util.ArrayList;

/**
 * Created by Liston on 5/2/2016.
 */
public class Delete_Pill extends Activity {
    String search_name,delete_name;
    EditText search;
    ArrayList<Pill> pills = new ArrayList<Pill>();
    Cursor cursor;
    SQLiteDatabase sql;
    FeedReaderContract.FeedReaderDbHelper feed;
    Context context = this;
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_pill_layout);
        search = (EditText) findViewById(R.id.pill_search);
        retrievePills();
    }


    public void search_bttn(View view)
    {
     search_name = search.getText().toString();
     boolean med_found=false;
        for (int i=0; i<pills.size(); i++)
        {if (search_name.equalsIgnoreCase(pills.get(i).getName())) {med_found=true;}}
     if (med_found==false){Toast.makeText(getBaseContext(),"The Medication was Not Found", Toast.LENGTH_LONG).show();}
     else
     {
         Toast.makeText(getBaseContext(),"Medication Found", Toast.LENGTH_LONG).show();
         delete_name = search_name;
     }
     }

    public void delete_bttn(View view)
    {
        feed = new FeedReaderContract().new FeedReaderDbHelper(getApplicationContext());
        sql = feed.getReadableDatabase();
        feed.deleteData(sql, delete_name);
        //Toast alerts user with prompted text
        Toast.makeText(getBaseContext(), "Medication Deleted", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }











    //USED ON CREATE OF PAGE, RE-establishes array list object of pills
    public void retrievePills(){
        int counter=0;
        feed = new FeedReaderContract().new FeedReaderDbHelper(getApplicationContext());
        sql = feed.getReadableDatabase();
        cursor = feed.getInfo(sql);
        if (cursor.moveToFirst())
        {
            do {
                pills.add(new Pill());
                pills.get(counter).setName(cursor.getString(0));
                pills.get(counter).setQuantity(Integer.parseInt(cursor.getString(1)));
                pills.get(counter).setTime(Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
                pills.get(counter).setInstrutctions(cursor.getString(4));
                counter++;
            } while(cursor.moveToNext());
        }



    }



}