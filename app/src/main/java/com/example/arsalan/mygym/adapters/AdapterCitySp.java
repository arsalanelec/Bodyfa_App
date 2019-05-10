package com.example.arsalan.mygym.adapters;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.arsalan.mygym.models.City;
import com.example.arsalan.mygym.models.CityNState;
import com.example.arsalan.mygym.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterCitySp implements SpinnerAdapter {

    List<City> CityList;
long provinceId;

    public AdapterCitySp(long provinceId) {
        CityList = new ArrayList<>();
        this.provinceId=provinceId;
        if(provinceId!=0)
            try {
                this.CityList.addAll(CityNState.getProvinceById(provinceId).getCities());

            }catch (Exception e){};
    }
public long getProvinceId(){return provinceId;}
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
        return CityList.size();
    }

    @Override
    public City getItem(int i) {
        return CityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return CityList.get(i).getId();
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
