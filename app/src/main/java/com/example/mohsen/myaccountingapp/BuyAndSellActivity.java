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
import android.support.annotation.IntegerRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class BuyAndSellActivity extends MainActivity {

    RecyclerView buyAndSellRecyclerView;
    RecyclerView.LayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;

    LayoutInflater inflaterInclude;

    TextView tvKharidSelect2nd,tvForoshSelect2nd,tvFactorCode2nd,tvAccountBedehi2nd
            ,tvPhone2nd,tvMobile2nd,tvAddress2nd;

    LinearLayout llAccountDetails2nd;
    AutoCompleteTextView atvAccounts;
    EditText etFullName,etPhone,etMobile,etAddress,etContactList,etCodeMelli;
    ImageView ivContactListPlus,ivHelp2nd,ivBack2nd;
    Spinner spContactsList,spPishvand;

    List<String> accountFullName,accountPhone,accountMobile,accountAddress,accountPishvands;
    List<Integer> accountIDs;

    @Override
    public void onBackPressed() {
        if(llAddLayer.getVisibility()==View.VISIBLE){
            llAddLayer.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.buy_and_sell_show_layout);

        tvFarsiTitle.setText("خرید و فروش");
        tvEngliashNormalTitle.setText("BUY ");
        tvEnglishBoldTitle.setText("& SELL");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflaterInclude = (LayoutInflater)BuyAndSellActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                fab.setVisibility(View.GONE);
                llAddLayer.removeAllViews();
                llAddLayer.setVisibility(View.VISIBLE);
                llAddLayer.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                inflaterInclude.inflate(R.layout.buy_and_sale_2nd_layout,llAddLayer);

                tvKharidSelect2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_buy_select);
                tvForoshSelect2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_sell_select);
                tvFactorCode2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_factor_code);
                tvAccountBedehi2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_bedehi_mablagh);
                tvPhone2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_phone);
                tvMobile2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_mobile);
                tvAddress2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_address);

                ivHelp2nd = (ImageView) findViewById(R.id.imageView_buy_and_sell_2nd_help);
                ivBack2nd = (ImageView) findViewById(R.id.imageView_buy_and_sell_2nd_back);

                llAccountDetails2nd = (LinearLayout)findViewById(R.id.linearLayout_buy_and_sell_2nd_account);

                atvAccounts = (AutoCompleteTextView) findViewById(R.id.autoTextView_buy_and_sell_2nd_account);

                SQLiteDatabase dbAccounts = new MyDatabase(BuyAndSellActivity.this).getReadableDatabase();
                Cursor cursorAccounts = dbAccounts.query("tblContacts",new String[]{"FullName"},null,null,null,null,null,null);
                List<String> listAccounts = new ArrayList<String>();
                if(cursorAccounts.moveToFirst()){
                    do{
                        listAccounts.add(cursorAccounts.getString(0));
                    }while (cursorAccounts.moveToNext());
                }
                cursorAccounts.close();
                dbAccounts.close();

                ArrayAdapter<String> adapterAccounts = new ArrayAdapter<String>(BuyAndSellActivity.this,android.R.layout.simple_list_item_1,listAccounts);
                atvAccounts.setAdapter(adapterAccounts);
                atvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SQLiteDatabase dbShowAccount = new MyDatabase(BuyAndSellActivity.this).getReadableDatabase();
                        Cursor cursorShowAccount = dbShowAccount.query("tblContacts",new String[]{"Tafzili_ID"},"FullName = ?",new String[]{atvAccounts.getText().toString().trim()+""},null,null,null);
                        List<Integer> accountTafziliIDs = new ArrayList<Integer>();
                        if(cursorShowAccount.moveToFirst()){
                            accountTafziliIDs.add(cursorShowAccount.getInt(0));
                        }
                        cursorShowAccount.close();
                        Cursor cursorAccountDetails = dbShowAccount.query("tblContacts",new String[]{"Phone","Mobile","AdressContacts"},"Tafzili_ID = ?",new String[]{accountTafziliIDs.get(0)+""},null,null,null);
                        cursorAccountDetails.moveToFirst();

                        llAccountDetails2nd.setVisibility(View.VISIBLE);
                        tvPhone2nd.setText(cursorAccountDetails.getString(0));
                        tvMobile2nd.setText(cursorAccountDetails.getString(1));
                        tvAddress2nd.setText(cursorAccountDetails.getString(2));

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                });

                ivBack2nd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llAddLayer.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        buyAndSellRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_buy_and_sell);
        buyAndSellRecyclerView.setHasFixedSize(true);
        buyAndSellRecyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        buyAndSellRecyclerView.setLayoutManager(recyclerManager);
        readAccountsFromDatabase();
        recyclerAdapter = new AccountsAdapter(this,accountFullName,accountPhone,accountMobile,accountAddress,accountIDs,accountPishvands,llAddLayer,fab);
        buyAndSellRecyclerView.setAdapter(recyclerAdapter);
    }

    public void cleanFrom(){
        etFullName.setText("");
        etCodeMelli.setText("");
        etPhone.setText("");
        etMobile.setText("");
        etAddress.setText("");
        etFullName.setText("");
        spPishvand.setSelection(0);
        spContactsList.setSelection(0);
    }

    public void readAccountsFromDatabase(){
        accountFullName = new ArrayList<String>();
        accountPhone = new ArrayList<String>();
        accountMobile = new ArrayList<String>();
        accountAddress = new ArrayList<String>();
        accountIDs = new ArrayList<Integer>();
        accountPishvands = new ArrayList<String>();


        SQLiteDatabase mydb = new MyDatabase(this).getReadableDatabase();
        Cursor cursor2 = mydb.query("tblContacts",new String[]{"FullName","Phone","Mobile","AdressContacts","Contacts_ID","Pishvand_ID"},null,null,null,null,null);
        if(cursor2.moveToFirst()){
            do{
                accountFullName.add(cursor2.getString(0));
                accountPhone.add(cursor2.getString(1));
                accountMobile.add(cursor2.getString(2));
                accountAddress.add(cursor2.getString(3));
                accountIDs.add(cursor2.getInt(4));
                Cursor c3 = mydb.query("tblPishvand",new String[]{"Pishvand"},"Pishvand_ID = ?",new String[]{cursor2.getString(5)},null,null,null,null);
                c3.moveToFirst();
                accountPishvands.add(c3.getString(0));
                c3.close();
            }while ((cursor2.moveToNext()));
        }
        cursor2.close();
        mydb.close();
    }
}
