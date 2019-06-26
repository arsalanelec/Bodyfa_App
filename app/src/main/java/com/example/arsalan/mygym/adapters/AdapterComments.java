package com.example.arsalan.mygym.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arsalan.mygym.MyUtil;
import com.example.arsalan.mygym.models.Comment;
import com.example.arsalan.mygym.R;

import java.util.List;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.VH> {
    List<Comment> commentList;


    public AdapterComments(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View iv = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new VH(iv);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Comment c = commentList.get(position);
        holder.senderNameTV.setText(c.getSenderName());
        holder.dateTV.setText(MyUtil.getLargStringFormatOfDate(c.getCommentDateTs()));
        holder.commentTv.setText(c.getComment());
    }

    @Override
    public int getItemCount() {
        return  commentList.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView senderNameTV;
        TextView dateTV;
        TextView commentTv;

        public VH(View iv) {
            super(iv);
            senderNameTV = iv.findViewById(R.id.txtName);
            dateTV = iv.findViewById(R.id.txtDate);
            commentTv = iv.findViewById(R.id.txtComment);
        }
    }
}
