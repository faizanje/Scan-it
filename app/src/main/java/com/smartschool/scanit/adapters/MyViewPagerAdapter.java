package com.smartschool.scanit.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.smartschool.scanit.fragments.scan_tab.ScanForAttendanceFragment;
import com.smartschool.scanit.fragments.scan_tab.ScanForModuleFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    String[] titlesArray;
    Fragment[] fragments;

    public MyViewPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity, String[] titlesArray, Fragment[] fragments) {

        super(fragmentActivity);
        this.titlesArray = titlesArray;
        this.fragments = fragments;

    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return titlesArray.length;
    }
}
