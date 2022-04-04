package com.example.recipeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyStepAdapter extends RecyclerView.Adapter<MyStepViewHolder>{
    private Context mContext;
    private ArrayList<MyStep> stepList;

    public MyStepAdapter(Context mContext, ArrayList<MyStep> stepList) {
        this.mContext = mContext;
        this.stepList = stepList;
    }

    @NonNull
    @Override
    public MyStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_step, parent, false);
        return new MyStepViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyStepViewHolder holder, int i) {
        int position = i + 1;
        holder.tvStep.setText("Bước " + position + ":");
        holder.stepDescription.setText(stepList.get(i).getStepDescription());
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }
}

class MyStepViewHolder extends RecyclerView.ViewHolder
{
    TextView tvStep;
    TextView stepDescription;

    public MyStepViewHolder(@NonNull View itemView) {
        super(itemView);
        tvStep = (TextView) itemView.findViewById(R.id.tv_title_step);
        stepDescription = (TextView) itemView.findViewById(R.id.tv_description_step);
    }
}