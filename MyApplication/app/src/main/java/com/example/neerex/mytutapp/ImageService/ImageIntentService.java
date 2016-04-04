package com.example.neerex.mytutapp.ImageService;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.neerex.mytutapp.R;
import com.example.neerex.mytutapp.Utility.FileUtility;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

                        SaveImagebyPicaaso(url, path);
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


    public  void SaveImagebyPicaaso(String url,String path) throws IOException {

        Bitmap  ThumbnailImage = Picasso.with(this).load(url).resize(80, 80).get();
        String name =URLUtil.guessFileName(url, null, null);
        File mypaththumb = new File(path+"/"+"thumbnail"+"/",name+".webp");
        FileOutputStream fosthumb;
        fosthumb = new FileOutputStream(mypaththumb);
        if(ThumbnailImage!=null) {
            ThumbnailImage.compress(Bitmap.CompressFormat.WEBP, 100, fosthumb);

        }


       int  index = url.lastIndexOf("http://media");
        String temp= url.substring(index);
        Bitmap  image = Picasso.with(this).load(temp).resize(400, 300).get();
        String fullname =URLUtil.guessFileName(url, null, null);
        //  Bitmap image = BitmapFactory.decodeStream(is);
        File mypath= new File(path+"/",fullname+".webp");
        FileOutputStream fos;
        fos = new FileOutputStream(mypath);
        if(image!=null) {
            image.compress(Bitmap.CompressFormat.WEBP, 30, fos);

        }


        fos.close();
        fosthumb.close();


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



                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, options);
                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, 80, 80);
                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                Bitmap imagethumb=BitmapFactory.decodeStream(new BufferedInputStream(is), null, options);
          //      Bitmap imagethumb = BitmapFactory.decodeStream(is);


                String name =URLUtil.guessFileName(url, null, null);
                File mypaththumb = new File(path+"/"+"thumbnail"+"/",name+".webp");
                FileOutputStream fosthumb;
                fosthumb = new FileOutputStream(mypaththumb);
                if(imagethumb!=null) {
                    imagethumb.compress(Bitmap.CompressFormat.WEBP, 100, fosthumb);
                    imagethumb.recycle();
                }



                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, options);
                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, 200, 200);
                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                Bitmap image=BitmapFactory.decodeStream(new BufferedInputStream(is), null, options);
                //      Bitmap imagethumb = BitmapFactory.decodeStream(is);


                String fullname =URLUtil.guessFileName(url, null, null);
              //  Bitmap image = BitmapFactory.decodeStream(is);
                File mypath= new File(path+"/",fullname+".webp");
                FileOutputStream fos;
                fos = new FileOutputStream(mypath);
                if(image!=null) {
                    image.compress(Bitmap.CompressFormat.WEBP, 100, fos);
                    image.recycle();

                }

                is.close();
                fos.close();
                fosthumb.close();


            }
            catch (Exception ex)
            {
                Log.e(TAG, "SaveImage: ", ex);
            }



    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
