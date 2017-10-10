package com.example.mohsen.myaccountingapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    static Context mContext;
    int mWidth;
    View v;
    static DrawerLayout mDrawer;
    String[] mNavigationMenuItemNames;
    ArrayList<Class> mClasses;

    public NavigationAdapter(Context context, int width,DrawerLayout drawer,String[] navigationMenuItemNames, ArrayList<Class> classes) {
        mContext = context;
        mWidth = width;
        mDrawer = drawer;
        mNavigationMenuItemNames = navigationMenuItemNames;
        mClasses = classes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.nav_textView);
        }
    }

    @Override
    public NavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(NavigationAdapter.ViewHolder holder, final int position) {
            holder.tv.setText(mNavigationMenuItemNames[position]);
            holder.setIsRecyclable(false);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(mClasses.get(position) != mContext.getClass()) {
                            Intent intent = new Intent(mContext, mClasses.get(position));
                            ((Activity)mContext).finish();
                            mContext.startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, "ERROR.", Toast.LENGTH_SHORT).show();
                    }
                    mDrawer.closeDrawer(Gravity.START);
                }
            });
    }


    @Override
    public int getItemCount() {
        return mNavigationMenuItemNames.length;
    }
}
