package com.example.mohsen.myaccountingapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
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

public class BuyAndSellAdapter extends RecyclerView.Adapter<BuyAndSellAdapter.ViewHolder> {

    Context mContext;
    View v;
    List<String> mBuyAndSellFactorCodes;
    List<String> mBuyAndSellMablaghKols;
    List<String> mBuyAndSellAccounts;
    List<String> mMode;
    LayoutInflater mInflaterInclude;
    LinearLayout mLlAddLayer;
    FloatingActionButton mFab;
    RecyclerView factorViewRecyclerView;
    RecyclerView.LayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;


    public BuyAndSellAdapter(Context context, List<String> buyAndSellFactorCodes, List<String> buyAndSellMablaghKols, List<String> buyAndSellAccounts, List<String> mode, LinearLayout llAddLayer, FloatingActionButton fab) {
        mContext = context;
        mBuyAndSellFactorCodes = buyAndSellFactorCodes;
        mBuyAndSellAccounts = buyAndSellAccounts;
        mBuyAndSellMablaghKols = buyAndSellMablaghKols;
        mMode = mode;
        mFab = fab;
        mLlAddLayer = llAddLayer;
    }

    @Override
    public BuyAndSellAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_and_sell_show_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFactorCode, tvMablaghKol, tvAccount;
        LinearLayout llMain;
        ImageView ivLabel;

        public ViewHolder(View v) {
            super(v);
            tvFactorCode = (TextView)v.findViewById(R.id.textView_buy_and_sell_factor_code);
            tvMablaghKol = (TextView)v.findViewById(R.id.textView_buy_and_sell_mablagh);
            tvAccount = (TextView)v.findViewById(R.id.textView_buy_and_sell_account_name);

            ivLabel = (ImageView)v.findViewById(R.id.imageView_buy_and_sell_label);

            llMain = (LinearLayout)v.findViewById(R.id.linearLayout_buy_and_sell_main);
        }
    }

    @Override
    public void onBindViewHolder(final BuyAndSellAdapter.ViewHolder holder, final int position) {
        holder.tvFactorCode.setText(mBuyAndSellFactorCodes.get(position));
        holder.tvMablaghKol.setText(MainActivity.priceFormatter(mBuyAndSellMablaghKols.get(position)));
        holder.tvAccount.setText(mBuyAndSellAccounts.get(position)+"");
        if(mMode.get(position).trim().equals("Buy")){
            holder.ivLabel.setImageResource(R.drawable.buy_image);
        }else if(mMode.get(position).trim().equals("Sell")){
            holder.ivLabel.setImageResource(R.drawable.sell_image);
        }
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
                TextView tvRadif = (TextView)v.findViewById(R.id.textView_order_list_jame_radif_4thnew);
                TextView tvMeghdar = (TextView)v.findViewById(R.id.textView_order_list_jame_meghdar_4thnew);
                TextView tvMablagh = (TextView)v.findViewById(R.id.textView_order_list_jame_mablagh_4thnew);
                ImageView ivBack4th = (ImageView)v.findViewById(R.id.imageView_buy_and_sell_4th_back);
                ivBack4th.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mFab.setVisibility(View.VISIBLE);
                        mLlAddLayer.removeAllViews();
                        mLlAddLayer.setVisibility(View.GONE);
                    }
                });

                factorViewRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView_buy_and_sell_4th);
                factorViewRecyclerView.setHasFixedSize(true);
                factorViewRecyclerView.setNestedScrollingEnabled(false);
                recyclerManager = new LinearLayoutManager(mContext);
                factorViewRecyclerView.setLayoutManager(recyclerManager);
                recyclerAdapter = new FactorListAdapter(mContext,mBuyAndSellFactorCodes.get(position),mMode.get(position),tvRadif,tvMeghdar,tvMablagh);
                factorViewRecyclerView.setAdapter(recyclerAdapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBuyAndSellFactorCodes.size();
    }
}
