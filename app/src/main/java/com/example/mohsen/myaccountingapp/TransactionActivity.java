package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class TransactionActivity extends MainActivity {

    RecyclerView transactionRecyclerView;
    RecyclerView.LayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;

    LayoutInflater inflaterInclude;

    TextView tvDaryaft,tvPardakht,tvHame,tvTransactionMablagh
            ,tvAccountName,tvMobile2nd,tvAddress2nd;

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

    class Date extends DateItem {
        String getDate() {
            Calendar calendar = getCalendar();
            return String.format(Locale.US,
                    "%d/%d/%d",
                    getYear(), getMonth(), getDay(),
                    calendar.get(Calendar.YEAR),
                    +calendar.get(Calendar.MONTH) + 1,
                    +calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    private Date mDate;

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

        TextView tvAddTransactionDate = (TextView)findViewById(R.id.textView_add_transaction_date);
        mDate = new Date();
        tvAddTransactionDate.setText(mDate);

        final TextView tvAddTransactionCheckDate = (TextView)findViewById(R.id.textView_add_transaction_check_date);
        tvAddTransactionCheckDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker.Builder builder = new DatePicker
                        .Builder()
                        .theme(R.style.DialogTheme)
                        .minYear(1390)
                        .future(true);
                mDate = new Date();
                builder.date(mDate.getDay(), mDate.getMonth(), mDate.getYear());
                builder.build(new DateSetListener() {
                    @Override
                    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                        mDate.setDate(day, month, year);

                        //textView
                        String monthName = "";
                        switch (mDate.getMonth()) {
                            case 1:
                                monthName = "فروردین";
                                break;
                            case 2:
                                monthName = "اردیبهشت";
                                break;
                            case 3:
                                monthName = "خرداد";
                                break;
                            case 4:
                                monthName = "تیر";
                                break;
                            case 5:
                                monthName = "مرداد";
                                break;
                            case 6:
                                monthName = "شهریور";
                                break;
                            case 7:
                                monthName = "مهر";
                                break;
                            case 8:
                                monthName = "آبان";
                                break;
                            case 9:
                                monthName = "آذر";
                                break;
                            case 10:
                                monthName = "دی";
                                break;
                            case 11:
                                monthName = "بهمن";
                                break;
                            case 12:
                                monthName = "اسفند";
                                break;
                        }

                        String dayName = "";
                        switch (mDate.getIranianDay()) {
                            case 0:
                                dayName = "شنبه";
                                break;
                            case 1:
                                dayName = "یک شنبه";
                                break;
                            case 2:
                                dayName = "دوشنبه";
                                break;
                            case 3:
                                dayName = "سه شنبه";
                                break;
                            case 4:
                                dayName = "چهارشنبه";
                                break;
                            case 5:
                                dayName = "پنج شنبه";
                                break;
                            case 6:
                                dayName = "جمعه";
                                break;
                        }

                        tvAddTransactionCheckDate.setText(dayName + " " + mDate.getDay() + " " + monthName + " سال " + mDate.getYear());

                    }
                }).show(getSupportFragmentManager(), "");
            }
        });
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
//        tvHameButton = (TextView)findViewById(R.id.textView_transaction_all);

        transactionRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_transaction);
        transactionRecyclerView.setHasFixedSize(true);
        transactionRecyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        transactionRecyclerView.setLayoutManager(recyclerManager);

        newLists();
        readTransactionsFromDatabase("Daryaft");
        recyclerAdapter = new TransactionsAdapter(TransactionActivity.this,transactionsMablaghKols,transactionsAccounts,transactionExps,transactionBanks,transactionCheckNumbers,transactionModes, llAddLayer, fab,"Daryaft");
        transactionRecyclerView.setAdapter(recyclerAdapter);

        tvDaryaft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDaryaft.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
                tvPardakht.setBackground(null);
//                tvHame.setBackground(null);
                newLists();
                readTransactionsFromDatabase("Daryaft");
                recyclerAdapter = new TransactionsAdapter(TransactionActivity.this,transactionsMablaghKols,transactionsAccounts,transactionExps,transactionBanks,transactionCheckNumbers,transactionModes, llAddLayer, fab, "Daryaft");
                transactionRecyclerView.setAdapter(recyclerAdapter);
            }
        });
//
        tvPardakht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvPardakht.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
                tvDaryaft.setBackground(null);
