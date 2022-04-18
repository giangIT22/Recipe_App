package com.example.recipeapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.recipeapp.CakeFeatureAdapter;
import com.example.recipeapp.FoodData;
import com.example.recipeapp.MainActivity2;
import com.example.recipeapp.MyApdater;
import com.example.recipeapp.R;
import com.example.recipeapp.UploadRecipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView mReCyclerView;
    ArrayList<FoodData> myFoodList;
    MyApdater myApdater;
    private DatabaseReference mDatabase;
    private ValueEventListener valueEventListener;
    ProgressDialog progressDialog;
    EditText txtSearch;

    private ImageSlider mImagesSlider;
    private CakeFeatureAdapter cakeFeatureAdapter;
    private RecyclerView mRCVCakeFeature;

    FloatingActionButton uploadRecipeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.showRecipes(view);

//        Slider
        initSlider(view);
        initCakeFeature(view);

        //redirect to upload recipe info screen
        uploadRecipeBtn = (FloatingActionButton) view.findViewById(R.id.uploadRecipe) ;
        uploadRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadRecipe.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initSlider(View view){
        mImagesSlider = view.findViewById(R.id.image_slider);
        List<SlideModel> list = new ArrayList<>();
        list.add(new SlideModel(R.drawable.cake_2,"Find your favorite cake", ScaleTypes.CENTER_CROP));
        list.add(new SlideModel(R.drawable.cake_3, "Better experience with mobile app", ScaleTypes.CENTER_CROP));

        mImagesSlider.setImageList(list);
    }

    private void initCakeFeature(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Recipe");
        mRCVCakeFeature = view.findViewById(R.id.rcv_cakeFeature);

        cakeFeatureAdapter = new CakeFeatureAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        mRCVCakeFeature.setLayoutManager(linearLayoutManager);

        List<FoodData> listCake = new ArrayList<>();


        mRCVCakeFeature.setAdapter(cakeFeatureAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCake.clear();

                for (DataSnapshot item: snapshot.getChildren()) {
                    FoodData foodData = item.getValue(FoodData.class);
                    foodData.setKey(item.getKey());
                    listCake.add(foodData);
                }
                cakeFeatureAdapter.setData(listCake);

//                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                progressDialog.dismiss();
            }
        });
    }

    public void showRecipes(View view)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("Recipe");
        mReCyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
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

                for (DataSnapshot item: snapshot.getChildren()) {
                    FoodData foodData = item.getValue(FoodData.class);
                    foodData.setKey(item.getKey());
                    myFoodList.add(foodData);
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
