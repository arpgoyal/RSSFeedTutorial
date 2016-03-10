package com.example.neerex.mytutapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Neerex on 07/03/16.
 */
public class CustomViewHolderPattern extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView txtview;



    public CustomViewHolderPattern(View itemView) {
        super(itemView);
        this.imageView =(ImageView) itemView.findViewById(R.id.imageviewrv);
        this.txtview =(TextView) itemView.findViewById(R.id.textViewrv);

    }
}
