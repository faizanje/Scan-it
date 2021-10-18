package com.smartschool.scanit.fragments.record_tab;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anggrayudi.storage.SimpleStorageHelper;
import com.anggrayudi.storage.callback.FileCallback;
import com.anggrayudi.storage.file.DocumentFileUtils;
import com.anggrayudi.storage.media.MediaFile;
import com.anggrayudi.storage.permission.ActivityPermissionRequest;
import com.anggrayudi.storage.permission.PermissionCallback;
import com.anggrayudi.storage.permission.PermissionReport;
import com.anggrayudi.storage.permission.PermissionResult;
import com.smartschool.scanit.Constants;
import com.smartschool.scanit.adapters.RecordsRVAdapter;
import com.smartschool.scanit.databinding.FragmentRecordForAttendanceBinding;
import com.smartschool.scanit.models.Record;
import com.smartschool.scanit.models.RecordToExport;
import com.smartschool.scanit.utils.DBUtils;
import com.smartschool.scanit.utils.RVEmptyObserver;

import org.jetbrains.annotations.NotNull;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class RecordForAttendanceFragment extends Fragment {

    private static final int WRITE_PERMISSION_REQUEST_CODE = 50;
    private static final int REQUEST_CODE_STORAGE_ACCESS = 99;
    private final SimpleStorageHelper storageHelper = new SimpleStorageHelper(this);
//    private final ActivityPermissionRequest permissionRequest = new ActivityPermissionRequest.Builder(requireActivity())
//            .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//            .withCallback(new PermissionCallback() {
//                @Override
//                public void onPermissionsChecked(@NotNull PermissionResult result, boolean fromSystemDialog) {
//                    String grantStatus = result.getAreAllPermissionsGranted() ? "granted" : "denied";
//                    Toast.makeText(requireContext(), "Storage permissions are " + grantStatus, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onShouldRedirectToSystemSettings(@NotNull List<PermissionReport> blockedPermissions) {
//                    SimpleStorageHelper.redirectToSystemSettings(requireContext());
//                }
//            })
//            .build();
    FragmentRecordForAttendanceBinding binding;
    RecordsRVAdapter adapter;
    ArrayList<Record> recordArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecordForAttendanceBinding.inflate(getLayoutInflater());
        setupSimpleStorage(savedInstanceState);
        init();
        setListeners();
        getData();
        return binding.getRoot();
    }

    private void init() {

//        binding.lottieView.animationView.setRenderMode(RenderMode.HARDWARE);
        recordArrayList = new ArrayList<>();
        adapter = new RecordsRVAdapter(recordArrayList, requireContext());
        adapter.registerAdapterDataObserver(new RVEmptyObserver(binding.recyclerView, binding.emptyLayout.getRoot()));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setListeners() {
        binding.tvExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToCSV();
//                storageHelper.requestStorageAccess();
            }
        });
    }

    private void getData() {
        recordArrayList.clear();
        recordArrayList.addAll(DBUtils.getAttendanceRecords());
        Collections.sort(recordArrayList, new Comparator<Record>() {
            @Override
            public int compare(Record lhs, Record rhs) {
                return lhs.getFullName().compareTo(rhs.getFullName());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void setupSimpleStorage(Bundle savedState) {
        if (savedState != null) {
            storageHelper.onRestoreInstanceState(savedState);
        }
        storageHelper.setOnStorageAccessGranted((requestCode, root) -> {
            String absolutePath = DocumentFileUtils.getAbsolutePath(root, requireContext());
            Toast.makeText(
                    requireContext(),
                    "Granted " + absolutePath,
                    Toast.LENGTH_SHORT
            ).show();
            return null;
        });

        storageHelper.setOnFileCreated((requestCode, file) -> {
            String message = "File created: " + file.getName();
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

//            DocumentFile.fromFile()
            return null;
        });

    }

    private void moveFile(DocumentFile source, DocumentFile destinationFolder) {
        DocumentFileUtils.moveFileTo(source, requireContext() , destinationFolder, null, new FileCallback() {
            @Override
            public void onConflict(@NotNull DocumentFile destinationFile, @NotNull FileCallback.FileConflictAction action) {
                // do stuff
            }

            @Override
            public void onCompleted(@NotNull Object result) {
                if (result instanceof DocumentFile) {
                    Toast.makeText(requireContext(), "Completed Document file", Toast.LENGTH_SHORT).show();
                    // do stuff
                } else if (result instanceof MediaFile) {
                    Toast.makeText(requireContext(), "Completed Media file", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onReport(Report report) {
                Log.d(Constants.TAG, "onReport: " + report.getProgress());
            }

            @Override
            public void onFailed(ErrorCode errorCode) {
                Log.d(Constants.TAG, "onFailed: " + errorCode.toString());
            }
        });
    }

    private void exportToCSV() {
        ArrayList<RecordToExport> recordToExportArrayList = generateRecordToExportArray();
        final String[] header = new String[]{"Name", "Arrival Date", "Departure"};
        Log.d(Constants.TAG, "exportToCSV: " + recordToExportArrayList.size());
//        for (RecordToExport recordToExport : recordToExportArrayList) {
//            Log.d(Constants.TAG, "exportToCSV: " + recordToExport);
//            final Map<String, Object> map = new HashMap<String, Object>();
//            map.put(header[0],recordToExport.getFullName());
//            map.put(header[1],recordToExport.getArrivalDateTime());
//            map.put(header[2],recordToExport.getDepartureDateTime());
//        }

        ICsvListWriter listWriter = null;
        try {
//            String directoryPath = getActivity().getFilesDir();
//            File fileName = new File()System.currentTimeMillis() + ".csv";
            File file = new File(getActivity().getFilesDir().getAbsolutePath() + "/data.csv");
            if (!file.exists())
                file.createNewFile();
            Log.d(Constants.TAG, "exportToCSV: " + file.getAbsoluteFile());
//            File file = new File();
            listWriter = new CsvListWriter(new FileWriter(file),
                    CsvPreference.STANDARD_PREFERENCE);

            // write the header
            listWriter.writeHeader(header);

            for (RecordToExport recordToExport : recordToExportArrayList) {
                Log.d(Constants.TAG, "exportToCSV: " + recordToExport);
                listWriter.write(
                        recordToExport.getFullName(),
                        recordToExport.getArrivalDateTime().toString(),
                        recordToExport.getDepartureDateTime().toString()
                );
            }

            listWriter.close();
            Toast.makeText(requireContext(), "Exported Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(Constants.TAG, "exportToCSV Exception: " + e.getMessage());
        }

    }

    private ArrayList<RecordToExport> generateRecordToExportArray() {
        HashMap<String, RecordToExport> recordToExportHashMap = new HashMap<>();
        for (Record record : recordArrayList) {
            RecordToExport recordToExport;
            if (recordToExportHashMap.containsKey(record.getFullName())) {
                recordToExport = recordToExportHashMap.get(record.getFullName());
            } else {
                recordToExport = new RecordToExport();
                recordToExport.setFullName(record.getFullName());
            }
            setAppropriateTimeForRecordToExport(recordToExport, record);
            recordToExportHashMap.put(record.getFullName(), recordToExport);
        }

        Collection<RecordToExport> values = recordToExportHashMap.values();
        return new ArrayList<RecordToExport>(values);
    }

    void setAppropriateTimeForRecordToExport(RecordToExport recordToExport, Record record) {
        switch (record.getRecord_type()) {
            case In:
                recordToExport.setArrivalDateTime(record.getTime());
            case Out:
                recordToExport.setDepartureDateTime(record.getTime());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        storageHelper.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}