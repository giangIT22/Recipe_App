package com.example.recipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private ArrayList<Comment> comments;
    private Context mContext;

    public CommentAdapter(ArrayList<Comment> comments, Context mContext) {
        this.comments = comments;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        return new CommentViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.userName.setText(comments.get(position).getUserName());
        holder.comment.setText(comments.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}

class CommentViewHolder extends RecyclerView.ViewHolder
{
    TextView userName;
    TextView comment;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.tv_user_name);
        comment = itemView.findViewById(R.id.tv_comment);
    }
}
