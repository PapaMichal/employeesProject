package com.example.employeesassignment.addemployee;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

class SingleChoiceSpinnerListener implements View.OnClickListener {
    TextView tv;
    String[] itemsArr;
    String title;
    String chosen;
    Context context;

    public SingleChoiceSpinnerListener(TextView tv, String[] itemsArr, String title, Context context) {
        this.tv = tv;
        this.itemsArr = itemsArr;
        this.title = title;
        this.context = context;
    }
    @Override
    public void onClick (View view){
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(false);

        builder.setSingleChoiceItems(itemsArr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chosen = itemsArr[i];
                tv.setText(itemsArr[i]);
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tv.setText(chosen.toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}