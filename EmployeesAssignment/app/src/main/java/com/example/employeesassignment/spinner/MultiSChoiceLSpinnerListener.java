package com.example.employeesassignment.spinner;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class MultiSChoiceLSpinnerListener implements View.OnClickListener {
    protected boolean[] selectedItems;
    protected ArrayList<String> chosenItemsList;
    protected String title;
    protected TextView tv;
    protected String[] itemsArr;
    protected AlertDialog.Builder alertDialog;
    private final String neutralBtnText;

    public MultiSChoiceLSpinnerListener(TextView tv, String[] itemsArr, String title, Context context, String neutralBtnText) {
        this.tv = tv;
        this.itemsArr = itemsArr;
        this.title = title;
        this.selectedItems = new boolean[itemsArr.length];
        this.neutralBtnText = neutralBtnText;
        chosenItemsList = new ArrayList<>();
        this.alertDialog = buildAlertDialog(context);
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

    private AlertDialog.Builder buildAlertDialog(Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMultiChoiceItems(itemsArr, selectedItems, (dialogInterface, i, b) -> setItem(i, b));

        alertDialog.setPositiveButton("OK", (dialogInterface, i) -> positiveButtonAction(i));

        alertDialog.setNeutralButton(neutralBtnText, (dialogInterface, i) -> neutralButtonAction());
        return alertDialog;
    }

    @Override
    public void onClick(View view) {
        this.alertDialog.show();
    }
}