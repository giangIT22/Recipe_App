package com.example.recipeapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.recipeapp.FoodData;
import com.example.recipeapp.MyApdater;
import com.example.recipeapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRecipeFragment extends Fragment {
    RecyclerView mReCyclerView;
    ArrayList<FoodData> myFoodList;
    DatabaseReference databaseReference;
    MyApdater myApdater;
    private DatabaseReference mDatabase;
    private ValueEventListener valueEventListener;
    private String userId;
    ProgressDialog progressDialog;
    EditText txtSearch;

    FloatingActionButton uploadRecipeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipe, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("user_id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = snapshot.getValue().toString();
                userId = id;
                Log.d("tag", "id" + userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        showRecipes(view);
        getActivity().setTitle("Công thức của tôi");

        return view;
    }

    public void showRecipes(View view)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("Recipe");
        mReCyclerView = (RecyclerView) view.findViewById(R.id.recylerViewMyRecipe);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mReCyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Items...");

        myFoodList = new ArrayList<>();
        myApdater = new MyApdater(getActivity(), myFoodList);
        mReCyclerView.setAdapter(myApdater);

        progressDialog.show();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myFoodList.clear();
                Log.d("tag", "id2" + userId);
                for (DataSnapshot item: snapshot.getChildren()) {
                    FoodData foodData = item.getValue(FoodData.class);
                    if (foodData.getUserId().equals(userId)) {
                        Log.d("tag", "id2 :" + userId);
                        foodData.setKey(item.getKey());
                        myFoodList.add(foodData);
                    }
                }
                myApdater.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        //Search recipe
        txtSearch = (EditText) view.findViewById(R.id.editTxtSearchRecipe);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchRecipe(txtSearch.getText().toString());
            }
        });

    }

    public void searchRecipe(String text)
    {
        ArrayList<FoodData> filterRecipes = new ArrayList<>();

        for (FoodData item : myFoodList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filterRecipes.add(item);
            }
        }

        myApdater.filteredList(filterRecipes);
    }
}