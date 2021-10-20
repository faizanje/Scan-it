package com.smartschool.scanit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.smartschool.scanit.Constants;
import com.smartschool.scanit.databinding.ActivityScanQrcodeBinding;
import com.smartschool.scanit.models.Record;
import com.smartschool.scanit.shared.Config;
import com.smartschool.scanit.utils.DBUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ScanQRCodeActivity extends AppCompatActivity {

    ActivityScanQrcodeBinding binding;
    Record.RECORD_TYPE record_type;
    ArrayList<Record> recordArrayListToSearch = new ArrayList<>();
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScanQrcodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        record_type = Record.RECORD_TYPE.valueOf(getIntent().getStringExtra(Constants.KEY_RECORD_TYPE));

        mCodeScanner = new CodeScanner(this, binding.scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recordArrayListToSearch.clear();
                        recordArrayListToSearch.addAll(DBUtils.getRecordByRecordType(record_type));
                        String fullName = result.getText();
                        if (fullName != null) {
                            Record record = new Record(fullName, LocalDateTime.now(), record_type);
                            if (recordExists(record)) {
                                Toast.makeText(ScanQRCodeActivity.this, "Record Already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ScanQRCodeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                                DBUtils.storeRecord(record);
                            }
                            if(Config.getInstance().isAlarmRingtone()){
                                ringAlarmTone();
                            }
                            if (!Config.getInstance().isContinuousScanning()) {
                                finish();
                            }else {
                                new Handler(Looper.getMainLooper())
                                        .postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mCodeScanner.startPreview();
                                            }
                                        },1500);

                            }
                        } else {
                            Toast.makeText(ScanQRCodeActivity.this, "QR code doesn't contain a name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void ringAlarmTone() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean recordExists(Record record) {
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