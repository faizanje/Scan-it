package com.smartschool.scanit.fragments.record_tab;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.airbnb.lottie.RenderMode;
import com.smartschool.scanit.Constants;
import com.smartschool.scanit.R;
import com.smartschool.scanit.adapters.RecordsRVAdapter;
import com.smartschool.scanit.databinding.FragmentRecordForModuleBinding;
import com.smartschool.scanit.fragments.ConfirmationDialog;
import com.smartschool.scanit.fragments.MessageDialog;
import com.smartschool.scanit.models.Record;
import com.smartschool.scanit.models.RecordToExport;
import com.smartschool.scanit.utils.DBUtils;
import com.smartschool.scanit.utils.DateTimeFormatUtils;
import com.smartschool.scanit.utils.FileUtils;
import com.smartschool.scanit.utils.RVEmptyObserver;

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

public class RecordForModuleFragment extends Fragment {

    final String[] header = new String[]{"Name", "Get Date Time", "Pass Date Time"};
    FragmentRecordForModuleBinding binding;
    RecordsRVAdapter adapter;
    ArrayList<Record> recordArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecordForModuleBinding.inflate(getLayoutInflater());
        init();
        setListeners();
        getData();
        return binding.getRoot();
    }

    private void init() {
//        binding.emptyLayout.animationView.setRenderMode(RenderMode.HARDWARE);
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
                if (recordArrayList.isEmpty()) {
                    Toast.makeText(requireContext(), "Assignment list is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ConfirmationDialog confirmationDialog = new ConfirmationDialog();
                confirmationDialog.setOnDialogButtonClickListener(new ConfirmationDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked() {
                        DBUtils.deleteModuleRecords();
                        recordArrayList.clear();
                        adapter.notifyDataSetChanged();
                        confirmationDialog.dismiss();
                        showMessageDialog("Deleted successfully", "Records list has been successfully deleted");
//                        Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                confirmationDialog.show(getChildFragmentManager(), "confirmation-dialog");

            }
        });
    }

    private void createCSV() {
        ArrayList<RecordToExport> recordToExportArrayList = generateRecordToExportArray();
        Log.d(Constants.TAG, "exportToCSV: " + recordToExportArrayList.size());

        ICsvListWriter listWriter = null;
        try {
            String inputDirPath = getActivity().getFilesDir().getAbsolutePath();
            String inputFileName = "/Module-records-" + System.currentTimeMillis() + ".csv";
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
            exportCSV(inputDirPath, inputFileName);
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(Constants.TAG, "exportToCSV Exception: " + e.getMessage());
        }

    }

    private void exportCSV(String inputDirPath, String inputFileName) throws IOException {
        String destinationDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/" + getString(R.string.app_name);
        Log.d(Constants.TAG, "destinationDir: " + destinationDir);
        FileUtils.moveFile(inputDirPath, inputFileName, destinationDir);
        showMessageDialog("Exported successfully", "Records list has been successfully exported to:\n" + destinationDir+ inputFileName);
//        Toast.makeText(requireContext(), "Exported to " + destinationDir, Toast.LENGTH_LONG).show();
    }

    private void showMessageDialog(String title, String message) {
        MessageDialog messageDialog = new MessageDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        messageDialog.setArguments(bundle);
        messageDialog.show(getChildFragmentManager(), "message-dialog");
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
            case Get:
                recordToExport.setArrivalDateTime(record.getTime());
            case Pass:
                recordToExport.setDepartureDateTime(record.getTime());
        }
    }

    private void getData() {
        recordArrayList.clear();
        recordArrayList.addAll(DBUtils.getModuleRecords());
        Collections.sort(recordArrayList, new Comparator<Record>() {
            @Override
            public int compare(Record lhs, Record rhs) {
                return lhs.getFullName().compareTo(rhs.getFullName());
            }
        });
        adapter.notifyDataSetChanged();
    }

}