package com.example.neerex.mytutapp.ImageService;

/**
 * Created by Neerex on 28/02/16.
 */

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.neerex.mytutapp.Item;
import com.example.neerex.mytutapp.MainActivity;
import com.example.neerex.mytutapp.Utility.FileUtility;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ImageServiceClass extends Service {
    @Nullable

    private static final String TAG = "MyService";

    private List<Item> listitem;

    private boolean isRunning  = false;
    private  Context context ;

    public  ImageServiceClass()
    {

    }

    public  ImageServiceClass(Context context, List<Item> listitem)
    {
        this.listitem =listitem;
        this.context =context;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public  void onCreate()
    {

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


                 String[] url = intent.getStringArrayExtra("Items");

                  for (int i = 0; i<url.length; i++) {
                      try {


                          if(url[i] !=null) {
                              String path = SaveImageonInternaldevice(url[i], i);
                          }

                      } catch (Exception e) {
                          Log.e(TAG, "run: ",e );
                      }

                      if(isRunning){
                          Log.i(TAG, "Service running");
                      }
                  }


                  stopSelf();



        return Service.START_NOT_STICKY;


    }


    public String SaveImageonInternaldevice(String url, int i) throws IOException {

       ContextWrapper cw =new ContextWrapper(getApplicationContext());
//
//        Context context = getApplicationContext();
//
//        Bitmap bitmapimage= Picasso.with(this.getBaseContext()).load(url).resize(400,400).get();

        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FileUtility fileobj = new FileUtility();
        fileobj.Appfolderstructure(context,"ImageDir");
        File directory =cw.getDir("ImageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, String.valueOf(i)+".jpg");

        FileOutputStream fos=null;

        try
        {
            fos = new FileOutputStream(mypath);
            bm.compress(Bitmap.CompressFormat.WEBP,100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            fos.close();
        }

        return   directory.getAbsolutePath();


    }
}

