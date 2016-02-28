package com.example.neerex.mytutapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neerex on 27/02/16.
 */
public class ItemAdapter extends BaseAdapter {

    private List<Item> items= new ArrayList<Item>() ;
    private  Context context;

    public ItemAdapter(List<Item> list,Context context)
    {
        this.items=list;
        this.context=context;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }


    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view =convertView;
        if(view==null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.list_item,parent,false);
        }

        Item localitem = items.get(position);
        ImageView Imageoftheday = (ImageView) view.findViewById(R.id.imageview);
        TextView Title = (TextView) view.findViewById(R.id.textView);


        if(localitem.getEnclosure() !=null) {

                Picasso.with(parent.getContext()).load(localitem.getEnclosure()).resize(80, 80).into(Imageoftheday);


        }

        if(localitem.getTitle() !=null) {
            Title.setText(localitem.getTitle());
        }


      return view;

    }

    public Bitmap CheckLocalDrive(int position) throws FileNotFoundException {

        if(context.getFilesDir()!=null) {
            String yourFilePath = context.getFilesDir() + "/" + String.valueOf(position) + ".jpg";

            File yourFile = new File(yourFilePath);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(yourFile));

            return b;
        }
        else
        {
            return  null;
        }

    }
}
