package com.smartschool.scanit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.smartschool.scanit.R;
import com.smartschool.scanit.adapters.HomeAdapter;
import com.smartschool.scanit.databinding.ActivityBottomNavBinding;
import com.smartschool.scanit.databinding.FragmentHomeBinding;
import com.smartschool.scanit.databinding.HomecardLayoutBinding;
import com.smartschool.scanit.models.HomeModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ArrayList<HomeModel> homeList;
    HomeAdapter adapter;
    int myInt;

//    Bundle bundle = getArguments().getBundle("calValue");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             myInt= bundle.getInt("colValue", 2);
        }

        homeList=new ArrayList<>();
        homeList.add(new HomeModel("Trace",R.drawable.pink_card_img,R.color.pink));
        homeList.add(new HomeModel("Attendance",R.drawable.yellow_card_img,R.color.yellow_400));
        homeList.add(new HomeModel("Health",R.drawable.light_pink_img,R.color.purple));
        homeList.add(new HomeModel("Academics",R.drawable.blue_img,R.color.blue));
        binding.rvHome.setHasFixedSize(true);
        binding.rvHome.setLayoutManager(new GridLayoutManager(getContext(),myInt));
        binding.rvHome.setAdapter(new HomeAdapter(homeList,getContext()));

//        MenuItem item = getMenu().findItem(R.id.listRecordsFragment);
//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getContext(), "Clied", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });





        return  binding.getRoot();

    }
}