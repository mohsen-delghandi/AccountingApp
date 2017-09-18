package com.example.mohsen.myaccountingapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class AccountSideActivity extends MainActivity {

    RecyclerView accountRecyclerView;
    RecyclerView.LayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.account_side_layout);

        tvFarsiTitle.setText("طرف حساب");
        tvEngliashNormalTitle.setText("ACCOUNT ");
        tvEnglishBoldTitle.setText("SIDE");

        accountRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_accountSide);
        accountRecyclerView.setHasFixedSize(true);
        accountRecyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        accountRecyclerView.setLayoutManager(recyclerManager);
        List<String> accountFullName = new ArrayList<String>();
        SQLiteDatabase mydb = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = mydb.query("tblContacts",new String[]{"FullName"},null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                accountFullName.add(cursor.getString(0));
            }while ((cursor.moveToNext()));
        }
        recyclerAdapter = new AccountsAdapter(this,accountFullName);
        accountRecyclerView.setAdapter(recyclerAdapter);
    }
}
