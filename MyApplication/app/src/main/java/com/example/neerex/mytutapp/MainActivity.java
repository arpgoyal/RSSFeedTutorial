package com.example.neerex.mytutapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.neerex.mytutapp.Db.DataContract;
import com.example.neerex.mytutapp.Db.DataHelper;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.IllegalFormatException;
import java.util.List;

public class MainActivity extends Activity  {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private static final String url ="http://www.nasa.gov/rss/dyn/image_of_the_day.rss";
    private List<Item> items;
    private String KEY = "lastdatesaved";

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  loadpage();
        DownloadXMLtask task= new DownloadXMLtask(this);
        final ListView mylistview = (ListView)findViewById(R.id.listview);
        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Item> items1 = new ArrayList<Item>();
                Item localitem = items.get(position);
                Item itemparcel = new Item(localitem.getTitle(), localitem.getLink(), localitem.getDescription(), localitem.getEnclosure(), localitem.getPubDate());
                Intent newActivity = new Intent(MainActivity.this, DetailLayoutActivtiy.class);
                newActivity.putExtra("Data", itemparcel);
                startActivity(newActivity);


            }
        });


            task.execute(url);
            task.result = new Asyncresult() {
                @Override
                public void onComplete(List<Item> list) {
                    items = list;


                    ItemAdapter myadapter = new ItemAdapter(items);
                    mylistview.setAdapter(myadapter);

                }
            };








    }




}

