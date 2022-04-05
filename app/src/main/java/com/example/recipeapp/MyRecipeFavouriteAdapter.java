package com.example.recipeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.fragment.DetailFragment;

import java.util.ArrayList;

public class MyRecipeFavouriteAdapter extends RecyclerView.Adapter<MyRecipeFavourite>{
    private Context mContext;
    private ArrayList<FoodData> myRecipeFavourite;
    @NonNull
    @Override
    public MyRecipeFavourite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_recipe_favourite, parent, false);
        return new MyRecipeFavourite(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeFavourite holder, int position) {
        Glide.with(mContext).load(myRecipeFavourite.get(position).getImage()).into(holder.imageView);
//        holder.imageView.setImageResource(myFoodList.get(i).getImage());
        holder.mTitle.setText(myRecipeFavourite.get(position).getName());

//        holder.mCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, DetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("name", myRecipeFavourite.get(holder.getAdapterPosition()).getName());
//                bundle.putString("image", myRecipeFavourite.get(holder.getAdapterPosition()).getImage());
//                bundle.putString("description", myRecipeFavourite.get(holder.getAdapterPosition()).getDescription());
//                bundle.putString("key", myRecipeFavourite.get(holder.getAdapterPosition()).getKey());
//                intent.putExtra("data", bundle);
//                DetailFragment detailFragment = new DetailFragment();
//                detailFragment.setArguments(bundle);
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

class MyRecipeFavourite extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView mTitle;
    CardView mCardView;

    public MyRecipeFavourite(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mCardView = itemView.findViewById(R.id.myCardView);
    }
}
