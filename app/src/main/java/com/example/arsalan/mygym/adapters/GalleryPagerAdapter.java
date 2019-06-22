package com.example.arsalan.mygym.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.MyConst;

import java.util.List;

public class GalleryPagerAdapter extends PagerAdapter {
    List<GalleryItem> galleryItemList;

    public GalleryPagerAdapter(List<GalleryItem> galleryItemList) {
        this.galleryItemList = galleryItemList;
    }

    @Override
    public int getCount() {
        if (galleryItemList == null) return 0;
        return galleryItemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o.equals(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView image = new ImageView(container.getContext());
        CircularProgressDrawable loadingDrawable = new CircularProgressDrawable(container.getContext());
        loadingDrawable.setStrokeWidth(5f);
        loadingDrawable.setCenterRadius(30f);

        Glide.with(container.getContext())
                .load(MyConst.BASE_CONTENT_URL + galleryItemList.get((getCount() - 1) - position).getPictureUrl())
                .apply(new RequestOptions().placeholder(loadingDrawable).centerCrop())
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
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}