package com.drexel.engr103grp061_02.pillreminder;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.drexel.engr103grp061_02.pillreminder.adapters.CustomCursorAdapter;
import com.drexel.engr103grp061_02.pillreminder.database.FeedReaderContract;
import com.drexel.engr103grp061_02.pillreminder.database.Time;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class SelectPill extends AppCompatActivity implements AdapterView.OnItemClickListener {
    SQLiteDatabase sql;
    FeedReaderContract.FeedReaderDbHelper feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(false).build();
        ImageView imgView =(ImageView) findViewById(R.id.selectLogo);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().displayImage("assets://logo2.png", imgView);

        TextView defaultText = (TextView) findViewById(R.id.defaultSelect);
        feed = new FeedReaderContract().new FeedReaderDbHelper(this);
        sql = feed.getWritableDatabase();
        ListView listView = (ListView) findViewById(R.id.listSelectPill);
        Cursor cursor = feed.getInfo(sql);
        if(cursor.getCount()==0){
            defaultText.setText("No medications have been added");
        }else{
            defaultText.setText("");
        }
        CustomCursorAdapter list_adapt = new CustomCursorAdapter(this, cursor,0);
        listView.setAdapter(list_adapt);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        String name;
        int quantity;
        String instructions;
        Time t = new Time();

        name = cursor.getString(1);
        quantity = cursor.getInt(2);
        t.setHours(cursor.getInt(3));
        t.setMinutes(cursor.getInt(4));
        instructions = cursor.getString(5);

        Intent intent = new Intent(this, EditPill.class);
        intent.putExtra("name",name);
        intent.putExtra("quantity",quantity);
        intent.putExtra("hours",t.getHours());
        intent.putExtra("minutes",t.getMinutes());
        intent.putExtra("instructions",instructions);
        startActivity(intent);
    }

}
