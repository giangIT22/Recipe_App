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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.fragment.DetailFragment;

import java.util.ArrayList;
import java.util.List;

public class MyApdater extends RecyclerView.Adapter<FoodViewHolder>{
    private Context mContext;
    private ArrayList<FoodData> myFoodList;
    private int lastPosition = -1;

    public MyApdater(Context mContext, ArrayList<FoodData> myFoodList) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_item, parent, false);
        return new FoodViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int i) {
        Glide.with(mContext).load(myFoodList.get(i).getImage()).into(holder.imageView);
//        holder.imageView.setImageResource(myFoodList.get(i).getImage());
        holder.mTitle.setText(myFoodList.get(i).getName());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", myFoodList.get(holder.getAdapterPosition()).getName());
                bundle.putString("image", myFoodList.get(holder.getAdapterPosition()).getImage());
                bundle.putString("description", myFoodList.get(holder.getAdapterPosition()).getDescription());
                bundle.putString("key", myFoodList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("data", bundle);
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);
                mContext.startActivity(intent);
            }
        });


        //set animation when loading data
//        this.setAnimation(holder.itemView, i);
    }

    public void setAnimation(View viewToAnimate, int position)
    {
        if (position > this.lastPosition) {
            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            animation.setDuration(1500);
            viewToAnimate.startAnimation(animation);

            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return myFoodList.size();
    }

    //change data for adapter
    public void filteredList(ArrayList<FoodData> listRecipe)
    {
        this.myFoodList = listRecipe;
        notifyDataSetChanged();
    }
}

class FoodViewHolder extends RecyclerView.ViewHolder
{
    ImageView imageView;
    TextView mTitle;
    CardView mCardView;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mCardView = itemView.findViewById(R.id.myCardView);
    }
}
