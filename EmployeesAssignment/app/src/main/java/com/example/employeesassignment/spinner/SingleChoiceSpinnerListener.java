package com.example.employeesassignment.spinner;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class SingleChoiceSpinnerListener implements View.OnClickListener {
    TextView tv;
    String[] itemsArr;
    String title;
    String chosen;
    AlertDialog.Builder alertDialog;


    public SingleChoiceSpinnerListener(TextView tv, String[] itemsArr, String title, Context context) {
        this.tv = tv;
        this.itemsArr = itemsArr;
        this.title = title;
        alertDialog = buildAlertDialog(context);
    }

    private AlertDialog.Builder buildAlertDialog(Context context) {
        // Initialize alert dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);

        alertDialog.setSingleChoiceItems(itemsArr, -1, (dialogInterface, i) -> {
            chosen = itemsArr[i];
            tv.setText(itemsArr[i]);
        });

        alertDialog.setPositiveButton("OK", (dialogInterface, i) -> tv.setText(chosen));
        return alertDialog;
    }

    @Override
    public void onClick (View view){
        alertDialog.show();
    }
}