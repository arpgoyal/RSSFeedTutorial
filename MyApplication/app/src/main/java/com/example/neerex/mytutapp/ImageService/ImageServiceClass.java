package com.example.neerex.mytutapp.ImageService;

/**
 * Created by Neerex on 28/02/16.
 */

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.neerex.mytutapp.Item;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ImageServiceClass extends Service {
    @Nullable

    private static final String TAG = "HelloService";

    private List<Item> listitem;

    private boolean isRunning  = false;
    private  Context context ;


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

          new Thread(new Runnable() {
              @Override
              public void run() {


                  for (int i = 0; i<listitem.size(); i++) {
                      try {

                          String url = listitem.get(i).getEnclosure();
                          if(url !=null) {
                              String path = SaveImageonInternaldevice(url, i);
                          }

                      } catch (Exception e) {
                          Log.e(TAG, "run: ",e );
                      }

                      if(isRunning){
                          Log.i(TAG, "Service running");
                      }
                  }


                  stopSelf();
              }
          });


        return Service.START_NOT_STICKY;


    }


    private  String SaveImageonInternaldevice(String url, int i) throws IOException {

        ContextWrapper cw =new ContextWrapper(getApplicationContext());

        Bitmap bitmapimage= Picasso.with(context).load(url).resize(400,400).get();


        File directory =cw.getDir("ImageDir", Context.MODE_PRIVATE);


        File mypath = new File(directory, String.valueOf(i)+".jpg");

        FileOutputStream fos=null;

        try
        {
            fos = new FileOutputStream(mypath);
            bitmapimage.compress(Bitmap.CompressFormat.WEBP,100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            fos.close();
        }

        return   directory.getAbsolutePath();


    }
}

