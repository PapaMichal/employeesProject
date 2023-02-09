package com.example.employeesassignment.employeelist;

import android.content.Context;
import android.widget.TextView;

public class SortFilterMultiChoiceSpinnerListener extends MultiSChoiceLSpinnerListener {
    private FilterOnClick filterOnClick;
    public SortFilterMultiChoiceSpinnerListener(TextView tv, String[] itemsArr, String title, Context context, FilterOnClick filterOnClick) {
        super(tv, itemsArr, title, context);
        this.neutralTextTitle = "Select All";
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
        for (int i = 0; i < itemsArr.length; i++) {
            chosenItemsList.add(itemsArr[i]);
        }
    }

    private void checkAllFields() {
        for (int i = 0; i < itemsArr.length; i++) {
            this.selectedItems[i] = true;
        }
    }

    @Override
    protected void setItem(int i, boolean b) {
        if (b) {
            chosenItemsList.add(itemsArr[i]);
        } else {
            chosenItemsList.remove(String.valueOf(itemsArr[i]));
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
