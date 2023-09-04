package com.smartschool.scanit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.scanit.R;
import com.smartschool.scanit.databinding.HomecardLayoutBinding;
import com.smartschool.scanit.databinding.ItemRecordLayoutBinding;
import com.smartschool.scanit.models.HomeModel;
import com.smartschool.scanit.models.Record;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    ArrayList<HomeModel> list;
    Context context;

    public HomeAdapter(ArrayList<HomeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomecardLayoutBinding binding = HomecardLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HomeAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeModel newList = list.get(holder.getAdapterPosition());
        holder.binding.cvTitle.setText(newList.getTitle());
        holder.binding.cvImg.setImageResource(newList.getImg());
        holder.binding.btnTrace.setCardBackgroundColor(context.getResources().getColor(newList.getColor()));

        if(position==0){
            holder.binding.btnTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_traceFragment);
            }
        });
        }
        if(position==1){
            holder.binding.btnTrace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_attendanceFragment);
                }
            });
        }
        if(position==2){
            holder.binding.btnTrace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_healthFragment);
                }
            });
        }
        if(position==3){
            holder.binding.btnTrace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_academicFragment);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HomecardLayoutBinding binding;
        public ViewHolder(@NonNull HomecardLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
