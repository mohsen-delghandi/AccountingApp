package com.example.mohsen.myaccountingapp;

import android.content.Context;
import android.util.Log;

import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.internal.BacktoryClient;
import com.backtory.java.internal.BacktoryObject;
import com.backtory.java.internal.BacktoryResponse;
import com.backtory.java.internal.BacktoryUser;
import com.backtory.java.internal.Config;
import com.backtory.java.internal.LoginResponse;

import static android.content.ContentValues.TAG;

/**
 * Created by Mohsen on 2017-08-29.
 */

public class MainApplication extends Font {

    public Context mContext;
    String mTitle,mIp;

    public MainApplication(Context context, String title, String ip) {
        mContext = context;
        mTitle = title;
        mIp = ip;
    }

    public MainApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Initializing backtory
        BacktoryClient.Android.init(Config.newBuilder().
                // Enabling User Services
                        initAuth("59bb81cbe4b05384c5920067",
                        "59bb81cbe4b0416729e792d7").
                // Finilizing sdk
                        initObjectStorage("59bb81cce4b055bfe6ed78a1").
                        initFileStorage("59bb8307e4b05384c5920a18").
                        build(), this);
    }

    public void loginBacktory(){
        // Request a guest user from backtory
        BacktoryUser.loginAsGuestInBackground(new BacktoryCallBack<LoginResponse>() {

            // Login done (fail or success), checking for result
            @Override
            public void onResponse(BacktoryResponse<LoginResponse> response) {
                // Checking if operation was successful
                if (response.isSuccessful()) {
                    // Getting new username and password from CURRENT user
                    String newUsername = BacktoryUser.getCurrentUser().getUsername();
                    String newPassword = BacktoryUser.getCurrentUser().getGuestPassword();

                    // Logging new username and password
                    Log.d(TAG, "your guest username: " + newUsername
                            + " & your guest password: " + newPassword);
                } else {
                    // Operation generally failed, maybe internet connection issue
                    Log.d(TAG, "Login failed for other reasons like network issues");
                }
            }
        });
    }

    public void loginAndSendInfo(){

        // Get current user from backtory
        BacktoryUser currentUser = BacktoryUser.getCurrentUser();

        // Check if currentUser is not empty
        if (currentUser != null) {
            // Log the result to output
            String firstname = currentUser.getFirstName();
            String username = currentUser.getUsername();

//            Toast.makeText(mContext, "user is logged in", Toast.LENGTH_SHORT).show();
//
//            Log.d(TAG, "firstname: " + firstname + " and username: " + username);
        } else {

            // Request a guest user from backtory
            BacktoryUser.loginAsGuestInBackground(new BacktoryCallBack<LoginResponse>() {

                // Login done (fail or success), checking for result
                @Override
                public void onResponse(BacktoryResponse<LoginResponse> response) {
                    // Checking if operation was successful
                    if (response.isSuccessful()) {
                        // Getting new username and password from CURRENT user
                        String newUsername = BacktoryUser.getCurrentUser().getUsername();
                        String newPassword = BacktoryUser.getCurrentUser().getGuestPassword();

//                        TelephonyManager mngr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);

                        BacktoryObject note = new BacktoryObject("Devices");
//                        try {
//                            note.put("IMEI", mngr.getDeviceId());
//                        }catch (Exception e){
//                            Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
//                        }
                        note.put("userName", newUsername);
                        note.put("ip", mIp);
                        note.put("title", mTitle);
                        note.saveInBackground(new BacktoryCallBack<Void>() {
                            @Override
                            public void onResponse(BacktoryResponse<Void> response) {
//                                if (response.isSuccessful()){
//                                    Toast.makeText(mContext, "INFO SENT", Toast.LENGTH_SHORT).show();
//                                }
//                                else {
//                                    Toast.makeText(mContext , "INFO not SENT", Toast.LENGTH_SHORT).show();
//                                }
                            }
                        });

                        // Logging new username and password
//                        Log.d(TAG, "your guest username: " + newUsername
//                                + " & your guest password: " + newPassword);
//
//                        Toast.makeText(mContext, "login ok", Toast.LENGTH_LONG).show();
                    } else {
                        // Operation generally failed, maybe internet connection issue
//                        Log.d(TAG, "Login failed for other reasons like network issues");
//                        Toast.makeText(mContext, "login no", Toast.LENGTH_LONG).show();
                    }
                }
            });
            // No user is logged-in
//            Log.d(TAG, "No user is logged in right now");
        }
    }
}

