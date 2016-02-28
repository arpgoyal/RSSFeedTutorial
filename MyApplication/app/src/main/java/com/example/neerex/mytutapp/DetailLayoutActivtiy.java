package com.example.neerex.mytutapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailLayoutActivtiy extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layout_activtiy);
        Item item = getIntent().getParcelableExtra("Data");

        ImageView imview = (ImageView)findViewById(R.id.DetailImageView);
        TextView txtviewdesc =(TextView)findViewById(R.id.txtdescription);
        TextView txtviewlink =(TextView)findViewById(R.id.txtLink);

        Picasso.with(this).load(item.getEnclosure()).resize(400,300).into(imview);
        txtviewdesc.setText(item.getDescription());
        txtviewlink.setText(item.getLink());

    }

}
