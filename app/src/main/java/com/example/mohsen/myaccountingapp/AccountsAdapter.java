package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

    static Context mContext;
    View v;
    List<String> mAccountFullName;
    View.OnClickListener oclCollapse,oclExpand;
    boolean isCollapsed;


    public AccountsAdapter(Context context, List<String> accountFullName) {
        mContext = context;
        mAccountFullName = accountFullName;
    }

    @Override
    public AccountsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_side_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llExtra;
        LinearLayout llMain;
        LinearLayout llName;
        TextView tvFullName,tvDash,tvCompanyName,tvBedehiDash,tvBedehiMablagh,tvBedehiText,tvBedehiVahed;
        ImageView ivCall,ivArrow,ivBedehi;
        public ViewHolder(View v) {
            super(v);
            llExtra = (LinearLayout)v.findViewById(R.id.linearLayout_account_side_extra_information);
            llMain = (LinearLayout)v.findViewById(R.id.linearLayout_account_side_main);
            llName = (LinearLayout)v.findViewById(R.id.linearLayout_acount_side_main_information);
            tvFullName = (TextView)v.findViewById(R.id.textView_acount_side_fullName);
            tvCompanyName = (TextView)v.findViewById(R.id.textView_acount_side_company_name);
            tvDash = (TextView)v.findViewById(R.id.textView_acount_side_dash);
            tvBedehiDash = (TextView)v.findViewById(R.id.textView_acount_side_bedehi_dash);
            tvBedehiMablagh = (TextView)v.findViewById(R.id.textView_acount_side_bedehi_mablagh);
            tvBedehiText = (TextView)v.findViewById(R.id.textView_acount_side_bedehi_text);
            tvBedehiVahed = (TextView)v.findViewById(R.id.textView_acount_side_bedehi_vahed);
            ivCall = (ImageView) v.findViewById(R.id.imageView_acount_side_call);
            ivArrow = (ImageView) v.findViewById(R.id.imageView_acount_side_arrow);
            ivBedehi = (ImageView)v. findViewById(R.id.imageView_acount_side_bedehi);
        }
    }

    @Override
    public void onBindViewHolder(final AccountsAdapter.ViewHolder  holder, final int position) {
        holder.tvFullName.setText(mAccountFullName.get(position));
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
                if(isCollapsed){
                    holder.llExtra.setVisibility(View.VISIBLE);
                    holder.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_background));
                    holder.llName.getBackground().setTint(mContext.getResources().getColor(R.color.icons));
                    holder.tvFullName.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvCompanyName.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvDash.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.ivCall.setBackground(null);
                    holder.ivCall.setImageResource(R.drawable.shape_call_expanded);
                    holder.ivArrow.setBackground(null);
                    holder.ivArrow.setImageResource(R.drawable.shape_arrow_drop_up);
                }else{
                    holder.llExtra.setVisibility(View.GONE);
                    holder.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_underline_dashed));
                    holder.llName.getBackground().setTint(mContext.getResources().getColor(R.color.primary_text));
                    holder.tvFullName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    holder.tvCompanyName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    holder.tvDash.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    holder.tvBedehiDash.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.tvBedehiMablagh.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.tvBedehiVahed.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.tvBedehiText.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.ivCall.setImageResource(R.drawable.shape_call_collapsed);
                    holder.ivCall.setBackground(mContext.getResources().getDrawable(R.drawable.shape_circle));
                    holder.ivCall.getBackground().setTint(mContext.getResources().getColor(R.color.primary_dark));
                    holder.ivArrow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_circle));
                    holder.ivArrow.getBackground().setTint(mContext.getResources().getColor(R.color.primary_dark));
                    holder.ivArrow.setImageResource(R.drawable.shape_arrow_drop_down);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mAccountFullName.size();
    }


}
