package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class BankAccountsAdapter extends RecyclerView.Adapter<BankAccountsAdapter.ViewHolder> {

    Context mContext;
    View v;
    List<String> accountBankName, accountShobeName, accountHesabNumbers, accountCardNumbers;
    List<Integer> accountHesabIDs;
    boolean isCollapsed;
    ViewHolder selectedHolder;
    LayoutInflater mInflaterInclude;
    LinearLayout mLlAddLayer;
    FloatingActionButton mFab;

    public BankAccountsAdapter(List<String> accountBankName, List<String> accountShobeName, List<String> accountHesabNumbers, List<String> accountCardNumbers, List<Integer> accountHesabIDs, LinearLayout llAddLayer, FloatingActionButton fab) {
        this.accountBankName = accountBankName;
        this.accountShobeName = accountShobeName;
        this.accountHesabNumbers = accountHesabNumbers;
        this.accountCardNumbers = accountCardNumbers;
        this.accountHesabIDs = accountHesabIDs;
        mLlAddLayer = llAddLayer;
        mFab = fab;
    }

    @Override
    public BankAccountsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.accounts_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        mContext = parent.getContext();
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llExtra;
        LinearLayout llMain;
        LinearLayout llName;
        TextView tvBankName, tvShobeName, tvHesabText, tvCardText, tvHesabNumber, tvCardNumber , tvEdit,tvDelete;
        ImageView ivArrow;

        public ViewHolder(View v) {
            super(v);
            llExtra = (LinearLayout) v.findViewById(R.id.linearLayout_accounts_extra_information);
            llMain = (LinearLayout) v.findViewById(R.id.linearLayout_accounts_main);
            llName = (LinearLayout) v.findViewById(R.id.linearLayout_acounts_name);
            tvBankName = (TextView) v.findViewById(R.id.textView_accounts_bankName);
            tvShobeName = (TextView) v.findViewById(R.id.textView_acounts_shobeName);
            tvHesabText = (TextView) v.findViewById(R.id.textView_acounts_shomareHesabText);
            tvCardText = (TextView) v.findViewById(R.id.textView_acounts_shomareKartText);
            tvHesabNumber = (TextView) v.findViewById(R.id.textView_acounts_shomareHesab);
            tvCardNumber = (TextView) v.findViewById(R.id.textView_acounts_shomareKart);
            tvEdit = (TextView) v.findViewById(R.id.textView_accounts_edit);
            tvDelete = (TextView) v.findViewById(R.id.textView_accounts_delete);
            ivArrow = (ImageView) v.findViewById(R.id.imageView_acounts_arrow);
        }
    }

    public void selectItem(ViewHolder h){
        h.llExtra.setVisibility(View.VISIBLE);
        h.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_background));
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.icons));
        h.tvBankName.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvShobeName.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvHesabText.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvCardText.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvHesabNumber.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.tvCardNumber.setTextColor(mContext.getResources().getColor(R.color.icons));
        h.ivArrow.setBackground(null);
        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_up);
    }

    public void deSelectItem(ViewHolder h){
        h.llExtra.setVisibility(View.GONE);
        h.llMain.setBackground(mContext.getResources().getDrawable(R.drawable.shape_underline_dashed));
        h.llName.getBackground().setTint(mContext.getResources().getColor(R.color.shiri));
        h.tvBankName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvShobeName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvHesabText.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvCardText.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvHesabNumber.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.tvCardNumber.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        h.ivArrow.setImageResource(R.drawable.shape_arrow_drop_down);
    }

    @Override
    public void onBindViewHolder(final BankAccountsAdapter.ViewHolder holder, final int position) {
        holder.tvBankName.setText(accountBankName.get(position));
        holder.tvShobeName.setText(accountShobeName.get(position));
        holder.tvHesabNumber.setText(accountHesabNumbers.get(position));
        holder.tvCardNumber.setText(accountCardNumbers.get(position));
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

                final ConfirmDialogClass cdd = new ConfirmDialogClass(mContext);
                cdd.show();
                Window window = cdd.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                cdd.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SQLiteDatabase dbDelete = new MyDatabase(mContext).getWritableDatabase();
                        dbDelete.execSQL("DELETE FROM tblHesabBanki WHERE HesabBanki_ID = " + accountHesabIDs.get(position));
                        dbDelete.close();

                        accountBankName.remove(position);
                        accountHesabIDs.remove(position);
                        accountCardNumbers.remove(position);
                        accountShobeName.remove(position);
                        accountHesabNumbers.remove(position);

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,accountHesabIDs.size());
                        notifyDataSetChanged();

                        cdd.dismiss();
                    }
                });
                cdd.no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cdd.dismiss();
                    }
                });
            }
        });
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
//                final View v = mInflaterInclude.inflate(R.layout.add_account_layout,mLlAddLayer);
//                v.findViewById(R.id.textView_add_account_contact_list).setVisibility(View.INVISIBLE);
//                v.findViewById(R.id.textView_add_account_clean).setVisibility(View.INVISIBLE);
//                ((TextView)v.findViewById(R.id.textView_add_account_save)).setText("بروزرسانی");
//
//                SQLiteDatabase db11 = new MyDatabase(mContext).getWritableDatabase();
//                Cursor c11 = db11.query("tblContacts",new String[]{"FullName","Code_Melli","Phone","Mobile","AdressContacts","GroupContact","Pishvand_ID"},"Contacts_ID = ?",
//                        new String[]{mAccountIDs.get(position)+""},null,null,null);
//                c11.moveToFirst();
//                ((EditText)v.findViewById(R.id.editText_add_account_fullName)).setText(c11.getString(0));
//                ((EditText)v.findViewById(R.id.editText_add_account_codeMelli)).setText(c11.getString(1));
//                ((EditText)v.findViewById(R.id.editText_add_account_phone)).setText(c11.getString(2));
//                ((EditText)v.findViewById(R.id.editText_add_account_mobile)).setText(c11.getString(3));
//                ((EditText)v.findViewById(R.id.editText_add_account_address)).setText(c11.getString(4));
//
//                SQLiteDatabase db2 = new MyDatabase(mContext).getReadableDatabase();
//                Cursor c = db2.query("tblGroupContact",new String[]{"GroupContactName","GroupContact"},null,null,null,null,null,null);
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
//                c.close();
//
//                Cursor c2 = db2.query("tblPishvand",new String[]{"Pishvand_ID","Pishvand"},null,null,null,null,null,null);
//                int[] pishvandIDs = null;
//                String[] pishvandNames = null;
//                if(c2.moveToFirst()){
//                    pishvandIDs = new int[c2.getCount()];
//                    pishvandNames = new String[c2.getCount()];
//                    int i = 0;
//                    do{
//                        pishvandIDs[i] = c2.getInt(0);
//                        pishvandNames[i] = c2.getString(1);
//                        i++;
//                    }while (c2.moveToNext());
//                }
//                c2.close();
//
//                db2.close();
//
////                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.travelreasons, R.layout.simple_spinner_item);
//                final int[] groupContactID = new int[1];
//
//                final ArrayAdapter adapter = new ArrayAdapter(mContext,R.layout.simple_spinner_item,groupNames);
//                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//                final int[] finalGroupIDs = groupIDs;
//                ((Spinner)v.findViewById(R.id.spinner_add_account_contacts_list)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        groupContactID[0] = finalGroupIDs[i];
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//                        groupContactID[0] = finalGroupIDs[0];
//                    }
//                });
//                ((Spinner)v.findViewById(R.id.spinner_add_account_contacts_list)).setAdapter(adapter);
//                int contactGroupPosition = 0;
//                for(int i = 0 ; i < groupIDs.length ; i++){
//                    if(c11.getInt(5) == groupIDs[i]) {
//                        contactGroupPosition = i;
//                        i = groupIDs.length;
//                    }
//                }
//                ((Spinner)v.findViewById(R.id.spinner_add_account_contacts_list)).setSelection(contactGroupPosition);
//
//                final int[] pishvandID = new int[1];
//
//                final ArrayAdapter adapter2 = new ArrayAdapter(mContext,R.layout.simple_spinner_item,pishvandNames);
//                adapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//                final int[] finalpishvandIDs = pishvandIDs;
//                ((Spinner)v.findViewById(R.id.spinner_add_account_pishvand)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        pishvandID[0] = finalpishvandIDs[i];
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//                        pishvandID[0] = finalpishvandIDs[0];
//                    }
//                });
//                ((Spinner)v.findViewById(R.id.spinner_add_account_pishvand)).setAdapter(adapter2);
//                int contactPishvandPosition = 0;
//                for(int i = 0 ; i < pishvandIDs.length ; i++){
//                    if(c11.getInt(6) == pishvandIDs[i]) {
//                        contactPishvandPosition = i;
//                        i = pishvandIDs.length;
//                    }
//                }
//                ((Spinner)v.findViewById(R.id.spinner_add_account_pishvand)).setSelection(contactPishvandPosition);
//
//                ((TextView)v.findViewById(R.id.textView_add_account_fullName)).setVisibility(View.VISIBLE);
//                ((TextView)v.findViewById(R.id.textView_add_account_codeMelli)).setVisibility(View.VISIBLE);
//                ((TextView)v.findViewById(R.id.textView_add_account_phone)).setVisibility(View.VISIBLE);
//                ((TextView)v.findViewById(R.id.textView_add_account_mobile)).setVisibility(View.VISIBLE);
//                ((TextView)v.findViewById(R.id.textView_add_account_address)).setVisibility(View.VISIBLE);
//
//                ((EditText)v.findViewById(R.id.editText_add_account_fullName)).setHint("");
//                ((EditText)v.findViewById(R.id.editText_add_account_codeMelli)).setHint("");
//                ((EditText)v.findViewById(R.id.editText_add_account_phone)).setHint("");
//                ((EditText)v.findViewById(R.id.editText_add_account_mobile)).setHint("");
//                ((EditText)v.findViewById(R.id.editText_add_account_address)).setHint("");
//
//                ((TextView)v.findViewById(R.id.textView_add_account_close)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mLlAddLayer.setVisibility(View.GONE);
//                        mFab.setVisibility(View.VISIBLE);
//                    }
//                });
//
//                ((TextView)v.findViewById(R.id.textView_add_account_save)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        SQLiteDatabase db = new MyDatabase(mContext).getWritableDatabase();
//                        ContentValues cv2 = new ContentValues();
//                        cv2.put("FullName",((EditText)v.findViewById(R.id.editText_add_account_fullName)).getText().toString().trim());
//                        cv2.put("Phone",((EditText)v.findViewById(R.id.editText_add_account_phone)).getText().toString().trim());
//                        cv2.put("Mobile",((EditText)v.findViewById(R.id.editText_add_account_mobile)).getText().toString().trim());
//                        cv2.put("AdressContacts",((EditText)v.findViewById(R.id.editText_add_account_address)).getText().toString().trim());
//                        cv2.put("Code_Melli",((EditText)v.findViewById(R.id.editText_add_account_codeMelli)).getText().toString().trim());
//                        cv2.put("GroupContact",groupContactID[0]);
//                        cv2.put("Pishvand_ID",pishvandID[0]);
//                        db.update("tblContacts",cv2,"Contacts_ID = ?",new String[]{mAccountIDs.get(position)+""});
//
//                        mAccountFullName.set(position,((EditText)v.findViewById(R.id.editText_add_account_fullName)).getText().toString().trim());
//                        mAccountPhones.set(position,((EditText)v.findViewById(R.id.editText_add_account_phone)).getText().toString().trim());
//                        mAccountMobiles.set(position,((EditText)v.findViewById(R.id.editText_add_account_mobile)).getText().toString().trim());
//                        mAccountAddresses.set(position,((EditText)v.findViewById(R.id.editText_add_account_address)).getText().toString().trim());
//                        Cursor c55 = db.query("tblPishvand",new String[]{"Pishvand"},"Pishvand_ID = ?",new String[]{pishvandID[0]+""},null,null,null,null);
//                        c55.moveToFirst();
//                        mAccountPishvands.set(position,c55.getString(0));
//                        c55.close();
//
//                        db.close();
//
//                        notifyDataSetChanged();
//
//                        Toast.makeText(mContext, "با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
//                        mLlAddLayer.setVisibility(View.GONE);
//                        mFab.setVisibility(View.VISIBLE);
//                    }
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return accountHesabIDs.size();
    }
}
