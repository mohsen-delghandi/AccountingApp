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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class FactorListAdapter extends RecyclerView.Adapter<FactorListAdapter.ViewHolder> {

    Context mContext;
    View v;
    String mFactorCode,mMode;
    List<String> mFactorNames,mFactorSellPrices,mFactorUnits,mFactorMeghdars;
    TextView mTvRadif,mTvMeghdar,mTvMablagh;


    public FactorListAdapter(Context context, String factorCode, String mode, TextView tvRadif, TextView tvMeghdar, TextView tvMablagh) {
        mContext = context;
        mFactorCode = factorCode;
        mMode = mode;
        mTvMablagh = tvMablagh;
        mTvMeghdar = tvMeghdar;
        mTvRadif = tvRadif;
        SQLiteDatabase dbBasket = new MyDatabase(mContext).getReadableDatabase();
        if(mMode.toString().trim().equals("Buy")){
            Cursor cursorBasketName = dbBasket.query("TblChild_KharidKala",new String[]{"ChildKharidKala_KalaID","ChildKharidKala_TedadAsli"},"ChildKharidKala_ParentID = " + mFactorCode,null,null,null,null,null);
            mFactorNames = new ArrayList<>();
            mFactorSellPrices = new ArrayList<>();
            mFactorUnits = new ArrayList<>();
            mFactorMeghdars = new ArrayList<>();
            if(cursorBasketName.moveToFirst()){
                do{
                    Cursor cursorBasketName2 = dbBasket.query("TblKala",new String[]{"Name_Kala","Fk_VahedKalaAsli","GheymatKharidAsli"},"ID_Kala = " + cursorBasketName.getString(0),null,null,null,null);
                    cursorBasketName2.moveToFirst();
                    mFactorNames.add(cursorBasketName2.getString(0));
                    mFactorSellPrices.add(cursorBasketName2.getString(2));
                    Cursor cursorBasketName3 = dbBasket.query("TblVahedKalaAsli",new String[]{"NameVahed"},"ID_Vahed = " + cursorBasketName2.getString(1),null,null,null,null);
                    cursorBasketName3.moveToFirst();
                    mFactorUnits.add(cursorBasketName3.getString(0));
                    mFactorMeghdars.add(cursorBasketName.getString(1));
                }while(cursorBasketName.moveToNext());
            }
        }else if(mMode.toString().trim().equals("Sell")){
            Cursor cursorBasketName = dbBasket.query("TblChild_ForooshKala",new String[]{"ChildForooshKala_KalaID","ChildForooshKala_TedadAsli"},"ChildForooshKala_ParentID = " + mFactorCode,null,null,null,null,null);
            mFactorNames = new ArrayList<>();
            mFactorSellPrices = new ArrayList<>();
            mFactorUnits = new ArrayList<>();
            mFactorMeghdars = new ArrayList<>();
            if(cursorBasketName.moveToFirst()){
                do{
                    Cursor cursorBasketName2 = dbBasket.query("TblKala",new String[]{"Name_Kala","Fk_VahedKalaAsli","GheymatForoshAsli"},"ID_Kala = " + cursorBasketName.getString(0),null,null,null,null);
                    cursorBasketName2.moveToFirst();
                    mFactorNames.add(cursorBasketName2.getString(0));
                    mFactorSellPrices.add(cursorBasketName2.getString(2));
                    Cursor cursorBasketName3 = dbBasket.query("TblVahedKalaAsli",new String[]{"NameVahed"},"ID_Vahed = " + cursorBasketName2.getString(1),null,null,null,null);
                    cursorBasketName3.moveToFirst();
                    mFactorUnits.add(cursorBasketName3.getString(0));
                    mFactorMeghdars.add(cursorBasketName.getString(1));
                }while(cursorBasketName.moveToNext());
            }
        }

        mTvRadif.setText(mFactorNames.size()+"");
        int jameMablagh = 0 , jameMeghdar = 0;
        for(int i = 0 ; i < mFactorSellPrices.size() ; i++){
            jameMablagh += Integer.parseInt(mFactorSellPrices.get(i));
            jameMeghdar += Integer.parseInt(mFactorMeghdars.get(i));
        }
        mTvMablagh.setText(jameMablagh+"");
        mTvMeghdar.setText(jameMeghdar+"");
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

        holder.tvName.setText(mFactorNames.get(position));
        holder.tvUnit.setText(mFactorUnits.get(position));
        holder.tvSellPrice.setText(mFactorSellPrices.get(position));
        holder.tvMeghdar.setText(mFactorMeghdars.get(position));

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return mFactorNames.size();
    }
}
