package com.smartschool.scanit.fragments.scan_tab;

import static com.smartschool.scanit.Constants.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

@RuntimePermissions
public class ScanForAttendanceFragment extends Fragment {

    private static final String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS};
    private final int RC_SMS_CAMERA = 11;
    FragmentScanForAttendanceBinding binding;
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
    }

    private void setListeners() {
        binding.btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record_type = Record.RECORD_TYPE.In;
                ScanForAttendanceFragmentPermissionsDispatcher.showCameraWithPermissionCheck(ScanForAttendanceFragment.this);
            }
        });

        binding.btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record_type = Record.RECORD_TYPE.Out;
                ScanForAttendanceFragmentPermissionsDispatcher.showCameraWithPermissionCheck(ScanForAttendanceFragment.this);
            }
        });

    }

//    private void checkCameraPermission() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(), Manifest.permission.CAMERA) ==
//                PackageManager.PERMISSION_GRANTED) {
//            startScanQRActivity();
//        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//            Toast.makeText(requireContext(), "Camera permission is required to scan QR code", Toast.LENGTH_SHORT).show();
//        } else {
//            // You can directly ask for the permission.
//            // The registered ActivityResultCallback gets the result of this request.
//            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
//        }
//    }


    private void startScanQRActivity() {
        Intent intent = new Intent(requireContext(), ScanQRCodeActivity.class);
        intent.putExtra(Constants.KEY_RECORD_TYPE, record_type.toString());
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ScanForAttendanceFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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