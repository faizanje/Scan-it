package com.smartschool.scanit.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.smartschool.scanit.Constants;
import com.smartschool.scanit.R;
import com.smartschool.scanit.databinding.DialogDateTimeBinding;
import com.smartschool.scanit.models.DateTimeFormat;
import com.smartschool.scanit.shared.Config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DateTimeFormatDialog extends DialogFragment {

    public static final ArrayList<String> dateFormatsArrayList = new ArrayList<String>() {
        {
            add("Month%sDay%sYear");
            add("Day%sMonth%sYear");
            add("Year%sMonth%sDay");
        }
    };
    public static final ArrayList<String> dateFormatsArrayListPlaceholder = new ArrayList<String>();
    DialogDateTimeBinding binding;
    ArrayAdapter<String> dateSeparatorAdapter, dateFormatAdapter, timeFormatAdapter;
    int selectedIndex = 0;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        if(getDialog() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        binding = DialogDateTimeBinding.inflate(getLayoutInflater());
        builder.setView(binding.getRoot());
        init();
        populateViews();
        initDateFormatsArrayList();
        setListeners();
        return builder.create();
    }

    private void initDateFormatsArrayList() {
        dateFormatsArrayListPlaceholder.clear();
        String separator = binding.dropdownDateSeparator.getText().toString();
        for (String s : dateFormatsArrayList) {
            String item = String.format(s, separator, separator);
            dateFormatsArrayListPlaceholder.add(item);
        }
        dateFormatAdapter.notifyDataSetChanged();
    }

    private void populateViews() {
        DateTimeFormat dateTimeFormat = Config.getInstance().getDateTimeFormat();
        binding.dropdownDateSeparator.setText(dateTimeFormat.getDateSeparator(), false);
        binding.dropdownDateFormat.setText(
                String.format(dateFormatsArrayList.get(dateTimeFormat.getDateFormatIndex()),
                        dateTimeFormat.getDateSeparator(), dateTimeFormat.getDateSeparator()),false);
        binding.dropdownTimeFormat.setText(dateTimeFormat.getTimeFormat(), false);
        dateFormatAdapter.notifyDataSetChanged();
    }


    private void init() {
        setCancelable(true);
        dateSeparatorAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, requireContext().getResources().getStringArray(R.array.date_separator));
        dateFormatAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, dateFormatsArrayListPlaceholder);
        timeFormatAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, requireContext().getResources().getStringArray(R.array.time_format));
        binding.dropdownDateSeparator.setAdapter(dateSeparatorAdapter);
        binding.dropdownDateFormat.setAdapter(dateFormatAdapter);
        binding.dropdownTimeFormat.setAdapter(timeFormatAdapter);
    }

    private void setListeners() {
        binding.dropdownDateSeparator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                initDateFormatsArrayList();
                binding.dropdownDateFormat.setText(dateFormatAdapter.getItem(0), false);
            }
        });

        binding.dropdownDateFormat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIndex = i;
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDateTimeFormat();
                dismiss();
            }
        });
    }

    private void updateDateTimeFormat() {
        DateTimeFormat dateTimeFormat = Config.getInstance().getDateTimeFormat();
        dateTimeFormat.setDateSeparator(binding.dropdownDateSeparator.getText().toString());
        dateTimeFormat.setDateFormatIndex(selectedIndex);
        dateTimeFormat.setTimeFormat(binding.dropdownTimeFormat.getText().toString());
        Config.getInstance().setDateTimeFormat(dateTimeFormat);
    }

}