//                tvKharidButton.setBackground(null);
                newLists();
                readTransactionsFromDatabase("Pardakht");
                recyclerAdapter = new TransactionsAdapter(TransactionActivity.this,transactionsMablaghKols,transactionsAccounts,transactionExps,transactionBanks,transactionCheckNumbers,transactionModes, llAddLayer, fab, "Pardakht");
                transactionRecyclerView.setAdapter(recyclerAdapter);
            }
        });
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
        String whereNaghdi = null;
        String[] tableColumnsNaghdi = null;
        String tableNameCheki = null;
        String[] tableColumnsCheki = null;

        if(mode.trim().equals("Daryaft")){
            tableNameNaghdi = "tblPardakhtNaghdi";
            tableColumnsNaghdi = new String[]{"PNaghdi_ID","PNaghdiExp"};
            whereNaghdi = "TypeAction = 2";

            tableNameCheki = "tblCheckDaryaft";
            tableColumnsCheki = new String[]{"CheckDaryaft_ID","CheckDaryaft_Number","CheckDaryaft_Mablagh","CheckDaryaft_Exp","CheckDaryaft_BankID"};
        }else if(mode.trim().equals("Pardakht")){
            tableNameNaghdi = "tblPardakhtNaghdi";
            tableColumnsNaghdi = new String[]{"PNaghdi_ID","PNaghdiExp"};
            whereNaghdi = "TypeAction = 1";

            tableNameCheki = "tblCheckPardakht";
            tableColumnsCheki = new String[]{"CheckPardakht_ID","CheckPardakht_Number","CheckPardakht_Mablagh","CheckPardakht_Exp"};
        }
//        else if(mode.trim().equals("All")){
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
            Cursor cursor2 = mydb.query(tableNameNaghdi, tableColumnsNaghdi, whereNaghdi, null, null, null, null);
            if (cursor2.moveToFirst()) {
                do {
                    Cursor c3 = mydb.query("tblChildNaghdi", new String[]{"Tafzili_ID", "ChMablagh_Naghdi"}, "PNaghdi_ID = " + cursor2.getString(0), null, null, null, null, null);
                    c3.moveToFirst();
                    transactionsMablaghKols.add(c3.getString(1));

                    Cursor c4 = mydb.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + c3.getString(0), null, null, null, null, null);
                    if(c4.moveToFirst()) {
                        transactionsAccounts.add(c4.getString(0));
                    }
                    transactionExps.add(cursor2.getString(1));
                    transactionBanks.add("");
                    transactionCheckNumbers.add("");
                    transactionModes.add("Naghdi");
                    c3.close();
                } while ((cursor2.moveToNext()));
            }
            cursor2.close();
            mydb.close();
        }

        if(tableNameCheki!=null && tableColumnsCheki!=null && mode.trim().equals("Daryaft")) {
            SQLiteDatabase mydb = new MyDatabase(this).getReadableDatabase();
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
                    transactionModes.add("Checki");
                    transactionsMablaghKols.add(cursor3.getString(2));

                } while ((cursor3.moveToNext()));
            }
            cursor3.close();
            mydb.close();
        }

        if(tableNameCheki!=null && tableColumnsCheki!=null && mode.trim().equals("Pardakht")) {
            SQLiteDatabase mydb = new MyDatabase(this).getReadableDatabase();
            Cursor cursor3 = mydb.query(tableNameCheki, tableColumnsCheki,null, null, null, null, null);
            if (cursor3.moveToFirst()) {
                do {
                    Cursor c5 = mydb.query("tblCheckPardakht_Child", new String[]{"Tafzili_ID","CheckPardakhtParent_ID"}, "CheckPardakht_ID = " + cursor3.getString(0), null, null, null, null, null);
                    if(c5.moveToFirst()) {
                        Cursor c6 = mydb.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + c5.getString(0), null, null, null, null, null);
                        if (c6.moveToFirst()) {
                            transactionsAccounts.add(c6.getString(0));
                        }
                        Cursor c8 = mydb.query("tblCheckPardakht_Parent", new String[]{"Tafzili_ID"}, "CheckPardakhtParent_ID = " + c5.getString(1), null, null, null, null, null);
                        if (c8.moveToFirst()) {
                            Cursor c9 = mydb.query("tblTafzili", new String[]{"Tafzili_Name"}, "Tafzili_ID = " + c8.getString(0), null, null, null, null, null);
                            if (c9.moveToFirst()) {
                                transactionBanks.add(c9.getString(0));
                            }
                        }
                    }
                    transactionExps.add(cursor3.getString(3));
                    transactionCheckNumbers.add(cursor3.getString(1));
                    transactionModes.add("Checki");
                    transactionsMablaghKols.add(cursor3.getString(2));
                } while ((cursor3.moveToNext()));
            }
            cursor3.close();
            mydb.close();
        }
    }
}
