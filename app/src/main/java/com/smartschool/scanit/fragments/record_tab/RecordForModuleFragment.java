package com.smartschool.scanit.fragments.record_tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.airbnb.lottie.RenderMode;
import com.smartschool.scanit.adapters.RecordsRVAdapter;
import com.smartschool.scanit.databinding.FragmentRecordForModuleBinding;
import com.smartschool.scanit.models.Record;
import com.smartschool.scanit.utils.DBUtils;
import com.smartschool.scanit.utils.RVEmptyObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecordForModuleFragment extends Fragment {

    FragmentRecordForModuleBinding binding;
    RecordsRVAdapter adapter;
    ArrayList<Record> recordArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecordForModuleBinding.inflate(getLayoutInflater());
        init();
        getData();
        return binding.getRoot();
    }

    private void getData() {
        recordArrayList.clear();
        recordArrayList.addAll(DBUtils.getModuleRecords());
        Collections.sort(recordArrayList, new Comparator<Record>() {
            @Override
            public int compare(Record lhs, Record rhs) {
                return lhs.getFullName().compareTo(rhs.getFullName());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void init() {
//        binding.emptyLayout.animationView.setRenderMode(RenderMode.HARDWARE);
        recordArrayList = new ArrayList<>();
        adapter = new RecordsRVAdapter(recordArrayList,requireContext());
        adapter.registerAdapterDataObserver(new RVEmptyObserver(binding.recyclerView,binding.emptyLayout.getRoot()));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }
}