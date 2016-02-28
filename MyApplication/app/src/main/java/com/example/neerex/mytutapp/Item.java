package com.example.neerex.mytutapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Neerex on 24/02/16.
 */
public  class Item implements Parcelable{

    private String title;
    private String link;
    private String description;
    private String enclosure;
    private String pubDate;


    public Item() {

    }


    public Item(String title,String link,String description,String enclosure,String pubDate) {
        this.title =title;
        this.link =link;
        this.description=description;
        this.enclosure =enclosure;
        this.pubDate=pubDate;

    }


    protected Item(Parcel in) {
        title = in.readString();
        enclosure = in.readString();
        description = in.readString();
        link = in.readString();
        pubDate = in.readString();
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(getTitle());
        dest.writeString(getEnclosure());
        dest.writeString(getDescription());
        dest.writeString(getLink());
        dest.writeString(getPubDate());


    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };


}
