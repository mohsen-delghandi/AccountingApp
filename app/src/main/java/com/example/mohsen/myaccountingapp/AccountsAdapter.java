package com.example.mohsen.myaccountingapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

    Context mContext;
    View v;
    List<String> mAccountFullName, mAccountPhones, mAccountMobiles, mAccountAddresses,mAccountPishvands,tafziliID;
    List<Integer> mAccountIDs;
    boolean isCollapsed;
    ViewHolder selectedHolder;
    LayoutInflater mInflaterInclude;
    LinearLayout mLlAddLayer;
    FloatingActionButton mFab;

    String mobile_regex = "09\\d\\d\\d\\d\\d\\d\\d\\d\\d";
    String tel_regex = "^0[0-9]{2,}[0-9]{7,}$";
    String national_id_regex = "^[0-9]{10}$";
    String mandeType,serialSanad,mandeAvalDovre,statusMande;

    public AccountsAdapter(Context context, List<String> accountFullName, List<String> accountPhone, List<String> accountMobile, List<String> accountAddress, List<Integer> accountIDs, List<String> accountPishvands, LinearLayout llAddLayer, FloatingActionButton fab) {
        mContext = context;
        mAccountFullName = accountFullName;
        mAccountAddresses = accountAddress;
        mAccountMobiles = accountMobile;
        mAccountPhones = accountPhone;
        mAccountIDs = accountIDs;
        mAccountPishvands = accountPishvands;
        mLlAddLayer = llAddLayer;
        mFab = fab;
        tafziliID = new ArrayList<>();
    }


    @Override
    public AccountsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_side_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llExtra;
        LinearLayout llMain;
        LinearLayout llName;
        TextView tvFullName, tvDash, tvCompanyName, tvBedehiDash, tvBedehiMablagh, tvBedehiText, tvBedehiVahed, tvPhone, tvMobile, tvAddress, tvEdit, tvDelete,tvPishvand;
        ImageView ivCall, ivArrow, ivBedehi;

        public ViewHolder(View v) {
            super(v);
            llExtra = (LinearLayout) v.findViewById(R.id.linearLayout_account_side_extra_information);
            llMain = (LinearLayout) v.findViewById(R.id.linearLayout_account_side_main);
            llName = (LinearLayout) v.findViewById(R.id.linearLayout_acount_side_main_information);
            tvFullName = (TextView) v.findViewById(R.id.textView_acount_side_fullName);
            tvPhone = (TextView) v.findViewById(R.id.textView_acount_side_phone);
            tvMobile = (TextView) v.findViewById(R.id.textView_acount_side_mobile);
            tvAddress = (TextView) v.findViewById(R.id.textView_acount_side_address);
            tvCompanyName = (TextView) v.findViewById(R.id.textView_acount_side_company_name);
            tvDash = (TextView) v.findViewById(R.id.textView_acount_side_dash);
            tvBedehiDash = (TextView) v.findViewById(R.id.textView_acount_side_bedehi_dash);
            tvBedehiMablagh = (TextView) v.findViewById(R.id.textView_acount_side_bedehi_mablagh);
            tvBedehiText = (TextView) v.findViewById(R.id.textView_acount_side_bedehi_text);
            tvBedehiVahed = (TextView) v.findViewById(R.id.textView_acount_side_bedehi_vahed);
            tvEdit = (TextView) v.findViewById(R.id.textView_acount_side_edit);
            tvDelete = (TextView) v.findViewById(R.id.textView_acount_side_delete);
            tvPishvand = (TextView) v.findViewById(R.id.textView_acount_side_pishvand);
            ivCall = (ImageView) v.findViewById(R.id.imageView_acount_side_call);
            ivArrow = (ImageView) v.findViewById(R.id.imageView_acount_side_arrow);
            ivBedehi = (ImageView) v.findViewById(R.id.imageView_acount_side_bedehi);
        }
    }

    public void selectItem(ViewHolder h){
        h.llExtra.setVisibility(View.VISIBLE);
        h.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_background));
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.icons));
        h.tvFullName.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvPishvand.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvCompanyName.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvDash.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.ivCall.setBackground(null);
        h.ivCall.setImageResource(R.drawable.shape_call_expanded);
        h.ivArrow.setBackground(null);
        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_up);
    }

    public void deSelectItem(ViewHolder h){
        h.llExtra.setVisibility(View.GONE);
        h.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_underline_dashed));
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.shiri));
        h.tvFullName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvPishvand.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvCompanyName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvDash.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        if(h.tvBedehiText.getText().toString().trim().equals("بستانکار")) {
            h.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.green));
            h.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.green));
            h.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.green));
            h.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.green));
        }else if(h.tvBedehiText.getText().toString().trim().equals("بدهکار")){
            h.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.red));
            h.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.red));
            h.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.red));
            h.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.red));
        }else{
            h.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            h.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            h.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            h.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        }
        h.ivCall.setImageResource(R.drawable.shape_call_collapsed);
        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_down);
    }

    @Override
    public void onBindViewHolder(final AccountsAdapter.ViewHolder holder, final int position) {
        holder.tvFullName.setText(mAccountFullName.get(position));
        holder.tvPishvand.setText(mAccountPishvands.get(position));
        holder.tvPhone.setText(mAccountPhones.get(position));
        holder.tvMobile.setText(mAccountMobiles.get(position));
        holder.tvAddress.setText(mAccountAddresses.get(position));
        holder.setIsRecyclable(false);

        final SQLiteDatabase dbBillList = new MyDatabase(mContext).getReadableDatabase();
        Cursor cursorTafzili = dbBillList.query("tblContacts",new String[]{"Tafzili_ID"},"Contacts_ID = ?",new String[]{mAccountIDs.get(position)+""},null,null,null);
        if(cursorTafzili.moveToFirst()){
            tafziliID.add(position,cursorTafzili.getString(0));
        }

//        Cursor cursorBilList = dbBillList.rawQuery("SELECT " +
//                "TblActionTypeSanad.OnvanAction, " +
//                "tblChildeSanad.Bedehkar, " +
//                "tblChildeSanad.Bestankar, " +
//                "tblChildeSanad.Sharh_Child_Sanad, " +
//                "tblParentSanad.Date_Sanad, " +
//                "tblChildeSanad.ID_Child_Sanad " +
//                "FROM tblChildeSanad " +
//                "INNER JOIN tblParentSanad ON tblChildeSanad.Serial_Sanad = tblParentSanad.Serial_Sanad " +
//                "INNER JOIN TblActionTypeSanad ON tblChildeSanad.ID_TypeAmaliyat = TblActionTypeSanad.ID_Action " +
//                "WHERE tblChildeSanad.Tafzili_ID = '" + tafziliID + "';", null);

//        if (cursorBilList.moveToLast()) {
            Cursor cursorVaziatHesab = dbBillList.rawQuery("SELECT " +
                    "IFNULL((SUM(IFNULL(Bedehkar,0)) - SUM(IFNULL(Bestankar,0))),0) " +
                    "AS MandeHesab " +
                    "FROM  tblChildeSanad " +
                    "WHERE Tafzili_ID = '" + tafziliID.get(position) + "' "
//                    "AND ID_Child_Sanad <= " + cursorBilList.getString(cursorBilList.getColumnIndex("ID_Child_Sanad"))
                    , null);
            if (cursorVaziatHesab.moveToFirst()) {
                if(((int)Double.parseDouble(cursorVaziatHesab.getString(cursorVaziatHesab.getColumnIndex("MandeHesab"))) < 0)){
                    holder.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.tvBedehiText.setText("بستانکار");
                    holder.ivBedehi.setImageResource(R.drawable.shape_arrow_down);
                    holder.ivBedehi.setColorFilter(mContext.getResources().getColor(R.color.green));
                    }else if((int)Double.parseDouble(cursorVaziatHesab.getString(cursorVaziatHesab.getColumnIndex("MandeHesab"))) > 0){
                    holder.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.red));
                    holder.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.red));
                    holder.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.red));
                    holder.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.red));
                    holder.tvBedehiText.setText("بدهکار");
                    holder.ivBedehi.setImageResource(R.drawable.shape_arrow_down);
                    holder.ivBedehi.setRotation(180);
                    holder.ivBedehi.setColorFilter(mContext.getResources().getColor(R.color.red));
                }else{
                    holder.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    holder.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    holder.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    holder.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    holder.tvBedehiText.setText("بی حساب");
                    holder.ivBedehi.setVisibility(View.INVISIBLE);
                }
                holder.tvBedehiMablagh.setText(MainActivity.priceFormatter(cursorVaziatHesab.getString(cursorVaziatHesab.getColumnIndex("MandeHesab")).replace("-","")) + "");
            }
