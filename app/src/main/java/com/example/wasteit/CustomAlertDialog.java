package com.example.wasteit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

public class CustomAlertDialog {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Context context;

    public CustomAlertDialog(Context context) {

        this.context = context;
    }

    public AlertDialog getDialog(){

        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.custom_loading_dialog, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
