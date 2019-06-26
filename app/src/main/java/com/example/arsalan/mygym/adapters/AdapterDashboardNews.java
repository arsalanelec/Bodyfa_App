package com.example.arsalan.mygym.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.ItemMyNewsGridBinding;
import com.example.arsalan.mygym.databinding.ItemMyNewsLinearBinding;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.NewsHead;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AdapterDashboardNews extends RecyclerView.Adapter<AdapterDashboardNews.VH> {
    public static final int LAYOUT_TYPE_LINEAR =1;
    public static final int LAYOUT_TYPE_GRID=2;
    private static final String TAG = "AdapterDashboardNews";
    List<NewsHead> newsList;
    int mViewType;
    OnAdapterNewsEventListener eventListener;

    public AdapterDashboardNews(List<NewsHead> newsList,int viewType, OnAdapterNewsEventListener onAdapterNewsEventListener) {
        this.newsList = newsList;
        mViewType=viewType;
        eventListener=onAdapterNewsEventListener;
    }
    public void setLayoutType(int viewType){
        this.mViewType=viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        if(mViewType== LAYOUT_TYPE_LINEAR) {
            binding= DataBindingUtil.inflate((LayoutInflater) parent.getContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE), R.layout.item_my_news_linear, parent, false);

        }else {
            binding= DataBindingUtil.inflate((LayoutInflater) parent.getContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE), R.layout.item_my_news_grid, parent, false);

        }
        /*Animation animation = null;
        animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.wave);
        animation.setDuration(200);
        itemView.startAnimation(animation);
        animation = null;*/
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(final VH h, int position) {
        SimpleDraweeView thumb=null;
        final NewsHead newsHead = newsList.get(position);
        if(h.binding instanceof ItemMyNewsLinearBinding){
            ((ItemMyNewsLinearBinding)h.binding).setNews(newsHead);
            ((ItemMyNewsLinearBinding) h.binding).txtTitle.setSelected(true);
            thumb = ((ItemMyNewsLinearBinding) h.binding).imgThumb;
        }else if(h.binding instanceof ItemMyNewsGridBinding){
            ((ItemMyNewsGridBinding)h.binding).setNews(newsHead);
            ((ItemMyNewsGridBinding) h.binding).txtTitle.setSelected(true);
            thumb = ((ItemMyNewsGridBinding) h.binding).imgThumb;
        }
        thumb.setImageURI(MyConst.BASE_CONTENT_URL + newsHead.getThumbUrl());
        h.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: newId:" + newsHead.getId());
                eventListener.onNewsHeadClick(newsHead.getId(),newsHead.getTypeId());
                /*Intent i = new Intent();
                i.setClass(h.itemView.getContext(), NewsDetailActivity.class);
                i.putExtra(NewsDetailActivity.KEY_NEWS_ID, newsHead.getId());
                h.itemView.getContext().startActivity(i);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class VH extends RecyclerView.ViewHolder {
        ViewDataBinding binding;
        public VH(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    public interface OnAdapterNewsEventListener{
        void onNewsHeadClick(long newsId,int catType);
    }
}
