package com.example.arsalan.mygym.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.Honor;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.RetHonorList;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_GALLERY_ARRAY;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PARCLABLE_OBJ;
import static com.example.arsalan.mygym.MyKeys.EXTRA_POSITION;

public class ProfileTrainerActivity extends AppCompatActivity {
    static public final String KEY_NAME = "key name";
    static public final String KEY_RATE = "key rate";
    private static final int REQ_SET_RATE = 1000;

    private ArrayList<Honor> mHonorList;
    private AdapterHonors mHonorAdapter;
    private RecyclerView honorRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_trainer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  supportPostponeEnterTransition();

        //ImageView imgTrainer = findViewById(R.id.imgTrainer);

        Bundle extras = getIntent().getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //  String imageTransitionName = extras.getString(EXTRA_IMAGE_TRANSITION_NAME);
            //  imgTrainer.setTransitionName(imageTransitionName);
        }
        Trainer t = extras.getParcelable(EXTRA_PARCLABLE_OBJ);

        TextView nameTV = findViewById(R.id.txtName);

        TextView workoutPlanPriceTV = findViewById(R.id.txtWorkoutPlanPrice);
        workoutPlanPriceTV.setText(String.format(Locale.US, getString(R.string.workout_plan_price), t.getWorkoutPlanPrice()));

        TextView mealPlanPrice = findViewById(R.id.txtMealPlanPrice);
        mealPlanPrice.setText(String.format(Locale.US, getString(R.string.meal_plan_price), t.getMealPlanPrice()));


        RatingBar ratingBar = findViewById(R.id.ratingBar);

        if (t != null) {
            nameTV.setText(t.getName());
            ratingBar.setRating(t.getRate());

/*            imgTrainer.setController(Fresco.newDraweeControllerBuilder().setControllerListener(new ControllerListener<ImageInfo>() {
                @Override
                public void onSubmit(String id, Object callerContext) {

                }

                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                    supportStartPostponedEnterTransition();

                }

                @Override
                public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                  //  supportStartPostponedEnterTransition();

                }

                @Override
                public void onIntermediateImageFailed(String id, Throwable throwable) {

                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    supportStartPostponedEnterTransition();

                }

                @Override
                public void onRelease(String id) {
                  //  supportStartPostponedEnterTransition();

                }
            }).build());*/
            /*Glide.with(this)
                    .load(BASE_API_URL + t.getPictureUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.avatar).centerCrop())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@android.support.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                          //  supportStartPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                      //      supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(imgTrainer);*/

            //  imgTrainer.setImageURI(BASE_API_URL+t.getPictureUrl());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager mGalleryPager = findViewById(R.id.vpGallery);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        mGalleryPager.setOnTouchListener((v, motionEvent) -> {
            if (
                    motionEvent.getAction() == MotionEvent.ACTION_DOWN &&
                            v instanceof ViewGroup
            ) {
                ((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
        tabLayout.setupWithViewPager(mGalleryPager);
        MyWebService.getGalleryWeb(
                "Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken()
                , t.getId(), galleryItems -> mGalleryPager.setAdapter(new GalleryPagerAdapter(galleryItems)));


        honorRV = findViewById(R.id.rvHonor);
        mHonorList = new ArrayList<>();
        mHonorAdapter = new AdapterHonors(mHonorList);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, GridLayoutManager.HORIZONTAL, false);
        honorRV.setLayoutManager(linearLayout);
        honorRV.setAdapter(mHonorAdapter);

        honorRV.setVisibility(View.GONE);
        getHonorsWeb(t.getId());
    }

    private void getHonorsWeb(long userId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetHonorList> call = apiService.getHonorList("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), userId);
        call.enqueue(new Callback<RetHonorList>() {
            @Override
            public void onResponse(Call<RetHonorList> call, Response<RetHonorList> response) {
                if (response.isSuccessful()) {
                    if (response.body().getRecordsCount() > 0) {
                        mHonorList.removeAll(mHonorList);
                        mHonorList.addAll(response.body().getRecords());
                        mHonorAdapter.notifyDataSetChanged();
                        honorRV.setVisibility(View.VISIBLE);
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<RetHonorList> call, Throwable t) {

            }
        });

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

            final ImageView image = new ImageView(container.getContext());
            CircularProgressDrawable loadingDrawable = new CircularProgressDrawable(container.getContext());
            loadingDrawable.setStrokeWidth(2 * getResources().getDisplayMetrics().density);
            loadingDrawable.setCenterRadius(48 * getResources().getDisplayMetrics().density);

            Glide.with(container.getContext())
                    .load(MyConst.BASE_CONTENT_URL + galleryItemList.get(position).getPictureUrl())
                    .apply(new RequestOptions().placeholder(loadingDrawable).centerCrop())
                    .into(image);
            container.addView(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(ProfileTrainerActivity.this, GalleryActivity.class);
                    intent.putExtra(EXTRA_POSITION, position);
                    intent.putExtra(EXTRA_GALLERY_ARRAY, galleryItemList);
                    startActivity(intent);


                }
            });
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
        public AdapterHonors.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.item_honor_grid, parent, false);

            return new AdapterHonors.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterHonors.ViewHolder holder, int position) {

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
