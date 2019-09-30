package com.example.arsalan.mygym.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_EDIT_MODE;
import static com.example.arsalan.mygym.MyKeys.EXTRA_GALLERY_ARRAY;

public class GalleryActivity extends AppCompatActivity {
    private long mUserId;
    private int mCurrentPosition;
    private boolean mEditMode;
    private List<GalleryItem> mGalleryItems;
    private ViewPager mGalleryPager;
    private GalleryPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        Bundle xtras = getIntent().getExtras();

        mCurrentPosition = xtras.getInt(MyKeys.EXTRA_POSITION);
        mGalleryItems = xtras.getParcelableArrayList(EXTRA_GALLERY_ARRAY);
        mEditMode = xtras.getBoolean(EXTRA_EDIT_MODE, false);
        mGalleryPager = findViewById(R.id.vp_gallery);
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
        mAdapter = new GalleryPagerAdapter(mGalleryItems);

        mGalleryPager.setAdapter(mAdapter);
        setTitle(getString(R.string.gallery_position_from_total, (mCurrentPosition + 1), mGalleryItems.size()));

        mGalleryPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(getString(R.string.gallery_position_from_total, (position + 1), mGalleryItems.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mGalleryPager.setCurrentItem(mCurrentPosition);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_remove) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.remove_photo))
                    .setMessage(getString(R.string.ask_remove_photo))
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        removeFromGallery(mGalleryItems.get(mGalleryPager.getCurrentItem()).getId());
                    })
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel())
                    .create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (mEditMode) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_gallery, menu);
        }
        return true;
    }

    private void removeFromGallery(final long galleryId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Log.d(getClass().getSimpleName(), "removeFromGallery: id:" + galleryId);
        Call<RetroResult> call = apiService.removeFromMyGallery("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken()
                , galleryId);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                Log.d("removeFromGallery", "onResponse: id:" + galleryId);
                int position = mGalleryPager.getCurrentItem();
                mAdapter.destroyItem(mGalleryPager, position, mGalleryPager.getChildAt(position));
                mGalleryItems.remove(position);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Log.d("removeFromGallery", "onFailure: ");
            }
        });
    }

    public class GalleryPagerAdapter extends PagerAdapter {
        final List<GalleryItem> galleryItemList;

        public GalleryPagerAdapter(List<GalleryItem> galleryItemList) {
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
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView image = new ImageView(container.getContext());
            CircularProgressDrawable loadingDrawable = new CircularProgressDrawable(container.getContext());
            loadingDrawable.setBackgroundColor(Color.RED);
            loadingDrawable.setStrokeWidth(5f);
            loadingDrawable.setCenterRadius(30f);

            Glide.with(container.getContext())
                    .load(MyConst.BASE_CONTENT_URL + galleryItemList.get(position).getPictureUrl())
                    .apply(new RequestOptions().placeholder(loadingDrawable).fitCenter())
                    .into(image);
            container.addView(image);
            return image;
        }

        @Override
        public int getItemPosition(Object object) {
            if (galleryItemList.contains((View) object)) {
                return galleryItemList.indexOf((View) object);
            } else {
                return POSITION_NONE;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

