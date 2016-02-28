package com.example.neerex.mytutapp.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Neerex on 28/02/16.
 */
public class DataHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION =1;

    public static final String DATABASE_NAME="NASA_DATA.db";

    public static final String TEXT_TYPE= " Text";
    public static final String Blob_TYPE= " blob";
    public static final String COMMA_SEP= ",";
    public static final String SQL_Create_Entries=
            "CREATE TABLE "+ DataContract.DataEntry.Table_Name+
            "("+ DataContract.DataEntry._ID +" INTEGER PRIMARY KEY "+COMMA_SEP+
                    DataContract.DataEntry.Column_Name_title+TEXT_TYPE+COMMA_SEP+
                    DataContract.DataEntry.Column_Name_enclosure+Blob_TYPE+COMMA_SEP+
                    DataContract.DataEntry.Column_Name_description+TEXT_TYPE+COMMA_SEP+
                    DataContract.DataEntry.Column_Name_link+TEXT_TYPE+COMMA_SEP+
                    DataContract.DataEntry.Column_Name_pubdate+TEXT_TYPE+
                    ")";

    public  static final String SQL_Delete_Entries=
            "DROP TABLE IF EXISTS" + DataContract.DataEntry.Table_Name;


    public DataHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_Create_Entries);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_Delete_Entries);
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
