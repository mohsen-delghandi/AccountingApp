package com.example.mohsen.myaccountingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class AccountSideActivity extends MainActivity {

    RecyclerView accountRecyclerView;
    RecyclerView.LayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;

    LayoutInflater inflaterInclude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.account_side_layout);

        tvFarsiTitle.setText("طرف حساب");
        tvEngliashNormalTitle.setText("ACCOUNT ");
        tvEnglishBoldTitle.setText("SIDE");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflaterInclude = (LayoutInflater)AccountSideActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                fab.setVisibility(View.GONE);
                llAddLayer.setVisibility(View.VISIBLE);
                llAddLayer.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                View addAccountLayer = inflaterInclude.inflate(R.layout.add_account_layout,llAddLayer);
                TextView tvContacts,tvClose,tvClean,tvSave;
                final EditText etFullName,etPhone,etMobile,etAddress,etContactList,etCodeMelli;
                ImageView ivContactListPlus;
                final Spinner spContactsList;

                tvContacts = (TextView)findViewById(R.id.textView_add_account_contact_list);
                tvClean = (TextView)findViewById(R.id.textView_add_account_clean);
                tvClose = (TextView)findViewById(R.id.textView_add_account_close);
                tvSave = (TextView)findViewById(R.id.textView_add_account_save);

                etFullName = (EditText)findViewById(R.id.editText_add_account_fullName);
                etPhone = (EditText)findViewById(R.id.editText_add_account_phone);
                etMobile = (EditText)findViewById(R.id.editText_add_account_mobile);
                etAddress = (EditText)findViewById(R.id.editText_add_account_address);
                etCodeMelli = (EditText)findViewById(R.id.editText_add_account_codeMelli);

                ivContactListPlus = (ImageView)findViewById(R.id.image_add_account_contact_list_plus);

                spContactsList = (Spinner)findViewById(R.id.spinner_add_account_contacts_list);

                SQLiteDatabase db2 = new MyDatabase(AccountSideActivity.this).getReadableDatabase();
                Cursor c = db2.query("tblGroupContact",new String[]{"GroupContactName","GroupContact"},null,null,null,null,null,null);
                String[] groupNames = null;
                int[] groupIDs = null;
                if(c.moveToFirst()){
                    groupNames = new String[c.getCount()];
                    groupIDs = new int[c.getCount()];
                    int i = 0;
                    do{
                        groupNames[i] = c.getString(0);
                        groupIDs[i] = c.getInt(1);
                        i++;
                    }while (c.moveToNext());
                }
                c.close();
                db2.close();

//                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.travelreasons, R.layout.simple_spinner_item);
                final int[] groupContactID = new int[1];

                ArrayAdapter adapter = new ArrayAdapter(AccountSideActivity.this,R.layout.simple_spinner_item,groupNames);
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                final int[] finalGroupIDs = groupIDs;
                spContactsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        groupContactID[0] = finalGroupIDs[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        groupContactID[0] = finalGroupIDs[0];
                    }
                });
                spContactsList.setAdapter(adapter);

                tvSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SQLiteDatabase db = new MyDatabase(AccountSideActivity.this).getWritableDatabase();
                        Cursor cursor = db.query("tblTafzili",new String[]{"MAX(Tafzili_ID)"},null,null,null,null,null);
                        cursor.moveToFirst();
                        ContentValues cv = new ContentValues();
                        cv.put("GroupTafzili_ID",20);
                        cv.put("Tafzili_Name",etFullName.getText().toString().trim());
                        cv.put("Tafzili_ID",cursor.getInt(0)+1);
                        db.insert("tblTafzili",null,cv);
//                        Toast.makeText(AccountSideActivity.this, db.insert("tblTafzili",null,cv)+"", Toast.LENGTH_SHORT).show();
                        ContentValues cv2 = new ContentValues();
                        cv2.put("Tafzili_ID",cursor.getInt(0)+1);
                        cv2.put("FullName",etFullName.getText().toString().trim());
                        cv2.put("Phone",etPhone.getText().toString().trim());
                        cv2.put("Mobile",etMobile.getText().toString().trim());
                        cv2.put("AdressContacts",etAddress.getText().toString().trim());
                        cv2.put("Code_Melli",etCodeMelli.getText().toString().trim());
                        cv2.put("GroupContact",groupContactID[0]);
                        db.insert("tblContacts",null,cv2);
//                        Toast.makeText(AccountSideActivity.this, db.insert("tblContacts",null,cv2)+"", Toast.LENGTH_SHORT).show();
                        cursor.close();
                        db.close();

                    }
                });
            }
        });

        accountRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_accountSide);
        accountRecyclerView.setHasFixedSize(true);
        accountRecyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        accountRecyclerView.setLayoutManager(recyclerManager);
        List<String> accountFullName = new ArrayList<String>();
        List<String> accountPhone = new ArrayList<String>();
        List<String> accountMobile = new ArrayList<String>();
        List<String> accountAddress = new ArrayList<String>();
        SQLiteDatabase mydb = new MyDatabase(this).getReadableDatabase();
        Cursor cursor2 = mydb.query("tblContacts",new String[]{"FullName","Phone","Mobile","AdressContacts"},null,null,null,null,null);
        if(cursor2.moveToFirst()){
            do{
                accountFullName.add(cursor2.getString(0));
                accountPhone.add(cursor2.getString(1));
                accountMobile.add(cursor2.getString(2));
                accountAddress.add(cursor2.getString(3));
            }while ((cursor2.moveToNext()));
        }
        cursor2.close();
        mydb.close();
        recyclerAdapter = new AccountsAdapter(this,accountFullName,accountPhone,accountMobile,accountAddress);
        accountRecyclerView.setAdapter(recyclerAdapter);
    }
}
