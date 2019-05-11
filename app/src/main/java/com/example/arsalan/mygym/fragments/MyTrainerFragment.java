package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.activities.MessageRoomActivity;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.dialog.RateDialog;
import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.Honor;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.viewModels.GalleryViewModel;
import com.example.arsalan.mygym.viewModels.HonorViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static com.example.arsalan.mygym.MyKeys.EXTRA_PARTY_ID;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PARTY_NAME;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PARTY_THUMB;
import static com.example.arsalan.mygym.MyKeys.EXTRA_USER_ID;


public class MyTrainerFragment extends Fragment implements Injectable {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ATHLETE = "param1";
    @Inject
    MyViewModelFactory factory;
    private User mCurrentAthlete;
    private OnFragmentInteractionListener mListener;
    private TrainerViewModel trainerViewModel;
    private HonorViewModel honorViewModel;
    private TextView nameTV;
    private RatingBar ratingBar;
    private View rateBtn;
    private View selectATrainer;
    private Button sendMessageBtn;
    private ViewPager mGalleryPager;
    private ArrayList<GalleryItem> mGalleryItemList;
    private GalleryPagerAdapter mGalleryAdapter;
    private GalleryViewModel galleryViewModel;
    private ArrayList<Honor> mHonorList;
    private AdapterHonors mHonorAdapter;
    private RecyclerView honorRV;

    public MyTrainerFragment() {
        // Required empty public constructor
    }


