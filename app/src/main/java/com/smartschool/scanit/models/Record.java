package com.smartschool.scanit.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Record {
    String id;
    String fullName;
    LocalDateTime time;
    RECORD_TYPE record_type;

    public Record(String fullName, LocalDateTime time, RECORD_TYPE record_type) {
        this.fullName = fullName;
        this.time = time;
        this.record_type = record_type;
        this.id = UUID.randomUUID().toString();
    }

    public Record() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public RECORD_TYPE getRecord_type() {
        return record_type;
    }

    public void setRecord_type(RECORD_TYPE record_type) {
        this.record_type = record_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(fullName, record.fullName) &&
                record_type == record.record_type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, record_type);
    }

    @Override
    public String toString() {
        return "Record{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", time=" + time +
                ", record_type=" + record_type +
                '}';
    }

    public enum RECORD_TYPE {
        In,
        Out,
        Get,
        Pass
    }
}
