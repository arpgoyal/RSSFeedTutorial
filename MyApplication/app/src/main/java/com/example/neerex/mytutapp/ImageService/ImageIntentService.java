package com.example.neerex.mytutapp.ImageService;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.neerex.mytutapp.Utility.FileUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;

/**
 * Created by Neerex on 01/03/16.
 */
public class ImageIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "ImageService";

    public   ImageIntentService()
    {
         super(ImageIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            Log.d(TAG, "Service Started!");
       //     String[] url = intent.getStringArrayExtra("Items");
            String url = intent.getStringExtra("Items");
            String path = intent.getStringExtra("path");
//            if (url != null) {
//                for (int i = 0; i < url.length; i++) {
//                   if(url[i]!=null) {
//                       SaveImage(url[i], i, path);
//                       Log.d(TAG, "Service saving the image!");
//
//                   }
//                }
//            }

            if (url != null) {
                //for (int i = 0; i < url.length; i++) {

                        SaveImage(url,1, path);
                        Log.d(TAG, "Service saving the image!");

                   // }

            }
            Log.d(TAG, "Service Stopping!");
            this.stopSelf();
        }
        catch (Exception ex)
        {
            Log.e(TAG, "onHandleIntent: ",ex );
        }
    }



    public void SaveImage(String url, int i,String path )
    {

            try
            {
                URL Imageurl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)Imageurl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream is =connection.getInputStream();

               

                Bitmap imagethumb = BitmapFactory.decodeStream(is);
                 if(imagethumb!=null)
                 {
                     Log.d(TAG, "SaveImage:null image");
                 }



                //ByteArrayOutputStream out = new ByteArrayOutputStream();
               // image.compress(Bitmap.CompressFormat.WEBP,100,out);
               // Bitmap compressed =BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                String name =URLUtil.guessFileName(url, null, null);

                File mypaththumb= new File(path+"/"+"thumbnail"+"/",name+".webp");
                FileOutputStream fosthumb;
                fosthumb = new FileOutputStream(mypaththumb);
                imagethumb.compress(Bitmap.CompressFormat.WEBP, 10, fosthumb);
              //  imagethumb.recycle();


              //  Bitmap image = BitmapFactory.decodeStream(is);
                File mypath= new File(path+"/",name+".webp");
                FileOutputStream fos;
                fos = new FileOutputStream(mypath);
                imagethumb.compress(Bitmap.CompressFormat.WEBP,30, fos);
                imagethumb.recycle();


                is.close();
                fos.close();
                fosthumb.close();


            }
            catch (Exception ex)
            {
                Log.e(TAG, "SaveImage: ", ex);
            }



    }


}
