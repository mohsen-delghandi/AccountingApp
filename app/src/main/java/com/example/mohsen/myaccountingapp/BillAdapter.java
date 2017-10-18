package com.example.mohsen.myaccountingapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.EditTextPreference;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.JDF;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.mohsen.myaccountingapp.MainActivity.dateToText;
import static com.example.mohsen.myaccountingapp.MainActivity.georgianDateToPersianDate;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    Context mContext;
    View v;
    List<String> billTypes;
    List<String> billMablaghs;
    List<String> billVaziatMablaghs;
    List<String> billExps;
    List<String> billDates;
    boolean isCollapsed;
    ViewHolder selectedHolder;

    public BillAdapter(List<String> billExps, List<String> billMablaghs, List<String> billTypes, List<String> billVaziatMablaghs, List<String> billDates) {

        this.billExps = billExps;
        this.billMablaghs = billMablaghs;
        this.billTypes = billTypes;
        this.billVaziatMablaghs = billVaziatMablaghs;
        this.billDates = billDates;
    }

    @Override
    public BillAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_show_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        mContext = parent.getContext();
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llExtra;
        LinearLayout llMain;
        LinearLayout llName;
        LinearLayout llDate;
        TextView tvType, tvMablaghText,tvMablagh,tvMablaghRial, tvVaziatText, tvVaziatMablagh,tvVaziatRial,tvExpText,tvExp,tvDate;
        ImageView ivLabel,ivArrow;

        public ViewHolder(View v) {
            super(v);
            llExtra = (LinearLayout) v.findViewById(R.id.linearLayout_bill_extra);
            llMain = (LinearLayout) v.findViewById(R.id.linearLayout_bill_main);
            llName = (LinearLayout) v.findViewById(R.id.linearLayout_bill_name);
            llDate = (LinearLayout) v.findViewById(R.id.linearLayout_bill_date);

            tvType = (TextView) v.findViewById(R.id.textView_bill_type);
            tvMablaghText = (TextView) v.findViewById(R.id.textView_bill_text);
            tvMablagh = (TextView) v.findViewById(R.id.textView_bill_mablagh);
            tvMablaghRial = (TextView) v.findViewById(R.id.textView_bill_rial);
            tvVaziatText = (TextView) v.findViewById(R.id.textView_bill_vaziat_hesab_text);
            tvVaziatMablagh = (TextView) v.findViewById(R.id.textView_bill_vaziat_hesab_mablagh);
            tvVaziatRial = (TextView) v.findViewById(R.id.textView_bill_vaziat_hesab_rial);
            tvExpText = (TextView) v.findViewById(R.id.textView_bill_exp_text);
            tvExp = (TextView) v.findViewById(R.id.textView_bill_exp);
            tvDate = (TextView) v.findViewById(R.id.textView_bill_date);

            ivLabel = (ImageView)v.findViewById(R.id.imageView_bill_label);
            ivArrow = (ImageView)v.findViewById(R.id.imageView_bill_arrow);
        }
    }

    private void selectItem(ViewHolder h, int position){
        h.llExtra.setVisibility(View.VISIBLE);
        h.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_background));
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.icons));
        h.tvType.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvMablaghText.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvMablagh.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvVaziatText.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvMablaghRial.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvExpText.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvExp.setTextColor(mContext.getResources().getColor(R.color.icons));

        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_up);
    }

    private void deSelectItem(ViewHolder h, int position){
        h.llExtra.setVisibility(View.GONE);
        h.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_underline_dashed));
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.shiri));
        h.tvType.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvMablaghText.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvVaziatText.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvMablagh.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvMablaghRial.setTextColor(mContext.getResources().getColor(R.color.primary_text));

        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_down);
    }

    @Override
    public void onBindViewHolder(final BillAdapter.ViewHolder holder, final int position) {
        holder.tvType.setText(billTypes.get(position));
        holder.tvMablagh.setText(MainActivity.priceFormatter(billMablaghs.get(position)));
        holder.tvExp.setText(billExps.get(position));
        holder.tvVaziatMablagh.setText(MainActivity.priceFormatter(Math.abs(Long.parseLong(billVaziatMablaghs.get(position)))+""));
        if((Long.parseLong(billVaziatMablaghs.get(position).toString().trim()) < 0)){
            holder.tvVaziatMablagh.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.tvVaziatRial.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.ivLabel.setImageResource(R.drawable.bestankar);
        }else if(Long.parseLong(billVaziatMablaghs.get(position).toString().trim()) > 0){
            holder.tvVaziatMablagh.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.tvVaziatRial.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.ivLabel.setImageResource(R.drawable.bedehkar);
        }else{
            holder.tvVaziatMablagh.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            holder.tvVaziatRial.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            holder.ivLabel.setVisibility(View.GONE);
        }
        holder.tvDate.setText(georgianDateToPersianDate(billDates.get(position)));
        if(position > 0 && billDates.get(position).trim().equals(billDates.get(position-1).trim())){
            holder.llDate.setVisibility(View.GONE);
        }
        holder.setIsRecyclable(false);
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCollapsed = holder.llExtra.getVisibility() == View.GONE;
                if (isCollapsed) {
                    selectItem(holder,position);
                    if(selectedHolder != holder){
                        if(selectedHolder != null)
                            deSelectItem(selectedHolder,position);
                        selectedHolder = holder;
                    }
                } else {
                    deSelectItem(holder, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return billMablaghs.size();
    }
}
