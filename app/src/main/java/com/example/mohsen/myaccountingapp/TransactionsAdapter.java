package com.example.mohsen.myaccountingapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.EditTextPreference;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

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

    List<Integer> accountTafziliIDs;


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
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.shiri));
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

                final View v = mInflaterInclude.inflate(R.layout.add_transaction_layout,mLlAddLayer);
                v.findViewById(R.id.textView_add_transaction_clean).setVisibility(View.GONE);
                v.findViewById(R.id.linearLayout_add_transaction_select).setVisibility(View.GONE);
                v.findViewById(R.id.linearLayout_add_transaction_date).setVisibility(View.GONE);
                ((TextView)v.findViewById(R.id.textView_add_transaction_save)).setText("بروزرسانی");

                final AutoCompleteTextView atvAccounts = (AutoCompleteTextView)v.findViewById(R.id.autoTextView_add_transaction_account);
                final EditText etExp = ((EditText)v.findViewById(R.id.editText_add_transaction_exp));
                final EditText etMablagh = ((EditText)v.findViewById(R.id.editText_add_transaction_mablagh));

                SQLiteDatabase dbAccounts = new MyDatabase(mContext).getReadableDatabase();
                Cursor cursorAccounts = dbAccounts.query("tblContacts",new String[]{"FullName"},null,null,null,null,null,null);
                List<String> listAccounts = new ArrayList<String>();
                if(cursorAccounts.moveToFirst()){
                    do{
                        listAccounts.add(cursorAccounts.getString(0));
                    }while (cursorAccounts.moveToNext());
                }
                cursorAccounts.close();

                ArrayAdapter<String> adapterAccounts = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,listAccounts);
                atvAccounts.setAdapter(adapterAccounts);
                atvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SQLiteDatabase dbShowAccount = new MyDatabase(mContext).getReadableDatabase();
                        Cursor cursorShowAccount = dbShowAccount.query("tblContacts",new String[]{"Tafzili_ID"},"FullName = ?",new String[]{atvAccounts.getText().toString().trim()+""},null,null,null);
                        accountTafziliIDs = new ArrayList<Integer>();
                        if(cursorShowAccount.moveToFirst()){
                            accountTafziliIDs.add(cursorShowAccount.getInt(0));
                        }
                        cursorShowAccount.close();

                        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(atvAccounts.getWindowToken(), 0);
                    }
                });

                if(mTranactionsModes.get(position).trim().equals("Naghdi")){
                    SQLiteDatabase dbReadTransactions = new MyDatabase(mContext).getWritableDatabase();
                    Cursor cursorReadTransactions = dbReadTransactions.query("tblPardakhtNaghdi",new String[]{"SumMabalgh","PNaghdiExp"},"PNaghdi_ID = ?",
                            new String[]{mTransactionDeleteID.get(position)+""},null,null,null);
                    cursorReadTransactions.moveToFirst();

                    ((TextView)v.findViewById(R.id.textView_add_transaction_mablagh)).setVisibility(View.VISIBLE);
                    etMablagh.setText(cursorReadTransactions.getString(0));

                    etExp.setText(cursorReadTransactions.getString(1));

                    SQLiteDatabase dbReadTafzili = new MyDatabase(mContext).getWritableDatabase();
                    Cursor cursorReadTafzili = dbReadTafzili.query("tblChildNaghdi",new String[]{"Tafzili_ID"},"PNaghdi_ID = ?",
                            new String[]{mTransactionDeleteID.get(position)+""},null,null,null);
                    cursorReadTafzili.moveToFirst();

                    accountTafziliIDs = new ArrayList<Integer>();
                    accountTafziliIDs.add(cursorReadTafzili.getInt(0));
                    SQLiteDatabase dbAccount = new MyDatabase(mContext).getReadableDatabase();
                    Cursor cursorAccount = dbAccount.query("tblContacts",new String[]{"FullName"},"Tafzili_ID = " + cursorReadTafzili.getString(0),null,null,null,null,null);
                    ((TextView)v.findViewById(R.id.textView_add_transaction_account)).setVisibility(View.VISIBLE);
                    if(cursorAccount.moveToFirst()) {
                        atvAccounts.setText(cursorAccount.getString(0));
                    }
                }
