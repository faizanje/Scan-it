package com.smartschool.scanit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.scanit.databinding.TraceListLayputBinding;
import com.smartschool.scanit.models.TraceListModel;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    ArrayList<TraceListModel> list;
    Context context;

    public AttendanceAdapter(ArrayList<TraceListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TraceListLayputBinding binding = TraceListLayputBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AttendanceAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TraceListModel newList = list.get(holder.getAdapterPosition());
        holder.binding.tvTitle.setText(newList.getTitle());
        holder.binding.tvIndex.setText(newList.getIndex());
        holder.binding.tvTime.setText(newList.getTime());
        holder.binding.tvStatus.setText(newList.getStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TraceListLayputBinding binding;
        public ViewHolder(@NonNull TraceListLayputBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
