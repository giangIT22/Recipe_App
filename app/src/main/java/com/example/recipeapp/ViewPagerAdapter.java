package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.recipeapp.fragment.ComentFragment;
import com.example.recipeapp.fragment.DetailFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DetailFragment();
            case 1:
                return new ComentFragment();

            default:
                return new DetailFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
