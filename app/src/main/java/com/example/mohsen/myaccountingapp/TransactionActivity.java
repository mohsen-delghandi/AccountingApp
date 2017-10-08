package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class TransactionActivity extends MainActivity {

    RecyclerView transactionRecyclerView;
    RecyclerView.LayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;

    LayoutInflater inflaterInclude;

    TextView tvDaryaft,tvPardakht,tvHame,tvTransactionMablagh
            ,tvAccountName,tvMobile2nd,tvAddress2nd,tvKharidButton,tvForoshButton,tvHameButton;

    LinearLayout llAccountDetails2nd,llTayid2nd;
    AutoCompleteTextView atvAccounts;
    ImageView ivHelp2nd,ivBack2nd;

    List<String> transactionsMablaghKols;
    List<String> transactionsAccounts;
    List<String> transactionExps;
    List<String> transactionBanks;
    List<String> transactionModes;
    List<String> transactionCheckNumbers;

    List<Integer> accountTafziliIDs;

    LinearLayout llTayid3rd;

    String type;

    int factorCode;

    @Override
    public void onBackPressed() {
        if(llAddLayer.getVisibility()==View.VISIBLE){
//            llAddLayer.setVisibility(View.GONE);
//            fab.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }

    public void openAddLayout(){
        inflaterInclude = (LayoutInflater)TransactionActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fab.setVisibility(View.GONE);
        llAddLayer.removeAllViews();
        llAddLayer.setVisibility(View.VISIBLE);
        llAddLayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        View transactionAddLayer = inflaterInclude.inflate(R.layout.add_transaction_layout,llAddLayer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.transaction_layout);

        tvFarsiTitle.setText("تراکنش ها");
        tvEngliashNormalTitle.setText("TRANSACTIONS");
        tvEnglishBoldTitle.setText("");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflaterInclude = (LayoutInflater)TransactionActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                fab.setVisibility(View.VISIBLE);
                llAddLayer.removeAllViews();
                llAddLayer.setVisibility(View.VISIBLE);
                llAddLayer.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                View transactionSelectLayer = inflaterInclude.inflate(R.layout.transaction_select_layout,llAddLayer);

                LinearLayout llDaryaftSelect = (LinearLayout)findViewById(R.id.linearLayout_daryaft_select);
                LinearLayout llPardakhtSelect = (LinearLayout)findViewById(R.id.linearLayout_pardakht_select);

                llDaryaftSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = "Daryaft";
                        openAddLayout();
                    }
                });

                llPardakhtSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = "Pardakht";
                        openAddLayout();
                    }
                });
            }
        });

        tvDaryaft = (TextView)findViewById(R.id.textView_transaction_daryaft);
        tvPardakht = (TextView)findViewById(R.id.textView_transaction_pardakht);
        tvHameButton = (TextView)findViewById(R.id.textView_transaction_all);

        transactionRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_transaction);
        transactionRecyclerView.setHasFixedSize(true);
        transactionRecyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        transactionRecyclerView.setLayoutManager(recyclerManager);

        newLists();
        readTransactionsFromDatabase("Daryaft");
        recyclerAdapter = new TransactionsAdapter(TransactionActivity.this,transactionsMablaghKols,transactionsAccounts,transactionExps,transactionBanks,transactionCheckNumbers,transactionModes, llAddLayer, fab);
        transactionRecyclerView.setAdapter(recyclerAdapter);

