package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "SanForoshgah.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


}
