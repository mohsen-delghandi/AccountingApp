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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mohsen on 2017-09-18.
 */

public class ProductsActivity extends MainActivity {

    static RecyclerView productRecyclerView;
    LinearLayoutManager recyclerManager;
    static RecyclerView.Adapter recyclerAdapter;

    LayoutInflater inflaterInclude;

    TextView tvClose,tvClean,tvSave;
    EditText etName,etBuyPrice,etSellPrice,etMojoodi,etAveragePrice;

    List<String> productName,productBuyPrice,productSellPrice,productMojoodi,productUnit;
    List<Integer> productIDs;

    Spinner spUnitList;

    @Override
    public void onBackPressed() {
        if(llAddLayer.getVisibility()==View.VISIBLE){
            llAddLayer.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.page = "Products";
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
                llAddLayer.removeAllViews();
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

                etName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etName.selectAll();
                    }
                });
                etBuyPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etBuyPrice.selectAll();
                    }
                });
                etSellPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etSellPrice.selectAll();
                    }
                });
                etMojoodi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etMojoodi.selectAll();
                    }
                });
                etAveragePrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etAveragePrice.selectAll();
                    }
                });

                etSellPrice.addTextChangedListener(new NumberTextWatcher(etSellPrice));
                etBuyPrice.addTextChangedListener(new NumberTextWatcher(etBuyPrice));
                etAveragePrice.addTextChangedListener(new NumberTextWatcher(etAveragePrice));


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
                        ContentValues cvKala = new ContentValues();
                        cvKala.put("Name_Kala", etName.getText().toString().trim());
                        cvKala.put("Fk_VahedKalaAsli", unitID[0]);
                        cvKala.put("GheymatKharidAsli", etBuyPrice.getText().toString().replaceAll(",", "").trim());
                        cvKala.put("GheymatForoshAsli", etSellPrice.getText().toString().replaceAll(",", "").trim());
                        cvKala.put("MojodiAvalDore", etMojoodi.getText().toString().trim());
                        cvKala.put("MianginFiAvalDovre", etAveragePrice.getText().toString().replaceAll(",", "").trim());
                        Cursor cursorMaxSrialSand2 = db.query("tblParentSanad", new String[]{"IFNULL(MAX(Serial_Sanad),0)"}, null, null, null, null, null);
                        if (cursorMaxSrialSand2.moveToFirst()) {
                            cvKala.put("SerialSanadEftetahiye", (Integer.parseInt(cursorMaxSrialSand2.getString(0)) + 1) + "");
                        }
                        long id = db.insert("TblKala", null, cvKala);

                        if(etMojoodi.getText().toString().replaceAll(",","").trim()!="" && etMojoodi.getText().toString().replaceAll(",","").trim()!="0" &&
                                etAveragePrice.getText().toString().replaceAll(",","").trim()!="" && etAveragePrice.getText().toString().replaceAll(",","").trim()!="0") {
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            final String currentDate = format2.format(new java.util.Date());

                            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            final String currentTime = format.format(new java.util.Date());


                            SQLiteDatabase dbKala = new MyDatabase(ProductsActivity.this).getWritableDatabase();
                            ContentValues cvBedehkar = new ContentValues();
                            ContentValues cvBestankar = new ContentValues();
                            ContentValues cvParentSanad = new ContentValues();
                            Cursor cursorMaxSrialSand = dbKala.query("tblParentSanad", new String[]{"IFNULL(MAX(Serial_Sanad),0)"}, null, null, null, null, null);
                            if (cursorMaxSrialSand.moveToFirst()) {
                                cvParentSanad.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                                cvBedehkar.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                                cvBestankar.put("Serial_Sanad", (Integer.parseInt(cursorMaxSrialSand.getString(0)) + 1) + "");
                            }

                            Cursor cursorMaxNumberSand = dbKala.query("tblParentSanad", new String[]{"IFNULL(MAX(Number_Sanad),0)"}, null, null, null, null, null);
                            if (cursorMaxNumberSand.moveToFirst()) {
                                cvParentSanad.put("Number_Sanad", (Integer.parseInt(cursorMaxNumberSand.getString(0)) + 1) + "");
                            }
                            cvParentSanad.put("StatusSanadID", "3");
                            cvParentSanad.put("TypeSanad_ID", "5");
                            cvParentSanad.put("Date_Sanad", currentDate);
                            cvParentSanad.put("Time_Sanad", currentTime);
                            cvParentSanad.put("Taraz_Sanad", "1");
                            cvParentSanad.put("Error_Sanad", "0");
                            cvParentSanad.put("Edited_Sanad", "0");
                            cvParentSanad.put("Deleted_Sanad", "0");

                            dbKala.insert("tblParentSanad", null, cvParentSanad);

                            cvBedehkar.put("AccountsID", "150");
                            cvBedehkar.put("Moein_ID", "15003");
                            cvBedehkar.put("Bedehkar", Integer.parseInt(etMojoodi.getText().toString().replaceAll(",", "").trim()) *
                                    Integer.parseInt(etAveragePrice.getText().toString().replaceAll(",", "").trim()) + "");
                            cvBedehkar.put("Bestankar", "0");
                            cvBedehkar.put("ID_Amaliyat", id + "");
                            cvBedehkar.put("ID_TypeAmaliyat", "9");
                            cvBedehkar.put("Sharh_Child_Sanad", "موجودی اول دوره");

                            cvBestankar.put("AccountsID", "930");
                            cvBestankar.put("Bestankar", Integer.parseInt(etMojoodi.getText().toString().replaceAll(",", "").trim()) *
                                    Integer.parseInt(etAveragePrice.getText().toString().replaceAll(",", "").trim()) + "");
                            cvBestankar.put("Bedehkar", "0");
                            cvBestankar.put("ID_Amaliyat", id + "");
                            cvBestankar.put("ID_TypeAmaliyat", "9");
                            cvBestankar.put("Sharh_Child_Sanad", "موجودی اول دوره");

                            dbKala.insert("tblChildeSanad", null, cvBedehkar);
                            dbKala.insert("tblChildeSanad", null, cvBestankar);
                        }

                        productName.add(etName.getText().toString().trim());
                        productBuyPrice.add(etBuyPrice.getText().toString().replaceAll(",","").trim());
                        productSellPrice.add(etSellPrice.getText().toString().replaceAll(",","").trim());
                        Cursor c22 = db.query("TblVahedKalaAsli",new String[]{"NameVahed"},"ID_Vahed = ?",new String[]{unitID[0]+""},null,null,null);
                        c22.moveToFirst();
                        productUnit.add(c22.getString(0));
                        c22.close();
                        productMojoodi.add(etMojoodi.getText().toString().trim());
                        productIDs.add((int)id);

//                        recyclerAdapter.notifyItemInserted(productIDs.size()-1);
                        recyclerAdapter.notifyDataSetChanged();

                            db.close();

                        Toast.makeText(ProductsActivity.this, "با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
                        cleanFrom();
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
        recyclerManager.setReverseLayout(true);
        recyclerManager.setStackFromEnd(true);
        productRecyclerView.setLayoutManager(recyclerManager);
        readProductsFromDatabase();
        recyclerAdapter = new ProductsAdapter(this,productName,productBuyPrice,productSellPrice,productUnit,productMojoodi,productIDs,llAddLayer,fab);
        productRecyclerView.setAdapter(recyclerAdapter);
    }

    public void cleanFrom(){
        etName.setText("");
        etAveragePrice.setText("");
        etMojoodi.setText("");
        etSellPrice.setText("");
        etBuyPrice.setText("");
        spUnitList.setSelection(0);
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
    }
}
