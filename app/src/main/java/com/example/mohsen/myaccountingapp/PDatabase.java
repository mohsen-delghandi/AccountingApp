package com.example.mohsen.myaccountingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohsen on 2017-07-03.
 */

public class PDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "database";
    private static final int DB_VERSION = 1;


    public PDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS Permission_TBL (Id INTEGER PRIMARY KEY,Permission TEXT);");

        db.execSQL("INSERT INTO Permission_TBL VALUES (1 , 'Yes');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
