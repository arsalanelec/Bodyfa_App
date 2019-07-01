package com.example.arsalan.mygym.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.TrainerAthlete;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

/**
 * Created by Arsalan on 10-02-2018.
 */

public class AdapterAthletesDialog extends Adapter<AdapterAthletesDialog.VH> {
    private final OnItemClickListener mListener;
    private final List<TrainerAthlete> mUserList;

    public AdapterAthletesDialog(List<TrainerAthlete> userList, OnItemClickListener listener) {
        this.mUserList = userList;
        mListener = listener;

    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_athlete_dialog, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(final VH h, int position) {

        h.bind(mUserList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(TrainerAthlete athlete);
    }

    class VH extends RecyclerView.ViewHolder {
        final ImageView thumbImg;
        final TextView nameTV;
        final TextView registerDateTV;
        final Button sendMessageBtn;

        VH(View iv) {
            super(iv);
            nameTV = iv.findViewById(R.id.txtName);
            thumbImg = iv.findViewById(R.id.img_thumb);
            registerDateTV = iv.findViewById(R.id.txtRegisterDate);
            sendMessageBtn = iv.findViewById(R.id.btnSendMessage);

        }

        void bind(final TrainerAthlete user, final OnItemClickListener listener) {
            Glide.with(itemView.getContext())
                    .load(MyConst.BASE_CONTENT_URL + user.getAthleteThumbPicture())
                    .apply(new RequestOptions().placeholder(R.drawable.bodybuilder_place_holder))
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbImg);

            nameTV.setText(user.getAthleteName());
            registerDateTV.setText(user.getRegisterDate());
            //  h.honorTV.setText(t.getTitle());
/*
            ViewCompat.setTransitionName(thumbImg, user.getName());
*/
            itemView.setOnClickListener(v -> listener.onItemClick(user));
        }
    }


}
