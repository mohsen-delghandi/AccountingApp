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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class ProductsListSelectAdapter extends RecyclerView.Adapter<ProductsListSelectAdapter.ViewHolder> {

    Context mContext;
    View v;
    List<String> mProductName, mProductSellPrice, mProductUnit;
    List<Integer> mProductIDs,mProductMojoodi,mBasketProductMeghdar,mBasketProduct,mBasketProductMablagh;
    FloatingActionButton mFab;
    LinearLayout mLlAddLayer;
    LayoutInflater mInflaterInclude;
    RecyclerView orderRecyclerView;
    RecyclerView.LayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;
    int mFactorCode,mTafziliID;

    public ProductsListSelectAdapter(Context context, List<String> productName, List<String> productSellPrice, List<String> productUnit, List<Integer> productMojoodi, List<Integer> productIDs, FloatingActionButton fab, LinearLayout llAddLayer, int factorCode, Integer tafziliID) {
        mContext = context;
        mProductName = productName;
        mProductSellPrice = productSellPrice;
        mProductUnit = productUnit;
        mProductMojoodi = productMojoodi;
        mProductIDs = productIDs;
        mFab = fab;
        mLlAddLayer = llAddLayer;
        mFactorCode = factorCode;

        mBasketProductMeghdar = new ArrayList<>();
        mBasketProduct = new ArrayList<>();
        mBasketProductMablagh = new ArrayList<>();

        mTafziliID = tafziliID;
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
                if(Integer.parseInt(holder.tvMeghdar.getText().toString().trim()) < mProductMojoodi.get(position)) {
                    holder.tvMeghdar.setText((Integer.parseInt(holder.tvMeghdar.getText().toString().trim()) + 1)+"");
                }else{
                    Toast.makeText(mContext, "حداکثر موجودی انتخاب شده است.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.ivDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(holder.tvMeghdar.getText().toString().trim()) > 0) {
                    holder.tvMeghdar.setText((Integer.parseInt(holder.tvMeghdar.getText().toString().trim()) - 1)+"");
                }
            }
        });

        holder.ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.llMain.setBackgroundColor(mContext.getResources().getColor(R.color.divider));
                SQLiteDatabase dbOrder = new MyDatabase(mContext).getReadableDatabase();
                Cursor cursorOrder = dbOrder.query("TblKala",new String[]{"GheymatForoshAsli"},"ID_Kala = " + mProductIDs.get(position),null,null,null,null);
                cursorOrder.moveToFirst();
                mBasketProductMablagh.add(cursorOrder.getInt(0));
                mBasketProduct.add(mProductIDs.get(position));
                mBasketProductMeghdar.add(Integer.parseInt(holder.tvMeghdar.getText().toString().trim()));
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
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

                final View v = mInflaterInclude.inflate(R.layout.buy_and_sell_4th_layout,mLlAddLayer);

                TextView tvJameRadif = (TextView)v.findViewById(R.id.textView_order_list_jame_radif_4th);
                TextView tvJameMeghdar = (TextView)v.findViewById(R.id.textView_order_list_jame_meghdar_4th);
                TextView tvJameMablagh = (TextView)v.findViewById(R.id.textView_order_list_jame_mablagh_4th);
                LinearLayout llTayidOrderList = (LinearLayout)v.findViewById(R.id.linearLayout_order_list_tayid_4th);



                tvJameRadif.setText(mBasketProduct.size()+"");
                int jameMeghdar = 0;
                for(int i = 0 ; i < mBasketProductMeghdar.size() ; i++){
                    jameMeghdar += mBasketProductMeghdar.get(i);
                }
                tvJameMeghdar.setText(jameMeghdar+"");
                int jameMablagh = 0;
                for(int i = 0 ; i < mBasketProductMablagh.size() ; i++){
                    jameMablagh += mBasketProductMablagh.get(i)*mBasketProductMeghdar.get(i);
                }
                tvJameMablagh.setText(jameMablagh+"");

                final int finalJameMablagh = jameMablagh;
                final int finalJameMeghdar = jameMeghdar;
                llTayidOrderList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SQLiteDatabase dbTayidList = new MyDatabase(mContext).getWritableDatabase();
                        ContentValues cvTayidList = new ContentValues();
                        cvTayidList.put("ForooshKalaParent_ID",mFactorCode + "");
                        cvTayidList.put("ForooshKalaParent_Tafzili",mTafziliID + "");
                        cvTayidList.put("ForooshKalaParent_JameKol", finalJameMablagh + "");
                        dbTayidList.insert("TblParent_FrooshKala",null,cvTayidList);
                        Toast.makeText(mContext, "خرید با موفقیت ثبت شد.", Toast.LENGTH_SHORT).show();

                        for(int i = 0; i < mBasketProduct.size() ; i++) {
                            ContentValues cvTayidLIstChild = new ContentValues();
                            cvTayidLIstChild.put("ChildForooshKala_ParentID",mFactorCode);
                            cvTayidLIstChild.put("ChildForoodhKalaID",mBasketProduct.get(i));
                            cvTayidLIstChild.put("ChildForooshKala_TedadAsli",mBasketProductMeghdar.get(i));
                            cvTayidLIstChild.put("ChildForooshKala_JameKol",mBasketProductMablagh.get(i));
                        }
                    }
                });

                orderRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView_buy_and_sell_4th);
                orderRecyclerView.setHasFixedSize(true);
                orderRecyclerView.setNestedScrollingEnabled(false);
                recyclerManager = new LinearLayoutManager(mContext);
                orderRecyclerView.setLayoutManager(recyclerManager);
                recyclerAdapter = new OrderProductAdapter(mContext,mBasketProduct,mBasketProductMeghdar,mFab,mLlAddLayer,tvJameRadif,tvJameMeghdar,tvJameMablagh);
                orderRecyclerView.setAdapter(recyclerAdapter);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductName.size();
    }
}
