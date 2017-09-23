package com.example.mohsen.myaccountingapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    Context mContext;
    View v;
    List<String> mProductName, mProductBuyPrice, mProductSellPrice, mProductUnit,mProductMojoodi;
    List<Integer> mProductIDs;
    View.OnClickListener oclCollapse, oclExpand;
    boolean isCollapsed;


    public ProductsAdapter(Context context, List<String> productName, List<String> productBuyPrice, List<String> productSellPrice, List<String> productUnit, List<String> productMojoodi, List<Integer> productIDs) {
        mContext = context;
        mProductName = productName;
        mProductBuyPrice = productBuyPrice;
        mProductSellPrice = productSellPrice;
        mProductUnit = productUnit;
        mProductMojoodi = productMojoodi;
        mProductIDs = productIDs;
    }

    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llExtra;
        LinearLayout llMain;
        LinearLayout llName;
        TextView tvName, tvSellPrice, tvBuyPrice, tvSellText, tvUnit, tvMojodi, tvEdit, tvDelete,tvRial;
        ImageView ivArrow;

        public ViewHolder(View v) {
            super(v);
            llExtra = (LinearLayout) v.findViewById(R.id.linearLayout_product_extra);
            llMain = (LinearLayout) v.findViewById(R.id.linearLayout_product_main);
            llName = (LinearLayout) v.findViewById(R.id.linearLayout_product_name);

            tvName = (TextView) v.findViewById(R.id.textView_product_name);
            tvSellPrice = (TextView) v.findViewById(R.id.textView_product_sell_price);
            tvBuyPrice = (TextView) v.findViewById(R.id.textView_product_buy_price);
            tvSellText = (TextView) v.findViewById(R.id.textView_product_sell_text);
            tvUnit = (TextView) v.findViewById(R.id.textView_product_unit);
            tvMojodi = (TextView) v.findViewById(R.id.textView_product_mojodi);
            tvEdit = (TextView) v.findViewById(R.id.textView_product_edit);
            tvDelete = (TextView) v.findViewById(R.id.textView_product_delete);
            tvRial = (TextView) v.findViewById(R.id.textView_product_rial);

            ivArrow = (ImageView) v.findViewById(R.id.imageView_product_arrow);
        }
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.ViewHolder holder, final int position) {
        holder.tvName.setText(mProductName.get(position));
        holder.tvMojodi.setText(mProductMojoodi.get(position));
        holder.tvUnit.setText(mProductUnit.get(position));
        holder.tvBuyPrice.setText(mProductBuyPrice.get(position));
        holder.tvSellPrice.setText(mProductSellPrice.get(position));
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
                    holder.llExtra.setVisibility(View.VISIBLE);
                    holder.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_background));
                    holder.llName.getBackground().setTint(mContext.getResources().getColor(R.color.icons));
                    holder.tvName.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvMojodi.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvUnit.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvSellText.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvSellPrice.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.tvRial.setTextColor(mContext.getResources().getColor(R.color.icons));
                    holder.ivArrow.setImageResource(R.drawable.shape_arrow_drop_up);
                    holder.ivArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.primary_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else {
                    holder.llExtra.setVisibility(View.GONE);
                    holder.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_underline_dashed));
                    holder.llName.getBackground().setTint(mContext.getResources().getColor(R.color.primary_text));
                    holder.tvName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    holder.tvMojodi.setTextColor(mContext.getResources().getColor(R.color.A6));
                    holder.tvUnit.setTextColor(mContext.getResources().getColor(R.color.A6));
                    holder.tvSellText.setTextColor(mContext.getResources().getColor(R.color.A6));
                    holder.tvSellPrice.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.tvRial.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.ivArrow.setImageResource(R.drawable.shape_arrow_drop_down);
                    holder.ivArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.primary), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase dbb = new MyDatabase(mContext).getWritableDatabase();
                dbb.execSQL("DELETE FROM TblKala WHERE ID_Kala = " + mProductIDs.get(position));
                dbb.close();

                mProductName.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mProductName.size());
                notifyDataSetChanged();
            }
        });

//        holder.ivCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String[] permissions = {Manifest.permission.CALL_PHONE};
//                new PermissionHandler().checkPermission(mContext, permissions, new PermissionHandler.OnPermissionResponse() {
//                    @Override
//                    public void onPermissionGranted() {
//                        Intent intent = new Intent(Intent.ACTION_CALL);
//                        intent.setData(Uri.parse(holder.tvMobile.getText().toString()));
//                        mContext.this.startActivity(intent);
//                    }
//
//                    @Override
//                    public void onPermissionDenied() {
////                                Toast.makeText(AccountSideActivity.this, "NO Permission.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mProductName.size();
    }
}
