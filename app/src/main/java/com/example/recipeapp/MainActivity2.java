package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.recipeapp.fragment.ChangePasswordFragment;
import com.example.recipeapp.fragment.DetailFragment;
import com.example.recipeapp.fragment.HomeFragment;
import com.example.recipeapp.fragment.MyRecipeFavouriteFragment;
import com.example.recipeapp.fragment.MyRecipeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference mDataBase;
    private DrawerLayout drawerLayout;
    private TextView userName;
    private View view;
    public static final int FRAGMENT_HOME = 0;
    public static final int FRAGMENT_FAVOURITE = 1;
    public static final int FRAGMENT_MY_RECIPE = 2;
    public static final int FRAGMENT_CHANGE_PASS = 3;
    public int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //get username of user was login
        view = navigationView.getHeaderView(0);
        userName = view.findViewById(R.id.userName);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("user_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        replaceFragment(new HomeFragment());//default start app is into home
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);//set checked item
    }

//    Choose item in menu and clode navigation when cliced on item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (currentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                currentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_favourite) {
            if (currentFragment != FRAGMENT_FAVOURITE) {
                replaceFragment(new MyRecipeFavouriteFragment());
                currentFragment = FRAGMENT_FAVOURITE;
            }
        } else if (id == R.id.nav_my_recipe) {
            if (currentFragment != FRAGMENT_MY_RECIPE) {
                replaceFragment(new MyRecipeFragment());
                currentFragment = FRAGMENT_MY_RECIPE;
            }
        } else if (id == R.id.nav_change_pass) {
            if (currentFragment != FRAGMENT_CHANGE_PASS) {
                replaceFragment(new ChangePasswordFragment());
                currentFragment = FRAGMENT_CHANGE_PASS;
            }
        } else {
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFrame, fragment);
        transaction.commit();
    }
}