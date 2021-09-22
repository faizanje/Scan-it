package com.smartschool.scanit.models;

import java.time.LocalDateTime;

public class RecordToExport {
    String fullName;
    LocalDateTime arrivalDateTime, departureDateTime;

    public RecordToExport() {
    }

    public RecordToExport(String fullName, LocalDateTime arrivalDateTime, LocalDateTime departureDateTime) {
        this.fullName = fullName;
        this.arrivalDateTime = arrivalDateTime;
        this.departureDateTime = departureDateTime;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

}
