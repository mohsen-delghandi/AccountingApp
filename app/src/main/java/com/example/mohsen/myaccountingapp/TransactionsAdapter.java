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

import java.util.List;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    Context mContext;
    View v;
    List<String> mTranactionMablaghKols, mTranactionAccounts, mTranactionsExps;
    List<String> mTranactionBanks, mTranactionCheckNumbers, mTranactionsModes,mTransactionDeleteID;
    boolean isCollapsed;
    ViewHolder selectedHolder;
    LayoutInflater mInflaterInclude;
    LinearLayout mLlAddLayer;
    FloatingActionButton mFab;
    String mType;


    public TransactionsAdapter(Context context, List<String> transactionsMablaghKols, List<String> transactionsAccounts, List<String> transactionExps, List<String> transactionBanks, List<String> transactionCheckNumbers, List<String> transactionModes, LinearLayout llAddLayer, FloatingActionButton fab, String type, List<String> transactionDeleteID) {
        mContext = context;
        mTranactionMablaghKols = transactionsMablaghKols;
        mTranactionAccounts = transactionsAccounts;
        mTranactionsExps = transactionExps;
        mTranactionBanks = transactionBanks;
        mTranactionsModes = transactionModes;
        mTranactionCheckNumbers = transactionCheckNumbers;
        mLlAddLayer = llAddLayer;
        mFab = fab;
        mType = type;
        mTransactionDeleteID = transactionDeleteID;
    }

    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llExtra;
        LinearLayout llMain;
        LinearLayout llName,llChecki;
        TextView tvName, tvMablagh,tvExp,tvBankName, tvCheckNumber, tvType, tvRial,tvBankText,tvCheckNumberText,tvExpText,tvDelete,tvEdit;
        ImageView ivLabel,ivArrow;

        public ViewHolder(View v) {
            super(v);
            llExtra = (LinearLayout) v.findViewById(R.id.linearLayout_transaction_extra);
            llMain = (LinearLayout) v.findViewById(R.id.linearLayout_transaction_main);
            llName = (LinearLayout) v.findViewById(R.id.linearLayout_transaction_mablagh);
            llChecki = (LinearLayout) v.findViewById(R.id.linearLayout_transaction_checki);

            tvName = (TextView) v.findViewById(R.id.textView_transaction_account);
            tvMablagh = (TextView) v.findViewById(R.id.textView_transaction_mablagh);
            tvExp = (TextView) v.findViewById(R.id.textView_transaction_exp);
            tvBankName = (TextView) v.findViewById(R.id.textView_transaction_bank_name);
            tvCheckNumber = (TextView) v.findViewById(R.id.textView_transaction_check_number);
            tvType = (TextView) v.findViewById(R.id.textView_transaction_type_text);
            tvBankText = (TextView) v.findViewById(R.id.textView_transaction_bank_text);
            tvCheckNumberText = (TextView) v.findViewById(R.id.textView_transaction_check_number_text);
            tvExpText = (TextView) v.findViewById(R.id.textView_transaction_exp_text);
            tvRial = (TextView) v.findViewById(R.id.textView_transaction_rial);
            tvDelete = (TextView) v.findViewById(R.id.textView_transaction_delete);
            tvEdit = (TextView) v.findViewById(R.id.textView_transaction_edit);

            ivLabel = (ImageView)v.findViewById(R.id.imageView_transaction_label);
            ivArrow = (ImageView)v.findViewById(R.id.imageView_transaction_arrow);
        }
    }

    private void selectItem(ViewHolder h, int position){
        h.llExtra.setVisibility(View.VISIBLE);
        h.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_background));
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.icons));
        h.tvName.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvMablagh.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvExpText.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvExp.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvBankText.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvBankName.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvCheckNumberText.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvCheckNumber.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvType.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvRial.setTextColor(mContext.getResources().getColor(R.color.icons));

        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_up);
        if(mTranactionsModes.get(position).trim().equals("Checki")){
            h.ivLabel.setImageResource(R.drawable.cheki_selected);
        }else if(mTranactionsModes.get(position).trim().equals("Naghdi")){
            h.ivLabel.setImageResource(R.drawable.naghdi_selected);
        }
    }

    private void deSelectItem(ViewHolder h, int position){
        h.llExtra.setVisibility(View.GONE);
        h.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_underline_dashed));
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.divider));
        h.tvName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvMablagh.setTextColor(mContext.getResources().getColor(R.color.green));
        h.tvType.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvRial.setTextColor(mContext.getResources().getColor(R.color.green));

        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_down);
        if(mTranactionsModes.get(position).trim().equals("Checki")){
            h.ivLabel.setImageResource(R.drawable.cheki_normal);
        }else if(mTranactionsModes.get(position).trim().equals("Naghdi")){
            h.ivLabel.setImageResource(R.drawable.naghdi_normal);
        }
    }

    @Override
    public void onBindViewHolder(final TransactionsAdapter.ViewHolder holder, final int position) {
        holder.tvName.setText(mTranactionAccounts.get(position));
        holder.tvMablagh.setText(MainActivity.priceFormatter(mTranactionMablaghKols.get(position)));
        holder.tvExp.setText(mTranactionsExps.get(position));
        holder.tvBankName.setText(mTranactionBanks.get(position));
        holder.tvCheckNumber.setText(mTranactionCheckNumbers.get(position));
        if(mTranactionsModes.get(position).toString().trim().equals("Naghdi")){
            holder.llChecki.setVisibility(View.GONE);
        }
        if(mTranactionsModes.get(position).toString().trim().equals("Checki")){
            holder.ivLabel.setImageResource(R.drawable.cheki_normal);
        }
        if(mType.toString().trim().equals("Pardakht")){
            holder.tvType.setText("پرداخت به : ");
        }else if(mType.toString().trim().equals("Daryaft")){
            holder.tvType.setText("دریافت از : ");
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

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTranactionsModes.get(position).toString().trim().equals("Naghdi")) {
                    SQLiteDatabase dbb = new MyDatabase(mContext).getWritableDatabase();
                    dbb.delete("tblChildNaghdi", "PNaghdi_ID = ?", new String[]{mTransactionDeleteID.get(position) + ""});
                    dbb.delete("tblPardakhtNaghdi", "PNaghdi_ID = ?", new String[]{mTransactionDeleteID.get(position) + ""});
                    dbb.close();

                    mTranactionMablaghKols.remove(position);
                    mTranactionAccounts.remove(position);
                    mTranactionsExps.remove(position);
                    mTranactionBanks.remove(position);
                    mTranactionCheckNumbers.remove(position);
                    mTranactionsModes.remove(position);
                    mTransactionDeleteID.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mTranactionAccounts.size());
                    notifyDataSetChanged();
                }else if(mTranactionsModes.get(position).toString().trim().equals("Checki")) {
                    if(mType.toString().trim().equals("Daryaft")) {
                        SQLiteDatabase dbb = new MyDatabase(mContext).getWritableDatabase();
                        dbb.delete("tblCheckDaryaft", "CheckDaryaft_ID = ?", new String[]{mTransactionDeleteID.get(position) + ""});
                        Cursor cursor = dbb.query("tblCheckDaryaft_Child",new String[]{"CheckDaryaftParent_ID"},"CheckDaryaft_ID = " + mTransactionDeleteID.get(position),null,null,null,null);
                        if(cursor.moveToFirst()) {
                            dbb.delete("tblCheckDaryaft_Parent", "CheckDaryaftParent_ID = ?", new String[]{cursor.getString(0)});
                        }
                        dbb.delete("tblCheckDaryaft_Child", "CheckDaryaft_ID = ?", new String[]{mTransactionDeleteID.get(position) + ""});
                        dbb.close();

                        mTranactionMablaghKols.remove(position);
                        mTranactionAccounts.remove(position);
                        mTranactionsExps.remove(position);
                        mTranactionBanks.remove(position);
                        mTranactionCheckNumbers.remove(position);
                        mTranactionsModes.remove(position);
                        mTransactionDeleteID.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mTranactionAccounts.size());
                        notifyDataSetChanged();

                    }else if(mType.toString().trim().equals("Pardakht")) {
                        SQLiteDatabase dbb = new MyDatabase(mContext).getWritableDatabase();
                        Cursor cursor = dbb.query("tblCheckPardakht_Child",new String[]{"CheckPardakhtParent_ID"},"CheckPardakht_ID = " + mTransactionDeleteID.get(position),null,null,null,null);

                        dbb.delete("tblCheckPardakht_Child", "CheckPardakht_ID = ?", new String[]{mTransactionDeleteID.get(position) + ""});
                        if(cursor.moveToFirst()) {
                            dbb.delete("tblCheckPardakht_Parent", "CheckPardakhtParent_ID = ?", new String[]{cursor.getString(0)});
                        }

                        dbb.delete("tblCheckPardakht", "CheckPardakht_ID = ?", new String[]{mTransactionDeleteID.get(position) + ""});
                        dbb.close();

                        mTranactionMablaghKols.remove(position);
                        mTranactionAccounts.remove(position);
                        mTranactionsExps.remove(position);
                        mTranactionBanks.remove(position);
                        mTranactionCheckNumbers.remove(position);
                        mTranactionsModes.remove(position);
                        mTransactionDeleteID.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mTranactionAccounts.size());
                        notifyDataSetChanged();
                    }
                }
            }
        });
