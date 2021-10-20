package com.smartschool.scanit.activities;

import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.smartschool.scanit.R;
import com.smartschool.scanit.databinding.ActivityBottomNavBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class BottomNavActivity extends AppCompatActivity{

    private final int RC_STORAGE = 10;
    private ActivityBottomNavBinding binding;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBottomNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBottomNavRadius();

        binding.navView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.listRecordsFragment, R.id.settingsFragment)
//                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_nav);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_global_scanFragment);
            }
        });

        MenuItem item = binding.navView.getMenu().findItem(R.id.listRecordsFragment);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                checkStoragePermission();
                return true;
            }
        });

    }

    private void setBottomNavRadius() {
//        float radius = getResources().getDimension(R.dimen.cornerSize);
        float radius = 100;
        MaterialShapeDrawable shapeDrawable = (MaterialShapeDrawable) binding.bottomAppBar.getBackground();
        shapeDrawable.setShapeAppearanceModel(shapeDrawable
                .getShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED,radius)
                .build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_STORAGE)
    private void checkStoragePermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            navController.navigate(R.id.action_global_listRecordsFragment);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Storage permissions are required",
                    RC_STORAGE, perms);
        }
    }

}