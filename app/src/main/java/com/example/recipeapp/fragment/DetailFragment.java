package com.example.recipeapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.FoodData;
import com.example.recipeapp.IngredientAdapter;
import com.example.recipeapp.MyStep;
import com.example.recipeapp.MyStepAdapter;
import com.example.recipeapp.R;
import com.example.recipeapp.UpdateRecipe;
import com.example.recipeapp.UploadRecipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailFragment extends Fragment {
    DatabaseReference databaseReference;
    RecyclerView recyclerViewIngredient;
    RecyclerView recyclerViewStep;
    ArrayList<String> ingredients;
    ArrayList<MyStep> steps;
    IngredientAdapter ingredientAdapter;
    MyStepAdapter myStepAdapter;
    TextView tvDescription;
    TextView tvName;
    ImageView foodImage;
    String key = "";
    String urlImage = "";
    CheckBox checkBoxFavourite;
    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userId = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        this.showRecipeDetail(view);
        this.addRecipeFavourite(view);
        this.ischeckedFavourite();

//        getActivity().setTitle("Chi tiết");
        return view;
    }

    public void showRecipeDetail(View view) {
        this.tvDescription = (TextView) view.findViewById(R.id.tvDetailDescription);
        this.tvName = (TextView) view.findViewById(R.id.tvDetailName);
        this.foodImage = (ImageView) view.findViewById(R.id.imageDetal);

        Bundle bundle = getActivity().getIntent().getBundleExtra("data");

        if (bundle != null) {
            tvName.setText(bundle.getString("name"));
            tvDescription.setText(bundle.getString("description"));
            this.key =  bundle.getString("key");
            this.urlImage = bundle.getString("image");
            databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");
            databaseReference.child(this.key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    FoodData foodData = snapshot.getValue(FoodData.class);
                    //recycler view add ingredients
                    recyclerViewIngredient = (RecyclerView) view.findViewById(R.id.recycler_ingredents);
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                    ingredients = foodData.getIngredients();
                    recyclerViewIngredient.setLayoutManager(manager);
                    recyclerViewIngredient.setHasFixedSize(true);
                    ingredientAdapter = new IngredientAdapter(getActivity(), ingredients);
                    recyclerViewIngredient.setAdapter(ingredientAdapter);
                    RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
                    recyclerViewIngredient.addItemDecoration(itemDecoration);

                    //recycler view add step
                    recyclerViewStep = (RecyclerView) view.findViewById(R.id.recycler_steps);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    steps = foodData.getStepList();
                    recyclerViewStep.setLayoutManager(linearLayoutManager);
                    recyclerViewStep.setHasFixedSize(true);
                    myStepAdapter = new MyStepAdapter(getActivity(), steps);
                    recyclerViewStep.setAdapter(myStepAdapter);
                    RecyclerView.ItemDecoration itemDecorationStep = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
                    recyclerViewStep.addItemDecoration(itemDecorationStep);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Glide.with(getActivity()).load(bundle.getString("image")).into(foodImage);
        }
    }

    //delete recipe
    public void btn_DeleteRecipe(View view) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipe");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReferenceFromUrl(this.urlImage);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference.child(key).removeValue();
                Toast.makeText(getActivity(), "Recipe deleted", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                getActivity().finish();
            }
        });
    }

    //reidrect to screen update recipe
    public void btn_UpdateRecipe(View view) {
        Intent intent = new Intent(getActivity(), UpdateRecipe.class);
        Bundle bundle = new Bundle();
        bundle.putString("recipeName", tvName.getText().toString());
        bundle.putString("description", tvDescription.getText().toString());
        bundle.putString("oldUrlImage", this.urlImage);
        bundle.putString("key", this.key);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }

    //add recipe favourite
    public void addRecipeFavourite(View view)
    {
        checkBoxFavourite = (CheckBox) view.findViewById(R.id.checkFavorite);
        checkBoxFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("MyRecipeFavourite");
                Bundle bundle = getActivity().getIntent().getBundleExtra("data");
                if (isChecked)
                {
                    FoodData foodData = new FoodData(bundle.getString("name"),
                            bundle.getString("description"), bundle.getString("image"), steps);
                    foodData.setUserId(userId);
                    foodData.setIngredients(ingredients);

                    mDatabase.child(bundle.getString("key")).setValue(foodData);
                    checkBoxFavourite.setBackgroundResource(R.drawable.ic_baseline_favorite_24);

                } else {
                    mDatabase.child(bundle.getString("key")).removeValue();
                    checkBoxFavourite.setBackgroundResource(R.drawable.ic_like);
                }

            }
        });
    }

    public void ischeckedFavourite()
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("MyRecipeFavourite");
        Bundle bundle = getActivity().getIntent().getBundleExtra("data");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item: snapshot.getChildren()) {
                    FoodData foodData = item.getValue(FoodData.class);
                    if (item.getKey().equals(bundle.getString("key")) && foodData.getUserId().equals(userId)) {
                        checkBoxFavourite.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
