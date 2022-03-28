package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateRecipe extends AppCompatActivity {
    ImageView recipeImage;
    Button btnSelectImage;
    Uri uri;
    EditText title;
    EditText description;
    String imageUrl;
    String key, oldImageUrl;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);

        recipeImage =(ImageView) findViewById(R.id.ivRecipeImage);
        title = (EditText) findViewById(R.id.editTxtTitle);
        description = (EditText) findViewById(R.id.editTxtDescription);

        Bundle bundle = getIntent().getBundleExtra("data");

        if (bundle != null) {
            Glide.with(UpdateRecipe.this).load(bundle.getString("oldUrlImage")).into(recipeImage);
            title.setText(bundle.getString("recipeName"));
            description.setText(bundle.getString("description"));
            this.key = bundle.getString("key");
            this.oldImageUrl = bundle.getString("oldUrlImage");
        }
    }

    //select image
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

    //update info recipe
    public void btn_updateRecipe(View view) {
        String recipeName = title.getText().toString(). trim();
        String recipeDescription = description.getText().toString().trim();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe updating ...");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe").child(this.key);
        storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImage").child(uri.getLastPathSegment());

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask =  taskSnapshot.getStorage().getDownloadUrl();

                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();//get url image from firebase
                imageUrl = urlImage.toString();
                FoodData foodData = new FoodData(recipeName, recipeDescription, imageUrl);
                databaseReference.setValue(foodData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                        storageReference.delete();
                        Toast.makeText(UpdateRecipe.this, "Recipe Updated", Toast.LENGTH_SHORT);
                    }
                });
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateRecipe.this, "Recipe Update fail", Toast.LENGTH_SHORT);
                progressDialog.dismiss();
            }
        });
    }
}