package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UploadRecipe extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView recyclerViewStep;
    ArrayList<String> ingredients;
    ArrayList<MyStep> stepList;
    IngredientAdapter ingredientAdapter;
    MyStepAdapter myStepAdapter;
    ImageView recipeImage;
    Button btnSelectImage;
    Uri uri;
    EditText title;
    EditText description;
    String imageUrl;
    Button btnIngredient;
    EditText editTextIngredient;
    TextView tvStep;
    Button btnAddStep;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        recipeImage =(ImageView) findViewById(R.id.ivRecipeImage);
        btnSelectImage = (Button) findViewById(R.id.btnSelectImage);
        title = (EditText) findViewById(R.id.editTxtTitle);
        description = (EditText) findViewById(R.id.editTxtDescription);
        btnIngredient = (Button) findViewById(R.id.btnAddIngredient);
        editTextIngredient = (EditText) findViewById(R.id.editTxtIngredient);
        tvStep = (TextView) findViewById(R.id.tv_step);
        btnAddStep = (Button) findViewById(R.id.btn_add_step);

        //recycler view add ingredients
        recyclerView = (RecyclerView) findViewById(R.id.recylerViewIngredients);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ingredients = new ArrayList<>();
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        ingredientAdapter = new IngredientAdapter(UploadRecipe.this, ingredients);
        recyclerView.setAdapter(ingredientAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        //recycler view add step
        recyclerViewStep = (RecyclerView) findViewById(R.id.recylerViewStep);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        stepList = new ArrayList<>();
        recyclerViewStep.setLayoutManager(linearLayoutManager);
        recyclerViewStep.setHasFixedSize(true);
        myStepAdapter = new MyStepAdapter(UploadRecipe.this, stepList);
        recyclerViewStep.setAdapter(myStepAdapter);
        RecyclerView.ItemDecoration itemDecorationStep = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewStep.addItemDecoration(itemDecorationStep);

        btnIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredients.add(editTextIngredient.getText().toString());
                ingredientAdapter.notifyDataSetChanged();
                editTextIngredient.setText("");
            }
        });

        btnAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepList.add(new MyStep(tvStep.getText().toString()));
                myStepAdapter.notifyDataSetChanged();
                tvStep.setText("");
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("user_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userId = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //back btn
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //select recipe image
    public void btn_SelectRecipeImage(View view) {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            uri = data.getData();
            recipeImage.setImageURI(uri);
        } else {
            Toast.makeText(this, "You haven't picked image", Toast.LENGTH_SHORT);
        }
    }

    //upload image and save to firebase
    public void uploadImage()
    {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe uploading ...");
        progressDialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("RecipeImage").child(uri.getLastPathSegment());

        storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               Task<Uri> uriTask =  taskSnapshot.getStorage().getDownloadUrl();

               while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();//get url image from firebase
                imageUrl = urlImage.toString();

                FoodData foodData = new FoodData(title.getText().toString(),
                        description.getText().toString(), imageUrl, stepList);
                foodData.setUserId(userId);
                foodData.setIngredients(ingredients);
                String myCurrentDateTime = DateFormat.getDateTimeInstance().
                        format((Calendar.getInstance().getTime()));
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Recipe");
                mDatabase.child(myCurrentDateTime).setValue(foodData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadRecipe.this, "Recipe uploaded", Toast.LENGTH_LONG);
                        }
                    }
                });
                finish();
            }
        });
    }

    //create recipe and save to firebase
    public void btn_uploadRecipe(View view) {
        this.uploadImage();
    }
}