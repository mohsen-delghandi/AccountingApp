package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        TextView tvFactorCode, tvMablaghKol, tvAccount,tvDelete;
        LinearLayout llMain;
        ImageView ivLabel;

        public ViewHolder(View v) {
            super(v);
            tvFactorCode = (TextView)v.findViewById(R.id.textView_buy_and_sell_factor_code);
            tvMablaghKol = (TextView)v.findViewById(R.id.textView_buy_and_sell_mablagh);
            tvAccount = (TextView)v.findViewById(R.id.textView_buy_and_sell_account_name);
            tvDelete = (TextView)v.findViewById(R.id.textView_buy_and_sell_delete);

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

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ConfirmDialogClass cdd = new ConfirmDialogClass(mContext);
                cdd.show();
                Window window = cdd.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                cdd.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(mMode.get(position).trim().equals("Sell")) {
                            SQLiteDatabase dbDelete = new MyDatabase(mContext).getWritableDatabase();
                            Cursor cursorSerialSanad = dbDelete.query("TblParent_FrooshKala",new String[]{"ForooshKalaParent_SerialSanad"},"ForooshKalaParent_ID = ?",new String[]{mBuyAndSellFactorCodes.get(position)+""},
                                    null,null,null);
                            if(cursorSerialSanad.moveToFirst()){
                                dbDelete.delete("tblChildeSanad","Serial_Sanad = ?",new String[]{cursorSerialSanad.getString(0)+""});
                                dbDelete.delete("tblParentSanad","Serial_Sanad = ?",new String[]{cursorSerialSanad.getString(0)+""});
                                dbDelete.delete("TblChild_ForooshKala","ChildForooshKala_ParentID = ?",new String[]{mBuyAndSellFactorCodes.get(position)+""});
                                dbDelete.delete("TblParent_FrooshKala","ForooshKalaParent_SerialSanad = ?",new String[]{cursorSerialSanad.getString(0)+""});
                            }


                        } else if(mMode.get(position).trim().equals("Buy")) {
                            SQLiteDatabase dbDelete = new MyDatabase(mContext).getWritableDatabase();
                            Cursor cursorSerialSanad = dbDelete.query("TblParent_KharidKala",new String[]{"KharidKalaParent_SerialSanad"},"KharidKalaParent_ID = ?",new String[]{mBuyAndSellFactorCodes.get(position)+""},
                                    null,null,null);
                            if(cursorSerialSanad.moveToFirst()){
                                dbDelete.delete("tblChildeSanad","Serial_Sanad = ?",new String[]{cursorSerialSanad.getString(0)+""});
                                dbDelete.delete("tblParentSanad","Serial_Sanad = ?",new String[]{cursorSerialSanad.getString(0)+""});
                                dbDelete.delete("TblChild_KharidKala","ChildKharidKala_ParentID = ?",new String[]{mBuyAndSellFactorCodes.get(position)+""});
                                dbDelete.delete("TblParent_KharidKala","KharidKalaParent_SerialSanad = ?",new String[]{cursorSerialSanad.getString(0)+""});
                            }
                        }


                        mBuyAndSellFactorCodes.remove(position);
                        mBuyAndSellMablaghKols.remove(position);
                        mBuyAndSellAccounts.remove(position);
                        mMode.remove(position);

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mBuyAndSellFactorCodes.size());
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
    }

    @Override
    public int getItemCount() {
        return mBuyAndSellFactorCodes.size();
    }
}
