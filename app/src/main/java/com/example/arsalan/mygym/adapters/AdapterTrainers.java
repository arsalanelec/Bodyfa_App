package com.example.arsalan.mygym.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.SortedList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.Trainer;

import java.util.List;

/**
 * Created by Arsalan on 10-02-2018.
 */

public class AdapterTrainers extends Adapter<AdapterTrainers.VH> {
    private final OnItemClickListener mListener;
    private SortedList<Trainer> trainerSortedList;

    public AdapterTrainers(OnItemClickListener listener) {
        mListener = listener;
        trainerSortedList = new SortedList<Trainer>(Trainer.class, new SortedList.Callback<Trainer>() {
            @Override
            public int compare(Trainer o1, Trainer o2) {
                if(o1.getPoint()<o2.getPoint())return 1;
                if(o1.getPoint()>o2.getPoint())return -1;
                return 0;//o1.getName().compareTo(o2.getName());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Trainer oldItem, Trainer newItem) {
                return oldItem.getName().equals(newItem.getName());
            }

            @Override
            public boolean areItemsTheSame(Trainer item1, Trainer item2) {
                return item1.getId() == item2.getId();
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    //conversation helpers
    public void addAll(List<Trainer> trainers) {
        trainerSortedList.beginBatchedUpdates();
        for (int i = 0; i < trainers.size(); i++) {
            trainerSortedList.add(trainers.get(i));
        }
        trainerSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<Trainer> trainers) {
        trainerSortedList.beginBatchedUpdates();
        for (int i = trainerSortedList.size() - 1; i >= 0; i--) {
            final Trainer trainer = trainerSortedList.get(i);
            if (!trainers.contains(trainer)) {
                trainerSortedList.remove(trainer);
            }
        }
        trainerSortedList.addAll(trainers);
        trainerSortedList.endBatchedUpdates();
    }

    public void clear() {
        trainerSortedList.beginBatchedUpdates();
        //remove items at end, to avoid unnecessary array shifting
        while (trainerSortedList.size() > 0) {
            trainerSortedList.removeItemAt(trainerSortedList.size() - 1);
        }
        trainerSortedList.endBatchedUpdates();
    }

    public void remove(Trainer trainer) {
        trainerSortedList.remove(trainer);
    }

    public void add(Trainer trainer) {
        trainerSortedList.add(trainer);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainer, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(final VH h, int position) {

        h.bind(trainerSortedList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return trainerSortedList.size();
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
            thumbImg = iv.findViewById(R.id.img_thumb);
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
