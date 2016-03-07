package com.example.neerex.mytutapp.Utility;

import android.content.Context;

import java.io.File;

/**
 * Created by Neerex on 29/02/16.
 */
public class FileUtility {


    public String  Appfolderstructure(Context context,String Dirname)
    {
        File file = context.getExternalFilesDir(null);
        if(!file.exists())
            file.mkdirs();

        String path = file.getPath();

        File thumbnailpath = new File(path+"/"+"thumbnail");
        if(!thumbnailpath.exists())
            thumbnailpath.mkdirs();

        return  path;

    }






}
