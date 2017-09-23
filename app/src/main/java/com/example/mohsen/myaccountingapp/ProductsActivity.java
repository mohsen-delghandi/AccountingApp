package com.example.mohsen.myaccountingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class ProductsActivity extends MainActivity {

    RecyclerView productRecyclerView;
    RecyclerView.LayoutManager recyclerManager;
    RecyclerView.Adapter recyclerAdapter;

    LayoutInflater inflaterInclude;

    TextView tvClose,tvClean,tvSave;
    EditText etName,etBuyPrice,etSellPrice,etMojoodi,etAveragePrice;

    List<String> productName,productBuyPrice,productSellPrice,productMojoodi,productUnit;
    List<Integer> productIDs;

    Spinner spUnitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.products_layout);

        tvFarsiTitle.setText("محصولات");
        tvEngliashNormalTitle.setText("PRODUCT ");
        tvEnglishBoldTitle.setText("LIST");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflaterInclude = (LayoutInflater)ProductsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                fab.setVisibility(View.GONE);
                llAddLayer.setVisibility(View.VISIBLE);
                llAddLayer.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                View addAccountLayer = inflaterInclude.inflate(R.layout.add_product_layout,llAddLayer);

                tvClean = (TextView)findViewById(R.id.textView_add_product_clean);
                tvClose = (TextView)findViewById(R.id.textView_add_product_close);
                tvSave = (TextView)findViewById(R.id.textView_add_product_save);

                etName = (EditText)findViewById(R.id.editText_add_product_name);
                etBuyPrice = (EditText)findViewById(R.id.editText_add_product_buy_price);
                etSellPrice = (EditText)findViewById(R.id.editText_add_product_sell_price);
                etMojoodi = (EditText)findViewById(R.id.editText_add_product_mojoodi);
                etAveragePrice = (EditText)findViewById(R.id.editText_add_product_average_price);

                spUnitList = (Spinner)findViewById(R.id.spinner_add_product_units_list);

                SQLiteDatabase db2 = new MyDatabase(ProductsActivity.this).getReadableDatabase();
                Cursor c = db2.query("TblVahedKalaAsli",new String[]{"NameVahed","ID_Vahed"},null,null,null,null,null,null);
                String[] unitNames = null;
                int[] unitIDs = null;
                if(c.moveToFirst()){
                    unitNames = new String[c.getCount()];
                    unitIDs = new int[c.getCount()];
                    int i = 0;
                    do{
                        unitNames[i] = c.getString(0);
                        unitIDs[i] = c.getInt(1);
                        i++;
                    }while (c.moveToNext());
                }
                c.close();
                db2.close();

//                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.travelreasons, R.layout.simple_spinner_item);
                final int[] unitID = new int[1];

                final ArrayAdapter adapter = new ArrayAdapter(ProductsActivity.this,R.layout.simple_spinner_item,unitNames);
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                final int[] finalUnitIDs = unitIDs;
                spUnitList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        unitID[0] = finalUnitIDs[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        unitID[0] = finalUnitIDs[0];
                    }
                });
                spUnitList.setAdapter(adapter);

                tvSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SQLiteDatabase db = new MyDatabase(ProductsActivity.this).getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("Name_Kala",etName.getText().toString().trim());
                        cv.put("Fk_VahedKalaAsli",unitID[0]);
                        cv.put("GheymatKharidAsli",etBuyPrice.getText().toString().trim());
                        cv.put("GheymatForoshAsli",etSellPrice.getText().toString().trim());
                        cv.put("MojodiAvalDore",etMojoodi.getText().toString().trim());
                        cv.put("MianginFiAvalDovre",etAveragePrice.getText().toString().trim());
                        db.insert("TblKala",null,cv);
                        db.close();

                        Toast.makeText(ProductsActivity.this, "با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
                        cleanFrom();
//                        accountRecyclerView.setAdapter(recyclerAdapter);
                    }
                });

                tvClean.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cleanFrom();
                    }
                });

                tvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llAddLayer.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        productRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_products);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setNestedScrollingEnabled(false);
        recyclerManager = new LinearLayoutManager(this);
        productRecyclerView.setLayoutManager(recyclerManager);
        readProductsFromDatabase();
        recyclerAdapter = new ProductsAdapter(this,productName,productBuyPrice,productSellPrice,productUnit,productMojoodi,productIDs);
        productRecyclerView.setAdapter(recyclerAdapter);
    }

    public void cleanFrom(){
        etName.setText("");
        etAveragePrice.setText("");
        etMojoodi.setText("");
        etSellPrice.setText("");
        etBuyPrice.setText("");
    }

    public void readProductsFromDatabase(){
        productName = new ArrayList<String>();
        productBuyPrice = new ArrayList<String>();
        productSellPrice = new ArrayList<String>();
        productUnit = new ArrayList<String>();
        productMojoodi = new ArrayList<String>();
        productIDs = new ArrayList<Integer>();
//        List<List> accountList = new ArrayList<List>();

        SQLiteDatabase mydb = new MyDatabase(this).getReadableDatabase();
        Cursor cursor2 = mydb.query("TblKala",new String[]{"Name_Kala","GheymatKharidAsli","GheymatForoshAsli","Fk_VahedKalaAsli","MojodiAvalDore","ID_Kala"},null,null,null,null,null);
        if(cursor2.moveToFirst()){
            do{
                productName.add(cursor2.getString(0));
                productBuyPrice.add(cursor2.getString(1));
                productSellPrice.add(cursor2.getString(2));
                Cursor cursor3 = mydb.query("TblVahedKalaAsli",new String[]{"NameVahed"},"ID_Vahed = ?",new String[]{cursor2.getString(3)},null,null,null);
                cursor3.moveToFirst();
                productUnit.add(cursor3.getString(0));
                cursor3.close();
                productMojoodi.add(cursor2.getString(4));
                productIDs.add(cursor2.getInt(5));
            }while ((cursor2.moveToNext()));
        }
        cursor2.close();
        mydb.close();

//        accountList.add(accountFullName);
//        accountList.add(accountPhone);
//        accountList.add(accountMobile);
//        accountList.add(accountAddress);
//        accountList.add(accountIDs);
//
//        return accountList;
    }
}
