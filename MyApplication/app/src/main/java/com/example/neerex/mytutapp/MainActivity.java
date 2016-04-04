package com.example.neerex.mytutapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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

public class MainActivity extends AppCompatActivity  {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private static final String url ="http://www.nasa.gov/rss/dyn/image_of_the_day.rss";
    private static final String urlnews = "http://news.yahoo.com/rss/entertainment";
    private static final String googlenews ="https://news.google.com/news?cf=all&hl=en&pz=1&ned=in&q=all&output=rss";
 //   https://news.google.com/news/section?cf=all&ned=in&topic=w&output=rss


    private List<Item> items;
    private String KEY = "lastdatesaved";
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    public Context context;
    ViewPagerAdapter adapter;
    RelativeLayout layoutlistview;
    RelativeLayout layoutDetailview;
    int  tabposition =0;

    public  Context getContext(){
        return context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadXMLtask task= new DownloadXMLtask(this);
        context =this;

        progressBar = (ProgressBar)findViewById(R.id.pbar);
        progressBar.setVisibility(View.VISIBLE);

        tabLayout = (TabLayout)findViewById(R.id.tabs);

        //tabLayout.setupWithViewPager(pager);
        tabLayout.addTab(tabLayout.newTab().setText("List_View").setIcon(R.mipmap.list),true);
        tabLayout.addTab(tabLayout.newTab().setText("Page_View").setIcon(R.mipmap.page));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              int postion= tab.getPosition();

              if(postion==0)
              {
                  if(layoutDetailview !=null)
                  {
                      layoutDetailview.setVisibility(View.GONE);
                  }
                  layoutlistview =(RelativeLayout)findViewById(R.id.rvrecyclerviewmain);
                  layoutlistview.setVisibility(View.VISIBLE);





                  tabposition =0;
              }
              else
              {
                  if(layoutlistview !=null) {

                      layoutlistview.setVisibility(View.GONE);
                  }
                  layoutDetailview=(RelativeLayout)findViewById(R.id.viewpagerlayoutmain);
                  layoutDetailview.setVisibility(View.VISIBLE);


                  if(items !=null)
                  {
                      adapter = new ViewPagerAdapter(items,context);
                      ViewPager pager = (ViewPager)findViewById(R.id.viewpager1);
                      pager.setAdapter(adapter);

                  }
                 // setContentView(R.layout.viewpagerlayout);


                  tabposition=1;
              }

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

//        final ListView mylistview = (ListView)findViewById(R.id.listview);
//        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                List<Item> items1 = new ArrayList<Item>();
//                Item localitem = items.get(position);
//                Item itemparcel = new Item(localitem.getTitle(), localitem.getLink(), localitem.getDescription(), localitem.getEnclosure(), localitem.getPubDate());
//                Intent newActivity = new Intent(MainActivity.this, DetailLayoutActivtiy.class);
//                newActivity.putExtra("Data", itemparcel);
//                startActivity(newActivity);
//
//
//            }
//        });


            task.execute(urlnews);
            task.result = new Asyncresult() {
                @Override
                public void onComplete(List<Item> list) {
                    items = list;

                    if(tabposition==0) {

                        RAdapter adapter1 = new RAdapter(items, context);
                       // setContentView(R.layout.activity_recycleview);
                        recyclerView = (RecyclerView) findViewById(R.id.MainViewrv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                       // recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        recyclerView.setAdapter(adapter1);
                        progressBar.setVisibility(View.INVISIBLE);
                        // ItemAdapter myadapter = new ItemAdapter(items,context);
                        //  recyclerView.setAdapter(myadapter);
                    }
                    else
                    {
                        adapter = new ViewPagerAdapter(items,context);
                        ViewPager pager = (ViewPager)findViewById(R.id.viewpager1);
                        pager.setAdapter(adapter);
                    }
                }
            };









    }

    private  List<Fragment> getfragment()
    {
        List<Fragment> list= new ArrayList<Fragment>();
        list.add(FragmentViewPager.newInstance("Fragement1"));
        list.add(FragmentViewPager.newInstance("Fragement2"));

        return list;

    }



}

