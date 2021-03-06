package com.example.mohsen.myaccountingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.components.JDF;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.ContentValues.TAG;

public class MainActivity extends BaseActivity {



    TextView tvFarsiTitle,tvEnglishBoldTitle,tvEngliashNormalTitle;
    LinearLayout llTitleBar;

    int width;

    public static String page;

    LayoutInflater inflater;
    NestedScrollView ns;
    NavigationView navigationView;

    RecyclerView mNavigationRecycler;
    LinearLayoutManager mRecyclerManager;
    RecyclerView.Adapter mRecyclerAdapter;

    FloatingActionButton fab;

    DrawerLayout drawer;
    ArrayList<Class> classes;

    LinearLayout llAddLayer,llBuyAndSellAttached,llTransactionAttached;
    RelativeLayout flAttached;

    ImageView ivNav;



//    ResideMenu resideMenu;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static String priceFormatter(String price){
        int a =  (price+"").length();
        String formattedPrice = (price+"").substring(0,a%3);
        for(int i = 0 ; i < (price+"").length()/3 ; i++) {
            if (!formattedPrice.equals("")) formattedPrice += ",";
            formattedPrice += (price+"").substring(a % 3 + 3 * i , a % 3 + 3 * (i+1));
        }
        return formattedPrice;
    }

    public static String persianDateToGeorgianDate(DatePersian mDate){
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(mDate.getCalendar().getTime());
        return fDate;
    }

    public static String georgianDateToPersianDate(String date){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = inputFormat.parse(date.toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePersian dp = new DatePersian();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        dp.setDate(new JDF(cal));
        return dp.getDate();
    }

    public static String dateToText(DatePersian mDate){
        String monthName = "";
        switch (mDate.getMonth()) {
            case 1:
                monthName = "فروردین";
                break;
            case 2:
                monthName = "اردیبهشت";
                break;
            case 3:
                monthName = "خرداد";
                break;
            case 4:
                monthName = "تیر";
                break;
            case 5:
                monthName = "مرداد";
                break;
            case 6:
                monthName = "شهریور";
                break;
            case 7:
                monthName = "مهر";
                break;
            case 8:
                monthName = "آبان";
                break;
            case 9:
                monthName = "آذر";
                break;
            case 10:
                monthName = "دی";
                break;
            case 11:
                monthName = "بهمن";
                break;
            case 12:
                monthName = "اسفند";
                break;
        }

        String dayName = "";
        switch (mDate.getIranianDay()) {
            case 0:
                dayName = "شنبه";
                break;
            case 1:
                dayName = "یک شنبه";
                break;
            case 2:
                dayName = "دوشنبه";
                break;
            case 3:
                dayName = "سه شنبه";
                break;
            case 4:
                dayName = "چهارشنبه";
                break;
            case 5:
                dayName = "پنج شنبه";
                break;
            case 6:
                dayName = "جمعه";
                break;
        }

        return dayName + " " + mDate.getDay() + " " + monthName + " سال " + mDate.getYear();
    }

//    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
//        @Override
//        public void openMenu() {
//            Toast.makeText(MainActivity.this, "Menu is opened!", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void closeMenu() {
//            Toast.makeText(MainActivity.this, "Menu is closed!", Toast.LENGTH_SHORT).show();
//        }
//    };
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {             //static
//        return resideMenu.dispatchTouchEvent(ev);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        MainApplication ma = new MainApplication();
        ma.loginAndGetPermission(this);

        SQLiteDatabase dbP = new PDatabase(this).getReadableDatabase();
        Cursor cursorP = dbP.query("Permission_TBL",new String[]{"Permission"},null,null,null,null,null,null);
        if(cursorP.moveToFirst()){
            if(cursorP.getString(0).trim().equals("No")){
                setContentView(R.layout.blank_layout);
            }else{
                setContentView(R.layout.activity_main);

//        resideMenu = new ResideMenu(MainActivity.this, R.layout.residemenu_custom_left_scrollview, R.layout.residemenu_custom_right_scrollview);
//        resideMenu.setBackground(R.drawable.shape_gradient_background);    //set background
//        resideMenu.attachToActivity(MainActivity.this);
//        resideMenu.setScaleValue(0.5f);
//        resideMenu.setMenuListener(menuListener);          //listener
//        resideMenu.setUse3D(true);                                 //support 3d
//        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);        //dsible swipe left




                TextView tvNavigationDate = (TextView)findViewById(R.id.textView_navigation_date);
                DatePersian mDate = new DatePersian();
                final String currentDate = mDate.getDate();
                tvNavigationDate.setText(dateToText(mDate));

                llBuyAndSellAttached = (LinearLayout)findViewById(R.id.linearLayout_buy_and_sell_attached);
                llTransactionAttached = (LinearLayout)findViewById(R.id.linearLayout_transaction_attached);
                flAttached = (RelativeLayout)findViewById(R.id.frameLayout_attached);

                if(page!=null && page.toString().trim().equals("BuyAndSell")){
                    llTransactionAttached.setVisibility(View.GONE);
                }else if(page!=null && page.toString().trim().equals("Transaction")){
                    llBuyAndSellAttached.setVisibility(View.GONE);
                }else {
                    flAttached.setVisibility(View.GONE);
                }

                tvFarsiTitle = (TextView)findViewById(R.id.textView_title_farsi);
                tvEngliashNormalTitle = (TextView)findViewById(R.id.textView_title_english_normal);
                tvEnglishBoldTitle = (TextView)findViewById(R.id.textView_title_english_bold);

                fab = (FloatingActionButton) findViewById(R.id.fab);
                llAddLayer = (LinearLayout) findViewById(R.id.linearLayout_add_layer);

                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                navigationView = (NavigationView)findViewById(R.id.nav_view);

                ivNav = (ImageView)findViewById(R.id.imageView_navigation_menu);
                ivNav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    }
                });

                ns = (NestedScrollView)findViewById(R.id.nestedscrollview);
                llTitleBar = (LinearLayout)findViewById(R.id.linearLayout_titleBar);

                llTitleBar.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ns.getLayoutParams();
                layoutParams.topMargin = llTitleBar.getMeasuredHeight();
                ns.setLayoutParams(layoutParams);

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                width = size.x;

                DrawerLayout.LayoutParams layoutParams1 = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
                layoutParams1.width = width*2/3;
                navigationView.setLayoutParams(layoutParams1);

                classes = new ArrayList<>();

                classes.add(FirstPageActivity.class);
                classes.add(AccountSideActivity.class);
                classes.add(ProductsActivity.class);
                classes.add(BuyAndSellActivity.class);
                classes.add(TransactionActivity.class);
                classes.add(AccountsActivity.class);
                classes.add(BillActivity.class);

                drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                });

                mNavigationRecycler = (RecyclerView)findViewById(R.id.nav_recyclerView);
                mNavigationRecycler.setHasFixedSize(true);
                mNavigationRecycler.setNestedScrollingEnabled(false);
                mRecyclerManager = new LinearLayoutManager(this);
                mNavigationRecycler.setLayoutManager(mRecyclerManager);
                String[] navigationMenuItemNames = getResources().getStringArray(R.array.navigation_menu_item_names);
                mRecyclerAdapter = new NavigationAdapter(this,width,drawer,navigationMenuItemNames,classes,
                        llTransactionAttached,llBuyAndSellAttached,flAttached);
                mNavigationRecycler.setAdapter(mRecyclerAdapter);

                if(page == null){
                    Intent i = new Intent(this,FirstPageActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    protected View setInflater(Context context, @LayoutRes int resource){

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,ns);

        return view;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

