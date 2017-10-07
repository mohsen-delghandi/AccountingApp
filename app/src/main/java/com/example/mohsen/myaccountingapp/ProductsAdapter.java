package com.example.mohsen.myaccountingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
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
    ViewHolder selectedHolder;
    LayoutInflater mInflaterInclude;
    LinearLayout mLlAddLayer;
    FloatingActionButton mFab;


    public ProductsAdapter(Context context, List<String> productName, List<String> productBuyPrice, List<String> productSellPrice, List<String> productUnit, List<String> productMojoodi, List<Integer> productIDs, LinearLayout llAddLayer, FloatingActionButton fab) {
        mContext = context;
        mProductName = productName;
        mProductBuyPrice = productBuyPrice;
        mProductSellPrice = productSellPrice;
        mProductUnit = productUnit;
        mProductMojoodi = productMojoodi;
        mProductIDs = productIDs;
        mLlAddLayer = llAddLayer;
        mFab = fab;
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

    private void selectItem(ProductsAdapter.ViewHolder h){
        h.llExtra.setVisibility(View.VISIBLE);
        h.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_background));
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.icons));
        h.tvName.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvMojodi.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvUnit.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvSellText.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvSellPrice.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvRial.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_up);
    }

    private void deSelectItem(ProductsAdapter.ViewHolder h){
        h.llExtra.setVisibility(View.GONE);
        h.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_underline_dashed));
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.divider));
        h.tvName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvMojodi.setTextColor(mContext.getResources().getColor(R.color.secondary_text));
        h.tvUnit.setTextColor(mContext.getResources().getColor(R.color.secondary_text));
        h.tvSellText.setTextColor(mContext.getResources().getColor(R.color.secondary_text));
        h.tvSellPrice.setTextColor(mContext.getResources().getColor(R.color.green));
        h.tvRial.setTextColor(mContext.getResources().getColor(R.color.green));
        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_down_old);
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.ViewHolder holder, final int position) {
        holder.tvName.setText(mProductName.get(position));
        holder.tvMojodi.setText(mProductMojoodi.get(position));
        holder.tvUnit.setText(mProductUnit.get(position));
        holder.tvBuyPrice.setText(mProductBuyPrice.get(position));
        holder.tvSellPrice.setText(mProductSellPrice.get(position));
        holder.setIsRecyclable(false);
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCollapsed = holder.llExtra.getVisibility() == View.GONE;
                if (isCollapsed) {
                    selectItem(holder);
                    if(selectedHolder != holder){
                        if(selectedHolder != null)
                            deSelectItem(selectedHolder);
                        selectedHolder = holder;
                    }
                } else {
                    deSelectItem(holder);
                }
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase dbb = new MyDatabase(mContext).getWritableDatabase();
                dbb.execSQL("DELETE FROM TblKala WHERE ID_Kala = " + mProductIDs.get(position));
                dbb.close();

                mProductIDs.remove(position);
                mProductName.remove(position);
                mProductBuyPrice.remove(position);
                mProductSellPrice.remove(position);
                mProductUnit.remove(position);
                mProductMojoodi.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mProductName.size());
                notifyDataSetChanged();
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
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

                final View v = mInflaterInclude.inflate(R.layout.add_product_layout,mLlAddLayer);
                v.findViewById(R.id.textView_add_product_clean).setVisibility(View.INVISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_product_save)).setText("بروزرسانی");

                SQLiteDatabase db11 = new MyDatabase(mContext).getWritableDatabase();
                Cursor c11 = db11.query("TblKala",new String[]{"Name_Kala","Fk_VahedKalaAsli","GheymatKharidAsli","GheymatForoshAsli","MojodiAvalDore","MianginFiAvalDovre"},"ID_Kala = ?",
                        new String[]{mProductIDs.get(position)+""},null,null,null);
                c11.moveToFirst();
                ((EditText)v.findViewById(R.id.editText_add_product_name)).setText(c11.getString(0));
                ((EditText)v.findViewById(R.id.editText_add_product_buy_price)).setText(c11.getString(2));
                ((EditText)v.findViewById(R.id.editText_add_product_sell_price)).setText(c11.getString(3));
                ((EditText)v.findViewById(R.id.editText_add_product_mojoodi)).setText(c11.getString(4));
                ((EditText)v.findViewById(R.id.editText_add_product_average_price)).setText(c11.getString(5));

                SQLiteDatabase db2 = new MyDatabase(mContext).getReadableDatabase();
                Cursor c = db2.query("TblVahedKalaAsli",new String[]{"NameVahed","ID_Vahed"},null,null,null,null,null,null);
                String[] groupNames = null;
                int[] groupIDs = null;
                if(c.moveToFirst()){
                    groupNames = new String[c.getCount()];
                    groupIDs = new int[c.getCount()];
                    int i = 0;
                    do{
                        groupNames[i] = c.getString(0);
                        groupIDs[i] = c.getInt(1);
                        i++;
                    }while (c.moveToNext());
                }
                db2.close();
                final int[] groupVahedID = new int[1];

                final ArrayAdapter adapter = new ArrayAdapter(mContext,R.layout.simple_spinner_item,groupNames);
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                final int[] finalGroupIDs = groupIDs;
                ((Spinner)v.findViewById(R.id.spinner_add_product_units_list)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        groupVahedID[0] = finalGroupIDs[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        groupVahedID[0] = finalGroupIDs[0];
                    }
                });
                ((Spinner)v.findViewById(R.id.spinner_add_product_units_list)).setAdapter(adapter);
                int vahedGroupPosition = 0;
                for(int i = 0 ; i < groupIDs.length ; i++){
                    if(c11.getInt(1) == groupIDs[i]) {
                        vahedGroupPosition = i;
                        i = groupIDs.length;
                    }
                }
                ((Spinner)v.findViewById(R.id.spinner_add_product_units_list)).setSelection(vahedGroupPosition);

                ((TextView)v.findViewById(R.id.textView_add_product_name)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_product_buy_price)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_product_sell_price)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_product_mojoodi)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.textView_add_product_average_price)).setVisibility(View.VISIBLE);

                ((EditText)v.findViewById(R.id.editText_add_product_name)).setHint("");
                ((EditText)v.findViewById(R.id.editText_add_product_buy_price)).setHint("");
                ((EditText)v.findViewById(R.id.editText_add_product_sell_price)).setHint("");
                ((EditText)v.findViewById(R.id.editText_add_product_mojoodi)).setHint("");
                ((EditText)v.findViewById(R.id.editText_add_product_average_price)).setHint("");

                ((TextView)v.findViewById(R.id.textView_add_product_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLlAddLayer.setVisibility(View.GONE);
                        mFab.setVisibility(View.VISIBLE);
                    }
                });

                ((TextView)v.findViewById(R.id.textView_add_product_save)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SQLiteDatabase db = new MyDatabase(mContext).getWritableDatabase();
                        ContentValues cv2 = new ContentValues();
                        cv2.put("Name_Kala",((EditText)v.findViewById(R.id.editText_add_product_name)).getText().toString().trim());
                        cv2.put("GheymatKharidAsli",((EditText)v.findViewById(R.id.editText_add_product_buy_price)).getText().toString().trim());
                        cv2.put("GheymatForoshAsli",((EditText)v.findViewById(R.id.editText_add_product_sell_price)).getText().toString().trim());
                        cv2.put("MojodiAvalDore",((EditText)v.findViewById(R.id.editText_add_product_mojoodi)).getText().toString().trim());
                        cv2.put("MianginFiAvalDovre",((EditText)v.findViewById(R.id.editText_add_product_average_price)).getText().toString().trim());
                        cv2.put("Fk_VahedKalaAsli",groupVahedID[0]);
                        db.update("TblKala",cv2,"ID_Kala = ?",new String[]{mProductIDs.get(position)+""});


                        mProductName.set(position,((EditText)v.findViewById(R.id.editText_add_product_name)).getText().toString().trim());
                        mProductBuyPrice.set(position,((EditText)v.findViewById(R.id.editText_add_product_buy_price)).getText().toString().trim());
                        mProductSellPrice.set(position,((EditText)v.findViewById(R.id.editText_add_product_sell_price)).getText().toString().trim());
                        Cursor c6 = db.query("TblVahedKalaAsli",new String[]{"NameVahed"},"ID_Vahed = ?",new String[]{groupVahedID[0]+""},null,null,null,null);
                        c6.moveToFirst();
                        mProductUnit.set(position,c6.getString(0));
                        mProductMojoodi.set(position,((EditText)v.findViewById(R.id.editText_add_product_mojoodi)).getText().toString().trim());

                        c6.close();
                        db.close();

                        notifyDataSetChanged();

                        Toast.makeText(mContext, "با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
                        mLlAddLayer.setVisibility(View.GONE);
                        mFab.setVisibility(View.VISIBLE);
                    }
                });

                c11.close();
                db11.close();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProductName.size();
    }
}
