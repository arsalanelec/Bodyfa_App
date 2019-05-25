package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.arsalan.mygym.dialog.SelectTrainerJoinTimeDialog;
import com.example.arsalan.mygym.models.MealRow;
import com.example.arsalan.mygym.R;

import java.util.ArrayList;
import java.util.List;


public class NewMealPlanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_IS_EDITABLE = "PARAM_IS_EDITABLE";

    private static final String ARG_LIST = "param list";

    // TODO: Rename and change types of parameters
    private int mDayOfWeek;

    private boolean mIsEditable;
    private List<MealRow> mMealRowList;
    private OnFragmentInteractionListener mListener;

    public NewMealPlanFragment() {
        // Required empty public constructor
    }


    public static NewMealPlanFragment newInstance(int dayOfWeek,boolean isEditable) {
        NewMealPlanFragment fragment = new NewMealPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, dayOfWeek);
        args.putBoolean(ARG_IS_EDITABLE,isEditable);
        fragment.setArguments(args);
        return fragment;
    }
    public static NewMealPlanFragment newInstance(int dayOfWeek,ArrayList<MealRow> mealRowList,boolean isEditable) {
        NewMealPlanFragment fragment = new NewMealPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, dayOfWeek);
        args.putBoolean(ARG_IS_EDITABLE,isEditable);
        args.putParcelableArrayList(ARG_LIST, mealRowList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDayOfWeek = getArguments().getInt(ARG_PARAM1);
            mIsEditable = getArguments().getBoolean(ARG_IS_EDITABLE);
            if(getArguments().getParcelableArrayList(ARG_LIST)!=null){
                mMealRowList =getArguments().getParcelableArrayList(ARG_LIST);
            }else {
                mMealRowList = new ArrayList<>();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_meal_plan, container, false);

        ListView mealLV = v.findViewById(R.id.lstMeals);
        final AdapterMealLV adapterMeal = new AdapterMealLV(mMealRowList);
        mealLV.setAdapter(adapterMeal);


        Button newRowBtn = v.findViewById(R.id.btnNewRow);
        newRowBtn.setVisibility(mIsEditable ? View.VISIBLE : View.GONE);
        newRowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMealRowList.add(new MealRow());
                adapterMeal.notifyDataSetChanged();

            }
        });
        v.setRotation(180);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void setNewMeal(int dayOfWeek, List<MealRow> mealRowList);
    }

    private class AdapterMealLV extends BaseAdapter {
        List<MealRow> mealRows;

        public AdapterMealLV(List<MealRow> mealRows) {
            this.mealRows = mealRows;
        }

        @Override
        public int getCount() {
            return mealRows.size();
        }

        @Override
        public MealRow getItem(int i) {
            return mealRows.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getContext(), R.layout.item_new_meal, null);
                EditText descTV = view.findViewById(R.id.txtDescription);
                if(!mIsEditable)descTV.setInputType( InputType.TYPE_NULL);
                descTV.setText(mealRows.get(i).getDesc());
                descTV.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int j, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int j, int i1, int i2) {
                        getItem(i).setDesc(charSequence.toString());
                        mListener.setNewMeal(mDayOfWeek, mMealRowList);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                Spinner mealTypeSpn = view.findViewById(R.id.spnMealType);
                mealTypeSpn.setEnabled(mIsEditable);
                String[] meanTypes = {getString(R.string.breakfest), getString(R.string.lunch), getString(R.string.diner), getString(R.string.snack),getString(R.string.supplements)};
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, meanTypes);
                mealTypeSpn.setAdapter(stringArrayAdapter);
                mealTypeSpn.setSelection(mealRows.get(i).getType());
                mealTypeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int j, long id) {
                        getItem(i).setType(((Spinner) adapterView).getSelectedItemPosition());
                        mListener.setNewMeal(mDayOfWeek, mMealRowList);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            return view;
        }
    }
}
