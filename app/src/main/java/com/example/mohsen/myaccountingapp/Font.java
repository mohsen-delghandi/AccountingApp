package com.example.mohsen.myaccountingapp;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mohsen on 2017-04-30.
 */

public class Font extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSansWeb_UltraLight.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
