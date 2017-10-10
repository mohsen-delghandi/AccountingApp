package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class BuyAndSellActivity extends MainActivity {

    RecyclerView buyAndSellRecyclerView,productListRecyclerView;
    RecyclerView.LayoutManager recyclerManager,recyclerManagerProductList;
    RecyclerView.Adapter recyclerAdapter,recyclerAdapterProductList;

    LayoutInflater inflaterInclude;

    TextView tvKharidSelect2nd,tvForoshSelect2nd,tvFactorCode2nd,tvAccountBedehi2nd
            ,tvPhone2nd,tvMobile2nd,tvAddress2nd,tvKharidButton,tvForoshButton,tvHameButton;

    LinearLayout llAccountDetails2nd,llTayid2nd;
    AutoCompleteTextView atvAccounts;
    ImageView ivHelp2nd,ivBack2nd;

    List<String> buyAndSellFactorCodes;
    List<String> buyAndSellMablaghKols;
    List<String> buyAndSellAccounts;
    List<String> buyAndSellModes;

    List<Integer> accountTafziliIDs;

    LinearLayout llTayid3rd,llAttached;

    String mode;

    int factorCode;

    @Override
    public void onBackPressed() {
        if(llAddLayer.getVisibility()==View.VISIBLE){
//            llAddLayer.setVisibility(View.GONE);
//            fab.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }

    public void open2ndLayout(){
        inflaterInclude = (LayoutInflater)BuyAndSellActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fab.setVisibility(View.GONE);
        llAddLayer.removeAllViews();
        llAddLayer.setVisibility(View.VISIBLE);
        llAddLayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        inflaterInclude.inflate(R.layout.buy_and_sell_2nd_layout,llAddLayer);

        tvKharidSelect2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_buy_select);
        tvForoshSelect2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_sell_select);
        tvFactorCode2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_factor_code);
        tvAccountBedehi2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_bedehi_mablagh);
        tvPhone2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_phone);
        tvMobile2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_mobile);
        tvAddress2nd = (TextView)findViewById(R.id.textView_buy_and_sell_2nd_address);
        ivHelp2nd = (ImageView) findViewById(R.id.imageView_buy_and_sell_2nd_help);
        ivBack2nd = (ImageView) findViewById(R.id.imageView_buy_and_sell_2nd_back);

        llAccountDetails2nd = (LinearLayout)findViewById(R.id.linearLayout_buy_and_sell_2nd_account);
        llTayid2nd = (LinearLayout)findViewById(R.id.linearLayout_buy_and_sell_2nd_tayid);

        atvAccounts = (AutoCompleteTextView) findViewById(R.id.autoTextView_buy_and_sell_2nd_account);

        SQLiteDatabase dbAccounts = new MyDatabase(BuyAndSellActivity.this).getReadableDatabase();
        Cursor cursorAccounts = dbAccounts.query("tblContacts",new String[]{"FullName"},null,null,null,null,null,null);
        List<String> listAccounts = new ArrayList<String>();
        if(cursorAccounts.moveToFirst()){
            do{
                listAccounts.add(cursorAccounts.getString(0));
            }while (cursorAccounts.moveToNext());
        }
        cursorAccounts.close();

        factorCode = -1;
        Cursor cursorFactorCode1 = dbAccounts.query("TblParent_FrooshKala",new String[]{"MAX(ForooshKalaParent_ID)"},null,null,null,null,null,null);
        if(cursorFactorCode1.moveToFirst()){
            factorCode = cursorFactorCode1.getInt(0)+1;
        }
        Cursor cursorFactorCode2 = dbAccounts.query("TblParent_KharidKala",new String[]{"MAX(KharidKalaParent_ID)"},null,null,null,null,null,null);
        if(cursorFactorCode2.moveToFirst()){
            if(cursorFactorCode2.getInt(0) >= factorCode){
                factorCode = cursorFactorCode2.getInt(0)+1;
            }
        }
        if(factorCode == -1){
            Cursor cursorDefaultFactorCode = dbAccounts.query("tblSettingIDFactor",new String[]{"StartID"},null,null,null,null,null);
            cursorDefaultFactorCode.moveToFirst();
            factorCode = cursorDefaultFactorCode.getInt(0);
        }
        dbAccounts.close();

        tvFactorCode2nd.setText(factorCode+"");

        ArrayAdapter<String> adapterAccounts = new ArrayAdapter<String>(BuyAndSellActivity.this,android.R.layout.simple_list_item_1,listAccounts);
        atvAccounts.setAdapter(adapterAccounts);
        atvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SQLiteDatabase dbShowAccount = new MyDatabase(BuyAndSellActivity.this).getReadableDatabase();
                Cursor cursorShowAccount = dbShowAccount.query("tblContacts",new String[]{"Tafzili_ID"},"FullName = ?",new String[]{atvAccounts.getText().toString().trim()+""},null,null,null);
                accountTafziliIDs = new ArrayList<Integer>();
                if(cursorShowAccount.moveToFirst()){
                    accountTafziliIDs.add(cursorShowAccount.getInt(0));
                }
                cursorShowAccount.close();
                Cursor cursorAccountDetails = dbShowAccount.query("tblContacts",new String[]{"Phone","Mobile","AdressContacts"},"Tafzili_ID = ?",new String[]{accountTafziliIDs.get(0)+""},null,null,null);
                cursorAccountDetails.moveToFirst();

                llAccountDetails2nd.setVisibility(View.VISIBLE);
                tvPhone2nd.setText(cursorAccountDetails.getString(0));
                tvMobile2nd.setText(cursorAccountDetails.getString(1));
                tvAddress2nd.setText(cursorAccountDetails.getString(2));

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        mode = "Sell";

        tvKharidSelect2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKharidSelect2nd.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
                tvForoshSelect2nd.setBackground(null);
                mode = "Buy";

            }
        });

        tvForoshSelect2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvForoshSelect2nd.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
                tvKharidSelect2nd.setBackground(null);
                mode = "Sell";
            }
        });

        llTayid2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open3rdLayout();
            }
        });

        ivBack2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAddLayer.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });
    }

    public void open3rdLayout(){
        if(atvAccounts.getText().toString().trim().equals("")){
            Toast.makeText(BuyAndSellActivity.this, "لطفا طرف حساب را مشخص کنید.", Toast.LENGTH_SHORT).show();
        }else{
//            fab.setVisibility(View.VISIBLE);
            llAddLayer.removeAllViews();
            llAddLayer.setVisibility(View.VISIBLE);
            llAddLayer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            inflaterInclude.inflate(R.layout.buy_and_sell_3rd_layout, llAddLayer);

            llTayid3rd = (LinearLayout)findViewById(R.id.linearLayout_order_list_tayid_3rd);

            List<String> productName = new ArrayList<String>();
            List<String> productSellPrice = new ArrayList<String>();
            List<String> productUnit = new ArrayList<String>();
            List<Integer> productMojoodi = new ArrayList<Integer>();
            List<Integer> productIDs = new ArrayList<Integer>();

            SQLiteDatabase dbProductList = new MyDatabase(BuyAndSellActivity.this).getReadableDatabase();
            Cursor cursorProductList = dbProductList.query("TblKala", new String[]{"Name_Kala", "GheymatForoshAsli", "Fk_VahedKalaAsli", "MojodiAvalDore", "ID_Kala"}, null, null, null, null, null);
            if (cursorProductList.moveToFirst()) {
                do {
                    productName.add(cursorProductList.getString(0));
                    productSellPrice.add(cursorProductList.getString(1));
                    Cursor cursor3 = dbProductList.query("TblVahedKalaAsli", new String[]{"NameVahed"}, "ID_Vahed = ?", new String[]{cursorProductList.getString(2)}, null, null, null);
                    cursor3.moveToFirst();
                    productUnit.add(cursor3.getString(0));
                    cursor3.close();
                    productMojoodi.add(cursorProductList.getInt(3));
                    productIDs.add(cursorProductList.getInt(4));
                } while ((cursorProductList.moveToNext()));
            }
            cursorProductList.close();
            dbProductList.close();

            TextView tvJameRadif = (TextView)findViewById(R.id.textView_order_list_jame_radif_4th);
            TextView tvJameMeghdar = (TextView)findViewById(R.id.textView_order_list_jame_meghdar_4th);
            TextView tvJameMablagh = (TextView)findViewById(R.id.textView_order_list_jame_mablagh_4th);

            productListRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_accountSide);
            productListRecyclerView.setHasFixedSize(true);
            productListRecyclerView.setNestedScrollingEnabled(false);
            recyclerManagerProductList = new LinearLayoutManager(BuyAndSellActivity.this);
            productListRecyclerView.setLayoutManager(recyclerManagerProductList);
            recyclerAdapterProductList = new ProductsListSelectAdapter(BuyAndSellActivity.this, productName, productSellPrice, productUnit
                    , productMojoodi, productIDs, llAddLayer, factorCode, accountTafziliIDs.get(0), mode,llTayid3rd,tvJameRadif,tvJameMeghdar,tvJameMablagh);
            productListRecyclerView.setAdapter(recyclerAdapterProductList);

            ImageView tvBack3rd = (ImageView) findViewById(R.id.imageView_buy_and_sell_3rd_back);
            tvBack3rd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    open2ndLayout();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.buy_and_sell_show_layout);

        tvFarsiTitle.setText("خرید و فروش");
        tvEngliashNormalTitle.setText("BUY ");
        tvEnglishBoldTitle.setText("& SELL");

        llAttached = (LinearLayout)findViewById(R.id.linearLayout_attached);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open2ndLayout();
            }
        });

        tvKharidButton = (TextView)findViewById(R.id.textView_buy_and_sell_kharid);
        tvForoshButton = (TextView)findViewById(R.id.textView_buy_and_sell_forosh);
        tvHameButton = (TextView)findViewById(R.id.textView_buy_and_sell_hame);

        buyAndSellRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_buy_and_sell);
        buyAndSellRecyclerView.setHasFixedSize(true);
        buyAndSellRecyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        buyAndSellRecyclerView.setLayoutManager(recyclerManager);

        newLists();
        readBuyAndSellsFromDatabase("Sell");
        recyclerAdapter = new BuyAndSellAdapter(BuyAndSellActivity.this,buyAndSellFactorCodes,buyAndSellMablaghKols,buyAndSellAccounts,buyAndSellModes, llAddLayer, fab);
        buyAndSellRecyclerView.setAdapter(recyclerAdapter);

        tvKharidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKharidButton.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
                tvHameButton.setBackground(null);
                tvForoshButton.setBackground(null);
                newLists();
                readBuyAndSellsFromDatabase("Buy");
                recyclerAdapter = new BuyAndSellAdapter(BuyAndSellActivity.this,buyAndSellFactorCodes,buyAndSellMablaghKols,buyAndSellAccounts,buyAndSellModes,llAddLayer,fab);
                buyAndSellRecyclerView.setAdapter(recyclerAdapter);
            }
        });

        tvForoshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvForoshButton.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
                tvHameButton.setBackground(null);
                tvKharidButton.setBackground(null);
                newLists();
                readBuyAndSellsFromDatabase("Sell");
                recyclerAdapter = new BuyAndSellAdapter(BuyAndSellActivity.this,buyAndSellFactorCodes,buyAndSellMablaghKols,buyAndSellAccounts,buyAndSellModes, llAddLayer, fab);
                buyAndSellRecyclerView.setAdapter(recyclerAdapter);
            }
        });

        tvHameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHameButton.setBackground(getResources().getDrawable(R.drawable.shape_circle_dark));
                tvForoshButton.setBackground(null);
                tvKharidButton.setBackground(null);
                newLists();
                readBuyAndSellsFromDatabase("All");
                recyclerAdapter = new BuyAndSellAdapter(BuyAndSellActivity.this,buyAndSellFactorCodes,buyAndSellMablaghKols,buyAndSellAccounts,buyAndSellModes, llAddLayer, fab);
                buyAndSellRecyclerView.setAdapter(recyclerAdapter);
            }
        });
    }

    public void newLists(){
        buyAndSellFactorCodes = new ArrayList<String>();
        buyAndSellMablaghKols = new ArrayList<String>();
        buyAndSellAccounts = new ArrayList<String>();
        buyAndSellModes = new ArrayList<String>();
    }

    public void readBuyAndSellsFromDatabase(String mode){

        String tableName = null;
        String[] tableColumns = null;
        String type = null;

        if(mode.trim().equals("Buy")){
            tableName = "TblParent_KharidKala";
            tableColumns = new String[]{"KharidKalaParent_ID","KharidKalaParent_JameKol","KharidKalaParent_Tafzili"};
            type = "Buy";
        }else if(mode.trim().equals("Sell")){
            tableName = "TblParent_FrooshKala";
            tableColumns = new String[]{"ForooshKalaParent_ID","ForooshKalaParent_JameKol","ForooshKalaParent_Tafzili"};
            type = "Sell";
        }else if(mode.trim().equals("All")){
            readBuyAndSellsFromDatabase("Buy");
            readBuyAndSellsFromDatabase("Sell");
            List<List> allList = new ArrayList<List>();
            allList.add(buyAndSellFactorCodes);
            allList.add(buyAndSellMablaghKols);
            allList.add(buyAndSellAccounts);
            allList.add(buyAndSellModes);
        }

        if(tableName!=null && tableColumns!=null) {
            SQLiteDatabase mydb = new MyDatabase(this).getReadableDatabase();
            Cursor cursor2 = mydb.query(tableName, tableColumns, null, null, null, null, null);
            if (cursor2.moveToFirst()) {
                do {
                    buyAndSellFactorCodes.add(cursor2.getString(0));
                    buyAndSellMablaghKols.add(cursor2.getString(1));
                    Cursor c3 = mydb.query("tblContacts", new String[]{"FullName"}, "Tafzili_ID = " + cursor2.getString(2), null, null, null, null, null);
                    c3.moveToFirst();
                    buyAndSellAccounts.add(c3.getString(0));
                    c3.close();
                    buyAndSellModes.add(type);
                } while ((cursor2.moveToNext()));
            }
            cursor2.close();
            mydb.close();
        }
    }
}