//        tvKharidButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tvKharidButton.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
//                tvHameButton.setBackground(null);
//                tvForoshButton.setBackground(null);
//                newLists();
//                readBuyAndSellsFromDatabase("Buy");
//                recyclerAdapter = new BuyAndSellAdapter(BuyAndSellActivity.this,buyAndSellFactorCodes,buyAndSellMablaghKols,buyAndSellAccounts,buyAndSellModes,llAddLayer,fab);
//                buyAndSellRecyclerView.setAdapter(recyclerAdapter);
//            }
//        });
//
//        tvForoshButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tvForoshButton.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
//                tvHameButton.setBackground(null);
//                tvKharidButton.setBackground(null);
//                newLists();
//                readBuyAndSellsFromDatabase("Sell");
//                recyclerAdapter = new BuyAndSellAdapter(BuyAndSellActivity.this,buyAndSellFactorCodes,buyAndSellMablaghKols,buyAndSellAccounts,buyAndSellModes, llAddLayer, fab);
//                buyAndSellRecyclerView.setAdapter(recyclerAdapter);
//            }
//        });
//
//        tvHameButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tvHameButton.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
//                tvForoshButton.setBackground(null);
//                tvKharidButton.setBackground(null);
//                newLists();
//                readBuyAndSellsFromDatabase("All");
//                recyclerAdapter = new BuyAndSellAdapter(BuyAndSellActivity.this,buyAndSellFactorCodes,buyAndSellMablaghKols,buyAndSellAccounts,buyAndSellModes, llAddLayer, fab);
//                buyAndSellRecyclerView.setAdapter(recyclerAdapter);
//            }
//        });
    }

    public void newLists(){
        transactionsAccounts = new ArrayList<String>();
        transactionsMablaghKols = new ArrayList<String>();
        transactionExps = new ArrayList<String>();
        transactionBanks = new ArrayList<String>();
        transactionCheckNumbers = new ArrayList<String>();
        transactionModes = new ArrayList<String>();

    }

    public void readTransactionsFromDatabase(String mode){

        String tableNameNaghdi = null;
        String[] tableColumnsNaghdi = null;
        String tableNameCheki = null;
        String[] tableColumnsCheki = null;
        String type = null;

        if(mode.trim().equals("Daryaft")){
            tableNameNaghdi = "tblPardakhtNaghdi";
            tableColumnsNaghdi = new String[]{"PNaghdi_ID","PNaghdiExp"};
            tableNameCheki = "tblCheckDaryaft";
            tableColumnsCheki = new String[]{"CheckDaryaft_ID","CheckDaryaft_Number","CheckDaryaft_Mablagh","CheckDaryaft_Exp","CheckDaryaft_BankID"};
            type = "Daryaft";
        }
//        else if(mode.trim().equals("Sell")){
//            tableName = "TblParent_FrooshKala";
//            tableColumns = new String[]{"ForooshKalaParent_ID","ForooshKalaParent_JameKol","ForooshKalaParent_Tafzili"};
//            type = "Sell";
//        }else if(mode.trim().equals("All")){
//            readBuyAndSellsFromDatabase("Buy");
//            readBuyAndSellsFromDatabase("Sell");
//            List<List> allList = new ArrayList<List>();
//            allList.add(buyAndSellFactorCodes);
//            allList.add(buyAndSellMablaghKols);
//            allList.add(buyAndSellAccounts);
//            allList.add(buyAndSellModes);
//        }

        if(tableNameNaghdi!=null && tableColumnsNaghdi!=null) {
            SQLiteDatabase mydb = new MyDatabase(this).getReadableDatabase();
            Cursor cursor2 = mydb.query(tableNameNaghdi, tableColumnsNaghdi, "TypeAction = 2", null, null, null, null);
            if (cursor2.moveToFirst()) {
                do {
                    Cursor c3 = mydb.query("tblChildNaghdi", new String[]{"Tafzili_ID","ChMablagh_Naghdi"}, "PNaghdi_ID = " + cursor2.getString(0), null, null, null, null, null);
                    c3.moveToFirst();
                    transactionsMablaghKols.add(c3.getString(1));

                    Cursor c4 = mydb.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + c3.getString(0), null, null, null, null, null);
                    c4.moveToFirst();
                    transactionsAccounts.add(c4.getString(0));
                    transactionExps.add(cursor2.getString(1));
                    transactionBanks.add("");
                    transactionCheckNumbers.add("");
                    transactionModes.add("Naghdi");
                    c3.close();
                } while ((cursor2.moveToNext()));
            }
            cursor2.close();

            Cursor cursor3 = mydb.query(tableNameCheki, tableColumnsCheki,null, null, null, null, null);
            if (cursor3.moveToFirst()) {
                do {
                    Cursor c5 = mydb.query("tblCheckDaryaft_Child", new String[]{"Tafzili_ID"}, "CheckDaryaft_ID = " + cursor3.getString(0), null, null, null, null, null);
                    if(c5.moveToFirst()) {
                        Cursor c6 = mydb.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + c5.getString(0), null, null, null, null, null);
                        if (c6.moveToFirst()) {
                            transactionsAccounts.add(c6.getString(0));
                        }
                    }
                    transactionExps.add(cursor3.getString(3));
                    Cursor c7 = mydb.query("tblBank", new String[]{"NameBank"}, "ID_Bank = " + cursor3.getString(4), null, null, null, null, null);
                    c7.moveToFirst();
                    transactionBanks.add(c7.getString(0));
                    transactionCheckNumbers.add(cursor3.getString(1));
                    transactionModes.add("Naghdi");
                    transactionsMablaghKols.add(cursor3.getString(2));

                } while ((cursor2.moveToNext()));
            }
            cursor2.close();


            mydb.close();
        }
    }
}
