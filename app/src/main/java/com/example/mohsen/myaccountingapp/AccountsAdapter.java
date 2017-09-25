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
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.view.Gravity;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

    Context mContext;
    View v;
    List<String> mAccountFullName, mAccountPhones, mAccountMobiles, mAccountAddresses;
    List<Integer> mAccountIDs;
    boolean isCollapsed;
    ViewHolder selectedHolder;
    LayoutInflater mInflaterInclude;
    LinearLayout mLlAddLayer;
    FloatingActionButton mFab;


    public AccountsAdapter(Context context, List<String> accountFullName, List<String> accountPhone, List<String> accountMobile, List<String> accountAddress, List<Integer> accountIDs, LinearLayout llAddLayer, FloatingActionButton fab) {
        mContext = context;
        mAccountFullName = accountFullName;
        mAccountAddresses = accountAddress;
        mAccountMobiles = accountMobile;
        mAccountPhones = accountPhone;
        mAccountIDs = accountIDs;
        mLlAddLayer = llAddLayer;
        mFab = fab;
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
        TextView tvFullName, tvDash, tvCompanyName, tvBedehiDash, tvBedehiMablagh, tvBedehiText, tvBedehiVahed, tvPhone, tvMobile, tvAddress, tvEdit, tvDelete;
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
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.primary_text));
        h.tvFullName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvCompanyName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvDash.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.green));
        h.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.green));
        h.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.green));
        h.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.green));
        h.ivCall.setImageResource(R.drawable.shape_call_collapsed);
        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_down);
    }

    @Override
    public void onBindViewHolder(final AccountsAdapter.ViewHolder holder, final int position) {
        holder.tvFullName.setText(mAccountFullName.get(position));
        holder.tvPhone.setText(mAccountPhones.get(position));
        holder.tvMobile.setText(mAccountMobiles.get(position));
        holder.tvAddress.setText(mAccountAddresses.get(position));
        holder.setIsRecyclable(false);
//        oclExpand = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.llExtra.setVisibility(View.VISIBLE);
//                holder.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_background));
//                holder.llName.getBackground().setTint(mContext.getResources().getColor(R.color.icons));
//                holder.tvFullName.setTextColor(mContext.getResources().getColor(R.color.icons));
//                holder.tvCompanyName.setTextColor(mContext.getResources().getColor(R.color.icons));
//                holder.tvDash.setTextColor(mContext.getResources().getColor(R.color.icons));
//                holder.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.icons));
//                holder.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.icons));
//                holder.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.icons));
//                holder.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.icons));
//                holder.ivCall.setBackground(null);
//                holder.ivCall.setColorFilter(ContextCompat.getColor(mContext, R.color.primary_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
//                holder.ivArrow.setBackground(null);
//                holder.ivArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.primary_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
//                holder.llMain.setOnClickListener(oclCollapse);
//            }
//        };
//
//        oclCollapse = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.llExtra.setVisibility(View.GONE);
//                holder.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_underline_dashed));
//                holder.llName.getBackground().setTint(mContext.getResources().getColor(R.color.primary_text));
//                holder.tvFullName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
//                holder.tvCompanyName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
//                holder.tvDash.setTextColor(mContext.getResources().getColor(R.color.primary_text));
//                holder.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.green));
//                holder.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.green));
//                holder.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.green));
//                holder.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.green));
//                holder.ivCall.setBackground(mContext.getResources().getDrawable(R.drawable.shape_circle));
//                holder.ivCall.getBackground().setTint(mContext.getResources().getColor(R.color.primary_dark));
//                holder.ivCall.setColorFilter(ContextCompat.getColor(mContext, R.color.primary), android.graphics.PorterDuff.Mode.MULTIPLY);
//                holder.ivArrow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_circle));
//                holder.ivArrow.getBackground().setTint(mContext.getResources().getColor(R.color.primary_dark));
//                holder.ivArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.primary), android.graphics.PorterDuff.Mode.MULTIPLY);
//                holder.llMain.setOnClickListener(oclExpand);
//            }
//        };
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
                SQLiteDatabase dbb = new MyDatabase(mContext).getWritableDatabase();
                dbb.execSQL("DELETE FROM tblContacts WHERE Contacts_ID = " + mAccountIDs.get(position));
                dbb.close();

                mAccountFullName.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mAccountFullName.size());
                notifyDataSetChanged();
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

                SQLiteDatabase db11 = new MyDatabase(mContext).getWritableDatabase();
                Cursor c11 = db11.query("tblContacts",new String[]{"FullName","Code_Melli","Phone","Mobile","AdressContacts","GroupContact","Pishvand_ID"},"Contacts_ID = ?",
                        new String[]{mAccountIDs.get(position)+""},null,null,null);
                c11.moveToFirst();
                ((EditText)v.findViewById(R.id.editText_add_account_fullName)).setText(c11.getString(0));
                ((EditText)v.findViewById(R.id.editText_add_account_codeMelli)).setText(c11.getString(1));
                ((EditText)v.findViewById(R.id.editText_add_account_phone)).setText(c11.getString(2));
                ((EditText)v.findViewById(R.id.editText_add_account_mobile)).setText(c11.getString(3));
                ((EditText)v.findViewById(R.id.editText_add_account_address)).setText(c11.getString(4));

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

                ((TextView)v.findViewById(R.id.textView_add_account_fullName)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_account_codeMelli)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_account_phone)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_account_mobile)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_account_address)).setVisibility(View.VISIBLE);

                ((EditText)v.findViewById(R.id.editText_add_account_fullName)).setHint("");
                ((EditText)v.findViewById(R.id.editText_add_account_codeMelli)).setHint("");
                ((EditText)v.findViewById(R.id.editText_add_account_phone)).setHint("");
                ((EditText)v.findViewById(R.id.editText_add_account_mobile)).setHint("");
                ((EditText)v.findViewById(R.id.editText_add_account_address)).setHint("");

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
                        SQLiteDatabase db = new MyDatabase(mContext).getWritableDatabase();
                        ContentValues cv2 = new ContentValues();
                        cv2.put("FullName",((EditText)v.findViewById(R.id.editText_add_account_fullName)).getText().toString().trim());
                        cv2.put("Phone",((EditText)v.findViewById(R.id.editText_add_account_phone)).getText().toString().trim());
                        cv2.put("Mobile",((EditText)v.findViewById(R.id.editText_add_account_mobile)).getText().toString().trim());
                        cv2.put("AdressContacts",((EditText)v.findViewById(R.id.editText_add_account_address)).getText().toString().trim());
                        cv2.put("Code_Melli",((EditText)v.findViewById(R.id.editText_add_account_codeMelli)).getText().toString().trim());
                        cv2.put("GroupContact",groupContactID[0]);
                        cv2.put("Pishvand_ID",pishvandID[0]);
                        db.update("tblContacts",cv2,"Contacts_ID = ?",new String[]{mAccountIDs.get(position)+""});
                        db.close();

                        Toast.makeText(mContext, "با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
                        mLlAddLayer.setVisibility(View.GONE);
                        mFab.setVisibility(View.VISIBLE);
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
