package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arsalan.mygym.models.MealRow;
import com.example.arsalan.mygym.R;

import java.util.ArrayList;
import java.util.List;


public class MealPlanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_IS_EDITABLE = "PARAM_IS_EDITABLE";

    private static final String ARG_LIST = "param list";

    // TODO: Rename and change types of parameters
    private int mDayOfWeek;

    private List<MealRow> mMealRowList;
    private OnFragmentInteractionListener mListener;

    public MealPlanFragment() {
        // Required empty public constructor
    }


    public static MealPlanFragment newInstance(int dayOfWeek) {
        MealPlanFragment fragment = new MealPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, dayOfWeek);
        fragment.setArguments(args);
        return fragment;
    }
    public static MealPlanFragment newInstance(int dayOfWeek, ArrayList<MealRow> mealRowList) {
        MealPlanFragment fragment = new MealPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, dayOfWeek);
        args.putParcelableArrayList(ARG_LIST, mealRowList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDayOfWeek = getArguments().getInt(ARG_PARAM1);
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
        View v = inflater.inflate(R.layout.fragment_meal_plan, container, false);


        ListView mealLV = v.findViewById(R.id.lstMeals);
        final AdapterMealLV adapterMeal = new AdapterMealLV(mMealRowList);
        mealLV.setAdapter(adapterMeal);



        v.setRotation(180);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
                view = View.inflate(getContext(), R.layout.item_meal_plan, null);
                String[] meanTypes = {getString(R.string.breakfest), getString(R.string.lunch), getString(R.string.diner), getString(R.string.snack),getString(R.string.supplements)};

                TextView descTV = view.findViewById(R.id.txtDescription);
                descTV.setText(mealRows.get(i).getDesc());


                TextView titleTV = view.findViewById(R.id.txtTitle);
                titleTV.setText(meanTypes[getItem(i).getType()]);

                FrameLayout frameLayout = view.findViewById(R.id.flMealHead);
                ImageView imgThum = view.findViewById(R.id.img_thumb);

                switch (getItem(i).getType()){
                    case 0: //صبحانه
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.green));
                        imgThum.setImageResource(R.drawable.breakfast);
                        break;
                    case 1: //نهار
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.red));
                        imgThum.setImageResource(R.drawable.lunch);
                        break;
                    case 2: //شام
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        imgThum.setImageResource(R.drawable.dinner);
                        break;
                    case 3://میان وعده
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                        imgThum.setImageResource(R.drawable.dish);
                        break;
                    case 4://مکمل
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
                        imgThum.setImageResource(R.drawable.protein);
                        break;

                }
            }

            return view;
        }
    }
}
