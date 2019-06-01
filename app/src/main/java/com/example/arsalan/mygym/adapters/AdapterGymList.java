package com.example.arsalan.mygym.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.activities.ProfileGymActivity;
import com.example.arsalan.mygym.models.Gym;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import static com.example.arsalan.mygym.models.MyConst.BASE_API_URL;
import static com.example.arsalan.mygym.models.MyConst.BASE_CONTENT_URL;

/**
 * Created by Arsalan on 10-02-2018.
 */

public class AdapterGymList extends Adapter<AdapterGymList.VH> {
    List<Gym> gymList;
    Activity mActivity;

    public AdapterGymList(Activity activity, List<Gym> gymList) {
        this.gymList = gymList;
        this.mActivity = activity;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.item_gym, parent, false);

        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(final VH h, int position) {
        final Gym g = gymList.get(position);
        h.ratingBar.setRating((float) g.getRate());
        h.pointsTV.setText(String.valueOf(g.getPoint()));
        h.addressTV.setText(g.getAddress());
        h.gymNameTV.setText(g.getTitle());
        Glide.with(h.itemView.getContext())
                .load(BASE_CONTENT_URL + g.getThumbUrl())
                .apply(new RequestOptions().placeholder(R.drawable.avatar).centerCrop())
                .into(h.thumbImg);
/*
        ViewCompat.setTransitionName(h.thumbImg, g.getTitle());
*/

        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(mActivity, ProfileGymActivity.class);
                i.putExtra(MyKeys.EXTRA_OBJ_GYM, g);
               /* i.putExtra(EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(h.thumbImg));

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        mActivity,
                        h.thumbImg,
                        ViewCompat.getTransitionName(h.thumbImg));*/

                //   mActivity.startVideoRecorderActivity(i, options.toBundle());
                mActivity.startActivity(i);
            }
        });
        Log.d("AdapterGymList", "onBindViewHolder: rate:" + g.getRate());

    }

    @Override
    public int getItemCount() {
        return gymList.size();
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView thumbImg;
        TextView gymNameTV;
        TextView addressTV;
        TextView pointsTV;
        RatingBar ratingBar;

        public VH(View iv) {
            super(iv);
            thumbImg = iv.findViewById(R.id.imgThumb);
            gymNameTV = iv.findViewById(R.id.txtName);
            addressTV = iv.findViewById(R.id.txtTitle);
            pointsTV = iv.findViewById(R.id.txtPoints);
            ratingBar = iv.findViewById(R.id.ratingBar);
        }
    }

}
