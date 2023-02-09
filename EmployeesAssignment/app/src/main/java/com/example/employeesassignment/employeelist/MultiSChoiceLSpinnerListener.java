package com.example.employeesassignment.employeelist;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

class MultiSChoiceLSpinnerListener implements View.OnClickListener {
    protected boolean[] selectedItems;
    protected ArrayList<String> chosenItemsList;
    protected String title;
    protected TextView tv;
    protected String[] itemsArr;
    protected Context context;
    protected String neutralTextTitle;
    protected AlertDialog.Builder builder;

    public MultiSChoiceLSpinnerListener(TextView tv, String[] itemsArr, String title, Context context) {
        this.tv = tv;
        this.itemsArr = itemsArr;
        this.title = title;
        this.context = context;
        this.selectedItems = new boolean[itemsArr.length];
        this.neutralTextTitle = "Clear";
        builder = new AlertDialog.Builder(context);
        chosenItemsList = new ArrayList<>();
    }

    public ArrayList<String> getChosenItemsList() {
        return chosenItemsList;
    }

    protected void setItem(int i, boolean b) {
        if (b) {
            chosenItemsList.add(itemsArr[i]);
        } else {
            chosenItemsList.remove(String.valueOf(itemsArr[i]));
        }
    }
    protected void positiveButtonAction(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < chosenItemsList.size(); j++) {
            stringBuilder.append(chosenItemsList.get(j));
            if (j != chosenItemsList.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        tv.setText(stringBuilder.toString());
    }

    protected void neutralButtonAction() {
        // use for loop
        for (int j = 0; j < selectedItems.length; j++) {
            selectedItems[j] = false;
            tv.setText("");
        }
        chosenItemsList.clear();
    }


    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMultiChoiceItems(itemsArr, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
               setItem(i, b);
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                positiveButtonAction(i);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton(neutralTextTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // use for loop
                neutralButtonAction();
            }
        });
        // show dialog
        builder.show();
    }
}