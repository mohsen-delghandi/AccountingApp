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
import android.support.v7.widget.LinearLayoutManager;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class ProductsListSelectAdapter extends RecyclerView.Adapter<ProductsListSelectAdapter.ViewHolder> {

    Context mContext;
    View v;
    List<String> mProductName, mProductSellPrice, mProductUnit;
    List<Integer> mProductIDs,mProductMojoodi,mBasketProductMeghdar,mBasketProduct,mBasketProductMablagh;
    LinearLayout mLlAddLayer,mLlTayid3rd;
    LayoutInflater mInflaterInclude;
    RecyclerView orderRecyclerView;
    RecyclerView.LayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;
    int mFactorCode,mTafziliID;
    String mMode;
    TextView mTvJameRadif,mTvJameMeghdar,mTvJameMablagh;
    int jameMablagh,jameMeghdar;

    public ProductsListSelectAdapter(Context context, List<String> productName, List<String> productSellPrice, List<String> productUnit, List<Integer> productMojoodi, List<Integer> productIDs, LinearLayout llAddLayer, int factorCode, Integer tafziliID, String mode, LinearLayout llTayid3rd, TextView tvJameRadif, TextView tvJameMeghdar, TextView tvJameMablagh) {
        mContext = context;
        mProductName = productName;
        mProductSellPrice = productSellPrice;
        mProductUnit = productUnit;
        mProductMojoodi = productMojoodi;
        mProductIDs = productIDs;
        mLlAddLayer = llAddLayer;
        mFactorCode = factorCode;
        mLlTayid3rd = llTayid3rd;
        mBasketProductMeghdar = new ArrayList<>();
        mBasketProduct = new ArrayList<>();
        mBasketProductMablagh = new ArrayList<>();
        mMode = mode;
        mTafziliID = tafziliID;
        mTvJameMablagh = tvJameMablagh;
        mTvJameMeghdar = tvJameMeghdar;
        mTvJameRadif = tvJameRadif;
    }

    @Override
    public ProductsListSelectAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_and_sell_3rd_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvSellPrice, tvUnit, tvMeghdar;
        LinearLayout llMain;
        ImageView ivUp,ivDown,ivProductImage;

        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.textView_product_name_3rd);
            tvSellPrice = (TextView) v.findViewById(R.id.textView_product_sell_price_3rd);
            tvUnit = (TextView) v.findViewById(R.id.textView_product_unit_3rd);
            tvMeghdar = (TextView) v.findViewById(R.id.textView_product_quintity_3rd);

            ivUp = (ImageView)v.findViewById(R.id.imageView_product_up_3rd);
            ivDown = (ImageView)v.findViewById(R.id.imageView_product_down_3rd);
            ivProductImage = (ImageView)v.findViewById(R.id.imageView_product_image_3rd);

            llMain = (LinearLayout)v.findViewById(R.id.linearLayout_product_main_3rd);
        }
    }

    @Override
    public void onBindViewHolder(final ProductsListSelectAdapter.ViewHolder holder, final int position) {
        holder.tvName.setText(mProductName.get(position));
        holder.tvUnit.setText(mProductUnit.get(position));
        holder.tvSellPrice.setText(mProductSellPrice.get(position));
        holder.setIsRecyclable(false);

        holder.ivUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(holder.tvMeghdar.getText().toString().trim()) < mProductMojoodi.get(position)) {
                    holder.tvMeghdar.setText((Integer.parseInt(holder.tvMeghdar.getText().toString().trim()) + 1) + "");
                } else {
                    Toast.makeText(mContext, "حداکثر موجودی انتخاب شده است.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.ivDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(holder.tvMeghdar.getText().toString().trim()) > 0) {
                    holder.tvMeghdar.setText((Integer.parseInt(holder.tvMeghdar.getText().toString().trim()) - 1) + "");
                }
            }
        });

        holder.ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.llMain.setBackgroundColor(mContext.getResources().getColor(R.color.divider));
                SQLiteDatabase dbOrder = new MyDatabase(mContext).getReadableDatabase();
                Cursor cursorOrder = dbOrder.query("TblKala", new String[]{"GheymatForoshAsli"}, "ID_Kala = " + mProductIDs.get(position), null, null, null, null);
                cursorOrder.moveToFirst();
                mBasketProductMablagh.add(cursorOrder.getInt(0));
                mBasketProduct.add(mProductIDs.get(position));
                mBasketProductMeghdar.add(Integer.parseInt(holder.tvMeghdar.getText().toString().trim()));

                mTvJameRadif.setText(mBasketProduct.size() + "");
                jameMeghdar = 0;
                for (int i = 0; i < mBasketProductMeghdar.size(); i++) {
                    jameMeghdar += mBasketProductMeghdar.get(i);
                }
                mTvJameMeghdar.setText(jameMeghdar + "");
                jameMablagh = 0;
                for (int i = 0; i < mBasketProductMablagh.size(); i++) {
                    jameMablagh += mBasketProductMablagh.get(i) * mBasketProductMeghdar.get(i);
                }
                mTvJameMablagh.setText(jameMablagh + "");
            }
        });

        mLlTayid3rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMode.equals("Sell")) {
                    SQLiteDatabase dbTayidList = new MyDatabase(mContext).getWritableDatabase();
                    ContentValues cvTayidList = new ContentValues();
                    ContentValues cvParentSanad = new ContentValues();
                    ContentValues cvChildSanad = new ContentValues();
                    ContentValues cvChildSanad2 = new ContentValues();
                    cvTayidList.put("ForooshKalaParent_ID", mFactorCode + "");
                    cvTayidList.put("ForooshKalaParent_Tafzili", mTafziliID + "");
                    cvTayidList.put("ForooshKalaParent_JameKol", jameMablagh + "");
                    Cursor cursorMaxSrialSand = dbTayidList.query("tblParentSanad", new String[]{"MAX(Serial_Sanad)"}, null, null, null, null, null);
                    if (cursorMaxSrialSand.moveToFirst()) {
                        cvTayidList.put("ForooshKalaParent_SerialSanad", cursorMaxSrialSand.getString(0)+1);
                        cvParentSanad.put("Serial_Sanad",cursorMaxSrialSand.getString(0)+1);
                        cvChildSanad.put("Serial_Sanad",cursorMaxSrialSand.getString(0)+1);
                        cvChildSanad2.put("Serial_Sanad",cursorMaxSrialSand.getString(0)+1);
                    } else {
                        cvTayidList.put("ForooshKalaParent_SerialSanad", "1");
                        cvParentSanad.put("Serial_Sanad","1");
                        cvChildSanad.put("Serial_Sanad","1");
                        cvChildSanad2.put("Serial_Sanad","1");
                    }

                    SimpleDateFormat format= new SimpleDateFormat("HH:mm", Locale.getDefault());
                    final String currentTime = format.format(new java.util.Date());

                    DatePersian mDate = new DatePersian();
                    final String currentDate = mDate.getDate();

                    cvTayidList.put("ForooshKalaParent_Date",currentDate + currentTime);

                    dbTayidList.insert("TblParent_FrooshKala", null, cvTayidList);
                    Toast.makeText(mContext, "خرید با موفقیت ثبت شد.", Toast.LENGTH_SHORT).show();

                    Cursor cursorMaxNumberSand = dbTayidList.query("tblParentSanad", new String[]{"MAX(Number_Sanad)"}, null, null, null, null, null);
                    if (cursorMaxNumberSand.moveToFirst()) {
                        cvParentSanad.put("Number_Sanad", cursorMaxNumberSand.getString(0)+1);
                    } else {
                        cvParentSanad.put("Number_Sanad","1");
                    }
                    cvParentSanad.put("StatusSanadID","3");
                    cvParentSanad.put("TypeSanad_ID","4");
                    cvParentSanad.put("Date_Sanad",currentDate);
                    cvParentSanad.put("Time_Sanad",currentTime);
                    cvParentSanad.put("Taraz_Sanad","1");
                    cvParentSanad.put("Error_Sanad","0");
                    cvParentSanad.put("Edited_Sanad","0");
                    cvParentSanad.put("Deleted_Sanad","0");

                    dbTayidList.insert("tblParentSanad",null,cvParentSanad);

                    cvChildSanad.put("Accounts_ID","130");
                    cvChildSanad.put("Moein_ID","13001");
                    cvChildSanad.put("Tafzili_ID",mTafziliID + "");
                    cvChildSanad.put("ID_Amaliyat",mFactorCode + "");
                    cvChildSanad.put("ID_TypeAmaliyat","5");
                    cvChildSanad.put("Bedehkar",jameMablagh + "");
                    cvChildSanad.put("Bestankar","0");
                    dbTayidList.insert("tblChildeSanad",null,cvChildSanad);

                    cvChildSanad2.put("Accounts_ID","610");
                    cvChildSanad2.put("Moein_ID","61001");
                    cvChildSanad2.put("ID_Amaliyat",mFactorCode + "");
                    cvChildSanad2.put("ID_TypeAmaliyat","5");
                    cvChildSanad2.put("Bestankar",jameMablagh + "");
                    cvChildSanad2.put("Bedehkar","0");
                    dbTayidList.insert("tblChildeSanad",null,cvChildSanad2);


                    for (int i = 0; i < mBasketProduct.size(); i++) {
                        ContentValues cvTayidLIstChild = new ContentValues();
                        cvTayidLIstChild.put("ChildForooshKala_ParentID", mFactorCode);
                        cvTayidLIstChild.put("ChildForooshKala_KalaID", mBasketProduct.get(i));
                        cvTayidLIstChild.put("ChildForooshKala_TedadAsli", mBasketProductMeghdar.get(i));
                        cvTayidLIstChild.put("ChildForooshKala_JameKol", mBasketProductMablagh.get(i));
                        dbTayidList.insert("TblChild_ForooshKala", null, cvTayidLIstChild);
                    }
                } else if (mMode.equals("Buy")) {
                    SQLiteDatabase dbTayidList = new MyDatabase(mContext).getWritableDatabase();
                    ContentValues cvTayidList = new ContentValues();
                    ContentValues cvParentSanad = new ContentValues();
                    ContentValues cvChildSanad = new ContentValues();
                    ContentValues cvChildSanad2 = new ContentValues();
                    cvTayidList.put("KharidKalaParent_ID", mFactorCode + "");
                    cvTayidList.put("KharidKalaParent_Tafzili", mTafziliID + "");
                    cvTayidList.put("KharidKalaParent_JameKol", jameMablagh + "");

                    Cursor cursorMaxSrialSand = dbTayidList.query("tblParentSanad", new String[]{"MAX(Serial_Sanad)"}, null, null, null, null, null);
                    if (cursorMaxSrialSand.moveToFirst()) {
                        cvTayidList.put("KharidKalaParent_SerialSanad", cursorMaxSrialSand.getString(0)+1);
                        cvParentSanad.put("Serial_Sanad",cursorMaxSrialSand.getString(0)+1);
                        cvChildSanad.put("Serial_Sanad",cursorMaxSrialSand.getString(0)+1);
                        cvChildSanad2.put("Serial_Sanad",cursorMaxSrialSand.getString(0)+1);
                    } else {
                        cvTayidList.put("KharidKalaParent_SerialSanad", "1");
                        cvParentSanad.put("Serial_Sanad","1");
                        cvChildSanad.put("Serial_Sanad","1");
                        cvChildSanad2.put("Serial_Sanad","1");
                    }

                    SimpleDateFormat format= new SimpleDateFormat("HH:mm", Locale.getDefault());
                    final String currentTime = format.format(new java.util.Date());

                    DatePersian mDate = new DatePersian();
                    final String currentDate = mDate.getDate();

                    cvTayidList.put("KharidKalaParent_Date",currentDate + currentTime);

                    dbTayidList.insert("TblParent_KharidKala", null, cvTayidList);
                    Toast.makeText(mContext, "خرید با موفقیت ثبت شد.", Toast.LENGTH_SHORT).show();

                    Cursor cursorMaxNumberSand = dbTayidList.query("tblParentSanad", new String[]{"MAX(Number_Sanad)"}, null, null, null, null, null);
                    if (cursorMaxNumberSand.moveToFirst()) {
                        cvParentSanad.put("Number_Sanad", cursorMaxNumberSand.getString(0)+1);
                    } else {
                        cvParentSanad.put("Number_Sanad","1");
                    }
                    cvParentSanad.put("StatusSanadID","3");
                    cvParentSanad.put("TypeSanad_ID","4");
                    cvParentSanad.put("Date_Sanad",currentDate);
                    cvParentSanad.put("Time_Sanad",currentTime);
                    cvParentSanad.put("Taraz_Sanad","1");
                    cvParentSanad.put("Error_Sanad","0");
                    cvParentSanad.put("Edited_Sanad","0");
                    cvParentSanad.put("Deleted_Sanad","0");

                    dbTayidList.insert("tblParentSanad",null,cvParentSanad);

                    cvChildSanad.put("Accounts_ID","150");
                    cvChildSanad.put("Moein_ID","15001");
                    cvChildSanad.put("Tafzili_ID",mTafziliID + "");
                    cvChildSanad.put("ID_Amaliyat",mFactorCode + "");
                    cvChildSanad.put("ID_TypeAmaliyat","11");
                    cvChildSanad.put("Bedehkar",jameMablagh + "");
                    cvChildSanad.put("Bestankar","0");
                    dbTayidList.insert("tblChildeSanad",null,cvChildSanad);

                    cvChildSanad2.put("Accounts_ID","320");
                    cvChildSanad2.put("Moein_ID","32001");
                    cvChildSanad2.put("ID_Amaliyat",mFactorCode + "");
                    cvChildSanad2.put("ID_TypeAmaliyat","11");
                    cvChildSanad2.put("Bestankar",jameMablagh + "");
                    cvChildSanad2.put("Bedehkar","0");
                    dbTayidList.insert("tblChildeSanad",null,cvChildSanad2);

                    for (int i = 0; i < mBasketProduct.size(); i++) {
                        ContentValues cvTayidLIstChild = new ContentValues();
                        cvTayidLIstChild.put("ChildKharidKala_ParentID", mFactorCode);
                        cvTayidLIstChild.put("ChildKharidKala_KalaID", mBasketProduct.get(i));
                        cvTayidLIstChild.put("ChildKharidKala_TedadAsli", mBasketProductMeghdar.get(i));
                        cvTayidLIstChild.put("ChildKharidKala_JameKol", mBasketProductMablagh.get(i));
                        dbTayidList.insert("TblChild_KharidKala", null, cvTayidLIstChild);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductName.size();
    }
}
