package com.example.mohsen.myaccountingapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class AccountSideActivity extends MainActivity {

    RecyclerView accountRecyclerView;
    RecyclerView.LayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;

    LinearLayout llExtra;
    LinearLayout llMain;
    LinearLayout llName;
    TextView tvFullName,tvDash,tvCompanyName,tvBedehiDash,tvBedehiMablagh,tvBedehiText,tvBedehiVahed;
    ImageView ivCall,ivArrow,ivBedehi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.account_side_layout);

        tvFarsiTitle.setText("طرف حساب");
        tvEngliashNormalTitle.setText("ACCOUNT ");
        tvEnglishBoldTitle.setText("SIDE");

        llExtra = (LinearLayout)findViewById(R.id.linearLayout_account_side_extra_information);
        llMain = (LinearLayout)findViewById(R.id.linearLayout_account_side_main);
        llName = (LinearLayout)findViewById(R.id.linearLayout_acount_side_main_information);
        tvFullName = (TextView)findViewById(R.id.textView_acount_side_fullName);
        tvCompanyName = (TextView)findViewById(R.id.textView_acount_side_company_name);
        tvDash = (TextView)findViewById(R.id.textView_acount_side_dash);
        tvBedehiDash = (TextView)findViewById(R.id.textView_acount_side_bedehi_dash);
        tvBedehiMablagh = (TextView)findViewById(R.id.textView_acount_side_bedehi_mablagh);
        tvBedehiText = (TextView)findViewById(R.id.textView_acount_side_bedehi_text);
        tvBedehiVahed = (TextView)findViewById(R.id.textView_acount_side_bedehi_vahed);
        ivCall = (ImageView) findViewById(R.id.imageView_acount_side_call);
        ivArrow = (ImageView) findViewById(R.id.imageView_acount_side_arrow);
        ivBedehi = (ImageView) findViewById(R.id.imageView_acount_side_bedehi);

        accountRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_accountSide);
        accountRecyclerView.setHasFixedSize(true);
        accountRecyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        accountRecyclerView.setLayoutManager(recyclerManager);
        List<String> accountFullName = new ArrayList<String>();
        SQLiteDatabase mydb = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = mydb.query("tblContacts",new String[]{"FullName"},null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                accountFullName.add(cursor.getString(0));
            }while ((cursor.moveToNext()));
        }
        recyclerAdapter = new AccountsAdapter(this,accountFullName,oclCollapse);
        accountRecyclerView.setAdapter(recyclerAdapter);
    }


    View.OnClickListener oclCollapse = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            llExtra.setVisibility(View.GONE);
            llMain.setBackground(getResources().getDrawable(R.drawable.shape_underline_dashed));
            llName.getBackground().setTint(getResources().getColor(R.color.primary_text));
            tvFullName.setTextColor(getResources().getColor(R.color.primary_text));
            tvCompanyName.setTextColor(getResources().getColor(R.color.primary_text));
            tvDash.setTextColor(getResources().getColor(R.color.primary_text));
            tvBedehiDash.setTextColor(getResources().getColor(R.color.green));
            tvBedehiMablagh.setTextColor(getResources().getColor(R.color.green));
            tvBedehiVahed.setTextColor(getResources().getColor(R.color.green));
            tvBedehiText.setTextColor(getResources().getColor(R.color.green));
            ivCall.setBackground(getResources().getDrawable(R.drawable.shape_circle));
            ivCall.getBackground().setTint(getResources().getColor(R.color.primary_dark));
            ivCall.setColorFilter(ContextCompat.getColor(AccountSideActivity.this, R.color.primary), android.graphics.PorterDuff.Mode.MULTIPLY);
            ivArrow.setBackground(getResources().getDrawable(R.drawable.shape_circle));
            ivArrow.getBackground().setTint(getResources().getColor(R.color.primary_dark));
            ivArrow.setColorFilter(ContextCompat.getColor(AccountSideActivity.this, R.color.primary), android.graphics.PorterDuff.Mode.MULTIPLY);
            llMain.setOnClickListener(oclExpand);

        }
    };

    View.OnClickListener oclExpand = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            llExtra.setVisibility(View.VISIBLE);
//            ll3.setBackground(getResources().getDrawable(R.drawable.shape_gradient_background));
//            llPrice.getBackground().setTint(getResources().getColor(R.color.icons));
//            tvCollapsed.setTextColor(getResources().getColor(R.color.icons));
//            tvCollapsed2.setTextColor(getResources().getColor(R.color.icons));
//            tvCollapsed3.setTextColor(getResources().getColor(R.color.icons));
//            ivExpand.setVisibility(View.GONE);
//            ivCollapse.setVisibility(View.VISIBLE);
//            ll3.setOnClickListener(oclCollapse);
        }
    };
}
