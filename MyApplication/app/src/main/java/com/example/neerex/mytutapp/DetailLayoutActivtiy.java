package com.example.neerex.mytutapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neerex.mytutapp.Utility.FileUtility;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;

public class DetailLayoutActivtiy extends AppCompatActivity {

    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    if(x2>x1) {
                        Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "right2left swipe", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layout_activtiy);
      final  Item item = getIntent().getParcelableExtra("Data");

        ImageView imview = (ImageView)findViewById(R.id.DetailImageView);
        imview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(DetailLayoutActivtiy.this,ViewPagerActivity.class);
//                intent.putExtra("Url",item.getEnclosure());
//                startActivity(intent);

            }
        });
        TextView txtviewdesc =(TextView)findViewById(R.id.txtdescription);
        TextView txtviewlink =(TextView)findViewById(R.id.txtLink);
        TextView txtdate =(TextView)findViewById(R.id.txtdate);

        if(item.getEnclosure() !=null) {

            boolean localpresent= false;
            File mypath;
            try {
                FileUtility fileobj = new FileUtility();
                String path= fileobj.Appfolderstructure(getApplicationContext(),"ImageDir");
                String name = URLUtil.guessFileName(item.getEnclosure(), null, null);
                mypath= new File(path+"/",name+".webp");
                if(mypath.exists())
                {
                    Picasso.with(this).load(mypath).resize(400, 300).placeholder(R.mipmap.progress).into(imview);
                }
                else
                {
                    Picasso.with(this).load(item.getEnclosure()).resize(400, 300).placeholder(R.mipmap.progress).into(imview);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else
        {
            imview.setImageResource(R.mipmap.default_image);
        }
      //  Picasso.with(this).load(item.getEnclosure()).resize(400,300).placeholder(R.drawable.progress_animation).into(imview);
        txtdate.setText(item.getPubDate());
        if(item.getDescription()!=null) {
            txtviewdesc.setText(item.getDescription());
        }
        else
        {
            txtviewdesc.setText("No Description available from the source");
        }
        txtviewlink.setText(item.getLink());

    }

}
