package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class FactorListAdapter extends RecyclerView.Adapter<FactorListAdapter.ViewHolder> {

    Context mContext;
    View v;
    String mFactorCode,mMode;


    public FactorListAdapter(Context context, String factorCode, String mode) {
        mContext = context;
        mFactorCode = factorCode;
        mMode = mode;
    }

    @Override
    public FactorListAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_and_sell_4th_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSellPrice, tvUnit, tvMeghdar;

        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.textView_order_list_name_4th);
            tvSellPrice = (TextView) v.findViewById(R.id.textView_order_list_mablagh_4th);
            tvUnit = (TextView) v.findViewById(R.id.textView_order_list_unit_4th);
            tvMeghdar = (TextView) v.findViewById(R.id.textView_order_list_meghdar_4th);
        }
    }

    @Override
    public void onBindViewHolder(final FactorListAdapter.ViewHolder holder, final int position) {
        SQLiteDatabase dbBasket = new MyDatabase(mContext).getReadableDatabase();
        if(mMode.toString().trim().equals("Buy")){

        }
        Cursor cursorBasketName = dbBasket.query("TblChild_KharidKala",new String[]{"ChildKharidKala_KalaID","ChildKharidKala_TedadAsli","ChilKharidKala_JameKol"},"ID_Kala = " + mProductBasket.get(position),null,null,null,null,null);
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
