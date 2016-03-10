package com.example.neerex.mytutapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neerex.mytutapp.ImageService.ImageIntentService;
import com.example.neerex.mytutapp.Utility.FileUtility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neerex on 07/03/16.
 */
public class RAdapter extends RecyclerView.Adapter<RAdapter.CustomViewHolder> {

    private List<Item> items= new ArrayList<Item>() ;
    private Context context;


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView txtview;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView =(ImageView) itemView.findViewById(R.id.imageviewrv);
            this.txtview =(TextView) itemView.findViewById(R.id.textViewrv);
        }
    }

    public RAdapter(List<Item> list,Context context)
    {
        this.items=list;
        this.context=context;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_row_cardview, parent, false);

        CustomViewHolder viewHolderPattern = new CustomViewHolder(view) ;
        return viewHolderPattern;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        Item localitem = items.get(position);
        if(localitem.getEnclosure() !=null) {

            boolean localpresent= false;
            File mypath;
            try {
                FileUtility fileobj = new FileUtility();
                String path= fileobj.Appfolderstructure(context,"ImageDir");
                String name = URLUtil.guessFileName(localitem.getEnclosure(), null, null);
                mypath= new File(path+"/"+"thumbnail"+"/",name+".webp");
                if(mypath.exists())
                {
                    Picasso.with(context).load(mypath).resize(80, 80).placeholder(R.mipmap.progress).into(holder.imageView);
                }
                else
                {
                    Picasso.with(context).load(localitem.getEnclosure()).resize(80, 80).placeholder(R.mipmap.progress).into(holder.imageView);


                    Intent intent = new Intent(context, ImageIntentService.class);
                    intent.putExtra("Items",localitem.getEnclosure());
                    intent.putExtra("path",path);
                    context.startService(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(localitem.getTitle() !=null) {
            holder.txtview.setText(localitem.getTitle());
        }

        holder.txtview.setOnClickListener(clickListener);
        holder.imageView.setOnClickListener(clickListener);

        holder.txtview.setTag(holder);
        holder.imageView.setTag(holder);

    }

    @Override
    public int getItemCount() {
        return (null !=items?items.size():0);
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomViewHolder holder = (CustomViewHolder) view.getTag();
            int position = holder.getPosition();

            Item localitem = items.get(position);
            Item itemparcel = new Item(localitem.getTitle(), localitem.getLink(), localitem.getDescription(), localitem.getEnclosure(), localitem.getPubDate());
            Intent newActivity = new Intent(context, DetailLayoutActivtiy.class);
            newActivity.putExtra("Data", itemparcel);
            context.startActivity(newActivity);
        }
    };
}
