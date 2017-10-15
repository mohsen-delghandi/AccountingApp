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

    DatePersian mDate;
    String checkDate;

    List<Integer> accountTafziliIDs;

    boolean isAccountChanged;


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
                mInflaterInclude = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mFab.setVisibility(View.GONE);
                mLlAddLayer.removeAllViews();
                mLlAddLayer.setVisibility(View.VISIBLE);
                mLlAddLayer.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                isAccountChanged = false;

                final View v = mInflaterInclude.inflate(R.layout.add_transaction_layout, mLlAddLayer);
                v.findViewById(R.id.textView_add_transaction_clean).setVisibility(View.GONE);
                v.findViewById(R.id.linearLayout_add_transaction_select).setVisibility(View.GONE);
                v.findViewById(R.id.linearLayout_add_transaction_date).setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.textView_add_transaction_save)).setText("بروزرسانی");

                final LinearLayout llCheckNumber = (LinearLayout) v.findViewById(R.id.linearLayout_add_transaction_check_number);
                final LinearLayout llCheckDate = (LinearLayout) v.findViewById(R.id.linearLayout_add_transaction_check_date);
                final LinearLayout llBankName = (LinearLayout) v.findViewById(R.id.linearLayout_add_transaction_bank);
                final EditText etCheckNumber = (EditText) v.findViewById(R.id.editText_add_transaction_check_number);
                final TextView tvCheckDate = (TextView) v.findViewById(R.id.textView_add_transaction_check_date);

                final AutoCompleteTextView atvAccounts = (AutoCompleteTextView) v.findViewById(R.id.autoTextView_add_transaction_account);
                final EditText etExp = ((EditText) v.findViewById(R.id.editText_add_transaction_exp));
                final EditText etMablagh = ((EditText) v.findViewById(R.id.editText_add_transaction_mablagh));
                final Spinner spBanks = (Spinner) v.findViewById(R.id.spinner_add_transaction_banks_list);


                final int[] bankID = new int[1];
                final String[] bankName = new String[1];

                SQLiteDatabase dbAccounts = new MyDatabase(mContext).getReadableDatabase();
                Cursor cursorAccounts = dbAccounts.query("tblContacts", new String[]{"FullName"}, null, null, null, null, null, null);
                List<String> listAccounts = new ArrayList<String>();
                if (cursorAccounts.moveToFirst()) {
                    do {
                        listAccounts.add(cursorAccounts.getString(0));
                    } while (cursorAccounts.moveToNext());
                }
                cursorAccounts.close();

                ArrayAdapter<String> adapterAccounts = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, listAccounts);
                atvAccounts.setAdapter(adapterAccounts);
                atvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SQLiteDatabase dbShowAccount = new MyDatabase(mContext).getReadableDatabase();
                        Cursor cursorShowAccount = dbShowAccount.query("tblContacts", new String[]{"Tafzili_ID"}, "FullName = ?", new String[]{atvAccounts.getText().toString().trim() + ""}, null, null, null);
                        accountTafziliIDs = new ArrayList<Integer>();
                        if (cursorShowAccount.moveToFirst()) {
                            accountTafziliIDs.add(cursorShowAccount.getInt(0));
                        }
                        cursorShowAccount.close();

                        isAccountChanged = true;

                        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(atvAccounts.getWindowToken(), 0);
                    }
                });

                if (mTranactionsModes.get(position).trim().equals("Naghdi")) {
                    SQLiteDatabase dbReadTransactions = new MyDatabase(mContext).getWritableDatabase();
                    Cursor cursorReadTransactions = dbReadTransactions.query("tblPardakhtNaghdi", new String[]{"SumMabalgh", "PNaghdiExp"}, "PNaghdi_ID = ?",
                            new String[]{mTransactionDeleteID.get(position) + ""}, null, null, null);
                    cursorReadTransactions.moveToFirst();

                    ((TextView) v.findViewById(R.id.textView_add_transaction_mablagh)).setVisibility(View.VISIBLE);
                    etMablagh.setText(cursorReadTransactions.getString(0));

                    etExp.setText(cursorReadTransactions.getString(1));

                    SQLiteDatabase dbReadTafzili = new MyDatabase(mContext).getWritableDatabase();
                    Cursor cursorReadTafzili = dbReadTafzili.query("tblChildNaghdi", new String[]{"Tafzili_ID"}, "PNaghdi_ID = ?",
                            new String[]{mTransactionDeleteID.get(position) + ""}, null, null, null);
                    cursorReadTafzili.moveToFirst();

                    accountTafziliIDs = new ArrayList<Integer>();
                    accountTafziliIDs.add(cursorReadTafzili.getInt(0));
                    SQLiteDatabase dbAccount = new MyDatabase(mContext).getReadableDatabase();
                    Cursor cursorAccount = dbAccount.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + cursorReadTafzili.getString(0), null, null, null, null, null);
                    ((TextView) v.findViewById(R.id.textView_add_transaction_account)).setVisibility(View.VISIBLE);
                    if (cursorAccount.moveToFirst()) {
                        atvAccounts.setText(cursorAccount.getString(0));
                    }
                } else if (mTranactionsModes.get(position).trim().equals("Checki")) {

                    llCheckDate.setVisibility(View.VISIBLE);
                    llCheckNumber.setVisibility(View.VISIBLE);
                    llBankName.setVisibility(View.VISIBLE);

                    ((TextView) v.findViewById(R.id.textView_add_transaction_mablagh)).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.textView_add_transaction_check_number)).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.textView_add_transaction_account)).setVisibility(View.VISIBLE);

                    String selectedBankName = null;
                    String selectedCheckDate = null;

                    if (mType.trim().equals("Daryaft")) {
                        SQLiteDatabase dbCheckiDaryaft = new MyDatabase(mContext).getReadableDatabase();
                        Cursor cursorCheckiDaryaft = dbCheckiDaryaft.rawQuery("SELECT " +
                                        "tblCheckDaryaft.CheckDaryaft_Number, " +
                                        "tblCheckDaryaft.CheckDaryaft_Mablagh, " +
                                        "tblCheckDaryaft.CheckDaryaft_Exp, " +
                                        "tblContacts.FullName, " +
                                        "tblBank.NameBank, " +
                                        "tblCheckDaryaft.CheckDaryaft_DateSarResid " +
                                        "FROM tblCheckDaryaft_Child " +
                                        "INNER JOIN tblCheckDaryaft ON tblCheckDaryaft_Child.CheckDaryaft_ID = tblCheckDaryaft.CheckDaryaft_ID " +
                                        "INNER JOIN tblContacts ON tblCheckDaryaft_Child.Tafzili_ID = tblContacts.Tafzili_ID " +
                                        "INNER JOIN tblBank ON tblCheckDaryaft.CheckDaryaft_BankID = tblBank.ID_Bank " +
                                        "WHERE tblCheckDaryaft.CheckDaryaft_ID = " + mTransactionDeleteID.get(position)
                                , null);
                        if (cursorCheckiDaryaft.moveToFirst()) {
                            atvAccounts.setText(cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("FullName")));
                            etExp.setText(cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("CheckDaryaft_Exp")));
                            etCheckNumber.setText(cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("CheckDaryaft_Number")));
                            etMablagh.setText(cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("CheckDaryaft_Mablagh")));
                            selectedCheckDate = cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("CheckDaryaft_DateSarResid"));
                            selectedBankName = cursorCheckiDaryaft.getString(cursorCheckiDaryaft.getColumnIndex("NameBank"));
                        }
                        cursorCheckiDaryaft.close();
                        dbCheckiDaryaft.close();
                    }
                    else if(mType.trim().equals("Pardakht")){
                        SQLiteDatabase dbCheckiPardakht = new MyDatabase(mContext).getReadableDatabase();
                        Cursor cursorCheckiPardakht = dbCheckiPardakht.rawQuery("SELECT " +
                                "tblCheckPardakht.CheckPardakht_Number, " +
                                "tblCheckPardakht.CheckPardakht_Mablagh, " +
                                "tblCheckPardakht.CheckPardakht_Exp, " +
                                "tblContacts.FullName, " +
                                "tblTafzili.Tafzili_Name, " +
                                "tblCheckPardakht.CheckPardakht_DateSarResid " +
                                "FROM tblCheckPardakht_Parent " +
                                "INNER JOIN tblCheckPardakht_Child ON tblCheckPardakht_Child.CheckPardakhtParent_ID = tblCheckPardakht_Parent.checkPardakhtParent_ID " +
                                "INNER JOIN tblCheckPardakht ON tblCheckPardakht.CheckPardakht_ID = tblCheckPardakht_Child.CheckPardakht_ID " +
                                "INNER JOIN tblContacts ON tblContacts.Tafzili_ID = tblCheckPardakht_Child.Tafzili_ID " +
                                "INNER JOIN tblTafzili ON tblTafzili.Tafzili_ID = tblCheckPardakht_Parent.Tafzili_ID " +
                                "WHERE tblCheckPardakht.CheckPardakht_ID = " + mTransactionDeleteID.get(position)
                                ,null);
                        if (cursorCheckiPardakht.moveToFirst()) {
                            do {
                                atvAccounts.setText(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("FullName")));
                                etExp.setText(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_Exp")));
                                etCheckNumber.setText(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_Number")));
                                etMablagh.setText(cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_Mablagh")));
                                selectedCheckDate = cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("CheckPardakht_DateSarResid"));
                                selectedBankName = cursorCheckiPardakht.getString(cursorCheckiPardakht.getColumnIndex("Tafzili_Name"));
                            } while ((cursorCheckiPardakht.moveToNext()));
                        }
                        cursorCheckiPardakht.close();
                        dbCheckiPardakht.close();
                    }

                    mDate = new DatePersian();

                    mDate.setDate(Integer.parseInt(selectedCheckDate.substring(selectedCheckDate.lastIndexOf("/") + 1)),
                            Integer.parseInt(selectedCheckDate.substring(selectedCheckDate.indexOf("/") + 1, selectedCheckDate.lastIndexOf("/"))),
                            Integer.parseInt(selectedCheckDate.substring(0, selectedCheckDate.indexOf("/"))));

                    checkDate = mDate.getDate();
                    tvCheckDate.setText(dateToText(mDate));
                    tvCheckDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePicker.Builder builder = new DatePicker
                                    .Builder()
                                    .theme(R.style.DialogTheme)
                                    .minYear(1390)
                                    .future(true);
                            mDate = new DatePersian();
                            builder.date(mDate.getDay(), mDate.getMonth(), mDate.getYear());
                            builder.build(new DateSetListener() {
                                @Override
                                public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                                    mDate.setDate(day, month, year);

                                    checkDate = mDate.getDate();
                                    //textView
                                    tvCheckDate.setText(dateToText(mDate));

                                }
                            }).show(((FragmentActivity) mContext).getSupportFragmentManager(), "");
                        }
                    });

                    String[] bankNames = null;
                    int[] bankIDs = null;
                    int selectedBankIndex = 0;

                    if (mType.toString().trim().equals("Daryaft")) {
                        SQLiteDatabase dbBankList = new MyDatabase(mContext).getReadableDatabase();
                        Cursor cursorBankNames = dbBankList.query("tblBank", new String[]{"NameBank", "ID_Bank"}, null, null, null, null, null, null);
                        if (cursorBankNames.moveToFirst()) {
                            bankNames = new String[cursorBankNames.getCount()];
                            bankIDs = new int[cursorBankNames.getCount()];
                            int i = 0;
                            do {
                                if (selectedBankName.trim().equals(cursorBankNames.getString(0))) {
                                    selectedBankIndex = i;
                                }
                                bankNames[i] = cursorBankNames.getString(0);
                                bankIDs[i] = cursorBankNames.getInt(1);
                                i++;
                            } while (cursorBankNames.moveToNext());
                        }
                        cursorBankNames.close();
                        dbBankList.close();
                    }
                    else if (mType.toString().trim().equals("Pardakht")) {
                        SQLiteDatabase dbBankList = new MyDatabase(mContext).getReadableDatabase();
                        Cursor cursorBankTafzilis = dbBankList.query("tblHesabBanki", new String[]{"Tafzili_ID"}, null, null, null, null, null, null);
                        if (cursorBankTafzilis.moveToFirst()) {
                            bankNames = new String[cursorBankTafzilis.getCount()];
                            bankIDs = new int[cursorBankTafzilis.getCount()];
                            int i = 0;
                            do {
                                Cursor cursorBankName = dbBankList.query("tblTafzili", new String[]{"Tafzili_Name"}, "Tafzili_ID = ?", new String[]{cursorBankTafzilis.getString(0)}, null, null, null, null);
                                if (cursorBankName.moveToFirst()) {
                                    bankNames[i] = cursorBankName.getString(0);
                                    if (selectedBankName.trim().equals(cursorBankName.getString(0))) {
                                        selectedBankIndex = i;
                                    }
                                }
                                bankIDs[i] = cursorBankTafzilis.getInt(0);
                                i++;
                            } while (cursorBankTafzilis.moveToNext());
                        }
                        cursorBankTafzilis.close();
                        dbBankList.close();
                    }

                    bankID[0] = bankIDs[selectedBankIndex];
                    final ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.simple_spinner_item, bankNames);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    final int[] finalBankIDs = bankIDs;
                    final String[] finalBankNames = bankNames;
                    spBanks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            bankID[0] = finalBankIDs[i];
                            bankName[0] = finalBankNames[i];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            bankID[0] = finalBankIDs[0];
                            bankName[0] = finalBankNames[0];
                        }
                    });
                    spBanks.setAdapter(adapter);
                    spBanks.setSelection(selectedBankIndex);
                }

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
                                if(isAccountChanged) cvUpdate2.put("Tafzili_ID", accountTafziliIDs.get(0) + "");
                                cvUpdate2.put("ChMablagh_Naghdi", etMablagh.getText().toString().trim());
                                dbUpdate.update("tblChildNaghdi", cvUpdate2, "PNaghdi_ID = ?",new String[]{mTransactionDeleteID.get(position)});
                                Toast.makeText(mContext, "به روز رسانی انجام شد.", Toast.LENGTH_SHORT).show();

                                mTranactionMablaghKols.set(position,etMablagh.getText().toString().trim());
                                if(isAccountChanged) {
                                    Cursor cursorAccountName = dbUpdate.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + accountTafziliIDs.get(0), null, null, null, null, null);
                                    cursorAccountName.moveToFirst();
                                    mTranactionAccounts.set(position, cursorAccountName.getString(0) + "");
                                }
                                mTranactionsExps.set(position,etExp.getText().toString().trim());

                                notifyItemChanged(position);
                                notifyDataSetChanged();

                            }
                            else if (mTranactionsModes.get(position).toString().trim().equals("Checki")) {
                                if (mType.toString().trim().equals("Daryaft")) {
                                    SQLiteDatabase dbUpdate = new MyDatabase(mContext).getWritableDatabase();
                                    ContentValues cvUpdate = new ContentValues();
                                    if (isAccountChanged)
                                        cvUpdate.put("Tafzili_ID", accountTafziliIDs.get(0).toString());
                                    cvUpdate.put("Tozih_DaryaftCheck", etExp.getText().toString().trim());
                                    Cursor cursorCheckParentId = dbUpdate.query("tblCheckDaryaft_Child", new String[]{"CheckDaryaftParent_ID"}, "CheckDaryaft_ID = " + mTransactionDeleteID.get(position), null, null, null, null, null);
                                    if (cursorCheckParentId.moveToFirst()) {
                                        dbUpdate.update("tblCheckDaryaft_Parent", cvUpdate, "CheckDaryaftParent_ID = " + cursorCheckParentId.getString(0), null);
                                    }
                                    ContentValues cvUpdate2 = new ContentValues();
                                    cvUpdate2.put("CheckDaryaft_Number", etCheckNumber.getText().toString().trim());
                                    cvUpdate2.put("CheckDaryaft_DateSarResid", checkDate);
                                    cvUpdate2.put("CheckDaryaft_Mablagh", etMablagh.getText().toString().trim());
                                    cvUpdate2.put("CheckDaryaft_Exp", etExp.getText().toString().trim());
                                    cvUpdate2.put("CheckDaryaft_BankID", bankID[0]);
                                    dbUpdate.update("tblCheckDaryaft", cvUpdate2, "CheckDaryaft_ID = " + mTransactionDeleteID.get(position), null);

                                    ContentValues cvUpdate3 = new ContentValues();
                                    if (isAccountChanged)
                                        cvUpdate3.put("Tafzili_ID", accountTafziliIDs.get(0).toString());
                                    cvUpdate3.put("CheckDaryaft_DateDaryaft", checkDate);
                                    cvUpdate3.put("CheckDaryaftChild_Tozih", etExp.getText().toString().trim());
                                    dbUpdate.update("tblCheckDaryaft_Child", cvUpdate3, "CheckDaryaft_ID = " + mTransactionDeleteID.get(position), null);
                                    Toast.makeText(mContext, "به روز رسانی با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();

                                    mTranactionMablaghKols.set(position, etMablagh.getText().toString().trim());
                                    if (isAccountChanged) {
                                        Cursor cursorAccountName = dbUpdate.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + accountTafziliIDs.get(0), null, null, null, null, null);
                                        cursorAccountName.moveToFirst();
                                        mTranactionAccounts.set(position, cursorAccountName.getString(0) + "");
                                    }
                                    mTranactionsExps.set(position, etExp.getText().toString().trim());
                                    mTranactionBanks.set(position, bankName[0]);
                                    mTranactionCheckNumbers.set(position, etCheckNumber.getText().toString().trim());

                                    notifyItemChanged(position);
                                    notifyDataSetChanged();
                                }
                                else if (mType.toString().trim().equals("Pardakht")) {
                                    SQLiteDatabase dbUpdate = new MyDatabase(mContext).getWritableDatabase();
                                    ContentValues cvUpdate = new ContentValues();
                                    cvUpdate.put("Tafzili_ID", bankID[0]);
                                    cvUpdate.put("Tozih_PardakhtCheck", etExp.getText().toString().trim());
                                    Cursor cursorCheckParentId = dbUpdate.query("tblCheckPardakht_Child", new String[]{"CheckPardakhtParent_ID"}, "CheckPardakht_ID = " + mTransactionDeleteID.get(position), null, null, null, null, null);
                                    if (cursorCheckParentId.moveToFirst()) {
                                        dbUpdate.update("tblCheckPardakht_Parent", cvUpdate, "CheckPardakhtParent_ID = " + cursorCheckParentId.getString(0), null);
                                    }

                                    ContentValues cvUpdate2 = new ContentValues();
                                    cvUpdate2.put("CheckPardakht_Number", etCheckNumber.getText().toString().trim());
                                    cvUpdate2.put("CheckPardakht_DateSarResid", checkDate);
                                    cvUpdate2.put("CheckPardakht_Mablagh", etMablagh.getText().toString().trim());
                                    cvUpdate2.put("CheckPardakht_Exp", etExp.getText().toString().trim());
                                    dbUpdate.update("tblCheckPardakht",cvUpdate2,"CheckPardakht_ID = " + mTransactionDeleteID.get(position), null);

                                    ContentValues cvUpdate3 = new ContentValues();
                                    if(isAccountChanged) cvUpdate3.put("Tafzili_ID", accountTafziliIDs.get(0).toString());
                                    cvUpdate3.put("Date_Pardakht", checkDate);
                                    cvUpdate3.put("CheckChildPadakht_Tozih", etExp.getText().toString().trim());
                                    dbUpdate.update("tblCheckPardakht_Child",cvUpdate3,"CheckPardakht_ID = " + mTransactionDeleteID.get(position), null);
                                    Toast.makeText(mContext, "به روز رسانی با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();

                                    mTranactionMablaghKols.set(position,etMablagh.getText().toString().trim());
                                if(isAccountChanged){
                                    Cursor cursorAccountName = dbUpdate.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + accountTafziliIDs.get(0), null, null, null,null,null);
                                    cursorAccountName.moveToFirst();
                                    mTranactionAccounts.set(position,cursorAccountName.getString(0)+"");
                            }
                                    mTranactionsExps.set(position,etExp.getText().toString().trim());
                                    mTranactionBanks.set(position,bankName[0]);
                                    mTranactionCheckNumbers.set(position,etCheckNumber.getText().toString().trim());

                                    notifyItemChanged(position);
                                    notifyDataSetChanged();
                                }
                            }
                            mFab.setVisibility(View.VISIBLE);
                            mLlAddLayer.removeAllViews();
                            mLlAddLayer.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTranactionAccounts.size();
    }
}
