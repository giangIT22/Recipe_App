package com.example.recipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientViewHolder> {
    private Context mContext;
    private ArrayList<String> ingredients;

    public IngredientAdapter(Context mContext, ArrayList<String> ingredients) {
        this.mContext = mContext;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredients, parent, false);
        return new IngredientViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.myTextView.setText(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}

class IngredientViewHolder extends RecyclerView.ViewHolder
{
    TextView myTextView;

    public IngredientViewHolder(@NonNull View itemView) {
        super(itemView);
        myTextView = (TextView) itemView.findViewById(R.id.tvIngredients);
    }
}
