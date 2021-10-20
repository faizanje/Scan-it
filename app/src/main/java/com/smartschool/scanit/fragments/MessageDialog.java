package com.smartschool.scanit.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.smartschool.scanit.R;
import com.smartschool.scanit.databinding.DialogConfirmationBinding;
import com.smartschool.scanit.databinding.DialogDateTimeBinding;
import com.smartschool.scanit.databinding.DialogMessageBinding;
import com.smartschool.scanit.models.DateTimeFormat;
import com.smartschool.scanit.shared.Config;

import org.jetbrains.annotations.NotNull;

public class MessageDialog extends DialogFragment {

    public interface OnDialogButtonClickListener {
        void onPositiveClicked();
    }


    DialogMessageBinding binding;
    OnDialogButtonClickListener onDialogButtonClickListener;

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        this.onDialogButtonClickListener = onDialogButtonClickListener;
    }

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
        binding = DialogMessageBinding.inflate(getLayoutInflater());
        builder.setView(binding.getRoot());

            init();

        setListeners();
        return builder.create();
    }

    private void init() {
        if(getArguments() != null){
            String title = getArguments().getString("title");
            String message = getArguments().getString("message");
            binding.tvTitle.setText(title);
            binding.tvMessage.setText(message);
        }
    }

    private void setListeners() {

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
