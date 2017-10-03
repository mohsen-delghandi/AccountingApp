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

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder> {

    Context mContext;
    View v;
    List<Integer> mProductBasket,mProductBasketMeghdar;
    LayoutInflater mInflaterInclude;
    LinearLayout mLlAddLayer;
    FloatingActionButton mFab;
    TextView mTvJameRadif,mTvJameMeghdar,mTvJameMablagh;


    public OrderProductAdapter(Context context, List<Integer> productBasket, List<Integer> productBasketMeghdar, FloatingActionButton fab, LinearLayout llAddLayer, TextView tvJameRadif, TextView tvJameMeghdar, TextView tvJameMablagh) {
        mContext = context;
        mProductBasket = productBasket;
        mProductBasketMeghdar = productBasketMeghdar;
        mLlAddLayer = llAddLayer;
        mFab = fab;
        mTvJameMablagh = tvJameMablagh;
        mTvJameMeghdar = tvJameMeghdar;
        mTvJameRadif = tvJameRadif;
    }

    @Override
    public OrderProductAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_and_sell_4th_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llExtra;
        LinearLayout llMain;
        LinearLayout llName;
        TextView tvName, tvSellPrice, tvUnit, tvMeghdar;
        ImageView ivDelete,ivImage,ivUp,ivDown;

        public ViewHolder(View v) {
            super(v);
//            llExtra = (LinearLayout) v.findViewById(R.id.linearLayout_product_extra);
//            llMain = (LinearLayout) v.findViewById(R.id.linearLayout_product_main);
//            llName = (LinearLayout) v.findViewById(R.id.linearLayout_product_name);
            tvName = (TextView) v.findViewById(R.id.textView_order_list_name_4th);
            tvSellPrice = (TextView) v.findViewById(R.id.textView_order_list_mablagh_4th);
//            tvUnit = (TextView) v.findViewById(R.id.textView_product_unit);
            tvMeghdar = (TextView) v.findViewById(R.id.textView_order_list_meghdar_4th);

            ivDelete = (ImageView) v.findViewById(R.id.imageView_order_list_delete_4th);
            ivUp = (ImageView) v.findViewById(R.id.imageView_order_list_up_4th);
            ivDown = (ImageView) v.findViewById(R.id.imageView_order_list_down_4th);
        }
    }

    @Override
    public void onBindViewHolder(final OrderProductAdapter.ViewHolder holder, final int position) {
        SQLiteDatabase dbBasket = new MyDatabase(mContext).getReadableDatabase();
        Cursor cursorBasketName = dbBasket.query("TblKala",new String[]{"Name_Kala","GheymatForoshAsli"},"ID_Kala = " + mProductBasket.get(position),null,null,null,null,null);
        cursorBasketName.moveToFirst();
        holder.tvName.setText(cursorBasketName.getString(0));
//        holder.tvUnit.setText(mProductUnit.get(position));
        holder.tvSellPrice.setText(cursorBasketName.getString(1));
        holder.tvMeghdar.setText(mProductBasketMeghdar.get(position)+"");

        holder.setIsRecyclable(false);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SQLiteDatabase dbb = new MyDatabase(mContext).getWritableDatabase();
//                dbb.execSQL("DELETE FROM TblKala WHERE ID_Kala = " + mProductIDs.get(position));
//                dbb.close();
//
//                mProductIDs.remove(position);
//                mProductName.remove(position);
//                mProductBuyPrice.remove(position);
//                mProductSellPrice.remove(position);
//                mProductUnit.remove(position);
//                mProductMojoodi.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, mProductName.size());
//                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductBasket.size();
    }
}
