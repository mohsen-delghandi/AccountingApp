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

    LinearLayout llBillMain,llBillFilter,llTayidFilter;
    AutoCompleteTextView atvAccounts;
    ImageView ivHelpFilter,ivBackFilter;

    List<String> billTypes;
    List<String> billMablaghs;
    List<String> billVaziatMablaghs;
    List<String> billExps;
    List<String> billDates;

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

        final LinearLayout llBillVaziatKol = (LinearLayout)findViewById(R.id.linearLayout_bill_vazia_kol);
        final TextView tvBillVaziatKol = (TextView) findViewById(R.id.textView_bill_vaziat_kol);

        tvFarsiTitle.setText("صورت حساب");
        tvEngliashNormalTitle.setText("PEOPLES'S ");
        tvEnglishBoldTitle.setText("BILLS");

        fab.setVisibility(View.GONE);

        llBillFilter = (LinearLayout)findViewById(R.id.linearLayout_bill_filter);
        llBillMain = (LinearLayout)findViewById(R.id.linearLayout_bill_main);

        llBillFilter.setVisibility(View.VISIBLE);
        llBillMain.setVisibility(View.GONE);

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

        llTayidFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(atvAccounts.getText().toString().trim().equals("")){
                    Toast.makeText(BillActivity.this, "لطفا طرف حساب را مشخص کنید.", Toast.LENGTH_SHORT).show();
                }else {
                    SQLiteDatabase dbShowAccount = new MyDatabase(BillActivity.this).getReadableDatabase();
                    Cursor cursorShowAccount = dbShowAccount.query("tblContacts",new String[]{"Tafzili_ID"},"FullName = ?",new String[]{atvAccounts.getText().toString().trim()+""},null,null,null);

                    if(cursorShowAccount.moveToFirst()){
                        accountTafziliIDs = new ArrayList<Integer>();
                        accountTafziliIDs.add(cursorShowAccount.getInt(0));
                    }
                    cursorShowAccount.close();

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    if(accountTafziliIDs == null){
                        Toast.makeText(BillActivity.this, "طرف حساب معتبر نیست.", Toast.LENGTH_SHORT).show();
                    }else{
                        SQLiteDatabase dbBillList = new MyDatabase(BillActivity.this).getReadableDatabase();
                        Cursor cursorBilList = dbBillList.rawQuery("SELECT " +
                                "TblActionTypeSanad.OnvanAction, " +
                                "tblChildeSanad.Bedehkar, " +
                                "tblChildeSanad.Bestankar, " +
                                "tblChildeSanad.Sharh_Child_Sanad, " +
                                "tblParentSanad.Date_Sanad, " +
                                "tblChildeSanad.ID_Child_Sanad " +
                                "FROM tblChildeSanad " +
                                "INNER JOIN tblParentSanad ON tblChildeSanad.Serial_Sanad = tblParentSanad.Serial_Sanad " +
                                "INNER JOIN TblActionTypeSanad ON tblChildeSanad.ID_TypeAmaliyat = TblActionTypeSanad.ID_Action " +
                                "WHERE tblParentSanad.Date_Sanad BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                                "AND tblChildeSanad.Tafzili_ID = '" + accountTafziliIDs.get(0) + "';", null);

                        if (cursorBilList.moveToFirst()) {
                            billExps = new ArrayList<String>();
                            billMablaghs = new ArrayList<String>();
                            billTypes = new ArrayList<String>();
                            billVaziatMablaghs = new ArrayList<String>();
                            billDates = new ArrayList<String>();

                            do {
                                Cursor cursorVaziatHesab = dbBillList.rawQuery("SELECT " +
                                        "(SUM(IFNULL(Bedehkar,0)) - SUM(IFNULL(Bestankar,0))) " +
                                        "AS MandeHesab " +
                                        "FROM  tblChildeSanad " +
                                        "WHERE Tafzili_ID = '" + accountTafziliIDs.get(0) + "' " +
                                        "AND ID_Child_Sanad <= " + cursorBilList.getString(cursorBilList.getColumnIndex("ID_Child_Sanad")), null);
                                if (cursorVaziatHesab.moveToFirst()) {
                                    billVaziatMablaghs.add(cursorVaziatHesab.getString(cursorVaziatHesab.getColumnIndex("MandeHesab")));
                                } else {
                                    billVaziatMablaghs.add("");
                                }
                                billTypes.add(cursorBilList.getString(cursorBilList.getColumnIndex("OnvanAction")));
                                if (cursorBilList.getString(cursorBilList.getColumnIndex("Bedehkar")).toString().trim().equals("0")) {
                                    billMablaghs.add(cursorBilList.getString(cursorBilList.getColumnIndex("Bestankar")));
                                } else {
                                    billMablaghs.add(cursorBilList.getString(cursorBilList.getColumnIndex("Bedehkar")));
                                }
                                billExps.add(cursorBilList.getString(cursorBilList.getColumnIndex("Sharh_Child_Sanad")));
                                billDates.add(cursorBilList.getString(cursorBilList.getColumnIndex("Date_Sanad")));
                            } while (cursorBilList.moveToNext());

                            billListRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_bill);
                            billListRecyclerView.setHasFixedSize(true);
                            billListRecyclerView.setNestedScrollingEnabled(false);
                            recyclerManager = new LinearLayoutManager(BillActivity.this);
                            billListRecyclerView.setLayoutManager(recyclerManager);
                            recyclerAdapter = new BillAdapter(billExps, billMablaghs, billTypes, billVaziatMablaghs, billDates);
                            billListRecyclerView.setAdapter(recyclerAdapter);

                            llBillFilter.setVisibility(View.GONE);
                            llBillMain.setVisibility(View.VISIBLE);
                            llBillVaziatKol.setVisibility(View.VISIBLE);
                            fab.setVisibility(View.VISIBLE);
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.shape_filter));
                            fab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    llBillFilter.setVisibility(View.VISIBLE);
                                    llBillMain.setVisibility(View.GONE);
                                    llBillVaziatKol.setVisibility(View.GONE);
                                    fab.setVisibility(View.GONE);
                                }
                            });
                            tvBillVaziatKol.setText(MainActivity.priceFormatter(Math.abs(Long.parseLong(billVaziatMablaghs.get(billVaziatMablaghs.size() - 1))) + ""));
                        } else {
                            Toast.makeText(BillActivity.this, "موردی یافت نشد.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


    }
}
