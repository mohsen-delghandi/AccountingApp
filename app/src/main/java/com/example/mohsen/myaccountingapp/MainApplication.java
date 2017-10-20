package com.example.mohsen.myaccountingapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.internal.BacktoryClient;
import com.backtory.java.internal.BacktoryObject;
import com.backtory.java.internal.BacktoryQuery;
import com.backtory.java.internal.BacktoryResponse;
import com.backtory.java.internal.BacktoryUser;
import com.backtory.java.internal.Config;
import com.backtory.java.internal.LoginResponse;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Mohsen on 2017-08-29.
 */

public class MainApplication extends Font {

    SQLiteDatabase dbPermission;
    String permission;

    public MainApplication() {
    }

    public void loginAndGetPermission(final Activity activity){

        BacktoryUser.loginAsGuestInBackground(new BacktoryCallBack<LoginResponse>() {
            // Login done (fail or success), checking for result
            @Override
            public void onResponse(BacktoryResponse<LoginResponse> response) {
                // Checking if operation was successful
                if (response.isSuccessful()) {
                    BacktoryQuery todoQuery = new BacktoryQuery("permissions");
                    todoQuery.whereEqualTo("User", "Abed");
                    todoQuery.findInBackground(new BacktoryCallBack<List<BacktoryObject>>() {
                        @Override
                        public void onResponse(BacktoryResponse<List<BacktoryObject>> response) {
                            if (response.isSuccessful()) {
                                List<BacktoryObject> todoNotes = response.body();
                                for (BacktoryObject todo : todoNotes) {
                                    permission = todo.getString("Permission");
                                    SQLiteDatabase dbPermission = new PDatabase(activity).getWritableDatabase();
                                    ContentValues cvPermission = new ContentValues();
                                    cvPermission.put("Permission", permission);
                                    dbPermission.update("Permission_TBL", cvPermission, "Id = 1", null);
                                    dbPermission.close();
                                }
                            } else {
                                String r = response.message();
                            }
                        }
                    });
                } else {
                    // Operation generally failed, maybe internet connection issue
                    Log.d(TAG, "Login failed for other reasons like network issues");
                }
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initializing backtory
        BacktoryClient.Android.init(Config.newBuilder().
                // Enabling User Services
                        initAuth("59e9d81fe4b03ffa02f97ef9",
                        "59e9d81fe4b0f840c1225757").
                // Finilizing sdk
                        initObjectStorage("59e9d81fe4b03ffa02f97efa").
//                        initFileStorage("59bb8307e4b05384c5920a18").
                        build(), this);
    }
}

