package com.smartschool.scanit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.smartschool.scanit.Constants;
import com.smartschool.scanit.databinding.ActivityScanQrcodeBinding;
import com.smartschool.scanit.models.Record;
import com.smartschool.scanit.utils.DBUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ScanQRCodeActivity extends AppCompatActivity {

    ActivityScanQrcodeBinding binding;
    private CodeScanner mCodeScanner;
    Record.RECORD_TYPE record_type;
    ArrayList<Record> recordArrayListToSearch = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScanQrcodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        record_type = Record.RECORD_TYPE.valueOf(getIntent().getStringExtra(Constants.KEY_RECORD_TYPE));

        mCodeScanner = new CodeScanner(this, binding.scannerView);

        recordArrayListToSearch.addAll(DBUtils.getRecordByRecordType(record_type));
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String fullName = result.getText();
                        Toast.makeText(ScanQRCodeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        if(fullName != null){
                            Record record = new Record(fullName, LocalDateTime.now(),record_type);
                            if(recordExists(record)){
                                Toast.makeText(ScanQRCodeActivity.this, "Record Already exists", Toast.LENGTH_SHORT).show();
                            }else{
                                DBUtils.storeRecord(record);
                            }
                            finish();
                        }else{
                            Toast.makeText(ScanQRCodeActivity.this, "QR code doesn't contain a name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    boolean recordExists(Record record){
        return recordArrayListToSearch.stream()
                .anyMatch(record::equals);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}