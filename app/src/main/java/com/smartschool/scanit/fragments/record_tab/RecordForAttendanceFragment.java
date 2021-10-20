package com.smartschool.scanit.fragments.record_tab;

import android.os.Bundle;
import android.os.Environment;
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
import com.anggrayudi.storage.file.DocumentFileCompat;
import com.anggrayudi.storage.file.DocumentFileUtils;
import com.anggrayudi.storage.media.MediaFile;
import com.anggrayudi.storage.permission.ActivityPermissionRequest;
import com.smartschool.scanit.Constants;
import com.smartschool.scanit.R;
import com.smartschool.scanit.adapters.RecordsRVAdapter;
import com.smartschool.scanit.databinding.FragmentRecordForAttendanceBinding;
import com.smartschool.scanit.fragments.ConfirmationDialog;
import com.smartschool.scanit.fragments.MessageDialog;
import com.smartschool.scanit.models.Record;
import com.smartschool.scanit.models.RecordToExport;
import com.smartschool.scanit.shared.Config;
import com.smartschool.scanit.utils.DBUtils;
import com.smartschool.scanit.utils.DateTimeFormatUtils;
import com.smartschool.scanit.utils.FileUtils;
import com.smartschool.scanit.utils.RVEmptyObserver;

import org.jetbrains.annotations.NotNull;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class RecordForAttendanceFragment extends Fragment {

    private static final int WRITE_PERMISSION_REQUEST_CODE = 50;
    private static final int REQUEST_CODE_STORAGE_ACCESS = 99;
    private final SimpleStorageHelper storageHelper = new SimpleStorageHelper(this);
    FragmentRecordForAttendanceBinding binding;
    RecordsRVAdapter adapter;
    ArrayList<Record> recordArrayList;
    final String[] header = new String[]{"Name", "Arrival Date Time", "Departure Date Time"};
    private ActivityPermissionRequest permissionRequest;
//    private int STORAGE_REQUEST_CODE = 90;

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
        recordArrayList = new ArrayList<>();
        adapter = new RecordsRVAdapter(recordArrayList, requireContext());
        adapter.registerAdapterDataObserver(new RVEmptyObserver(binding.recyclerView, binding.emptyLayout.getRoot()));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setListeners() {
        binding.btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCSV();
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recordArrayList.isEmpty()){
                    Toast.makeText(requireContext(), "Attendance list is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ConfirmationDialog confirmationDialog = new ConfirmationDialog();
                confirmationDialog.setOnDialogButtonClickListener(new ConfirmationDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked() {
                        DBUtils.deleteAttendanceRecords();
                        recordArrayList.clear();
                        adapter.notifyDataSetChanged();
                        confirmationDialog.dismiss();
                        showMessageDialog("Deleted successfully", "Records list has been successfully deleted");
                    }
                });
                confirmationDialog.show(getChildFragmentManager(),"confirmation-dialog");
            }
        });
    }

    private void showMessageDialog(String title, String message) {
        MessageDialog messageDialog = new MessageDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        messageDialog.setArguments(bundle);
        messageDialog.show(getChildFragmentManager(), "message-dialog");
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


            root.canWrite();
            Log.d(Constants.TAG, "root.canWrite(): " + root.canWrite());
            Log.d(Constants.TAG, "base path: " + DocumentFileUtils.getRootPath(root, requireContext()));
            Log.d(Constants.TAG, "DocumentFileUtils.isWritable root: " + DocumentFileUtils.isWritable(root, requireContext()));
            DocumentFile destination = DocumentFileCompat.fromFullPath(requireContext(), absolutePath);
            assert destination != null;
            Log.d(Constants.TAG, "DocumentFileUtils.isWritable destination: " + DocumentFileUtils.isWritable(destination, requireContext()));
            DocumentFile destination2 = DocumentFile.fromFile(new File(absolutePath));
            Log.d(Constants.TAG, "DocumentFileUtils.isWritable destination2: " + DocumentFileUtils.isWritable(destination2, requireContext()));
            Log.d(Constants.TAG, "root.canRead(): " + root.canRead());
            Log.d(Constants.TAG, "setupSimpleStorage: " + absolutePath);

            Config.getInstance().setStoragePath(absolutePath);
            createCSV();
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
        DocumentFileUtils.moveFileTo(source, requireContext(), destinationFolder, null, new FileCallback() {
            @Override
            public void onConflict(@NotNull DocumentFile destinationFile, @NotNull FileCallback.FileConflictAction action) {
                Toast.makeText(requireContext(), "OnConflict", Toast.LENGTH_SHORT).show();
                Log.d(Constants.TAG, "onConflict: ");
            }

            @Override
            public void onCompleted(@NotNull Object result) {
                Log.d(Constants.TAG, "onCompleted: ");
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

    private void createCSV() {
        ArrayList<RecordToExport> recordToExportArrayList = generateRecordToExportArray();
        Log.d(Constants.TAG, "exportToCSV: " + recordToExportArrayList.size());

        ICsvListWriter listWriter = null;
        try {
            String inputDirPath = getActivity().getFilesDir().getAbsolutePath();
            String inputFileName = "/Attendance-records-" + System.currentTimeMillis() + ".csv";
            File file = new File(inputDirPath + inputFileName);

            if (!file.exists())
                file.createNewFile();
            Log.d(Constants.TAG, "exportToCSV: " + file.getAbsoluteFile());
//            File file     = new File();
            listWriter = new CsvListWriter(new FileWriter(file),
                    CsvPreference.STANDARD_PREFERENCE);

            // write the header
            listWriter.writeHeader(header);


            for (RecordToExport recordToExport : recordToExportArrayList) {
                Log.d(Constants.TAG, "exportToCSV: " + recordToExport);
                listWriter.write(
                        recordToExport.getFullName(),
                        DateTimeFormatUtils.getFormattedLocalDateTime(recordToExport.getArrivalDateTime()),
                        DateTimeFormatUtils.getFormattedLocalDateTime(recordToExport.getDepartureDateTime())
                );
            }

            listWriter.close();
            exportCSV(inputDirPath,inputFileName);
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(Constants.TAG, "exportToCSV Exception: " + e.getMessage());
        }

    }

    private void exportCSV(String inputDirPath, String inputFileName) throws IOException {
        String destinationDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/" + getString(R.string.app_name);
        Log.d(Constants.TAG, "destinationDir: " + destinationDir);
        FileUtils.moveFile(inputDirPath, inputFileName, destinationDir);

        showMessageDialog("Exported successfully", "Records list has been successfully exported to:\n" + destinationDir + inputFileName);
//        Toast.makeText(requireContext(), "Exported to " + destinationDir, Toast.LENGTH_LONG).show();
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