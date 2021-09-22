package com.smartschool.scanit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.smartschool.scanit.adapters.MyViewPagerAdapter;
import com.smartschool.scanit.databinding.FragmentScanQrOptionsBinding;
import com.smartschool.scanit.fragments.scan_tab.ScanForAttendanceFragment;
import com.smartschool.scanit.fragments.scan_tab.ScanForModuleFragment;


public class ScanQROptionFragment extends Fragment {

    FragmentScanQrOptionsBinding binding;
    String[] titlesArray;
    Fragment[] fragments;
    MyViewPagerAdapter myViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = com.smartschool.scanit.databinding.FragmentScanQrOptionsBinding.inflate(getLayoutInflater());
        init();

        return binding.getRoot();
    }

    private void init() {
        titlesArray = new String[]{"Scan for Attendance","Scan for Module"};
        fragments = new Fragment[]{new ScanForAttendanceFragment(), new ScanForModuleFragment()};

        myViewPagerAdapter = new MyViewPagerAdapter(requireActivity(),titlesArray,fragments);
        binding.viewPager.setAdapter(myViewPagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(titlesArray[position])).attach();
    }
}