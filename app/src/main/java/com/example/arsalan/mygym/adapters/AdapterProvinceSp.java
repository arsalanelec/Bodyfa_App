package com.example.arsalan.mygym.adapters;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.arsalan.mygym.models.CityNState;
import com.example.arsalan.mygym.models.Province;
import com.example.arsalan.mygym.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterProvinceSp implements SpinnerAdapter {

    List<Province> provinceList;

    public AdapterProvinceSp() {
        provinceList = new ArrayList<>();
        this.provinceList.addAll(CityNState.getProvinceList());
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spinner_date_drop, viewGroup, false);
        }
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(getItem(i).getName());
        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return provinceList.size();
    }

    @Override
    public Province getItem(int i) {
        return provinceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return provinceList.get(i).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spinner_date, viewGroup, false);
        }
        TextView textView = view.findViewById(R.id.textView);

        textView.setText(getItem(i ).getName());
        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
