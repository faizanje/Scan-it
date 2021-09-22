package com.smartschool.scanit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.smartschool.scanit.R;
import com.smartschool.scanit.adapters.MyViewPagerAdapter;
import com.smartschool.scanit.databinding.FragmentListRecordsBinding;
import com.smartschool.scanit.fragments.record_tab.RecordForAttendanceFragment;
import com.smartschool.scanit.fragments.record_tab.RecordForModuleFragment;
import com.smartschool.scanit.fragments.scan_tab.ScanForAttendanceFragment;
import com.smartschool.scanit.fragments.scan_tab.ScanForModuleFragment;


public class ListRecordsFragment extends Fragment {
    String[] titlesArray;
    Fragment[] fragments;
    MyViewPagerAdapter myViewPagerAdapter;
    FragmentListRecordsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentListRecordsBinding.inflate(getLayoutInflater());
        init();

        return binding.getRoot();
    }

    private void init() {
        titlesArray = new String[]{"Attendance","Module"};
        fragments = new Fragment[]{new RecordForAttendanceFragment(), new RecordForModuleFragment()};

        myViewPagerAdapter = new MyViewPagerAdapter(requireActivity(),titlesArray,fragments);
        binding.viewPager.setAdapter(myViewPagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(titlesArray[position])).attach();
    }
}