package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

    static Context mContext;
    View v;
    List<String> mAccountFullName;

    public AccountsAdapter(Context context, List<String> accountFullName) {
        mContext = context;
        mAccountFullName = accountFullName;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.textView_acount_side_fullName);
        }
    }

    @Override
    public AccountsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_side_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(AccountsAdapter.ViewHolder holder, final int position) {
        holder.tv.setText(mAccountFullName.get(position));
        holder.setIsRecyclable(false);

//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Intent intent = new Intent(mContext, mClasses.get(position));
//                    mContext.startActivity(intent);
//                }catch (Exception e){
//                    Toast.makeText(mContext, "ERROR.", Toast.LENGTH_SHORT).show();
//                }
//                mDrawer.closeDrawer(Gravity.START);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return mAccountFullName.size();
    }
}
