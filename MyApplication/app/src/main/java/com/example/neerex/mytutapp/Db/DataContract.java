package com.example.neerex.mytutapp.Db;

import android.provider.BaseColumns;

/**
 * Created by Neerex on 28/02/16.
 */
public class DataContract {


    public DataContract()
    {

    }


    public static abstract class DataEntry implements BaseColumns
    {
       public  static final  String  Table_Name = "NASA_DATA";
        public  static final  String  Column_Name_title = "Title";
        public  static final  String  Column_Name_enclosure = "enclosure";
        public  static final  String  Column_Name_description = "description";
        public  static final  String  Column_Name_link= "link";
        public  static final  String  Column_Name_pubdate = "pubdate";


    }
}
