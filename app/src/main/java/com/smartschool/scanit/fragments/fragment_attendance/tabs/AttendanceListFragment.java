package com.smartschool.scanit.fragments.fragment_attendance.tabs;

import android.animation.LayoutTransition;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartschool.scanit.R;
import com.smartschool.scanit.adapters.TarceAdapter;
import com.smartschool.scanit.databinding.FragmentAttendanceListBinding;
import com.smartschool.scanit.databinding.FragmentTraceListBinding;
import com.smartschool.scanit.models.TraceListModel;

import java.util.ArrayList;


public class AttendanceListFragment extends Fragment {

    FragmentAttendanceListBinding binding;

    ArrayList<TraceListModel> arryList;
    TarceAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAttendanceListBinding.inflate(getLayoutInflater());
        binding.layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        arryList = new ArrayList<>();
        arryList.add(new TraceListModel(getString(R.string.example_name),"1.",getString(R.string.time),"Present"));
        arryList.add(new TraceListModel(getString(R.string.example_name),"2.",getString(R.string.time),"Absent"));
        arryList.add(new TraceListModel(getString(R.string.example_name),"3.",getString(R.string.time),"Present"));
        arryList.add(new TraceListModel(getString(R.string.example_name),"4.",getString(R.string.time),"Present"));
        arryList.add(new TraceListModel(getString(R.string.example_name),"5.",getString(R.string.time),"Absent"));
        arryList.add(new TraceListModel(getString(R.string.example_name),"6.",getString(R.string.time),"Absent"));
        arryList.add(new TraceListModel(getString(R.string.example_name),"7.",getString(R.string.time),"Present"));
        arryList.add(new TraceListModel(getString(R.string.example_name),"8.",getString(R.string.time),"Present"));
        arryList.add(new TraceListModel(getString(R.string.example_name),"9.",getString(R.string.time),"Absent"));
        binding.rvTraceList.setHasFixedSize(true);
        binding.rvTraceList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTraceList.setAdapter(new TarceAdapter(arryList,getContext()));
        binding.btnCollaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int view =(binding.rvTraceList.getVisibility()==View.GONE)?View.VISIBLE:View.GONE;
                int view2 =(binding.rvTraceList.getVisibility()==View.VISIBLE)?R.drawable.ic_yello_dwon_icon:R.drawable.ic_yello_up_icon;
                TransitionManager.beginDelayedTransition(binding.layout,new AutoTransition());
                binding.btnCollaps.setImageResource(view2);
                binding.rvTraceList.setVisibility(view);
//                if ( binding.layout.getVisibility()==View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(binding.layout,);
//                    binding.layout.setVisibility(View.GONE);
//                }
//            else{
//                    binding.layout.setVisibility(View.VISIBLE);
//                }

            }
        });
        return binding.getRoot();
    }
}