//                else if(mTranactionsModes.get(position).trim().equals("Checki")){
//                    SQLiteDatabase dbReadTransactions = new MyDatabase(mContext).getWritableDatabase();
//                    Cursor cursorReadTransactions = dbReadTransactions.query("tblPardakhtNaghdi",new String[]{"SumMabalgh","PNaghdiExp"},"PNaghdi_ID = ?",
//                            new String[]{mTransactionDeleteID.get(position)+""},null,null,null);
//                    cursorReadTransactions.moveToFirst();
//
//                    ((TextView)v.findViewById(R.id.textView_add_transaction_mablagh)).setVisibility(View.VISIBLE);
//                    etMablagh.setText(cursorReadTransactions.getString(0));
//
//                    etExp.setText(cursorReadTransactions.getString(1));
//
//                    SQLiteDatabase dbReadTafzili = new MyDatabase(mContext).getWritableDatabase();
//                    Cursor cursorReadTafzili = dbReadTafzili.query("tblChildNaghdi",new String[]{"Tafzili_ID"},"PNaghdi_ID = ?",
//                            new String[]{mTransactionDeleteID.get(position)+""},null,null,null);
//                    cursorReadTafzili.moveToFirst();
//
//                    accountTafziliIDs = new ArrayList<Integer>();
//                    accountTafziliIDs.add(cursorReadTafzili.getInt(0));
//                    SQLiteDatabase dbAccount = new MyDatabase(mContext).getReadableDatabase();
//                    Cursor cursorAccount = dbAccount.query("tblContacts",new String[]{"FullName"},"Tafzili_ID = " + cursorReadTafzili.getString(0),null,null,null,null,null);
//                    ((TextView)v.findViewById(R.id.textView_add_transaction_account)).setVisibility(View.VISIBLE);
//                    if(cursorAccount.moveToFirst()) {
//                        atvAccounts.setText(cursorAccount.getString(0));
//                    }
//                }

                TextView tvUpdate = (TextView)v.findViewById(R.id.textView_add_transaction_save);
                tvUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(atvAccounts.getText().toString().trim().equals("")){
                            Toast.makeText(mContext, "لطفا طرف حساب را مشخص کنید.", Toast.LENGTH_SHORT).show();
                        }else {
                            if (mTranactionsModes.get(position).trim().equals("Naghdi")) {
                                SQLiteDatabase dbUpdate = new MyDatabase(mContext).getWritableDatabase();
                                ContentValues cvUpdate = new ContentValues();
                                cvUpdate.put("PNaghdiExp", etExp.getText().toString().trim());
                                cvUpdate.put("SumMabalgh", etMablagh.getText().toString().trim());
                                dbUpdate.update("tblPardakhtNaghdi",cvUpdate,"PNaghdi_ID = ?",new String[]{mTransactionDeleteID.get(position)});
                                ContentValues cvUpdate2 = new ContentValues();
                                cvUpdate2.put("Tafzili_ID", accountTafziliIDs.get(0) + "");
                                cvUpdate2.put("ChMablagh_Naghdi", etMablagh.getText().toString().trim());
                                dbUpdate.update("tblChildNaghdi", cvUpdate2, "PNaghdi_ID = ?",new String[]{mTransactionDeleteID.get(position)});
                                Toast.makeText(mContext, "به روز رسانی انجام شد.", Toast.LENGTH_SHORT).show();

                                mTranactionMablaghKols.set(position,etMablagh.getText().toString().trim());
                                Cursor cursorAccountName = dbUpdate.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + accountTafziliIDs.get(0), null, null, null,null,null);
                                cursorAccountName.moveToFirst();
                                mTranactionAccounts.set(position,cursorAccountName.getString(0)+"");
                                mTranactionsExps.set(position,etExp.getText().toString().trim());

                                notifyItemChanged(position);
                                notifyDataSetChanged();

                            }
