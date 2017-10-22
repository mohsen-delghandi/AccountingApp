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

public class AccountsActivity extends MainActivity {

    RecyclerView accountsRecyclerView;
    LinearLayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;

    LayoutInflater inflaterInclude;

    String typeTaraz;

    TextView tvClean,tvSave;
    EditText etShobeName,etCardNumber,etHesabNumber,etTarazEftetahie;
    ImageView ivClose,ivHelp;
    Spinner spTarazList,spBankList,spTarazType;

    List<String> accountBankName,accountShobeName,accountHesabNumbers,accountCardNumbers;
    List<Integer> accountHesabIDs;

    String mandeType;

    @Override
    public void onBackPressed() {
        if(llAddLayer.getVisibility()==View.VISIBLE){
            llAddLayer.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }

    public static class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final char space = '-';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.page = "Accounts";
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.accounts_layout);

        tvFarsiTitle.setText("حساب ها");
        tvEngliashNormalTitle.setText("ACCOUNTS");
        tvEnglishBoldTitle.setText("");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflaterInclude = (LayoutInflater)AccountsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                fab.setVisibility(View.GONE);
                llAddLayer.removeAllViews();
                llAddLayer.setVisibility(View.VISIBLE);
                llAddLayer.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                inflaterInclude.inflate(R.layout.add_account_bank_layout,llAddLayer);

                mandeType = "Bedehkar";

                tvClean = (TextView)findViewById(R.id.textView_add_account_banki_clean);
                tvSave = (TextView)findViewById(R.id.textView_add_account_banki_save);

                etShobeName = (EditText)findViewById(R.id.editText_add_account_banki_shobe_name);
                etCardNumber = (EditText)findViewById(R.id.editText_add_account_banki_card_number);
                etHesabNumber = (EditText)findViewById(R.id.editText_add_account_banki_hesab_number);
                etTarazEftetahie = (EditText)findViewById(R.id.editText_add_account_banki_taraz_eftetahie);

                etShobeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etShobeName.selectAll();
                    }
                });
                etCardNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etCardNumber.selectAll();
                    }
                });
                etHesabNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etHesabNumber.selectAll();
                    }
                });
                etTarazEftetahie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etTarazEftetahie.selectAll();
                    }
                });

                etCardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());
                etTarazEftetahie.addTextChangedListener(new NumberTextWatcher(etTarazEftetahie));

                ivClose = (ImageView)findViewById(R.id.imageView_add_account_banki_close);
                ivHelp = (ImageView)findViewById(R.id.imageView_add_account_banki_help);

                spTarazList = (Spinner)findViewById(R.id.spinner_add_account_banki_taraz_list);
                spTarazType = (Spinner)findViewById(R.id.spinner_add_account_banki_taraz_list);

                String[] tarazList = new String[]{"بدهکار","بستانکار"};
                final ArrayAdapter adapterTarazList = new ArrayAdapter(AccountsActivity.this,R.layout.simple_spinner_item,tarazList);
                adapterTarazList.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

                spTarazList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i == 0){
                            mandeType = "Bedehkar";
                        }else if(i==1){
                            mandeType = "Bestankar";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        mandeType = "Bedehkar";
                    }
                });
                spTarazList.setAdapter(adapterTarazList);

                spBankList = (Spinner)findViewById(R.id.spinner_add_account_banki_bank_list);

                SQLiteDatabase dbBankList = new MyDatabase(AccountsActivity.this).getReadableDatabase();
                Cursor cursorBankList = dbBankList.query("tblBank",new String[]{"NameBank","ID_Bank"},null,null,null,null,null,null);
                String[] bankNames = null;
                int[] bankIDs = null;
                if(cursorBankList.moveToFirst()){
                    bankNames = new String[cursorBankList.getCount()];
                    bankIDs = new int[cursorBankList.getCount()];
                    int i = 0;
                    do{
                        bankNames[i] = cursorBankList.getString(0);
                        bankIDs[i] = cursorBankList.getInt(1);
                        i++;
                    }while (cursorBankList.moveToNext());
                }
                cursorBankList.close();
                dbBankList.close();

                final int[] bankID = new int[1];

                final ArrayAdapter adapterBankList = new ArrayAdapter(AccountsActivity.this,R.layout.simple_spinner_item,bankNames);
                adapterBankList.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                final int[] finalBankIDs = bankIDs;
                final String[] bankName = new String[1];
                final String[] finalBankNames = bankNames;
                spBankList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        bankID[0] = finalBankIDs[i];
                        bankName[0] = finalBankNames[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        bankID[0] = finalBankIDs[0];
                        bankName[0] = finalBankNames[0];
                    }
                });
                spBankList.setAdapter(adapterBankList);

                tvSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etCardNumber.getText().toString().trim().equals("")) {
                            Toast.makeText(AccountsActivity.this, "لظفا شماره کارت را وارد کنید.", Toast.LENGTH_SHORT).show();
                            etCardNumber.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(etCardNumber, InputMethodManager.SHOW_IMPLICIT);
                        } else {
                            long newTafziliCode;
                            SQLiteDatabase dbAddBankAccount = new MyDatabase(AccountsActivity.this).getWritableDatabase();
                            Cursor cursorNewTafzili = dbAddBankAccount.query("tblTafzili", new String[]{"IFNULL(MAX(Tafzili_ID),1001000)"}, null, null, null, null, null);
                            ContentValues cvAddNewTafzili = new ContentValues();
                            ContentValues cvAddNewBankAccount = new ContentValues();
                            cvAddNewTafzili.put("GroupTafzili_ID", 18);
                            if(cursorNewTafzili.moveToFirst()){
                                cvAddNewTafzili.put("Tafzili_ID", cursorNewTafzili.getInt(0) + 1+"");
                                cvAddNewBankAccount.put("Tafzili_ID", cursorNewTafzili.getInt(0) + 1+"");
                            }
                            cvAddNewTafzili.put("Tafzili_Name", bankName[0]);
                            dbAddBankAccount.insert("tblTafzili", null, cvAddNewTafzili);

                            cvAddNewBankAccount.put("ID_Bank", bankID[0]);
                            cvAddNewBankAccount.put("ShomareHesab", etHesabNumber.getText().toString().trim());
                            cvAddNewBankAccount.put("ShomareKart", etCardNumber.getText().toString().trim());
                            cvAddNewBankAccount.put("Shobe", etShobeName.getText().toString().trim());
                            cvAddNewBankAccount.put("TarazEftetahiye", etTarazEftetahie.getText().toString().replaceAll(",","").trim());
                            cvAddNewBankAccount.put("TypeTaraz", typeTaraz);
                            long id = dbAddBankAccount.insert("tblHesabBanki", null, cvAddNewBankAccount);

                            SimpleDateFormat format2= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            final String currentDate = format2.format(new java.util.Date());

                            SimpleDateFormat format= new SimpleDateFormat("HH:mm", Locale.getDefault());
                            final String currentTime = format.format(new java.util.Date());


                            SQLiteDatabase dbMande = new MyDatabase(AccountsActivity.this).getWritableDatabase();
                            ContentValues cvBedehkar = new ContentValues();
                            ContentValues cvBestankar = new ContentValues();
                            ContentValues cvParentSanad = new ContentValues();
                            ContentValues cvBanks = new ContentValues();
                            Cursor cursorMaxSrialSand = dbMande.query("tblParentSanad", new String[]{"IFNULL(MAX(Serial_Sanad),0)"}, null, null, null, null, null);
                            if (cursorMaxSrialSand.moveToFirst()) {
                                cvParentSanad.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                                cvBedehkar.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                                cvBestankar.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                                cvBanks.put("SerialSanadEftetahiye", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                            }

                            Cursor cursorMaxNumberSand = dbMande.query("tblParentSanad", new String[]{"IFNULL(MAX(Number_Sanad),0)"}, null, null, null, null, null);
                            if (cursorMaxNumberSand.moveToFirst()) {
                                cvParentSanad.put("Number_Sanad", (Integer.parseInt(cursorMaxNumberSand.getString(0)) + 1) + "");
                            }
                            cvParentSanad.put("StatusSanadID", "3");
                            cvParentSanad.put("TypeSanad_ID", "5");
                            cvParentSanad.put("Date_Sanad", currentDate);
                            cvParentSanad.put("Time_Sanad", currentTime);
                            cvParentSanad.put("Taraz_Sanad", "1");
                            cvParentSanad.put("Error_Sanad", "0");
                            cvParentSanad.put("Edited_Sanad", "0");
                            cvParentSanad.put("Deleted_Sanad", "0");

                            dbMande.insert("tblParentSanad", null, cvParentSanad);

                            cvBanks.put("TarazEftetahiye",etTarazEftetahie.getText().toString().replaceAll(",","").trim());

                            if(mandeType.trim().equals("Bedehkar")){

                                cvBedehkar.put("AccountsID","110");
                                cvBedehkar.put("Moein_ID","11003");
                                cvBedehkar.put("Tafzili_ID",cursorNewTafzili.getInt(0) + 1+"");
                                cvBedehkar.put("Bedehkar",etTarazEftetahie.getText().toString().replaceAll(",","").trim());
                                cvBedehkar.put("Bestankar","0");
                                cvBedehkar.put("ID_Amaliyat",id+"");
                                cvBedehkar.put("ID_TypeAmaliyat","9");
                                cvBedehkar.put("Sharh_Child_Sanad","مانده اول دوره");

                                cvBestankar.put("AccountsID","930");
                                cvBestankar.put("Bestankar",etTarazEftetahie.getText().toString().replaceAll(",","").trim());
                                cvBestankar.put("Bedehkar","0");
                                cvBestankar.put("ID_Amaliyat",id+"");
                                cvBestankar.put("ID_TypeAmaliyat","9");
                                cvBestankar.put("Sharh_Child_Sanad","مانده اول دوره");

                                cvBanks.put("StatusMande","بدهکار");
                                dbMande.update("tblContacts",cvBanks,"Tafzili_ID = ?",new String[]{cursorNewTafzili.getInt(0) + 1+""});

                                dbMande.insert("tblChildeSanad", null, cvBedehkar);
                                dbMande.insert("tblChildeSanad",null,cvBestankar);
                            }else if(mandeType.trim().equals("Bestankar")){
                                cvBedehkar.put("AccountsID","930");
                                cvBedehkar.put("Bedehkar",etTarazEftetahie.getText().toString().replaceAll(",","").trim());
                                cvBedehkar.put("Bestankar","0");
                                cvBedehkar.put("ID_Amaliyat",id+"");
                                cvBedehkar.put("ID_TypeAmaliyat","9");
                                cvBedehkar.put("Sharh_Child_Sanad","مانده اول دوره");

                                cvBestankar.put("AccountsID","110");
                                cvBestankar.put("Moein_ID","11003");
                                cvBestankar.put("Tafzili_ID",cursorNewTafzili.getInt(0) + 1+"");
                                cvBestankar.put("Bestankar",etTarazEftetahie.getText().toString().replaceAll(",","").trim());
                                cvBestankar.put("Bedehkar","0");
                                cvBestankar.put("ID_Amaliyat",id+"");
                                cvBestankar.put("ID_TypeAmaliyat","9");
                                cvBestankar.put("Sharh_Child_Sanad","مانده اول دوره");

                                cvBanks.put("StatusMande","بستانکار");
                                dbMande.update("tblHesabBanki",cvBanks,"Tafzili_ID = ?",new String[]{cursorNewTafzili.getInt(0) + 1+""});

                                dbMande.insert("tblChildeSanad", null, cvBedehkar);
                                dbMande.insert("tblChildeSanad",null,cvBestankar);
                            }

//                            List<String> accountBankName,accountShobeName,accountHesabNumbers,accountCardNumbers;
//                            List<Integer> accountHssabIDs;

                            accountBankName.add(bankName[0]);
                            accountShobeName.add(etShobeName.getText().toString().trim());
                            accountHesabNumbers.add(etHesabNumber.getText().toString().trim());
                            accountCardNumbers.add(etCardNumber.getText().toString().trim());
                            accountHesabIDs.add((int) id);

                            recyclerAdapter.notifyDataSetChanged();

                            cursorNewTafzili.close();
                            dbAddBankAccount.close();

                            Toast.makeText(AccountsActivity.this, "با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
                            cleanFrom();
                        }
                    }
                });

                tvClean.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cleanFrom();
                    }
                });

                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llAddLayer.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        accountsRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_accounts);
        accountsRecyclerView.setHasFixedSize(true);
        accountsRecyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        recyclerManager.setReverseLayout(true);
        recyclerManager.setStackFromEnd(true);
        accountsRecyclerView.setLayoutManager(recyclerManager);
        readAccountsFromDatabase();
        recyclerAdapter = new BankAccountsAdapter(accountBankName,accountShobeName,accountHesabNumbers,accountCardNumbers,accountHesabIDs,llAddLayer,fab);
        accountsRecyclerView.setAdapter(recyclerAdapter);
    }

    public void cleanFrom(){
        spBankList.setSelection(0);
        etShobeName.setText("");
        etCardNumber.setText("");
        etHesabNumber.setText("");
        etTarazEftetahie.setText("");
        spTarazList.setSelection(0);
    }

    public void readAccountsFromDatabase(){
        accountBankName = new ArrayList<String>();
        accountShobeName = new ArrayList<String>();
        accountHesabNumbers = new ArrayList<String>();
        accountCardNumbers = new ArrayList<String>();
        accountHesabIDs = new ArrayList<Integer>();


        SQLiteDatabase dbBankAccounts = new MyDatabase(this).getReadableDatabase();
        Cursor cursorBankAccounts = dbBankAccounts.query("tblHesabBanki",new String[]{"ID_Bank","Shobe","ShomareHesab","ShomareKart","HesabBanki_ID"},null,null,null,null,null);
        if(cursorBankAccounts.moveToFirst()){
            do{
                Cursor cursorBankName = dbBankAccounts.query("tblBank",new String[]{"NameBank"},"ID_Bank = ?",new String[]{cursorBankAccounts.getString(0)},null,null,null,null);
                if(cursorBankName.moveToFirst()){
                    accountBankName.add(cursorBankName.getString(0));
                }else{
                    accountBankName.add("نامشخص");
                }
                cursorBankName.close();
                accountShobeName.add(cursorBankAccounts.getString(1));
                accountHesabNumbers.add(cursorBankAccounts.getString(2));
                accountCardNumbers.add(cursorBankAccounts.getString(3));
                accountHesabIDs.add(cursorBankAccounts.getInt(4));
            }while ((cursorBankAccounts.moveToNext()));
        }
        cursorBankAccounts.close();
        dbBankAccounts.close();
    }
}
