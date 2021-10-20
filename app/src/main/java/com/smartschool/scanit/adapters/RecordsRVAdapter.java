package com.smartschool.scanit.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.scanit.Constants;
import com.smartschool.scanit.databinding.ItemRecordLayoutBinding;
import com.smartschool.scanit.models.Record;
import com.smartschool.scanit.shared.Config;
import com.smartschool.scanit.utils.DateTimeFormatUtils;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecordsRVAdapter extends RecyclerView.Adapter<RecordsRVAdapter.MyViewHolder> {

    ArrayList<Record> recordArrayList;
    Context context;

    public RecordsRVAdapter(ArrayList<Record> recordArrayList, Context context) {
        this.recordArrayList = recordArrayList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemRecordLayoutBinding binding = ItemRecordLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Record record = recordArrayList.get(holder.getAdapterPosition());
//        Log.d(Constants.TAG, "onBindViewHolder: " + record);
//        Log.d(Constants.TAG, "onBindViewHolder: ");
        holder.binding.tvName.setText(holder.getAdapterPosition() + 1 + ". " + record.getFullName());
        holder.binding.tvTime.setText(DateTimeFormatUtils.getFormattedLocalDateTime(record.getTime()));
        holder.binding.tvRecordType.setText(record.getRecord_type().toString());
    }

    @Override
    public int getItemCount() {
        return recordArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemRecordLayoutBinding binding;

        public MyViewHolder(@NonNull @NotNull ItemRecordLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
