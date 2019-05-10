package com.example.arsalan.mygym.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.Trainer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

/**
 * Created by Arsalan on 10-02-2018.
 */

public class AdapterTrainers extends Adapter<AdapterTrainers.VH> {
    private final OnItemClickListener mListener;
    List<Trainer> trainerList;

    public AdapterTrainers(List<Trainer> trainerList, OnItemClickListener listener) {
        this.trainerList = trainerList;
        mListener = listener;

    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainer, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(final VH h, int position) {

        h.bind(trainerList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return trainerList.size();
    }

    //Activity mActivity;
    public interface OnItemClickListener {
        void onItemClick(Trainer trainer, View view);
    }

    public interface OnFragmentInteractionListener {
        void onItemClicked(Trainer trainer, View sharedView);
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView thumbImg;
        TextView nameTV;
        TextView honorTV;
        TextView pointsTV;
        RatingBar ratingBar;


        public VH(View iv) {
            super(iv);
            nameTV = iv.findViewById(R.id.txtName);
            honorTV = iv.findViewById(R.id.txtTitle);
            ratingBar = iv.findViewById(R.id.ratingBar);
            pointsTV = iv.findViewById(R.id.txtPoints);
            thumbImg = iv.findViewById(R.id.imgThumb);

        }

        public void bind(final Trainer t, final OnItemClickListener listener) {
            Glide.with(itemView.getContext())
                    .load(MyConst.BASE_CONTENT_URL + t.getThumbUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.bodybuilder_place_holder).circleCrop())
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbImg);

            // h.thumbImg.setImageURI();
            pointsTV.setText(String.valueOf(t.getPoint()));
            ratingBar.setRating(t.getRate());
            nameTV.setText(t.getName());
            //  h.honorTV.setText(t.getTitle());
            /*ViewCompat.setTransitionName(thumbImg, t.getName());*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(t, thumbImg);
                }
            });
        }
    }
}