    public static MyTrainerFragment newInstance(User athlete) {
        MyTrainerFragment fragment = new MyTrainerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ATHLETE, athlete);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentAthlete = getArguments().getParcelable(ARG_ATHLETE);

        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_my_trainer, container, false);
        nameTV = v.findViewById(R.id.txtName);
        rateBtn = v.findViewById(R.id.flRate);
        ratingBar = v.findViewById(R.id.ratingBar);
        selectATrainer = v.findViewById(R.id.txtTrainerNotSelected);
        sendMessageBtn = v.findViewById(R.id.btnSendMessage);
        mGalleryPager = v.findViewById(R.id.vpGallery);
        TabLayout tabLayout = v.findViewById(R.id.tablayout);
        mGalleryPager.setOnTouchListener((v1, motionEvent) -> {
            Log.d("setOnTouchListener", "onTouch: " + motionEvent.toString());
            if (
                    motionEvent.getAction() == MotionEvent.ACTION_DOWN &&
                            v1 instanceof ViewPager
            ) {
                ((ViewPager) v1).requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
        tabLayout.setupWithViewPager(mGalleryPager);
        mGalleryItemList = new ArrayList<GalleryItem>();
        mGalleryAdapter = new GalleryPagerAdapter(mGalleryItemList);
        mGalleryPager.setAdapter(mGalleryAdapter);

        honorRV = v.findViewById(R.id.rvHonor);
        mHonorList = new ArrayList<>();
        mHonorAdapter = new AdapterHonors(mHonorList);
        GridLayoutManager linearLayout = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        honorRV.setLayoutManager(linearLayout);
        honorRV.setAdapter(mHonorAdapter);

        honorRV.setVisibility(View.GONE);
        // getHonorsWeb(mCurrentAthlete.getTrainerId());

        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trainerViewModel = ViewModelProviders.of(this, factory).get(TrainerViewModel.class);
        trainerViewModel.init(mCurrentAthlete.getTrainerId());
        trainerViewModel.getTrainer().observe(this, trainer -> {
            if (trainer != null) {
                Log.d(getClass().getSimpleName(), "observe: trainer");
                nameTV.setText(trainer.getName());

                ratingBar.setRating(trainer.getRate());
                rateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RateDialog dialog = RateDialog.newInstance(mCurrentAthlete.getId(), trainer.getId());
                        dialog.show(getFragmentManager(), "");
                    }
                });
                sendMessageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_USER_ID, mCurrentAthlete.getId());
                        intent.putExtra(EXTRA_PARTY_ID, trainer.getId());
                        intent.putExtra(EXTRA_PARTY_NAME, trainer.getName());
                        intent.putExtra(EXTRA_PARTY_THUMB, trainer.getThumbUrl());
                        intent.setClass(getActivity(), MessageRoomActivity.class);
                        startActivity(intent);
                    }
                });
                selectATrainer.setVisibility(View.GONE);
            }
        });
        galleryViewModel = ViewModelProviders.of(this, factory).get(GalleryViewModel.class);
        galleryViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mCurrentAthlete.getTrainerId());
        galleryViewModel.getGalleryItemList().observe(this, galleryItems -> {
            Log.d("onActivityCreated", "observe: ");
            mGalleryItemList.removeAll(mGalleryItemList);
            mGalleryItemList.addAll(galleryItems);
            mGalleryAdapter.notifyDataSetChanged();
            // waitingFL.setVisibility(View.GONE);
        });


        honorViewModel = ViewModelProviders.of(this, factory).get(HonorViewModel.class);
        honorViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mCurrentAthlete.getTrainerId());
        honorViewModel.getHonorList().observe(this, honors -> {
            if (honors != null) {
                Log.d(getClass().getSimpleName(), "observe: honor");
                mHonorList.removeAll(mHonorList);
                mHonorList.addAll(honors);
                mHonorAdapter.notifyDataSetChanged();
                honorRV.setVisibility(View.VISIBLE);
            }
        });


        Log.d(getClass().getSimpleName(), "onActivityCreated: ");
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

    private float convertDpToPix(int dp) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class GalleryPagerAdapter extends PagerAdapter {
        ArrayList<GalleryItem> galleryItemList;

        public GalleryPagerAdapter(ArrayList<GalleryItem> galleryItemList) {
            this.galleryItemList = galleryItemList;
        }

        @Override
        public int getCount() {
            return galleryItemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ImageView image = new ImageView(container.getContext());
            CircularProgressDrawable loadingDrawable = new CircularProgressDrawable(getContext());

            loadingDrawable.setStrokeWidth(convertDpToPix(2));
            loadingDrawable.setCenterRadius(convertDpToPix(12));
            loadingDrawable.start();
            Glide.with(getContext())
                    .load(MyConst.BASE_CONTENT_URL + galleryItemList.get((getCount() - 1) - position).getPictureUrl())
                    .apply(new RequestOptions()
                            .placeholder(loadingDrawable)
                            .error(R.drawable.bodybuilder_place_holder)
                            .centerCrop())
                    .into(image);

            container.addView(image);
/*            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), GalleryActivity.class);
                    intent.putExtra(EXTRA_POSITION, position);
                    intent.putExtra(EXTRA_GALLERY_ARRAY, galleryItemList);
                    intent.putExtra(EXTRA_EDIT_MODE, true);
                    startVideoRecorderActivity(intent);


                }
            });*/
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class AdapterHonors extends RecyclerView.Adapter<AdapterHonors.ViewHolder> {
        List<Honor> honorList;

        public AdapterHonors(List<Honor> honorList) {
            this.honorList = honorList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.item_honor_grid, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.titleTv.setText(honorList.get(position).getTitle());
            int medalImageRes;
            switch (honorList.get(position).getCategory()) {
                case 0:
                    medalImageRes = R.drawable.medal_jahani_1;
                    break;
                case 1:
                    medalImageRes = R.drawable.medal_jahani_2;
                    break;
                case 2:
                    medalImageRes = R.drawable.medal_jahani_3;
                    break;
                case 3:
                    medalImageRes = R.drawable.medal_keshvari_1;
                    break;
                case 4:
                    medalImageRes = R.drawable.medal_keshvari_2;
                    break;
                case 5:
                    medalImageRes = R.drawable.medal_keshvari_3;
                    break;
                case 6:
                    medalImageRes = R.drawable.medal_ostani_1;
                    break;
                case 7:
                    medalImageRes = R.drawable.medal_ostani_2;
                    break;
                case 8:
                    medalImageRes = R.drawable.medal_ostani_3;
                    break;
                default:
                    medalImageRes = R.drawable.medal_ostani_3;
                    break;
            }
            holder.medalImg.setImageResource(medalImageRes);

        }

        @Override
        public int getItemCount() {
            return honorList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTv;
            ImageView medalImg;

            public ViewHolder(View itemView) {
                super(itemView);
                titleTv = itemView.findViewById(R.id.txtTitle);
                medalImg = itemView.findViewById(R.id.imgMedal);

            }
        }
    }

}