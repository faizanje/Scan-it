package com.smartschool.scanit.fragments.fragment_trace;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.smartschool.scanit.R;
import com.smartschool.scanit.adapters.MyViewPagerAdapter;
import com.smartschool.scanit.databinding.FragmentTraceBinding;
import com.smartschool.scanit.fragments.fragment_trace.tabs.ScanForTraceFragment;
import com.smartschool.scanit.fragments.fragment_trace.tabs.TraceListFragment;
import com.smartschool.scanit.fragments.scan_tab.ScanForAttendanceFragment;
import com.smartschool.scanit.fragments.scan_tab.ScanForModuleFragment;

public class TraceFragment extends Fragment {

    FragmentTraceBinding binding;
    String[] titlesArray;
    Fragment[] fragments;
    MyViewPagerAdapter myViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentTraceBinding.inflate(getLayoutInflater());
        init();

        return binding.getRoot();
    }

    private void init() {
        titlesArray = new String[]{"SCAN","LISTS"};
            fragments = new Fragment[]{new ScanForTraceFragment(), new TraceListFragment()};

        myViewPagerAdapter = new MyViewPagerAdapter(requireActivity(),titlesArray,fragments);
        binding.viewPager.setAdapter(myViewPagerAdapter);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    setTabStyle(R.drawable.bg_rounded_outline_yellow,R.color.pink);
                      binding.pageBg.getBackground().setTint(getContext().getResources().getColor(R.color.white));
                }else{
                    setTabStyle(R.drawable.bg_rounded_outline_red, R.color.pink);
                   binding.pageBg.getBackground().setTint(getContext().getResources().getColor(R.color.grey_300));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    tab.setText(titlesArray[position]);
                }).attach();
    }

    private void setTabStyle(int bgId, int colorId) {
        GradientDrawable drawable = (GradientDrawable) binding.tabLayout.getTabSelectedIndicator();
        drawable.setStroke(4, ContextCompat.getColor(requireContext(),colorId));
        binding.tabLayout.setTabTextColors(ContextCompat.getColor(requireContext(),R.color.grey),
                ContextCompat.getColor(requireContext(),colorId));

    }
}