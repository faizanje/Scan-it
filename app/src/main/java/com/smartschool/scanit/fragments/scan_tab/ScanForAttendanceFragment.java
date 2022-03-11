package com.smartschool.scanit.fragments.scan_tab;

import static com.smartschool.scanit.Constants.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.smartschool.scanit.Constants;
import com.smartschool.scanit.R;
import com.smartschool.scanit.activities.ScanQRCodeActivity;
import com.smartschool.scanit.databinding.FragmentScanForAttendanceBinding;
import com.smartschool.scanit.models.Record;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ScanForAttendanceFragment extends Fragment {

    FragmentScanForAttendanceBinding binding;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String> mSMSPermissionResult;
    private final int RC_SMS = 11;

    Record.RECORD_TYPE record_type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScanForAttendanceBinding.inflate(getLayoutInflater());
        init();
        setListeners();
        return binding.getRoot();
    }

    private void init() {
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        startScanQRActivity();
                    } else {
                        Toast.makeText(requireContext(), "Camera permission is required to scan QR code", Toast.LENGTH_SHORT).show();
                    }
                });

        mSMSPermissionResult = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if(result) {
                        Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                    } else {
                        Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                    }
                });
    }

    private void setListeners() {
        binding.btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record_type = Record.RECORD_TYPE.In;
                checkCameraPermission();
            }
        });

        binding.btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record_type = Record.RECORD_TYPE.Out;
                checkCameraPermission();
            }
        });

    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            startScanQRActivity();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(requireContext(), "Camera permission is required to scan QR code", Toast.LENGTH_SHORT).show();
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }


    private void startScanQRActivity() {
        Intent intent = new Intent(requireContext(), ScanQRCodeActivity.class);
        intent.putExtra(Constants.KEY_RECORD_TYPE, record_type.toString());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_SMS)
    private void checkSMSPermissions() {
        String[] perms = {Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            Toast.makeText(requireContext(), "SMS permissions granted", Toast.LENGTH_SHORT).show();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Phone permissions are required to send SMS.",
                    RC_SMS, perms);
        }
    }

}