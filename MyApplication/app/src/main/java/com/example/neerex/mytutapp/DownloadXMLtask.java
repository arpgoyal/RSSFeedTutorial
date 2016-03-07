package com.example.neerex.mytutapp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.neerex.mytutapp.Db.DataContract;
import com.example.neerex.mytutapp.Db.DataHelper;
import com.example.neerex.mytutapp.ImageService.ImageIntentService;
import com.example.neerex.mytutapp.ImageService.ImageServiceClass;
import com.example.neerex.mytutapp.Utility.FileUtility;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Neerex on 26/02/16.
 */
public class DownloadXMLtask extends AsyncTask<String,Void,List<Item>> {

    public  Asyncresult result;
    public  Context context;
    private SharedPreferences sharedpreferences;
    private String KEY = "lastdatesaved";

    ProgressDialog pd ;




      public DownloadXMLtask(Context context)
      {
          //result=resultobj;
          this.context =context;
           sharedpreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
      }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setTitle("Please wait....");
        pd.setMessage("Loading......");
        pd.setCancelable(false);

    }


    @Override
    protected List<Item> doInBackground(String... urls) {

        List<Item> data =new ArrayList<Item>();
        try
        {

            if(!CheckDate()) {
                 data = loadxmlfromNetwork(urls[0]);
                if(!CheckSize(data)) {
                    UpdateSql(data);
                    FileUtility fileobj = new FileUtility();
                    String path = fileobj.Appfolderstructure(context, "ImageDir");


                    String[] url = new String[data.size()];
                    for (int i = 0; i < url.length; i++) {
                        url[i] = data.get(i).getEnclosure();
                    }
                }
                else
                {
                    data =ReadDataSql();
                }
               // ImageServiceClass obj = new ImageServiceClass(context,data);

//                Intent intent = new Intent(context, ImageIntentService.class);
//                intent.putExtra("Items",url);
//                intent.putExtra("path",path);
//                context.startService(intent);
            }
            else
            {
                data =ReadDataSql();
            }



        }
        catch(Exception ex)
        {
            Log.e("Tag", "doInBackground: ",ex );
        }
        return data;
    }

    @Override
    protected void onPostExecute(List<Item> list) {
        pd.dismiss();
      result.onComplete(list);

    }

    private List loadxmlfromNetwork(String urls) throws IOException {

        InputStream stream =null;
        List<Item> items =null;

        try {

            stream = downloadUrl(urls);
           Parser parser = new Parser();
           items=  parser.getparseddata(stream);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(stream !=null)
            {
                stream.close();
            }
        }

        return items;

    }

    private  InputStream downloadUrl(String urls) throws IOException {
        URL url = new URL(urls);
        HttpURLConnection conn =(HttpURLConnection)url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(20000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();




    }

    public void UpdateSql(List<Item> items)
    {

        ArrayList<Long> rowid =new ArrayList<Long>();
        DataHelper dbhelper = new DataHelper(context);
        SQLiteDatabase sqldbobj = dbhelper.getWritableDatabase();
        String lastdatesaved= null;
        ContentValues values = new ContentValues();


        try {
            for (int i = items.size(); i >= 0; i--) {
                values.put(DataContract.DataEntry.Column_Name_title, items.get(i).getTitle());
                values.put(DataContract.DataEntry.Column_Name_link, items.get(i).getLink());
                values.put(DataContract.DataEntry.Column_Name_description, items.get(i).getDescription());
                values.put(DataContract.DataEntry.Column_Name_enclosure, items.get(i).getEnclosure());
                values.put(DataContract.DataEntry.Column_Name_pubdate, items.get(i).getPubDate());

                lastdatesaved = items.get(0).getPubDate();


                long newRowID;
                newRowID = sqldbobj.insert(DataContract.DataEntry.Table_Name, null, values);
                rowid.add(newRowID);
            }

            if (!lastdatesaved.isEmpty()) {
                updateSharedPreference("lastdatesaved", lastdatesaved);
                updateSharedPreference("size", String.valueOf(items.size()));

            }
        }
        catch (Exception e)
        {


        }
        finally
        {
            sqldbobj.close();
        }



    }


    public  List<Item> ReadDataSql()
    {
        DataHelper  dbhelper = new DataHelper(context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Item items ;

        List<Item> Listitems = new ArrayList<Item>();

        String[] Projection =
                {
                        DataContract.DataEntry._ID,
                        DataContract.DataEntry.Column_Name_title,
                        DataContract.DataEntry.Column_Name_link,
                        DataContract.DataEntry.Column_Name_description,
                        DataContract.DataEntry.Column_Name_enclosure,
                        DataContract.DataEntry.Column_Name_pubdate,
                };


        Cursor c=db.query(DataContract.DataEntry.Table_Name,Projection,null,null,null,null,null);

        if (c.moveToFirst()){
            do{
                String title = c.getString(c.getColumnIndex(DataContract.DataEntry.Column_Name_title));
                String link = c.getString(c.getColumnIndex(DataContract.DataEntry.Column_Name_link));
                String description = c.getString(c.getColumnIndex(DataContract.DataEntry.Column_Name_description));
                String enclosure = c.getString(c.getColumnIndex(DataContract.DataEntry.Column_Name_enclosure));
                String pubdate = c.getString(c.getColumnIndex(DataContract.DataEntry.Column_Name_pubdate));
                items= new Item(title,link,description,enclosure,pubdate);
                Listitems.add(items);
            }while(c.moveToNext());
        }
        c.close();

        return Listitems;
    }

    public void  updateSharedPreference(String Key, String Value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Key, Value);
        editor.commit();
    }



    public String  getSharedPreference(String key)
    {
        SharedPreferences pref=  context.getSharedPreferences(KEY, Context.MODE_PRIVATE);

        String value = pref.getString(key, null);
        return value;
    }



    public boolean CheckDate() throws ParseException {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:MM");
        String CurrentDate = df.format(c.getTime());
        Boolean  value =false;

        String Lastsaveddate=getSharedPreference(KEY);
        if(Lastsaveddate!=null) {
            SimpleDateFormat dfq = new SimpleDateFormat("EEE");
            String CurrentDateq = dfq.format(c.getTime());
            String[] s = Lastsaveddate.split(",");
           // Date datetype = df.parse(Lastsaveddate);
         //   String dateday= dfq.format(datetype);
            if (CurrentDateq.equals("Sat")||CurrentDateq.equals("Sun"))
            {
                value=true;
            } else if (s[0].equals(CurrentDateq)) {
                value =true;
            }
        }


        return  value;

    }


    public boolean CheckSize(List<Item> data)  {

         boolean value =false;

        if(getSharedPreference("size")!=null) {
            int savedsize = Integer.valueOf(getSharedPreference("size"));
            int size = data.size();

            if (size == savedsize) {
                return value = true;
            } else {
                return value;
            }
        }
        else
        {
            return value;
        }

    }
}
