package com.example.employeesassignment.spinner;

import android.content.Context;
import android.widget.TextView;

import java.util.Collections;

public class SortFilterMultiChoiceSpinnerListener extends MultiSChoiceLSpinnerListener {
    private final FilterOnClick filterOnClick;
    public SortFilterMultiChoiceSpinnerListener(TextView tv, String[] itemsArr, String title, Context context, FilterOnClick filterOnClick) {
        super(tv, itemsArr, title, context, "Select All");
        this.filterOnClick = filterOnClick;
        checkAllFields();
        fillItemsList();
        tv.setText("All");
    }

    public boolean areAllFieldsChecked() {
        return chosenItemsList.size() == itemsArr.length;
    }

    private void fillItemsList() {
        chosenItemsList.clear();
        Collections.addAll(chosenItemsList, itemsArr);
    }

    private void checkAllFields() {
        for (int i = 0; i < itemsArr.length; i++) {
            this.selectedItems[i] = true;
        }
    }

    @Override
    protected void positiveButtonAction(int i) {
        if (areAllFieldsChecked()) {
            tv.setText("All");
        }
        else
        {
            super.positiveButtonAction(i);
        }
        filterOnClick.filterList();
    }

    @Override
    protected void neutralButtonAction() {
        checkAllFields();
        fillItemsList();
        tv.setText("All");
        filterOnClick.filterList();
    }
}
