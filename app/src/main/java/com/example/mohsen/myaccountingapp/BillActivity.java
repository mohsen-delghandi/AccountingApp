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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.JDF;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class BillActivity extends MainActivity {

    RecyclerView billRecyclerView,billListRecyclerView;
    RecyclerView.LayoutManager recyclerManager,recyclerManagerBillList;
    RecyclerView.Adapter recyclerAdapter,recyclerAdapterBillList;

    LayoutInflater inflaterInclude;

    TextView tvKharidSelect2nd,tvForoshSelect2nd,tvFactorCode2nd,tvAccountBedehi2nd
            ,tvPhone2nd,tvMobile2nd,tvAddress2nd
            ,tvStartDate,tvEndDate;

    LinearLayout llAccountDetails2nd,llTayidFilter;
    AutoCompleteTextView atvAccounts;
    ImageView ivHelpFilter,ivBackFilter;

    List<String> buyAndSellFactorCodes;
    List<String> buyAndSellMablaghKols;
    List<String> buyAndSellAccounts;
    List<String> buyAndSellModes;

    List<Integer> accountTafziliIDs;

    LinearLayout llTayid3rd;

    String dateStart,dateEnd;

    DatePersian mDateStart,mDateEnd;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.page = "Bill";
        super.onCreate(savedInstanceState);

        setInflater(this,R.layout.bill_show_layout);

        tvFarsiTitle.setText("صورت حساب");
        tvEngliashNormalTitle.setText("PEOPLES'S ");
        tvEnglishBoldTitle.setText("BILLS");

        fab.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tvStartDate = (TextView)findViewById(R.id.textView_bill_filter_startDate);
        tvEndDate = (TextView)findViewById(R.id.textView_bill_filter_endDate);

        mDateStart = new DatePersian();
        dateStart = persianDateToGeorgianDate(mDateStart);
        tvStartDate.setText(dateToText(mDateStart));

        Calendar cal = mDateStart.getCalendar();
        cal.add(Calendar.DAY_OF_MONTH,1);
        mDateEnd = new DatePersian();
        mDateEnd.setDate(new JDF(cal));
        dateEnd = persianDateToGeorgianDate(mDateEnd);
        tvEndDate.setText(dateToText(mDateEnd));

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker.Builder builder = new DatePicker
                        .Builder()
                        .theme(R.style.DialogTheme)
                        .minYear(1390)
                        .future(true);
                builder.date(mDateStart.getDay(), mDateStart.getMonth(), mDateStart.getYear());
                builder.build(new DateSetListener() {
                    @Override
                    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, final int year) {
                        mDateStart.setDate(day, month, year);
                        dateStart = persianDateToGeorgianDate(mDateStart);
                        tvStartDate.setText(dateToText(mDateStart));

                        if(mDateEnd.getCalendar().compareTo(mDateStart.getCalendar())<=0) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            mDateEnd.setDate(new JDF(calendar));
                            dateEnd = persianDateToGeorgianDate(mDateEnd);
                            tvEndDate.setText(dateToText(mDateEnd));
                        }
                    }
                }).show(getSupportFragmentManager(), "");
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker.Builder builder = new DatePicker
                        .Builder()
                        .theme(R.style.DialogTheme)
                        .minYear(1390)
                        .future(true);
                builder.date(mDateEnd.getDay(), mDateEnd.getMonth(), mDateEnd.getYear());
                builder.build(new DateSetListener() {
                    @Override
                    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                        DatePersian dp = new DatePersian();
                        dp.setDate(day, month, year);
                        if(dp.getCalendar().compareTo(mDateStart.getCalendar())<=0){
                            Toast.makeText(BillActivity.this, "تاریخ صحیح را وارد کنید.", Toast.LENGTH_SHORT).show();
                        }else{
                            mDateEnd.setDate(day, month, year);
                            dateEnd = persianDateToGeorgianDate(mDateEnd);
                            tvEndDate.setText(dateToText(mDateEnd));
                        }
                    }
                }).show(getSupportFragmentManager(), "");
            }
        });

        llTayidFilter = (LinearLayout)findViewById(R.id.linearLayout_bil_filter_tayid);

        atvAccounts = (AutoCompleteTextView) findViewById(R.id.autoTextView_bill_filter_account);

        SQLiteDatabase dbAccounts = new MyDatabase(BillActivity.this).getReadableDatabase();
        Cursor cursorAccounts = dbAccounts.query("tblContacts",new String[]{"FullName"},null,null,null,null,null,null);
        List<String> listAccounts = new ArrayList<String>();
        if(cursorAccounts.moveToFirst()){
            do{
                listAccounts.add(cursorAccounts.getString(0));
            }while (cursorAccounts.moveToNext());
        }
        cursorAccounts.close();

        ArrayAdapter<String> adapterAccounts = new ArrayAdapter<String>(BillActivity.this,android.R.layout.simple_list_item_1,listAccounts);
        atvAccounts.setAdapter(adapterAccounts);
        atvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SQLiteDatabase dbShowAccount = new MyDatabase(BillActivity.this).getReadableDatabase();
                Cursor cursorShowAccount = dbShowAccount.query("tblContacts",new String[]{"Tafzili_ID"},"FullName = ?",new String[]{atvAccounts.getText().toString().trim()+""},null,null,null);
                accountTafziliIDs = new ArrayList<Integer>();
                if(cursorShowAccount.moveToFirst()){
                    accountTafziliIDs.add(cursorShowAccount.getInt(0));
                }
                cursorShowAccount.close();

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        llTayidFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase dbBillList = new MyDatabase(BillActivity.this).getReadableDatabase();
                Cursor cursorBilList = dbBillList.rawQuery("SELECT " +
                        "TblActionTypeSanad.OnvanAction, " +
                        "tblChildeSanad.Bedehkar, " +
                        "tblChildeSanad.Bestankar " +
                        "FROM tblChildeSanad " +
                        "INNER JOIN tblParentSanad ON tblChildeSanad.Serial_Sanad = tblParentSanad.Serial_Sanad " +
                        "INNER JOIN TblActionTypeSanad ON tblChildeSanad.ID_TypeAmaliyat = TblActionTypeSanad.ID_Action " +
                        "WHERE tblParentSanad.Date_Sanad BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                        "AND tblChildeSanad.Tafzili_ID = '" + accountTafziliIDs.get(0) + "';",null);
            }
        });

