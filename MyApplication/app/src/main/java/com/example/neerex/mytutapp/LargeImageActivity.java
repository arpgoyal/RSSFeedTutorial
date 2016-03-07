package com.example.neerex.mytutapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.example.neerex.mytutapp.Utility.FileUtility;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Neerex on 06/03/16.
 */
public class LargeImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        ImageView imview =(ImageView)findViewById(R.id.ImageView1);

        String url = getIntent().getStringExtra("Url");

        if(url !=null) {

            boolean localpresent= false;
            File mypath;
            try {
                FileUtility fileobj = new FileUtility();
                String path= fileobj.Appfolderstructure(getApplicationContext(),"ImageDir");
                String name = URLUtil.guessFileName(url, null, null);
                mypath= new File(path+"/",name+".webp");
                if(mypath.exists())
                {
                    Picasso.with(this).load(mypath).resize(400,600).placeholder(R.mipmap.progress).into(imview);
                }
                else
                {
                    Picasso.with(this).load(url).resize(400,600).placeholder(R.mipmap.progress).into(imview);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
