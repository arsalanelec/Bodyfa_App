package com.example.arsalan.mygym.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.NewsHead;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

/**
 * Created by Arsalan on 10-02-2018.
 */

public class AdapterNews extends Adapter<AdapterNews.VH> {
    private static final String TAG = "AdapterDashboardNews";
    private final List<NewsHead> newsList;
    private final OnAdapterNewsEventListener eventListener;

    public AdapterNews(List<NewsHead> newsList,OnAdapterNewsEventListener onAdapterNewsEventListener) {
        this.newsList = newsList;
        eventListener=onAdapterNewsEventListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        /*Animation animation = null;
        animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.wave);
        animation.setDuration(200);
        itemView.startAnimation(animation);
        animation = null;*/
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(final VH h, int position) {
        final NewsHead newsHead = newsList.get(position);
        h.titleTV.setText(newsHead.getTitle());
        h.titleTV.setSelected(true);
        h.viewCntTV.setText(String.valueOf(newsHead.getVisitcnt()));
        h.likeCntTV.setText(String.valueOf(newsHead.getLikeCnt()));
        h.commentCntTV.setText(String.valueOf(newsHead.getCommentCnt()));
        h.dateTV.setText(newsHead.getPersianDate());
        h.thumb.setImageURI(MyConst.BASE_CONTENT_URL + newsHead.getThumbUrl());
        h.avatar.setImageURI(MyConst.BASE_CONTENT_URL + newsHead.getUserThumbUrl());
        h.itemView.setOnClickListener(view -> {
            Log.d(TAG, "onClick: newId:" + newsHead.getId());
            eventListener.onNewsHeadClick(newsHead.getId(),newsHead.getTypeId());
            /*Intent i = new Intent();
            i.setClass(h.itemView.getContext(), NewsDetailActivity.class);
            i.putExtra(NewsDetailActivity.KEY_NEWS_ID, newsHead.getId());
            h.itemView.getContext().startActivity(i);*/
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class VH extends RecyclerView.ViewHolder {
        final TextView titleTV;
        final TextView viewCntTV;
        final TextView likeCntTV;
        final TextView commentCntTV;
        final TextView dateTV;
        final SimpleDraweeView thumb;
        final SimpleDraweeView avatar;

        VH(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.txtTitle);
            viewCntTV = itemView.findViewById(R.id.txtViewCnt);
            likeCntTV = itemView.findViewById(R.id.txtLikeCnt);
            commentCntTV = itemView.findViewById(R.id.txtCommentCnt);
            dateTV = itemView.findViewById(R.id.txtDate);
            thumb = itemView.findViewById(R.id.img_thumb);
            avatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
    public interface OnAdapterNewsEventListener{
        void onNewsHeadClick(long newsId,int catType);
    }
}
