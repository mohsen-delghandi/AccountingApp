package com.example.mohsen.myaccountingapp;

import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    LinearLayout llTayid2nd;
    AutoCompleteTextView atvAccounts;
    ImageView ivHelp2nd,ivBack2nd;

    List<String> transactionsMablaghKols;
    List<String> transactionsAccounts;
    List<String> transactionExps;
    List<String> transactionBanks;
    List<String> transactionModes;
    List<String> transactionCheckNumbers;
    List<String> transactionDeleteID;

    List<Integer> accountTafziliIDs;

    LinearLayout llTayid3rd;

    String type,mode,checkDate;

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

    String dateToText(Date mDate){
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

        return dayName + " " + mDate.getDay() + " " + monthName + " سال " + mDate.getYear();
    }

    public void openAddLayout(final RecyclerView.Adapter recyclerAdapter){
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

        ImageView tvClose = (ImageView) findViewById(R.id.imageView_add_transaction_close);
        ImageView tvHelp = (ImageView) findViewById(R.id.imageView_add_transaction_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.VISIBLE);
                llAddLayer.removeAllViews();
                llAddLayer.setVisibility(View.GONE);
            }
        });

        mode = "Naghdi";

        final TextView tvChecki = (TextView)findViewById(R.id.textView_add_transaction_checki);
        final TextView tvNaghdi = (TextView)findViewById(R.id.textView_add_transaction_naghdi);
        final LinearLayout llCheckNumber = (LinearLayout)findViewById(R.id.linearLayout_add_transaction_check_number);
        final LinearLayout llCheckDate = (LinearLayout)findViewById(R.id.linearLayout_add_transaction_check_date);
        final LinearLayout llBankName = (LinearLayout)findViewById(R.id.linearLayout_add_transaction_bank);
        tvChecki.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tvChecki.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
                tvChecki.getBackground().setTint(getResources().getColor(R.color.icons));
                tvChecki.setTextColor(getResources().getColor(R.color.primary_dark));
                tvNaghdi.setTextColor(getResources().getColor(R.color.icons));
                tvNaghdi.setBackground(null);
                llCheckDate.setVisibility(View.VISIBLE);
                llCheckNumber.setVisibility(View.VISIBLE);
                llBankName.setVisibility(View.VISIBLE);
                mode = "Checki";
            }
        });
        tvNaghdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvNaghdi.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
                tvNaghdi.getBackground().setTint(getResources().getColor(R.color.icons));
                tvNaghdi.setTextColor(getResources().getColor(R.color.primary_dark));
                tvChecki.setTextColor(getResources().getColor(R.color.icons));
                tvChecki.setBackground(null);
                llCheckDate.setVisibility(View.GONE);
                llCheckNumber.setVisibility(View.GONE);
                llBankName.setVisibility(View.GONE);
                mode = "Naghdi";
            }
        });

        TextView tvAddTransactionTime = (TextView)findViewById(R.id.textView_add_transaction_time);
        SimpleDateFormat format= new SimpleDateFormat("HH:mm", Locale.getDefault());
        final String currentTime = format.format(new java.util.Date());
        tvAddTransactionTime.setText(currentTime);


        TextView tvAddTransactionDate = (TextView)findViewById(R.id.textView_add_transaction_date);
        mDate = new Date();
        final String currentDate = mDate.getDate();
        tvAddTransactionDate.setText(dateToText(mDate));

        final TextView tvAddTransactionCheckDate = (TextView)findViewById(R.id.textView_add_transaction_check_date);
        mDate = new Date();

        checkDate = mDate.getDate();
        tvAddTransactionCheckDate.setText(dateToText(mDate));
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

                        checkDate = mDate.getDate();
                        //textView
                        tvAddTransactionCheckDate.setText(dateToText(mDate));

                    }
                }).show(getSupportFragmentManager(), "");
            }
        });

        final EditText etCheckNumber = (EditText)findViewById(R.id.editText_add_transaction_check_number);
        final EditText etMablagh = (EditText)findViewById(R.id.editText_add_transaction_mablagh);
        final EditText etExp = (EditText)findViewById(R.id.editText_add_transaction_exp);

        SQLiteDatabase db2 = new MyDatabase(TransactionActivity.this).getReadableDatabase();
        Cursor c = db2.query("tblBank",new String[]{"NameBank","ID_Bank"},null,null,null,null,null,null);
        String[] bankNames = null;
        int[] bankIDs = null;
        if(c.moveToFirst()){
            bankNames = new String[c.getCount()];
            bankIDs = new int[c.getCount()];
            int i = 0;
            do{
                bankNames[i] = c.getString(0);
                bankIDs[i] = c.getInt(1);
                i++;
            }while (c.moveToNext());
        }
        c.close();
        db2.close();

        final Spinner spBanks = (Spinner)findViewById(R.id.spinner_add_transaction_banks_list);

        final int[] bankID = new int[1];
        final ArrayAdapter adapter = new ArrayAdapter(TransactionActivity.this,R.layout.simple_spinner_item,bankNames);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        final int[] finalBankIDs = bankIDs;
        spBanks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bankID[0] = finalBankIDs[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bankID[0] = finalBankIDs[0];
            }
        });
        spBanks.setAdapter(adapter);


        SQLiteDatabase dbAccounts = new MyDatabase(TransactionActivity.this).getReadableDatabase();
        Cursor cursorAccounts = dbAccounts.query("tblContacts",new String[]{"FullName"},null,null,null,null,null,null);
        List<String> listAccounts = new ArrayList<String>();
        if(cursorAccounts.moveToFirst()){
            do{
                listAccounts.add(cursorAccounts.getString(0));
            }while (cursorAccounts.moveToNext());
        }
        cursorAccounts.close();

        atvAccounts = (AutoCompleteTextView)findViewById(R.id.autoTextView_add_transaction_account);

        ArrayAdapter<String> adapterAccounts = new ArrayAdapter<String>(TransactionActivity.this,android.R.layout.simple_list_item_1,listAccounts);
        atvAccounts.setAdapter(adapterAccounts);
        atvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SQLiteDatabase dbShowAccount = new MyDatabase(TransactionActivity.this).getReadableDatabase();
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

        TextView tvSave = (TextView)findViewById(R.id.textView_add_transaction_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(atvAccounts.getText().toString().trim().equals("")){
                    Toast.makeText(TransactionActivity.this, "لطفا طرف حساب را مشخص کنید.", Toast.LENGTH_SHORT).show();
                }else {
                    if (mode.toString().trim().equals("Naghdi")) {
                        String typeAction = null;
                        String accoutsId = null;
                        String moeinId = null;
                        if (type.toString().trim().equals("Daryaft")) {
                            typeAction = "2";
                            accoutsId = "130";
                            moeinId = "13001";
                        } else if (type.toString().trim().equals("Pardakht")) {
                            typeAction = "1";
                            accoutsId = "320";
                            moeinId = "32001";
                        }
                        SQLiteDatabase dbInsert = new MyDatabase(TransactionActivity.this).getWritableDatabase();
                        ContentValues cvInsert = new ContentValues();
                        cvInsert.put("AccountsID", accoutsId);
                        cvInsert.put("Moein_ID", moeinId);
                        cvInsert.put("Tafzili_ID", "10010001");
                        cvInsert.put("DateSabt", currentDate + " " + currentTime);
                        cvInsert.put("PNaghdiExp", etExp.getText().toString().trim());
                        cvInsert.put("TypeAction", typeAction);
                        cvInsert.put("SumMabalgh", etMablagh.getText().toString().trim());
                        Cursor cursorMaxSrialSand = dbInsert.query("tblParentSanad", new String[]{"MAX(Serial_Sanad)"}, null, null, null, null, null);
                        if (cursorMaxSrialSand.moveToFirst()) {
                            cvInsert.put("Serial_Sanad", cursorMaxSrialSand.getString(0));
                        } else {
                            cvInsert.put("Serial_Sanad", "1");
                        }
                        long idPNaghdi = dbInsert.insert("tblPardakhtNaghdi", null, cvInsert);
                        ContentValues cvInsert2 = new ContentValues();
                        cvInsert2.put("AccountsID", accoutsId);
                        cvInsert2.put("Moein_ID", moeinId);
                        cvInsert2.put("Tafzili_ID", accountTafziliIDs.get(0) + "");
                        cvInsert2.put("PNaghdi_ID", idPNaghdi + "");
                        cvInsert2.put("ChMablagh_Naghdi", etMablagh.getText().toString().trim());
                        cvInsert2.put("ChDate_Pardakht", currentDate + " " + currentTime);
                        dbInsert.insert("tblChildNaghdi", null, cvInsert2);
                        Toast.makeText(TransactionActivity.this, "ذخیره با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();

                        transactionsMablaghKols.add(etMablagh.getText().toString().trim());
                        Cursor cursorAccountName = dbInsert.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + accountTafziliIDs.get(0), null, null, null,null,null);
                        cursorAccountName.moveToFirst();
                        transactionsAccounts.add(cursorAccountName.getString(0)+"");
                        transactionExps.add(etExp.getText().toString().trim());
                        transactionBanks.add("");
                        transactionCheckNumbers.add("");
                        transactionModes.add("Naghdi");
                        transactionDeleteID.add(idPNaghdi+"");

                    } else if (mode.toString().trim().equals("Checki")) {
                        if (type.toString().trim().equals("Daryaft")) {
                            SQLiteDatabase dbInsert = new MyDatabase(TransactionActivity.this).getWritableDatabase();
                            ContentValues cvInsert = new ContentValues();
                            cvInsert.put("Moein_ID", "13004");
                            cvInsert.put("AccountsID", "130");
                            cvInsert.put("Tafzili_ID", accountTafziliIDs.get(0).toString());
                            cvInsert.put("Date_Sabt", currentDate);
                            cvInsert.put("Tozih_DaryaftCheck", etExp.getText().toString().trim());
                            Cursor cursorMaxSrialSand = dbInsert.query("tblParentSanad", new String[]{"MAX(Serial_Sanad)"}, null, null, null, null, null);
                            if (cursorMaxSrialSand.moveToFirst()) {
                                cvInsert.put("SerialSanad", cursorMaxSrialSand.getString(0));
                            } else {
                                cvInsert.put("SerialSanad", "1");
                            }
                            long idParent = dbInsert.insert("tblCheckDaryaft_Parent", null, cvInsert);

                            ContentValues cvInsert2 = new ContentValues();
                            cvInsert2.put("StatusDaryaft_ID", "1");
                            cvInsert2.put("CheckDaryaft_Number", etCheckNumber.getText().toString().trim());
                            cvInsert2.put("CheckDaryaft_DateSarResid", checkDate);
                            cvInsert2.put("CheckDaryaft_Mablagh", etMablagh.getText().toString().trim());
                            cvInsert2.put("CheckDaryaft_Exp", etExp.getText().toString().trim());
                            cvInsert2.put("CheckDaryaft_BankID", bankID[0]);
                            long idCheck = dbInsert.insert("tblCheckDaryaft", null, cvInsert2);

                            ContentValues cvInsert3 = new ContentValues();
                            cvInsert3.put("Tafzili_ID", accountTafziliIDs.get(0).toString());
                            cvInsert3.put("AccountsID", "130");
                            cvInsert3.put("CheckDaryaft_ID", idCheck + "");
                            cvInsert3.put("Moein_ID", "13004");
                            cvInsert3.put("CheckDaryaftParent_ID", idParent + "");
                            cvInsert3.put("CheckDaryaft_DateDaryaft", checkDate);
                            cvInsert3.put("CheckDaryaftChild_Tozih", etExp.getText().toString().trim());
                            long idChild = dbInsert.insert("tblCheckDaryaft_Child", null, cvInsert3);
                            Toast.makeText(TransactionActivity.this, "ذخیره با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();

                        } else if (type.toString().trim().equals("Pardakht")) {

                        }
                    }


                    etCheckNumber.setText("");
                    etMablagh.setText("");
                    atvAccounts.setText("");
                    spBanks.setSelection(0);
                    etExp.setText("");

                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });

        TextView tvClean = (TextView)findViewById(R.id.textView_add_transaction_clean);
        tvClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCheckNumber.setText("");
                etMablagh.setText("");
                atvAccounts.setText("");
                spBanks.setSelection(0);
                etExp.setText("");
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
                        openAddLayout(recyclerAdapter);
                    }
                });

                llPardakhtSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = "Pardakht";
                        openAddLayout(recyclerAdapter);
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
        recyclerAdapter = new TransactionsAdapter(TransactionActivity.this,transactionsMablaghKols,transactionsAccounts,transactionExps,transactionBanks,transactionCheckNumbers,transactionModes, llAddLayer, fab,"Daryaft",transactionDeleteID);
        transactionRecyclerView.setAdapter(recyclerAdapter);

        tvDaryaft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDaryaft.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
                tvPardakht.setBackground(null);
