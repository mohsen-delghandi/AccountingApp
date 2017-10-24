package com.example.mohsen.myaccountingapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Mohsen on 2017-07-15.
 */

public class EditTextDialogClass extends Dialog {

    public Context context;
    public TextView yes,no;
    public EditText et;

    public EditTextDialogClass(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_text_dialog_class);

        yes = (TextView) findViewById(R.id.textView_ok);
        no = (TextView) findViewById(R.id.textView_cancel);
        et = (EditText) findViewById(R.id.editText_dialog);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}