//                            else if (mode.toString().trim().equals("Checki")) {
//                                if (type.toString().trim().equals("Daryaft")) {
//                                    SQLiteDatabase dbInsert = new MyDatabase(TransactionActivity.this).getWritableDatabase();
//                                    ContentValues cvInsert = new ContentValues();
//                                    cvInsert.put("Moein_ID", "13004");
//                                    cvInsert.put("AccountsID", "130");
//                                    cvInsert.put("Tafzili_ID", accountTafziliIDs.get(0).toString());
//                                    cvInsert.put("Date_Sabt", currentDate);
//                                    cvInsert.put("Tozih_DaryaftCheck", etExp.getText().toString().trim());
//                                    Cursor cursorMaxSrialSand = dbInsert.query("tblParentSanad", new String[]{"MAX(Serial_Sanad)"}, null, null, null, null, null);
//                                    if (cursorMaxSrialSand.moveToFirst()) {
//                                        cvInsert.put("SerialSanad", cursorMaxSrialSand.getString(0));
//                                    } else {
//                                        cvInsert.put("SerialSanad", "1");
//                                    }
//                                    long idParent = dbInsert.insert("tblCheckDaryaft_Parent", null, cvInsert);
//
//                                    ContentValues cvInsert2 = new ContentValues();
//                                    cvInsert2.put("StatusDaryaft_ID", "1");
//                                    cvInsert2.put("CheckDaryaft_Number", etCheckNumber.getText().toString().trim());
//                                    cvInsert2.put("CheckDaryaft_DateSarResid", checkDate);
//                                    cvInsert2.put("CheckDaryaft_Mablagh", etMablagh.getText().toString().trim());
//                                    cvInsert2.put("CheckDaryaft_Exp", etExp.getText().toString().trim());
//                                    cvInsert2.put("CheckDaryaft_BankID", bankID[0]);
//                                    long idCheck = dbInsert.insert("tblCheckDaryaft", null, cvInsert2);
//
//                                    ContentValues cvInsert3 = new ContentValues();
//                                    cvInsert3.put("Tafzili_ID", accountTafziliIDs.get(0).toString());
//                                    cvInsert3.put("AccountsID", "130");
//                                    cvInsert3.put("CheckDaryaft_ID", idCheck + "");
//                                    cvInsert3.put("Moein_ID", "13004");
//                                    cvInsert3.put("CheckDaryaftParent_ID", idParent + "");
//                                    cvInsert3.put("CheckDaryaft_DateDaryaft", checkDate);
//                                    cvInsert3.put("CheckDaryaftChild_Tozih", etExp.getText().toString().trim());
//                                    long idChild = dbInsert.insert("tblCheckDaryaft_Child", null, cvInsert3);
//                                    Toast.makeText(TransactionActivity.this, "ذخیره با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
//
//                                    transactionsMablaghKols.add(etMablagh.getText().toString().trim());
//                                    Cursor cursorAccountName = dbInsert.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + accountTafziliIDs.get(0), null, null, null,null,null);
//                                    cursorAccountName.moveToFirst();
//                                    transactionsAccounts.add(cursorAccountName.getString(0)+"");
//                                    transactionExps.add(etExp.getText().toString().trim());
//                                    transactionBanks.add(bankName[0]);
//                                    transactionCheckNumbers.add(etCheckNumber.getText().toString().trim());
//                                    transactionModes.add("Checki");
//                                    transactionDeleteID.add(idCheck+"");
//
//                                    recyclerAdapter.notifyDataSetChanged();
//                                } else if (type.toString().trim().equals("Pardakht")) {
//                                    SQLiteDatabase dbInsert = new MyDatabase(TransactionActivity.this).getWritableDatabase();
//                                    ContentValues cvInsert = new ContentValues();
//                                    cvInsert.put("Moein_ID", "31001");
//                                    cvInsert.put("AccountsID", "310");
//                                    cvInsert.put("Tafzili_ID", bankID[0]);
//                                    cvInsert.put("Date_Sabt", currentDate);
//                                    cvInsert.put("Tozih_PardakhtCheck", etExp.getText().toString().trim());
//                                    Cursor cursorMaxSrialSand = dbInsert.query("tblParentSanad", new String[]{"MAX(Serial_Sanad)"}, null, null, null, null, null);
//                                    if (cursorMaxSrialSand.moveToFirst()) {
//                                        cvInsert.put("SerialSanad", cursorMaxSrialSand.getString(0));
//                                    } else {
//                                        cvInsert.put("SerialSanad", "1");
//                                    }
//                                    long idParent = dbInsert.insert("tblCheckPardakht_Parent", null, cvInsert);
//
//                                    ContentValues cvInsert2 = new ContentValues();
//                                    cvInsert2.put("StatusPardakht_ID", "1003");
//                                    cvInsert2.put("CheckPardakht_Number", etCheckNumber.getText().toString().trim());
//                                    cvInsert2.put("CheckPardakht_DateSarResid", checkDate);
//                                    cvInsert2.put("CheckPardakht_Mablagh", etMablagh.getText().toString().trim());
//                                    cvInsert2.put("CheckPardakht_Exp", etExp.getText().toString().trim());
//                                    long idCheck = dbInsert.insert("tblCheckPardakht", null, cvInsert2);
//
//                                    ContentValues cvInsert3 = new ContentValues();
//                                    cvInsert3.put("Tafzili_ID", accountTafziliIDs.get(0).toString());
//                                    cvInsert3.put("AccountsID", "310");
//                                    cvInsert3.put("CheckPardakht_ID", idCheck + "");
//                                    cvInsert3.put("Moein_ID", "31001");
//                                    cvInsert3.put("CheckPardakhtParent_ID", idParent + "");
//                                    cvInsert3.put("Date_Pardakht", checkDate);
//                                    cvInsert3.put("CheckChildPadakht_Tozih", etExp.getText().toString().trim());
//                                    long idChild = dbInsert.insert("tblCheckPardakht_Child", null, cvInsert3);
//                                    Toast.makeText(TransactionActivity.this, "ذخیره با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
//
//                                    transactionsMablaghKols.add(etMablagh.getText().toString().trim());
//                                    Cursor cursorAccountName = dbInsert.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + accountTafziliIDs.get(0), null, null, null,null,null);
//                                    cursorAccountName.moveToFirst();
//                                    transactionsAccounts.add(cursorAccountName.getString(0)+"");
//                                    transactionExps.add(etExp.getText().toString().trim());
//                                    transactionBanks.add(bankName[0]);
//                                    transactionCheckNumbers.add(etCheckNumber.getText().toString().trim());
//                                    transactionModes.add("Checki");
//                                    transactionDeleteID.add(idCheck+"");
//
//                                    recyclerAdapter.notifyDataSetChanged();
//                                }
//                            }


                            mFab.setVisibility(View.VISIBLE);
                            mLlAddLayer.removeAllViews();
                            mLlAddLayer.setVisibility(View.GONE);

                        }
                    }
                });

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
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTranactionAccounts.size();
    }
}
