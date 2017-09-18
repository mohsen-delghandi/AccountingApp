package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.content.Intent;
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
import android.view.Display;
import android.view.LayoutInflater;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {


    LinearLayout llExtra;
    LinearLayout ll3;
    LinearLayout llPrice;
    TextView tvCollapsed;
    TextView tvCollapsed2;
    TextView tvCollapsed3;
    ImageView ivCollapse;
    ImageView ivExpand;

    TextView tvFarsiTitle,tvEnglishBoldTitle,tvEngliashNormalTitle;
    LinearLayout llTitleBar;

    int width;

    private LayoutInflater inflater;
    private NestedScrollView ns;

    RecyclerView mNavigationRecycler;
    RecyclerView.LayoutManager mRecyclerManager;
    RecyclerView.Adapter mRecyclerAdapter;


    DrawerLayout drawer;
    ArrayList<Class> classes;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    View.OnClickListener oclCollapse = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            llExtra.setVisibility(View.GONE);
            ll3.setBackground(getResources().getDrawable(R.drawable.shape_underline_dashed));
            llPrice.getBackground().setTint(getResources().getColor(R.color.primary_text));
            tvCollapsed.setTextColor(getResources().getColor(R.color.primary_text));
            tvCollapsed2.setTextColor(getResources().getColor(R.color.primary_text));
            tvCollapsed3.setTextColor(getResources().getColor(R.color.primary_text));
            ivExpand.setVisibility(View.VISIBLE);
            ivCollapse.setVisibility(View.GONE);
            ll3.setOnClickListener(oclExpand);

        }
    };

    View.OnClickListener oclExpand = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            llExtra.setVisibility(View.VISIBLE);
            ll3.setBackground(getResources().getDrawable(R.drawable.shape_gradient_background));
            llPrice.getBackground().setTint(getResources().getColor(R.color.icons));
            tvCollapsed.setTextColor(getResources().getColor(R.color.icons));
            tvCollapsed2.setTextColor(getResources().getColor(R.color.icons));
            tvCollapsed3.setTextColor(getResources().getColor(R.color.icons));
            ivExpand.setVisibility(View.GONE);
            ivCollapse.setVisibility(View.VISIBLE);
            ll3.setOnClickListener(oclCollapse);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tvFarsiTitle = (TextView)findViewById(R.id.textView_title_farsi);
        tvEngliashNormalTitle = (TextView)findViewById(R.id.textView_title_english_normal);
        tvEnglishBoldTitle = (TextView)findViewById(R.id.textView_title_english_bold);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        classes = new ArrayList<>();

        classes.add(AccountSideActivity.class);
//        classes.add(FormActivity.class);
//        classes.add(SettingsActivity2.class);
//        classes.add(DatabaseAcitivity.class);

        mNavigationRecycler = (RecyclerView)findViewById(R.id.nav_recyclerView);
        mNavigationRecycler.setHasFixedSize(true);
        mNavigationRecycler.setNestedScrollingEnabled(false);
        mRecyclerManager = new LinearLayoutManager(this);
        mNavigationRecycler.setLayoutManager(mRecyclerManager);
        String[] navigationMenuItemNames = getResources().getStringArray(R.array.navigation_menu_item_names);
        mRecyclerAdapter = new NavigationAdapter(this,width,drawer,navigationMenuItemNames,classes);
        mNavigationRecycler.setAdapter(mRecyclerAdapter);


//        llExtra = (LinearLayout)findViewById(R.id.linearLayout_extra);
//        ll3 = (LinearLayout)findViewById(R.id.linearLayout_4);
//        llPrice = (LinearLayout)findViewById(R.id.linearLayout_price);
//        tvCollapsed = (TextView)findViewById(R.id.textView_collapsed);
//        tvCollapsed2 = (TextView)findViewById(R.id.textView_collapsed2);
//        tvCollapsed3 = (TextView)findViewById(R.id.textView_collapsed3);
//        ivCollapse = (ImageView) findViewById(R.id.iv_up);
//        ivExpand = (ImageView) findViewById(R.id.iv_down);
//
//        final TextView tvReceiveSelect,tvPaySelect,tvAllSelect;
//        tvReceiveSelect = (TextView)findViewById(R.id.textView_recieve_select);
//        tvPaySelect = (TextView)findViewById(R.id.textView_pay_select);
//        tvAllSelect = (TextView)findViewById(R.id.textView_all_select);
//        tvReceiveSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    tvReceiveSelect.setBackground(getResources().getDrawable(R.drawable.shape_circle));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        tvReceiveSelect.getBackground().setTint(getResources().getColor(R.color.primary_dark));
//                    }
//                    tvPaySelect.setBackground(null);
//                    tvAllSelect.setBackground(null);
//                }
//            }
//        });
//        tvPaySelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    tvPaySelect.setBackground(getResources().getDrawable(R.drawable.shape_circle));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        tvPaySelect.getBackground().setTint(getResources().getColor(R.color.primary_dark));
//                    }
//                    tvReceiveSelect.setBackground(null);
//                    tvAllSelect.setBackground(null);
//                }
//            }
//        });
//        tvAllSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    tvAllSelect.setBackground(getResources().getDrawable(R.drawable.shape_circle));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        tvAllSelect.getBackground().setTint(getResources().getColor(R.color.primary_dark));
//                    }
//                    tvReceiveSelect.setBackground(null);
//                    tvPaySelect.setBackground(null);
//                }
//            }
//        });
//
//        ll3.setOnClickListener(oclCollapse);




//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);




        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final LinearLayout layout =(LinearLayout)findViewById(R.id.linearLayout_cheque);
        final LinearLayout layout2 =(LinearLayout)findViewById(R.id.linearLayout_cash);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                Animation slideLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
                if (layout.getVisibility() == View.GONE) {
                    layout.startAnimation(slideUp);
                    layout.setVisibility(View.VISIBLE);
                    layout2.startAnimation(slideLeft);
                    layout2.setVisibility(View.VISIBLE);
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.shape_close));
                }else{
                    layout.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.shape_plus));
                }
            }
            });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    protected View setInflater(Context context, @LayoutRes int resource){

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,ns);

        return view;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
