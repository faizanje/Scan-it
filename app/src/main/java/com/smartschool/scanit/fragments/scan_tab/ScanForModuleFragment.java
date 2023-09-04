package com.smartschool.scanit.fragments.scan_tab;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.smartschool.scanit.Constants;
import com.smartschool.scanit.activities.ScanQRCodeActivity;
import com.smartschool.scanit.databinding.FragmentScanForModuleBinding;
import com.smartschool.scanit.models.Record;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ScanForModuleFragment extends Fragment {

    FragmentScanForModuleBinding binding;
    Record.RECORD_TYPE record_type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScanForModuleBinding.inflate(getLayoutInflater());
        init();
        setListeners();
        return binding.getRoot();
    }

    private void init() {
    }

    private void setListeners() {
        binding.btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record_type = Record.RECORD_TYPE.Get;
                ScanForModuleFragmentPermissionsDispatcher.showCameraWithPermissionCheck(ScanForModuleFragment.this);
            }
        });

        binding.btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record_type = Record.RECORD_TYPE.Pass;
                ScanForModuleFragmentPermissionsDispatcher.showCameraWithPermissionCheck(ScanForModuleFragment.this);
            }
        });

    }


    private void startScanQRActivity() {
        Intent intent = new Intent(requireContext(), ScanQRCodeActivity.class);
        intent.putExtra(Constants.KEY_RECORD_TYPE, record_type.toString());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ScanForModuleFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})
    void showCamera() {
        Toast.makeText(requireContext(), "Permissions granted", Toast.LENGTH_SHORT).show();
        startScanQRActivity();
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(requireContext())
                .setMessage("Phone and camera permissions are required to send SMS and scan QR code respectively.")
                .setPositiveButton("Allow", (dialog, button) -> request.proceed())
                .setNegativeButton("Deny", (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})
    void showDeniedForCamera() {
        Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})
    void showNeverAskForCamera() {
        Toast.makeText(requireContext(), "Permissions required for camera and phone are denied. Please allow them from your phone settings", Toast.LENGTH_SHORT).show();
    }
}