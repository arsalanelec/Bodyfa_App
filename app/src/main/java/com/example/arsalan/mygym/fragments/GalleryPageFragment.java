package com.example.arsalan.mygym.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.MyConst;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public GalleryPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryPageFragment newInstance(String param1, String param2) {
        GalleryPageFragment fragment = new GalleryPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gallery_page, container, false);
        CircularProgressDrawable loadingDrawable = new CircularProgressDrawable(container.getContext());
        loadingDrawable.setStrokeWidth(5f);
        loadingDrawable.setCenterRadius(30f);
        ImageView image=v.findViewById(R.id.image);
        Glide.with(getContext())
                .load(MyConst.BASE_CONTENT_URL +mParam1)
                .apply(new RequestOptions().placeholder(loadingDrawable).centerCrop())
                .into(image);
        return v;
    }

}