//                tvHame.setBackground(null);
                newLists();
                readTransactionsFromDatabase("Daryaft");
                recyclerAdapter = new TransactionsAdapter(TransactionActivity.this,transactionsMablaghKols,transactionsAccounts,transactionExps,transactionBanks,transactionCheckNumbers,transactionModes, llAddLayer, fab, "Daryaft", transactionDeleteID);
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
                recyclerAdapter = new TransactionsAdapter(TransactionActivity.this,transactionsMablaghKols,transactionsAccounts,transactionExps,transactionBanks,transactionCheckNumbers,transactionModes, llAddLayer, fab, "Pardakht", transactionDeleteID);
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
        transactionDeleteID = new ArrayList<String>();
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

            tableNameCheki = "tb";
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
                    transactionDeleteID.add(cursor2.getString(0)+"");

                    Cursor c3 = mydb.query("tblChildNaghdi", new String[]{"Tafzili_ID", "ChMablagh_Naghdi"}, "PNaghdi_ID = " + cursor2.getString(0), null, null, null, null, null);
                    if(c3.moveToFirst()) {
                        transactionsMablaghKols.add(c3.getString(1));

                        Cursor c4 = mydb.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + c3.getString(0), null, null, null, null, null);
                        if (c4.moveToFirst()) {
                            transactionsAccounts.add(c4.getString(0));
                        }
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
            SQLiteDatabase dbCheckiDaryaft = new MyDatabase(this).getReadableDatabase();
            Cursor cursorCheckiDaryaft = dbCheckiDaryaft.rawQuery("SELECT " +
                    "tblCheckDaryaft.CheckDaryaft_ID, " +
                    "tblCheckDaryaft.CheckDaryaft_Number, " +
                    "tblCheckDaryaft.CheckDaryaft_Mablagh, " +
                    "tblCheckDaryaft.CheckDaryaft_Exp, " +
                    "tblContacts.FullName, " +
                    "tblBank.NameBank " +
                    "FROM tblCheckDaryaft_Child " +
                    "INNER JOIN tblCheckDaryaft ON tblCheckDaryaft_Child.CheckDaryaft_ID = tblCheckDaryaft.CheckDaryaft_ID " +
                    "INNER JOIN tblContacts ON tblCheckDaryaft_Child.Tafzili_ID = tblContacts.Tafzili_ID " +
                    "INNER JOIN tblBank ON tblCheckDaryaft.CheckDaryaft_BankID = tblBank.ID_Bank",null);
            if (cursorCheckiDaryaft.moveToFirst()) {
                do {
                    transactionDeleteID.add(cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("CheckDaryaft_ID")));
                    transactionsAccounts.add(cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("FullName")));
                    transactionExps.add(cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("CheckDaryaft_Exp")));
                    transactionBanks.add(cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("NameBank")));
                    transactionCheckNumbers.add(cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("CheckDaryaft_Number")));
                    transactionsMablaghKols.add(cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("CheckDaryaft_Mablagh")));
                    transactionModes.add("Checki");
                } while ((cursorCheckiDaryaft.moveToNext()));
            }
            cursorCheckiDaryaft.close();
            dbCheckiDaryaft.close();
        }

        if(tableNameCheki!=null && tableColumnsCheki!=null && mode.trim().equals("Pardakht")) {
            SQLiteDatabase dbCheckiPardakht = new MyDatabase(this).getReadableDatabase();
            Cursor cursorCheckiPardakht = dbCheckiPardakht.rawQuery("SELECT " +
                    "tblCheckPardakht.CheckPardakht_ID, " +
                    "tblCheckPardakht.CheckPardakht_Number, " +
                    "tblCheckPardakht.CheckPardakht_Mablagh, " +
                    "tblCheckPardakht.CheckPardakht_Exp, " +
                    "tblContacts.FullName, " +
                    "tblTafzili.Tafzili_Name " +
                    "FROM tblCheckPardakht_Parent " +
                    "INNER JOIN tblCheckPardakht_Child ON tblCheckPardakht_Child.CheckPardakhtParent_ID = tblCheckPardakht_Parent.checkPardakhtParent_ID " +
                    "INNER JOIN tblCheckPardakht ON tblCheckPardakht.CheckPardakht_ID = tblCheckPardakht_Child.CheckPardakht_ID " +
                    "INNER JOIN tblContacts ON tblContacts.Tafzili_ID = tblCheckPardakht_Child.Tafzili_ID " +
                    "INNER JOIN tblTafzili ON tblTafzili.Tafzili_ID = tblCheckPardakht_Parent.Tafzili_ID",null);
            if (cursorCheckiPardakht.moveToFirst()) {
                do {
                    transactionDeleteID.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_ID")));
                    transactionsAccounts.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("FullName")));
                    transactionExps.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_Exp")));
                    transactionBanks.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("Tafzili_Name")));
                    transactionCheckNumbers.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_Number")));
                    transactionsMablaghKols.add(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_Mablagh")));
                    transactionModes.add("Checki");
                } while ((cursorCheckiPardakht.moveToNext()));
            }
            cursorCheckiPardakht.close();
            dbCheckiPardakht.close();
        }
    }
}
