package com.example.mohsen.myaccountingapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Mohsen on 2017-07-15.
 */

public class ConfirmDialogClass extends Dialog {

    public Context context;
    public TextView yes,no,text;

    public ConfirmDialogClass(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.confirm_dialog_layout);

        yes = (TextView) findViewById(R.id.textView_ok);
        no = (TextView) findViewById(R.id.textView_cancel);
        text = (TextView) findViewById(R.id.textView_dialog_text);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}