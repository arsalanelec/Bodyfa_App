package com.example.arsalan.mygym.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.activities.NewsDetailActivity;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.News;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

/**
 * Created by Arsalan on 10-02-2018.
 */

public class AdapterNews extends Adapter<AdapterNews.VH> {
    private static final String TAG = "AdapterNews";
    List<News> newsList;
    Activity mActivity;

    public AdapterNews(Activity activity, List<News> newsList) {
        this.newsList = newsList;
        this.mActivity = activity;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.item_news, parent, false);
        /*Animation animation = null;
        animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.wave);
        animation.setDuration(200);
        itemView.startAnimation(animation);
        animation = null;*/
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(final VH h, int position) {
        final News news = newsList.get(position);
        h.titleTV.setText(news.getTitle());
        h.viewCntTV.setText(String.valueOf(news.getVisitcnt()));
        h.likeCntTV.setText(String.valueOf(news.getLikeCnt()));
        h.commentCntTV.setText(String.valueOf(news.getCommentCnt()));
        h.dateTV.setText(news.getDate());
        h.thumb.setImageURI(MyConst.BASE_CONTENT_URL + news.getThumbUrl());
        h.avatar.setImageURI(MyConst.BASE_CONTENT_URL + news.getUserThumbUrl());
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: newId:" + news.getId());
                Intent i = new Intent();
                i.setClass(h.itemView.getContext(), NewsDetailActivity.class);
                i.putExtra(NewsDetailActivity.KEY_NEWS_ID, news.getId());
                h.itemView.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView viewCntTV;
        TextView likeCntTV;
        TextView commentCntTV;
        TextView dateTV;
        SimpleDraweeView thumb;
        SimpleDraweeView avatar;

        public VH(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.txtTitle);
            viewCntTV = itemView.findViewById(R.id.txtViewCnt);
            likeCntTV = itemView.findViewById(R.id.txtLikeCnt);
            commentCntTV = itemView.findViewById(R.id.txtCommentCnt);
            dateTV = itemView.findViewById(R.id.txtDate);
            thumb = itemView.findViewById(R.id.imgThumb);
            avatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