//
//        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mInflaterInclude = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                mFab.setVisibility(View.GONE);
//                mLlAddLayer.removeAllViews();
//                mLlAddLayer.setVisibility(View.VISIBLE);
//                mLlAddLayer.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        return true;
//                    }
//                });
//
//                final View v = mInflaterInclude.inflate(R.layout.add_product_layout,mLlAddLayer);
//                v.findViewById(R.id.textView_add_product_clean).setVisibility(View.INVISIBLE);
//                ((TextView)v.findViewById(R.id.textView_add_product_save)).setText("بروزرسانی");
//
//                SQLiteDatabase db11 = new MyDatabase(mContext).getWritableDatabase();
//                Cursor c11 = db11.query("TblKala",new String[]{"Name_Kala","Fk_VahedKalaAsli","GheymatKharidAsli","GheymatForoshAsli","MojodiAvalDore","MianginFiAvalDovre"},"ID_Kala = ?",
//                        new String[]{mProductIDs.get(position)+""},null,null,null);
//                c11.moveToFirst();
//                ((EditText)v.findViewById(R.id.editText_add_product_name)).setText(c11.getString(0));
//                ((EditText)v.findViewById(R.id.editText_add_product_buy_price)).setText(c11.getString(2));
//                ((EditText)v.findViewById(R.id.editText_add_product_sell_price)).setText(c11.getString(3));
//                ((EditText)v.findViewById(R.id.editText_add_product_mojoodi)).setText(c11.getString(4));
//                ((EditText)v.findViewById(R.id.editText_add_product_average_price)).setText(c11.getString(5));
//
//                SQLiteDatabase db2 = new MyDatabase(mContext).getReadableDatabase();
//                Cursor c = db2.query("TblVahedKalaAsli",new String[]{"NameVahed","ID_Vahed"},null,null,null,null,null,null);
//                String[] groupNames = null;
//                int[] groupIDs = null;
//                if(c.moveToFirst()){
//                    groupNames = new String[c.getCount()];
//                    groupIDs = new int[c.getCount()];
//                    int i = 0;
//                    do{
//                        groupNames[i] = c.getString(0);
//                        groupIDs[i] = c.getInt(1);
//                        i++;
//                    }while (c.moveToNext());
//                }
//                db2.close();
//                final int[] groupVahedID = new int[1];
//
//                final ArrayAdapter adapter = new ArrayAdapter(mContext,R.layout.simple_spinner_item,groupNames);
//                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//                final int[] finalGroupIDs = groupIDs;
//                ((Spinner)v.findViewById(R.id.spinner_add_product_units_list)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        groupVahedID[0] = finalGroupIDs[i];
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//                        groupVahedID[0] = finalGroupIDs[0];
//                    }
//                });
//                ((Spinner)v.findViewById(R.id.spinner_add_product_units_list)).setAdapter(adapter);
//                int vahedGroupPosition = 0;
//                for(int i = 0 ; i < groupIDs.length ; i++){
//                    if(c11.getInt(1) == groupIDs[i]) {
//                        vahedGroupPosition = i;
//                        i = groupIDs.length;
//                    }
//                }
//                ((Spinner)v.findViewById(R.id.spinner_add_product_units_list)).setSelection(vahedGroupPosition);
//
//                ((TextView)v.findViewById(R.id.textView_add_product_name)).setVisibility(View.VISIBLE);
//                ((TextView)v.findViewById(R.id.textView_add_product_buy_price)).setVisibility(View.VISIBLE);
//                ((TextView)v.findViewById(R.id.textView_add_product_sell_price)).setVisibility(View.VISIBLE);
//                ((TextView)v.findViewById(R.id.textView_add_product_mojoodi)).setVisibility(View.VISIBLE);
//                ((TextView)v.findViewById(R.id.textView_add_product_average_price)).setVisibility(View.VISIBLE);
//
//                ((EditText)v.findViewById(R.id.editText_add_product_name)).setHint("");
//                ((EditText)v.findViewById(R.id.editText_add_product_buy_price)).setHint("");
//                ((EditText)v.findViewById(R.id.editText_add_product_sell_price)).setHint("");
//                ((EditText)v.findViewById(R.id.editText_add_product_mojoodi)).setHint("");
//                ((EditText)v.findViewById(R.id.editText_add_product_average_price)).setHint("");
//
//                ((TextView)v.findViewById(R.id.textView_add_product_close)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mLlAddLayer.setVisibility(View.GONE);
//                        mFab.setVisibility(View.VISIBLE);
//                    }
//                });
//
//                ((TextView)v.findViewById(R.id.textView_add_product_save)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        SQLiteDatabase db = new MyDatabase(mContext).getWritableDatabase();
//                        ContentValues cv2 = new ContentValues();
//                        cv2.put("Name_Kala",((EditText)v.findViewById(R.id.editText_add_product_name)).getText().toString().trim());
//                        cv2.put("GheymatKharidAsli",((EditText)v.findViewById(R.id.editText_add_product_buy_price)).getText().toString().trim());
//                        cv2.put("GheymatForoshAsli",((EditText)v.findViewById(R.id.editText_add_product_sell_price)).getText().toString().trim());
//                        cv2.put("MojodiAvalDore",((EditText)v.findViewById(R.id.editText_add_product_mojoodi)).getText().toString().trim());
//                        cv2.put("MianginFiAvalDovre",((EditText)v.findViewById(R.id.editText_add_product_average_price)).getText().toString().trim());
//                        cv2.put("Fk_VahedKalaAsli",groupVahedID[0]);
//                        db.update("TblKala",cv2,"ID_Kala = ?",new String[]{mProductIDs.get(position)+""});
//
//
//                        mProductName.set(position,((EditText)v.findViewById(R.id.editText_add_product_name)).getText().toString().trim());
//                        mProductBuyPrice.set(position,((EditText)v.findViewById(R.id.editText_add_product_buy_price)).getText().toString().trim());
//                        mProductSellPrice.set(position,((EditText)v.findViewById(R.id.editText_add_product_sell_price)).getText().toString().trim());
//                        Cursor c6 = db.query("TblVahedKalaAsli",new String[]{"NameVahed"},"ID_Vahed = ?",new String[]{groupVahedID[0]+""},null,null,null,null);
//                        c6.moveToFirst();
//                        mProductUnit.set(position,c6.getString(0));
//                        mProductMojoodi.set(position,((EditText)v.findViewById(R.id.editText_add_product_mojoodi)).getText().toString().trim());
//
//                        c6.close();
//                        db.close();
//
//                        notifyDataSetChanged();
//
//                        Toast.makeText(mContext, "با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
//                        mLlAddLayer.setVisibility(View.GONE);
//                        mFab.setVisibility(View.VISIBLE);
//                    }
//                });
//
//                c11.close();
//                db11.close();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mTranactionAccounts.size();
    }
}
