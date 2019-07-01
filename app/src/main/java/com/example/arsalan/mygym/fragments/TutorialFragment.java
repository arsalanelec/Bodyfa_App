package com.example.arsalan.mygym.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.TutorialGroup;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TutorialFragment#newInstance} mFactory method to
 * create an instance of this fragment.
 */
public class TutorialFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private boolean mCanSendVideo;

    private View detailFragment;

    public TutorialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this mFactory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param canSendVideo Parameter 1.
     * @return A new instance of fragment TutorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorialFragment newInstance(boolean canSendVideo) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, canSendVideo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCanSendVideo = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tutorial, container, false);
        ListView listView = v.findViewById(R.id.lstTutorial);
        listView.setAdapter(new AdapterTutorial());
        detailFragment = v.findViewById(R.id.container2);
        v.setRotation(180);
        return v;
    }

    @Override
    //Pressed return button - returns to the results menu
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && detailFragment.getVisibility() == View.VISIBLE) {
                detailFragment.setVisibility(View.GONE);
                //your code

                return true;
            }
            return false;
        });
    }

    class AdapterTutorial extends BaseAdapter {
        final List<TutorialGroup> tutorialGroups = TutorialGroup.getList();

        public AdapterTutorial() {

        }

        @Override
        public int getCount() {
            return tutorialGroups.size();
        }

        @Override
        public Object getItem(int i) {
            return tutorialGroups.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.item_tutorial_cat, viewGroup, false);
            TextView nameTV = view.findViewById(R.id.txtName);
            ImageView thumb = view.findViewById(R.id.img_thumb);
            nameTV.setText(tutorialGroups.get(i).getName());
            thumb.setImageResource(tutorialGroups.get(i).getThumbRes());
            Button showItemsBtn = view.findViewById(R.id.btnShowItems);
            view.setOnClickListener(view1 -> {

                detailFragment.setVisibility(View.VISIBLE);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container2, TutorialListFragment.newInstance(tutorialGroups.get(i).getId(),tutorialGroups.get(i).getName(), mCanSendVideo))
                        .commit();

                // mListener.goToTutorialList(tutorialGroups.get(i).getId(),tutorialGroups.get(i).getName());
            });
            return view;
        }
    }
}
