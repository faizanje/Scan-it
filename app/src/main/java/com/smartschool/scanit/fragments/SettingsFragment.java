package com.smartschool.scanit.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.smartschool.scanit.R;

import com.smartschool.scanit.databinding.FragmentSettingsBinding;
import com.smartschool.scanit.shared.Config;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        init();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.switchContinuousScanning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Config.getInstance().setContinuousScanning(b);
            }
        });

        binding.switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Config.getInstance().setAlarmRingtone(b);
            }
        });

        binding.tvDateTimeFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog();
            }
        });
    }

    private void showDateTimeDialog() {
        DateTimeFormatDialog dialog = new DateTimeFormatDialog();
        dialog.show(getChildFragmentManager(),"date-time-dialog");
    }


    private void init() {
        binding.switchAlarm.setChecked(Config.getInstance().isAlarmRingtone());
        binding.switchContinuousScanning.setChecked(Config.getInstance().isContinuousScanning());
    }
}