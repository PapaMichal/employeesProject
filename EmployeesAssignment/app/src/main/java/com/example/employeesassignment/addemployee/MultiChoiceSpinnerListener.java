package com.example.employeesassignment.addemployee;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Collections;

class MultiSChoiceLSpinnerListener implements View.OnClickListener {
    private boolean[] selectedItems;
    String title;
    TextView tv;
    private ArrayList<Integer> itemsList;
    String[] itemsArr;
    Context context;

    public MultiSChoiceLSpinnerListener(TextView tv, String[] itemsArr, String title, Context context) {
        this.tv = tv;
        this.itemsArr = itemsArr;
        this.title = title;
        this.itemsList = new ArrayList<>();
        this.context = context;
        this.selectedItems = new boolean[itemsArr.length];
    }

    public String[] getChosenOptions() {
        String[] chosenItemsList = new String[itemsList.size()];
        for (int i = 0; i < itemsList.size(); i++) {
            chosenItemsList[i] = itemsArr[itemsList.get(i)];
        }
        return chosenItemsList;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMultiChoiceItems(itemsArr, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                    itemsList.add(i);
                    Collections.sort(itemsList);
                } else {
                    itemsList.remove(Integer.valueOf(i));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder stringBuilder = new StringBuilder();
                // use for loop
                for (int j = 0; j < itemsList.size(); j++) {
                    stringBuilder.append(itemsArr[itemsList.get(j)]);
                    if (j != itemsList.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }
                tv.setText(stringBuilder.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // use for loop
                for (int j = 0; j < selectedItems.length; j++) {
                    // remove all selection
                    selectedItems[j] = false;
                    // clear language list
                    itemsList.clear();
                    // clear text view value
                    tv.setText("");
                }
            }
        });
        // show dialog
        builder.show();
    }
}