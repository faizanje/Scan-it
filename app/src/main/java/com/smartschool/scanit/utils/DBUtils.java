package com.smartschool.scanit.utils;

import com.smartschool.scanit.Constants;
import com.smartschool.scanit.models.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.paperdb.Paper;

public class DBUtils {

    public static void storeRecord(Record record){
        Paper.book(Constants.BOOK_RECORDS).write(record.getId(), record);
    }

    public static Record getRecordById(String id){
        return Paper.book(Constants.BOOK_RECORDS).read(id, new Record());
    }

    public static ArrayList<Record> getRecordByRecordType(Record.RECORD_TYPE record_type){
        ArrayList<Record> recordArrayList = new ArrayList<>();
        switch (record_type){
            case In:
            case Out:
                recordArrayList = getAttendanceRecords();
                break;
            case Get:
            case Pass:
                recordArrayList = getModuleRecords();
                break;
        }
        return recordArrayList.stream()
                .filter(record -> record.getRecord_type()
                        .equals(record_type)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Record> getAttendanceRecords(){
        List<String> keys = Paper.book(Constants.BOOK_RECORDS).getAllKeys();
        ArrayList<Record> recordArrayList = new ArrayList<>();
        for (String key : keys) {
            Record record = getRecordById(key);
            if(record.getRecord_type().equals(Record.RECORD_TYPE.In) ||
                    record.getRecord_type().equals(Record.RECORD_TYPE.Out)){
                recordArrayList.add(record);
            }
        }
        return recordArrayList;
    }
    public static ArrayList<Record> getModuleRecords(){
        List<String> keys = Paper.book(Constants.BOOK_RECORDS).getAllKeys();
        ArrayList<Record> recordArrayList = new ArrayList<>();
        for (String key : keys) {
            Record record = getRecordById(key);
            if(record.getRecord_type().equals(Record.RECORD_TYPE.Get) ||
                    record.getRecord_type().equals(Record.RECORD_TYPE.Pass)){
                recordArrayList.add(record);
            }
        }
        return recordArrayList;
    }
}
