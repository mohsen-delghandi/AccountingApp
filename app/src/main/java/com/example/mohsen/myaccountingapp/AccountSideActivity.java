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
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.security.AccessController.getContext;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class AccountSideActivity extends MainActivity {

    RecyclerView accountRecyclerView;
    LinearLayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;

    LayoutInflater inflaterInclude;

    TextView tvContacts,tvClose,tvClean,tvSave,tvMande;
    EditText etFullName,etPhone,etMobile,etAddress,etContactList,etCodeMelli,etMande;
    ImageView ivContactListPlus;
    Spinner spContactsList,spPishvand,spMandeType;

    List<String> accountFullName,accountPhone,accountMobile,accountAddress,accountPishvands;
    List<Integer> accountIDs;

    String mobile_regex = "09\\d\\d\\d\\d\\d\\d\\d\\d\\d";
    String tel_regex = "^0[0-9]{2,}[0-9]{7,}$";
    String national_id_regex = "^[0-9]{10}$";
    String mandeType;

    // Declare
    final int PICK_CONTACT=1;

    //code
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null,
                                    null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            String cName = phones.getString(phones.getColumnIndex("display_name"));
                            etMobile.setText(cNumber);
//                            etFullName.setText(cName);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        etFullName.setText(name);
                    }

//                    Cursor cursor = null;
//                    try {
//                        String phoneNo = null ;
//                        String name = null;
//                        // getData() method will have the Content Uri of the selected contact
//                        Uri uri = data.getData();
//                        //Query the content uri
//                        cursor = getContentResolver().query(uri, null, null, null, null);
//                        cursor.moveToFirst();
//                        // column index of the phone number
//                        int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                        // column index of the contact name
//                        int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//                        phoneNo = cursor.getString(phoneIndex);
//                        name = cursor.getString(nameIndex);
//                        // Set the value to the textviews
//                        etFullName.setText(name);
//                        etMobile.setText(phoneNo);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                }
                break;
        }
    }

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
        MainActivity.page = "AccountSide";
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
                llAddLayer.removeAllViews();
                llAddLayer.setVisibility(View.VISIBLE);
                llAddLayer.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                inflaterInclude.inflate(R.layout.add_account_layout,llAddLayer);

                mandeType = "Bedehkar";

                tvContacts = (TextView)findViewById(R.id.textView_add_account_contact_list);
                tvClean = (TextView)findViewById(R.id.textView_add_account_clean);
                tvClose = (TextView)findViewById(R.id.textView_add_account_close);
                tvSave = (TextView)findViewById(R.id.textView_add_account_save);
                tvMande = (TextView)findViewById(R.id.textView_add_account_mande);

                etFullName = (EditText)findViewById(R.id.editText_add_account_fullName);
                etPhone = (EditText)findViewById(R.id.editText_add_account_phone);
                etMobile = (EditText)findViewById(R.id.editText_add_account_mobile);
                etAddress = (EditText)findViewById(R.id.editText_add_account_address);
                etCodeMelli = (EditText)findViewById(R.id.editText_add_account_codeMelli);
                etMande = (EditText)findViewById(R.id.editText_add_account_mande);

                etMande.addTextChangedListener(new NumberTextWatcher(etMande));

                etMobile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(etMobile.getText().toString().trim().length() == 11 ){
                            if (!Pattern.matches(mobile_regex,etMobile.getText().toString().trim())){
                                Toast.makeText(AccountSideActivity.this, "شماره تلفن صحیح نیست.\nشکل صحیح 09xxxxxxxxx", Toast.LENGTH_LONG).show();
                                etMobile.selectAll();
                            }
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                etPhone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(etPhone.getText().toString().trim().length() == 11 ) {
                            if (!Pattern.matches(tel_regex, etPhone.getText().toString().trim())) {
                                Toast.makeText(AccountSideActivity.this, "شماره تلفن صحیح نیست.\nشکل صحیح کد شهر-xxxxxxxx", Toast.LENGTH_LONG).show();
                                etPhone.selectAll();
                            }
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                etCodeMelli.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(etCodeMelli.getText().toString().trim().length() == 10 ) {
                            if (!Pattern.matches(national_id_regex, etCodeMelli.getText().toString().trim())) {
                                Toast.makeText(AccountSideActivity.this, "کد ملی صحیح نیست.", Toast.LENGTH_LONG).show();
                                etCodeMelli.selectAll();
                            }
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

//                ivContactListPlus = (ImageView)findViewById(R.id.image_add_account_contact_list_plus);

                spContactsList = (Spinner)findViewById(R.id.spinner_add_account_contacts_list);
                spMandeType = (Spinner)findViewById(R.id.spinner_add_account_mande_type);
                spPishvand = (Spinner)findViewById(R.id.spinner_add_account_pishvand);

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

                Cursor c2 = db2.query("tblPishvand",new String[]{"Pishvand_ID","Pishvand"},null,null,null,null,null,null);
                int[] pishvandIDs = null;
                String[] pishvandNames = null;
                if(c2.moveToFirst()){
                    pishvandIDs = new int[c2.getCount()];
                    pishvandNames = new String[c2.getCount()];
                    int i = 0;
                    do{
                        pishvandIDs[i] = c2.getInt(0);
                        pishvandNames[i] = c2.getString(1);
                        i++;
                    }while (c2.moveToNext());
                }
                c2.close();

                db2.close();

//                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.travelreasons, R.layout.simple_spinner_item);
                final int[] groupContactID = new int[1];

                final ArrayAdapter adapter = new ArrayAdapter(AccountSideActivity.this,R.layout.simple_spinner_item,groupNames);
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

                final int[] pishvandID = new int[1];

                final ArrayAdapter adapter2 = new ArrayAdapter(AccountSideActivity.this,R.layout.simple_spinner_item,pishvandNames);
                adapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                final int[] finalpishvandIDs = pishvandIDs;
                spPishvand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        pishvandID[0] = finalpishvandIDs[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        pishvandID[0] = finalpishvandIDs[0];
                    }
                });
                spPishvand.setAdapter(adapter2);



                final ArrayAdapter adapter3 = new ArrayAdapter(AccountSideActivity.this,R.layout.simple_spinner_item,new String[]{"بدهکار","بستانکار"});
                adapter3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                spMandeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                spMandeType.setAdapter(adapter3);

                tvSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etFullName.getText().toString().trim().equals("")) {
                            Toast.makeText(AccountSideActivity.this, "لظفا نام را وارد کنید.", Toast.LENGTH_SHORT).show();
                            etFullName.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(etFullName, InputMethodManager.SHOW_IMPLICIT);
                        } else if (etMobile.getText().toString().trim().equals("")) {
                            Toast.makeText(AccountSideActivity.this, "لظفا شماره همراه را وارد کنید.", Toast.LENGTH_SHORT).show();
                            etMobile.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(etMobile, InputMethodManager.SHOW_IMPLICIT);
                        } else if (!Pattern.matches(mobile_regex,etMobile.getText().toString().trim())){
                            Toast.makeText(AccountSideActivity.this, "شماره تلفن صحیح نیست.\nشکل صحیح 09xxxxxxxxx", Toast.LENGTH_LONG).show();
                            etMobile.requestFocus();
                            etMobile.selectAll();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(etMobile, InputMethodManager.SHOW_IMPLICIT);
                        }else{
                            SQLiteDatabase db = new MyDatabase(AccountSideActivity.this).getWritableDatabase();

                            Cursor cursor = db.query("tblTafzili", new String[]{"IFNULL(MAX(Tafzili_ID),1001000)"}, null, null, null, null, null);
                            cursor.moveToFirst();
                            db.execSQL("INSERT INTO tblTafzili (Tafzili_ID,GroupTafzili_ID,Tafzili_Name,User_ID_Tafzili,TypeAcc_ID) VALUES ('"+(cursor.getInt(0) + 1)+"',20,'"+etFullName.getText().toString().trim()+"',2002,1)");
//                            ContentValues cv = new ContentValues();
//                            cv.put("GroupTafzili_ID", 20);
//                            cv.put("Tafzili_Name", etFullName.getText().toString().trim()+"");
////                            cv.put("Tafzili_ID", (cursor.getInt(0) + 1)+"");
//                            cv.put("Tafzili_ID", "10010030");
//                            db.insert("tblTafzili", null, cv);



                            ContentValues cv2 = new ContentValues();
                            cv2.put("Tafzili_ID", cursor.getInt(0) + 1);
                            cv2.put("FullName", etFullName.getText().toString().trim());
                            cv2.put("Phone", etPhone.getText().toString().trim());
                            cv2.put("Mobile", etMobile.getText().toString().trim());
                            cv2.put("AdressContacts", etAddress.getText().toString().trim());
                            cv2.put("Code_Melli", etCodeMelli.getText().toString().trim());
                            cv2.put("GroupContact", groupContactID[0]);
                            cv2.put("Pishvand_ID", pishvandID[0]);
                            long id = db.insert("tblContacts", null, cv2);

                            if(etMande.getText().toString().replaceAll(",","").trim()!="" && etMande.getText().toString().replaceAll(",","").trim()!="0"){
                                SimpleDateFormat format2= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                final String currentDate = format2.format(new java.util.Date());

                                SimpleDateFormat format= new SimpleDateFormat("HH:mm", Locale.getDefault());
                                final String currentTime = format.format(new java.util.Date());


                                SQLiteDatabase dbMande = new MyDatabase(AccountSideActivity.this).getWritableDatabase();
                                ContentValues cvBedehkar = new ContentValues();
                                ContentValues cvBestankar = new ContentValues();
                                ContentValues cvParentSanad = new ContentValues();
                                Cursor cursorMaxSrialSand = dbMande.query("tblParentSanad", new String[]{"IFNULL(MAX(Serial_Sanad),0)"}, null, null, null, null, null);
                                if (cursorMaxSrialSand.moveToFirst()) {
                                    cvParentSanad.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                                    cvBedehkar.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                                    cvBestankar.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
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

                                if(mandeType.trim().equals("Bedehkar")){

                                    cvBedehkar.put("AccountsID","130");
                                    cvBedehkar.put("Moein_ID","13001");
                                    cvBedehkar.put("Tafzili_ID",cursor.getInt(0) + 1+"");
                                    cvBedehkar.put("Bedehkar",etMande.getText().toString().replaceAll(",","").trim());
                                    cvBedehkar.put("Bestankar","0");
                                    cvBedehkar.put("ID_Amaliyat",id+"");
                                    cvBedehkar.put("ID_TypeAmaliyat","9");
                                    cvBedehkar.put("Sharh_Child_Sanad","مانده اول دوره");

                                    cvBestankar.put("AccountsID","930");
                                    cvBestankar.put("Bestankar",etMande.getText().toString().replaceAll(",","").trim());
                                    cvBestankar.put("Bedehkar","0");
                                    cvBestankar.put("ID_Amaliyat",id+"");
                                    cvBestankar.put("ID_TypeAmaliyat","9");
                                    cvBestankar.put("Sharh_Child_Sanad","مانده اول دوره");

                                    dbMande.insert("tblChildeSanad", null, cvBedehkar);
                                    dbMande.insert("tblChildeSanad",null,cvBestankar);
                                }else if(mandeType.trim().equals("Bestankar")){
                                    cvBedehkar.put("AccountsID","930");
                                    cvBedehkar.put("Bedehkar",etMande.getText().toString().replaceAll(",","").trim());
                                    cvBedehkar.put("Bestankar","0");
                                    cvBedehkar.put("ID_Amaliyat",id+"");
                                    cvBedehkar.put("ID_TypeAmaliyat","9");
                                    cvBedehkar.put("Sharh_Child_Sanad","مانده اول دوره");

                                    cvBestankar.put("AccountsID","130");
                                    cvBestankar.put("Moein_ID","13001");
                                    cvBestankar.put("Tafzili_ID",cursor.getInt(0) + 1+"");
                                    cvBestankar.put("Bestankar",etMande.getText().toString().replaceAll(",","").trim());
                                    cvBestankar.put("Bedehkar","0");
                                    cvBestankar.put("ID_Amaliyat",id+"");
                                    cvBestankar.put("ID_TypeAmaliyat","9");
                                    cvBestankar.put("Sharh_Child_Sanad","مانده اول دوره");

                                    dbMande.insert("tblChildeSanad", null, cvBedehkar);
                                    dbMande.insert("tblChildeSanad",null,cvBestankar);
                                }
                            }

                            accountFullName.add(etFullName.getText().toString().trim());
                            accountPhone.add(etPhone.getText().toString().trim());
                            accountMobile.add(etMobile.getText().toString().trim());
                            accountAddress.add(etAddress.getText().toString().trim());
                            accountIDs.add((int) id);
                            Cursor c4 = db.query("tblPishvand", new String[]{"Pishvand"}, "Pishvand_ID = ?", new String[]{pishvandID[0] + ""}, null, null, null, null);
                            c4.moveToFirst();
                            accountPishvands.add(c4.getString(0));
                            c4.close();

//                        recyclerAdapter.notifyItemInserted(accountIDs.size()-1);
                            recyclerAdapter.notifyDataSetChanged();

                            cursor.close();
                            db.close();

                            Toast.makeText(AccountSideActivity.this, "با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
                            cleanFrom();
                        }
                    }
                });

                tvContacts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] permissions = {Manifest.permission.READ_CONTACTS};
                        new PermissionHandler().checkPermission(AccountSideActivity.this, permissions, new PermissionHandler.OnPermissionResponse() {
                            @Override
                            public void onPermissionGranted() {
                                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                startActivityForResult(intent, PICK_CONTACT);
                            }

                            @Override
                            public void onPermissionDenied() {
//                                Toast.makeText(AccountSideActivity.this, "NO Permission.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                tvClean.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cleanFrom();
                    }
                });

                tvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llAddLayer.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        accountRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_accountSide);
        accountRecyclerView.setHasFixedSize(true);
        accountRecyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        recyclerManager.setReverseLayout(true);
        recyclerManager.setStackFromEnd(true);
        accountRecyclerView.setLayoutManager(recyclerManager);
        readAccountsFromDatabase();
        recyclerAdapter = new AccountsAdapter(this,accountFullName,accountPhone,accountMobile,accountAddress,accountIDs,accountPishvands,llAddLayer,fab);
        accountRecyclerView.setAdapter(recyclerAdapter);
    }

    public void cleanFrom(){
        etFullName.setText("");
        etCodeMelli.setText("");
        etPhone.setText("");
        etMobile.setText("");
        etAddress.setText("");
        etFullName.setText("");
        etMande.setText("");
        spPishvand.setSelection(0);
        spContactsList.setSelection(0);
        spMandeType.setSelection(0);
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