//        }

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCollapsed = holder.llExtra.getVisibility() == View.GONE;
                if (isCollapsed) {
                    selectItem(holder);
                    if(selectedHolder != holder){
                        if(selectedHolder != null)
                            deSelectItem(selectedHolder);
                        selectedHolder = holder;
                    }
                } else {
                    deSelectItem(holder);
                }
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CustomDialogClass cdd = new CustomDialogClass(mContext);
                cdd.show();
                Window window = cdd.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                cdd.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SQLiteDatabase dbb = new MyDatabase(mContext).getWritableDatabase();
                        dbb.execSQL("DELETE FROM tblContacts WHERE Contacts_ID = " + mAccountIDs.get(position));
                        dbb.close();

                        mAccountIDs.remove(position);
                        mAccountFullName.remove(position);
                        mAccountPhones.remove(position);
                        mAccountMobiles.remove(position);
                        mAccountAddresses.remove(position);

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mAccountFullName.size());
                        notifyDataSetChanged();

                        cdd.dismiss();
                    }
                });
                cdd.no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cdd.dismiss();
                    }
                });
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInflaterInclude = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mFab.setVisibility(View.GONE);
                mLlAddLayer.removeAllViews();
                mLlAddLayer.setVisibility(View.VISIBLE);
                mLlAddLayer.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                final View v = mInflaterInclude.inflate(R.layout.add_account_layout,mLlAddLayer);
                v.findViewById(R.id.textView_add_account_contact_list).setVisibility(View.INVISIBLE);
                v.findViewById(R.id.textView_add_account_clean).setVisibility(View.INVISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_account_save)).setText("بروزرسانی");

                final EditText et_nationalId = (EditText)v.findViewById(R.id.editText_add_account_codeMelli);
                final EditText et_mobileNumber = (EditText)v.findViewById(R.id.editText_add_account_mobile);
                final EditText et_telNumber = (EditText)v.findViewById(R.id.editText_add_account_phone);
                final EditText et_fullName = (EditText)v.findViewById(R.id.editText_add_account_fullName);
                final EditText et_address = (EditText)v.findViewById(R.id.editText_add_account_address);
                final EditText et_mande = (EditText)v.findViewById(R.id.editText_add_account_mande);

                et_mande.addTextChangedListener(new NumberTextWatcher(et_mande));

                et_fullName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_mande.selectAll();
                    }
                });

                et_fullName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_fullName.selectAll();
                    }
                });

                et_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_fullName.selectAll();
                    }
                });

                et_mobileNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(et_mobileNumber.getText().toString().trim().length() == 11 ){
                            if (!Pattern.matches(mobile_regex,et_mobileNumber.getText().toString().trim())){
                                Toast.makeText(mContext, "شماره تلفن صحیح نیست.\nشکل صحیح 09xxxxxxxxx", Toast.LENGTH_LONG).show();
                                et_mobileNumber.selectAll();
                            }
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                et_telNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(et_telNumber.getText().toString().trim().length() == 11 ) {
                            if (!Pattern.matches(tel_regex, et_telNumber.getText().toString().trim())) {
                                Toast.makeText(mContext, "شماره تلفن صحیح نیست.\nشکل صحیح کد شهر-xxxxxxxx", Toast.LENGTH_LONG).show();
                                et_telNumber.selectAll();
                            }
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                et_nationalId.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(et_nationalId.getText().toString().trim().length() == 10 ) {
                            if (!Pattern.matches(national_id_regex, et_nationalId.getText().toString().trim())) {
                                Toast.makeText(mContext, "کد ملی صحیح نیست.", Toast.LENGTH_LONG).show();
                                et_nationalId.selectAll();
                            }
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


                SQLiteDatabase db11 = new MyDatabase(mContext).getWritableDatabase();
                Cursor c11 = db11.query("tblContacts",new String[]{"FullName","Code_Melli","Phone","Mobile","AdressContacts","GroupContact","Pishvand_ID"},"Contacts_ID = ?",
                        new String[]{mAccountIDs.get(position)+""},null,null,null);
                c11.moveToFirst();
                (et_fullName).setText(c11.getString(0));
                (et_nationalId).setText(c11.getString(1));
                (et_telNumber).setText(c11.getString(2));
                (et_mobileNumber).setText(c11.getString(3));
                (et_address).setText(c11.getString(4));

                SQLiteDatabase db2 = new MyDatabase(mContext).getReadableDatabase();
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

                final ArrayAdapter adapter = new ArrayAdapter(mContext,R.layout.simple_spinner_item,groupNames);
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                final int[] finalGroupIDs = groupIDs;
                ((Spinner)v.findViewById(R.id.spinner_add_account_contacts_list)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        groupContactID[0] = finalGroupIDs[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        groupContactID[0] = finalGroupIDs[0];
                    }
                });
                ((Spinner)v.findViewById(R.id.spinner_add_account_contacts_list)).setAdapter(adapter);
                int contactGroupPosition = 0;
                for(int i = 0 ; i < groupIDs.length ; i++){
                    if(c11.getInt(5) == groupIDs[i]) {
                        contactGroupPosition = i;
                        i = groupIDs.length;
                    }
                }
                ((Spinner)v.findViewById(R.id.spinner_add_account_contacts_list)).setSelection(contactGroupPosition);

                final int[] pishvandID = new int[1];

                final ArrayAdapter adapter2 = new ArrayAdapter(mContext,R.layout.simple_spinner_item,pishvandNames);
                adapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                final int[] finalpishvandIDs = pishvandIDs;
                ((Spinner)v.findViewById(R.id.spinner_add_account_pishvand)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        pishvandID[0] = finalpishvandIDs[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        pishvandID[0] = finalpishvandIDs[0];
                    }
                });
                ((Spinner)v.findViewById(R.id.spinner_add_account_pishvand)).setAdapter(adapter2);
                int contactPishvandPosition = 0;
                for(int i = 0 ; i < pishvandIDs.length ; i++){
                    if(c11.getInt(6) == pishvandIDs[i]) {
                        contactPishvandPosition = i;
                        i = pishvandIDs.length;
                    }
                }
                ((Spinner)v.findViewById(R.id.spinner_add_account_pishvand)).setSelection(contactPishvandPosition);

                SQLiteDatabase dbMande = new MyDatabase(mContext).getReadableDatabase();
                Cursor cursorMande = dbMande.query("tblContacts",new String[]{"MandeAvalDovre","StatusMande","SerialSanadEftetahiye"},"Tafzili_ID = ?",new String[]{tafziliID.get(position)},null,null,null);

                if(cursorMande.moveToFirst()){
                    if(cursorMande.getString(0)!=null){

                        Spinner spMandeType = ((Spinner)v.findViewById(R.id.spinner_add_account_mande_type));
                        final ArrayAdapter adapter3 = new ArrayAdapter(mContext,R.layout.simple_spinner_item,new String[]{"بدهکار","بستانکار"});
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
                            }
                        });
                        spMandeType.setAdapter(adapter3);
                        if(cursorMande.getString(1).trim().equals("بدهکار")){
                            spMandeType.setSelection(0);
                            statusMande = "Bedehkar";
                        }else{
                            spMandeType.setSelection(1);
                            statusMande = "Bestankar";
                        }

                        serialSanad = cursorMande.getString(2);
                        mandeAvalDovre = cursorMande.getString(0);
                        et_mande.setText(cursorMande.getString(0));
                    }
                }



                ((TextView)v.findViewById(R.id.textView_add_account_fullName)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_account_codeMelli)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_account_phone)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_account_mobile)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_account_address)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_account_mande)).setVisibility(View.VISIBLE);

                (et_fullName).setHint("");
                (et_nationalId).setHint("");
                (et_telNumber).setHint("");
                (et_mobileNumber).setHint("");
                (et_address).setHint("");
                (et_mande).setHint("");

                ((TextView)v.findViewById(R.id.textView_add_account_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLlAddLayer.setVisibility(View.GONE);
                        mFab.setVisibility(View.VISIBLE);
                    }
                });

                ((TextView)v.findViewById(R.id.textView_add_account_save)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_fullName.getText().toString().trim().equals("")) {
                            Toast.makeText(mContext, "لظفا نام را وارد کنید.", Toast.LENGTH_SHORT).show();
                            et_fullName.requestFocus();
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(et_fullName, InputMethodManager.SHOW_IMPLICIT);
                        } else if (et_mobileNumber.getText().toString().trim().equals("")) {
                            Toast.makeText(mContext, "لظفا شماره همراه را وارد کنید.", Toast.LENGTH_SHORT).show();
                            et_mobileNumber.requestFocus();
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(et_mobileNumber, InputMethodManager.SHOW_IMPLICIT);
                        } else if (!Pattern.matches(mobile_regex,et_mobileNumber.getText().toString().trim())){
                            Toast.makeText(mContext, "شماره تلفن صحیح نیست.\nشکل صحیح 09xxxxxxxxx", Toast.LENGTH_LONG).show();
                            et_mobileNumber.requestFocus();
                            et_mobileNumber.selectAll();
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(et_mobileNumber, InputMethodManager.SHOW_IMPLICIT);
                        }else {
                            SQLiteDatabase db = new MyDatabase(mContext).getWritableDatabase();
                            ContentValues cv2 = new ContentValues();
                            cv2.put("FullName", (et_fullName).getText().toString().trim());
                            cv2.put("Phone", (et_telNumber).getText().toString().trim());
                            cv2.put("Mobile", (et_mobileNumber).getText().toString().trim());
                            cv2.put("AdressContacts", (et_address).getText().toString().trim());
                            cv2.put("Code_Melli", (et_nationalId).getText().toString().trim());
                            cv2.put("GroupContact", groupContactID[0]);
                            cv2.put("Pishvand_ID", pishvandID[0]);
                            db.update("tblContacts", cv2, "Contacts_ID = ?", new String[]{mAccountIDs.get(position) + ""});

                            if(!et_mande.getText().toString().replaceAll(",","").trim().equals(mandeAvalDovre) || !mandeType.equals(statusMande)){
                                SimpleDateFormat format2= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                final String currentDate = format2.format(new java.util.Date());

                                SimpleDateFormat format= new SimpleDateFormat("HH:mm", Locale.getDefault());
                                final String currentTime = format.format(new java.util.Date());


                                SQLiteDatabase dbMande = new MyDatabase(mContext).getWritableDatabase();

                                dbMande.delete("tblChildeSanad","Serial_Sanad = ?",new String[]{serialSanad});
                                dbMande.delete("tblParentSanad","Serial_Sanad = ?",new String[]{serialSanad});

                                ContentValues cvBedehkar = new ContentValues();
                                ContentValues cvBestankar = new ContentValues();
                                ContentValues cvParentSanad = new ContentValues();
                                ContentValues cvContacts = new ContentValues();
                                Cursor cursorMaxSrialSand = dbMande.query("tblParentSanad", new String[]{"IFNULL(MAX(Serial_Sanad),0)"}, null, null, null, null, null);
                                if (cursorMaxSrialSand.moveToFirst()) {
                                    cvParentSanad.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                                    cvBedehkar.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                                    cvBestankar.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                                    cvContacts.put("SerialSanadEftetahiye", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
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
                                cvParentSanad.put("Edited_Sanad", "1");
                                cvParentSanad.put("Date_Modify", currentDate);
                                cvParentSanad.put("Deleted_Sanad", "0");

                                dbMande.insert("tblParentSanad", null, cvParentSanad);

                                cvContacts.put("MandeAvalDovre",et_mande.getText().toString().replaceAll(",","").trim());

                                if(mandeType.trim().equals("Bedehkar")){

                                    cvBedehkar.put("AccountsID","130");
                                    cvBedehkar.put("Moein_ID","13001");
                                    cvBedehkar.put("Tafzili_ID",tafziliID.get(position));
                                    cvBedehkar.put("Bedehkar",et_mande.getText().toString().replaceAll(",","").trim());
                                    cvBedehkar.put("Bestankar","0");
                                    cvBedehkar.put("ID_Amaliyat",mAccountIDs.get(position) + "");
                                    cvBedehkar.put("ID_TypeAmaliyat","9");
                                    cvBedehkar.put("Sharh_Child_Sanad","مانده اول دوره");

                                    cvBestankar.put("AccountsID","930");
                                    cvBestankar.put("Bestankar",et_mande.getText().toString().replaceAll(",","").trim());
                                    cvBestankar.put("Bedehkar","0");
                                    cvBestankar.put("ID_Amaliyat",mAccountIDs.get(position) + "");
                                    cvBestankar.put("ID_TypeAmaliyat","9");
                                    cvBestankar.put("Sharh_Child_Sanad","مانده اول دوره");

                                    cvContacts.put("StatusMande","بدهکار");
                                    dbMande.update("tblContacts",cvContacts,"Tafzili_ID = ?",new String[]{tafziliID.get(position)});

                                    dbMande.insert("tblChildeSanad", null, cvBedehkar);
                                    dbMande.insert("tblChildeSanad",null,cvBestankar);
                                }else if(mandeType.trim().equals("Bestankar")){
                                    cvBedehkar.put("AccountsID","930");
                                    cvBedehkar.put("Bedehkar",et_mande.getText().toString().replaceAll(",","").trim());
                                    cvBedehkar.put("Bestankar","0");
                                    cvBedehkar.put("ID_Amaliyat",mAccountIDs.get(position) + "");
                                    cvBedehkar.put("ID_TypeAmaliyat","9");
                                    cvBedehkar.put("Sharh_Child_Sanad","مانده اول دوره");

                                    cvBestankar.put("AccountsID","130");
                                    cvBestankar.put("Moein_ID","13001");
                                    cvBestankar.put("Tafzili_ID",tafziliID.get(position));
                                    cvBestankar.put("Bestankar",et_mande.getText().toString().replaceAll(",","").trim());
                                    cvBestankar.put("Bedehkar","0");
                                    cvBestankar.put("ID_Amaliyat",mAccountIDs.get(position) + "");
                                    cvBestankar.put("ID_TypeAmaliyat","9");
                                    cvBestankar.put("Sharh_Child_Sanad","مانده اول دوره");

                                    cvContacts.put("StatusMande","بستانکار");
                                    dbMande.update("tblContacts",cvContacts,"Tafzili_ID = ?",new String[]{tafziliID.get(position)});

                                    dbMande.insert("tblChildeSanad", null, cvBedehkar);
                                    dbMande.insert("tblChildeSanad",null,cvBestankar);
                                }
                            }

                            Cursor cursorVaziatHesab = dbBillList.rawQuery("SELECT " +
                                            "IFNULL((SUM(IFNULL(Bedehkar,0)) - SUM(IFNULL(Bestankar,0))),0) " +
                                            "AS MandeHesab " +
                                            "FROM  tblChildeSanad " +
                                            "WHERE Tafzili_ID = '" + tafziliID.get(position) + "' "
//                    "AND ID_Child_Sanad <= " + cursorBilList.getString(cursorBilList.getColumnIndex("ID_Child_Sanad"))
                                    , null);
                            if (cursorVaziatHesab.moveToFirst()) {
                                if (((int)Double.parseDouble(cursorVaziatHesab.getString(cursorVaziatHesab.getColumnIndex("MandeHesab"))) < 0)) {
                                    holder.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.green));
                                    holder.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.green));
                                    holder.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.green));
                                    holder.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.green));
                                    holder.tvBedehiText.setText("بستانکار");
                                    holder.ivBedehi.setImageResource(R.drawable.shape_arrow_down);
                                    holder.ivBedehi.setColorFilter(mContext.getResources().getColor(R.color.green));
                                } else if ((int)Double.parseDouble(cursorVaziatHesab.getString(cursorVaziatHesab.getColumnIndex("MandeHesab"))) > 0) {
                                    holder.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.red));
                                    holder.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.red));
                                    holder.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.red));
                                    holder.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.red));
                                    holder.tvBedehiText.setText("بدهکار");
                                    holder.ivBedehi.setImageResource(R.drawable.shape_arrow_down);
                                    holder.ivBedehi.setRotation(180);
                                    holder.ivBedehi.setColorFilter(mContext.getResources().getColor(R.color.red));
                                } else {
                                    holder.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                                    holder.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                                    holder.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                                    holder.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                                    holder.tvBedehiText.setText("بی حساب");
                                    holder.ivBedehi.setVisibility(View.INVISIBLE);
                                }
                                holder.tvBedehiMablagh.setText(MainActivity.priceFormatter(cursorVaziatHesab.getString(cursorVaziatHesab.getColumnIndex("MandeHesab")).replace("-","")) + "");
                            }

                            mAccountFullName.set(position, (et_fullName).getText().toString().trim());
                            mAccountPhones.set(position, (et_telNumber).getText().toString().trim());
                            mAccountMobiles.set(position, (et_mobileNumber).getText().toString().trim());
                            mAccountAddresses.set(position, (et_address).getText().toString().trim());
                            Cursor c55 = db.query("tblPishvand", new String[]{"Pishvand"}, "Pishvand_ID = ?", new String[]{pishvandID[0] + ""}, null, null, null, null);
                            c55.moveToFirst();
                            mAccountPishvands.set(position, c55.getString(0));
                            c55.close();

                            db.close();

                            notifyDataSetChanged();

                            Toast.makeText(mContext, "با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
                            mLlAddLayer.setVisibility(View.GONE);
                            mFab.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissions = {Manifest.permission.CALL_PHONE};
                new PermissionHandler().checkPermission((Activity) mContext, permissions, new PermissionHandler.OnPermissionResponse() {
                    @Override
                    public void onPermissionGranted() {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+holder.tvMobile.getText().toString().trim()));
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mContext.startActivity(callIntent);
                    }

                    @Override
                    public void onPermissionDenied() {
                        Toast.makeText(mContext, "No Permission.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAccountFullName.size();
    }
}
