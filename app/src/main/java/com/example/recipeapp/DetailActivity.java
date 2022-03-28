package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.DatabaseMetaData;

public class DetailActivity extends AppCompatActivity {
    TextView tvDescription;
    TextView tvName;
    ImageView foodImage;
    String key = "";
    String urlImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        //show Recipe detail info
        this.showRecipeDetail();
    }

    public void showRecipeDetail() {
        this.tvDescription = (TextView) findViewById(R.id.tvDetailDescription);
        this.tvName = (TextView) findViewById(R.id.tvDetailName);
        this.foodImage = (ImageView) findViewById(R.id.imageDetal);
        Bundle bundle = getIntent().getBundleExtra("data");

        if (bundle != null) {
            tvName.setText(bundle.getString("name"));
            tvDescription.setText(bundle.getString("description"));
            this.key =  bundle.getString("key");
            this.urlImage = bundle.getString("image");

            Glide.with(DetailActivity.this).load(bundle.getString("image")).into(foodImage);
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
                Toast.makeText(DetailActivity.this, "Recipe deleted", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    //reidrect to screen update recipe
    public void btn_UpdateRecipe(View view) {
        Intent intent = new Intent(getApplicationContext(), UpdateRecipe.class);
        Bundle bundle = new Bundle();
        bundle.putString("recipeName", tvName.getText().toString());
        bundle.putString("description", tvDescription.getText().toString());
        bundle.putString("oldUrlImage", this.urlImage);
        bundle.putString("key", this.key);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }
}