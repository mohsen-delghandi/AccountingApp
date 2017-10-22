package com.example.mohsen.myaccountingapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.security.AccessController.getContext;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class FirstPageActivity extends MainActivity {

    RecyclerView recyclerView;
    LinearLayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;

    DatePersian mDate;

    List<String> mainBanks,mainCheckNumbers,mainExps,mainAccounts,mainMablaghs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.page = "Frist";
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.main_layout);

        tvFarsiTitle.setText("سان سیستم");
        tvEngliashNormalTitle.setText("SAN ");
        tvEnglishBoldTitle.setText("SYSTEM");

        fab.setVisibility(View.GONE);

        TextView tvDate = (TextView)findViewById(R.id.textView_first_date);
        mDate = new DatePersian();
        final String currentDate = mDate.getDate();
        tvDate.setText(dateToText(mDate));

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        recyclerManager.setReverseLayout(true);
        recyclerManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(recyclerManager);
        readChecksFromDatabase();
        recyclerAdapter = new FirstPageAdapter(mainAccounts,mainBanks,mainMablaghs,mainCheckNumbers,mainExps);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void readChecksFromDatabase(){
        mainBanks = new ArrayList<String>();
        mainAccounts = new ArrayList<String>();
        mainCheckNumbers = new ArrayList<String>();
        mainMablaghs = new ArrayList<String>();
        mainExps = new ArrayList<String>();


        SQLiteDatabase dbCheckiPardakht = new MyDatabase(this).getReadableDatabase();
        Cursor cursorCheckiPardakht = dbCheckiPardakht.rawQuery("SELECT " +
//                "tblCheckPardakht.CheckPardakht_ID, " +
                "tblCheckPardakht.CheckPardakht_Number, " +
                "tblCheckPardakht.CheckPardakht_Mablagh, " +
                "tblCheckPardakht.CheckPardakht_Exp, " +
                "tblContacts.FullName, " +
                "tblTafzili.Tafzili_Name " +
                "FROM tblCheckPardakht_Parent " +
                "INNER JOIN tblCheckPardakht_Child ON tblCheckPardakht_Child.CheckPardakhtParent_ID = tblCheckPardakht_Parent.checkPardakhtParent_ID " +
                "INNER JOIN tblCheckPardakht ON tblCheckPardakht.CheckPardakht_ID = tblCheckPardakht_Child.CheckPardakht_ID " +
                "AND tblCheckPardakht.CheckPardakht_DateSarResid = " + persianDateToGeorgianDate(mDate) + " " +
                "INNER JOIN tblContacts ON tblContacts.Tafzili_ID = tblCheckPardakht_Child.Tafzili_ID " +
                "INNER JOIN tblTafzili ON tblTafzili.Tafzili_ID = tblCheckPardakht_Parent.Tafzili_ID",null);
        if (cursorCheckiPardakht.moveToFirst()) {
            do {
//                transactionDeleteID.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_ID")));
                mainAccounts.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("FullName")));
                mainExps.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_Exp")));
                mainBanks.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("Tafzili_Name")));
                mainCheckNumbers.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_Number")));
                mainMablaghs.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_Mablagh")));
            } while ((cursorCheckiPardakht.moveToNext()));
        }
        cursorCheckiPardakht.close();
        dbCheckiPardakht.close();
    }
}