//        buyAndSellRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_buy_and_sell);
//        buyAndSellRecyclerView.setHasFixedSize(true);
//        buyAndSellRecyclerView.setNestedScrollingEnabled(false);
//        recyclerManager = new LinearLayoutManager(this);
//        buyAndSellRecyclerView.setLayoutManager(recyclerManager);
//
//        newLists();
//        readBuyAndSellsFromDatabase("Sell");
//        recyclerAdapter = new BuyAndSellAdapter(BuyAndSellActivity.this,buyAndSellFactorCodes,buyAndSellMablaghKols,buyAndSellAccounts,buyAndSellModes, llAddLayer, fab);
//        buyAndSellRecyclerView.setAdapter(recyclerAdapter);
//
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
        buyAndSellFactorCodes = new ArrayList<String>();
        buyAndSellMablaghKols = new ArrayList<String>();
        buyAndSellAccounts = new ArrayList<String>();
        buyAndSellModes = new ArrayList<String>();
    }

    public void readBuyAndSellsFromDatabase(String mode){

        String tableName = null;
        String[] tableColumns = null;
        String type = null;

        if(mode.trim().equals("Buy")){
            tableName = "TblParent_KharidKala";
            tableColumns = new String[]{"KharidKalaParent_ID","KharidKalaParent_JameKol","KharidKalaParent_Tafzili"};
            type = "Buy";
        }else if(mode.trim().equals("Sell")){
            tableName = "TblParent_FrooshKala";
            tableColumns = new String[]{"ForooshKalaParent_ID","ForooshKalaParent_JameKol","ForooshKalaParent_Tafzili"};
            type = "Sell";
        }else if(mode.trim().equals("All")){
            readBuyAndSellsFromDatabase("Buy");
            readBuyAndSellsFromDatabase("Sell");
            List<List> allList = new ArrayList<List>();
            allList.add(buyAndSellFactorCodes);
            allList.add(buyAndSellMablaghKols);
            allList.add(buyAndSellAccounts);
            allList.add(buyAndSellModes);
        }

        if(tableName!=null && tableColumns!=null) {
            SQLiteDatabase mydb = new MyDatabase(this).getReadableDatabase();
            Cursor cursor2 = mydb.query(tableName, tableColumns, null, null, null, null, null);
            if (cursor2.moveToFirst()) {
                do {
                    buyAndSellFactorCodes.add(cursor2.getString(0));
                    buyAndSellMablaghKols.add(cursor2.getString(1));
                    Cursor c3 = mydb.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + cursor2.getString(2), null, null, null, null, null);
                    if(c3.moveToFirst()) {
                        buyAndSellAccounts.add(c3.getString(0));
                    }
                    c3.close();
                    buyAndSellModes.add(type);
                } while ((cursor2.moveToNext()));
            }
            cursor2.close();
            mydb.close();
        }
    }
}
