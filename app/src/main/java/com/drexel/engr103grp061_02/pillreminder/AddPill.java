package com.drexel.engr103grp061_02.pillreminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Pill;

import java.util.ArrayList;


public class AddPill extends Activity{
    EditText Pill_name, Pill_quantity, Pill_instructions, Pill_hours, Pill_minutes;
    ArrayList<Pill> pills = new ArrayList<Pill>();
    Cursor cursor;
    SQLiteDatabase sql;
    FeedReaderContract.FeedReaderDbHelper feed;
    Context context = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pill);
        Pill_name = (EditText) findViewById(R.id.Pill_name);
        Pill_quantity = (EditText) findViewById(R.id.Pill_quantity);
        Pill_instructions = (EditText) findViewById(R.id.Pill_instructions);
        Pill_hours = (EditText) findViewById(R.id.Pill_hours);
        Pill_minutes = (EditText) findViewById(R.id.Pill_minutes);
        retrievePills();
    }

    //               BUTTON METHODS BELOW:

    // Button to add Pill with only one time
    public void add_pill(View view)
    {
        //converting all inputs to readeable vars
        boolean matches=false, allowed;
        String name = Pill_name.getText().toString();
        String quantity = Pill_quantity.getText().toString();
        String instructions = Pill_instructions.getText().toString();
        String hours = Pill_hours.getText().toString();
        String minutes = Pill_minutes.getText().toString();
        //checks for name matches
        for (int i=0; i<pills.size(); i++)
        {
            if (name.equals(pills.get(i).getName()))
            {matches=true;}
        }
        if (matches)
        {Toast.makeText(getBaseContext(), "Pill Previously Inserted", Toast.LENGTH_LONG).show();}
        else
        {
            //CHECKS EVERY ENTRY FOR UNUSEABLE CHARACTERS
            allowed=name.matches("[a-zA-Z1-9]+");
            allowed=quantity.matches("[1-9]+");
            allowed=instructions.matches("[a-zA-Z1-9]+");
            allowed=hours.matches("[1-9]+");
            allowed=minutes.matches("[1-9]");
            //need to implement checkers for time constraints on hours and minutes
            if (allowed==false)
            {
                Toast.makeText(getBaseContext(),"Un-Allowed Character Entered",Toast.LENGTH_LONG).show();
            }
            else
            {
                int hoursINT = Integer.parseInt(hours);
                int minutesINT = Integer.parseInt(minutes);
                //IF ELSE HERE CHECKS HOURS AND MINUTES IN RANGE
                if ((minutesINT<0)||(minutesINT>59)||(hoursINT<0)||(hoursINT>23))
                {Toast.makeText(getBaseContext(),"Hours or Minutes Un-Allowed",Toast.LENGTH_LONG).show();}
                //THIS ELSE REPRESENTS IF EVERYTHING IS FINE AND IS PASSED INTO DATABASE
                else {
                    //ADDING TO SQL
                    feed = new FeedReaderContract().new FeedReaderDbHelper(context);
                    sql = feed.getWritableDatabase();
                    feed.addData(sql, name, Integer.parseInt(quantity), hoursINT, minutesINT, instructions);
                    feed.close();
                    //intent Brings you back to main class
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }

            }

        }

    }

    // Button to add additional times to pill, ends with Add pill ultimately
    public void additional_time(View view)
    {
        //converting all inputs to readeable vars
        boolean matches=false, allowed;
        String name = Pill_name.getText().toString();
        String quantity = Pill_quantity.getText().toString();
        String instructions = Pill_instructions.getText().toString();
        String hours = Pill_hours.getText().toString();
        String minutes = Pill_minutes.getText().toString();
        //checks for name matches
        for (int i=0; i<pills.size(); i++)
        {
            if (name.equals(pills.get(i).getName()))
            {matches=true;}
        }
        if (matches)
        {Toast.makeText(getBaseContext(), "Pill Previously Inserted", Toast.LENGTH_LONG).show();}
        else
        {
            //CHECKS EVERY ENTRY FOR UNUSEABLE CHARACTERS
            allowed=name.matches("[a-zA-Z1-9]+");
            allowed=quantity.matches("[1-9]+");
            allowed=instructions.matches("[a-zA-Z1-9]+");
            allowed=hours.matches("[1-9]+");
            allowed=minutes.matches("[1-9]");
            //need to implement checkers for time constraints on hours and minutes
            if (allowed==false)
            {
                Toast.makeText(getBaseContext(),"Un-Allowed Character Entered",Toast.LENGTH_LONG).show();
            }
            else
            {
                int hoursINT = Integer.parseInt(hours);
                int minutesINT = Integer.parseInt(minutes);
                //IF ELSE HERE CHECKS HOURS AND MINUTES IN RANGE
                if ((minutesINT<0)||(minutesINT>59)||(hoursINT<0)||(hoursINT>23))
                {Toast.makeText(getBaseContext(),"Hours or Minutes Un-Allowed",Toast.LENGTH_LONG).show();}
                else {
                    //ADDING TO SQL
                    feed = new FeedReaderContract().new FeedReaderDbHelper(context);
                    sql = feed.getWritableDatabase();
                    feed.addData(sql, name, Integer.parseInt(quantity),hoursINT, minutesINT, instructions);
                    feed.close();
                }
            }

        }





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