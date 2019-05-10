package com.example.arsalan.mygym.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class SelectableRow extends BaseObservable {
    boolean isSelected;

    public SelectableRow() {
    }

    public SelectableRow(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Bindable
    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        if(this.isSelected!=selected) {
            isSelected = selected;
            notifyChange();
        }
    }
}
