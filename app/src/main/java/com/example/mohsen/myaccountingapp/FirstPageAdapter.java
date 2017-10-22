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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class FirstPageAdapter extends RecyclerView.Adapter<FirstPageAdapter.ViewHolder> {

    Context context;
    View v;
    List<String> mainAccounts, mainBanks, mainMablaghs, mainCheckNumbers,mainExps;

    public FirstPageAdapter(List<String> mainAccounts, List<String> mainBanks, List<String> mainMablaghs, List<String> mainCheckNumbers, List<String> mainExps) {
        this.mainAccounts = mainAccounts;
        this.mainBanks = mainBanks;
        this.mainCheckNumbers = mainCheckNumbers;
        this.mainExps = mainExps;
        this.mainMablaghs = mainMablaghs;
    }

    @Override
    public FirstPageAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        context = parent.getContext();
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMablagh,tvExp,tvAccount,tvCheckNumber,tvBank;

        public ViewHolder(View v) {
            super(v);
            tvMablagh = (TextView)v.findViewById(R.id.textView_first_mablagh);
            tvAccount = (TextView)v.findViewById(R.id.textView_first_account);
            tvBank = (TextView)v.findViewById(R.id.textView_first_bank);
            tvCheckNumber = (TextView)v.findViewById(R.id.textView_first_check_number);
            tvExp = (TextView)v.findViewById(R.id.textView_first_exp);
        }
    }

    @Override
    public void onBindViewHolder(final FirstPageAdapter.ViewHolder holder, final int position) {
        holder.tvCheckNumber.setText(mainCheckNumbers.get(position));
        holder.tvExp.setText(mainExps.get(position));
        holder.tvBank.setText(mainBanks.get(position));
        holder.tvAccount.setText(mainAccounts.get(position));
        holder.tvMablagh.setText(mainMablaghs.get(position));
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return mainAccounts.size();
    }
}
