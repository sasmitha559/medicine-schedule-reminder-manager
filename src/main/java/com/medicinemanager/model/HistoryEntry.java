package com.medicinemanager.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class HistoryEntry {

    private int id;
    private String medicineName;
    private LocalDate date;
    private LocalTime scheduledTime;
    private String status;

    public HistoryEntry() {
    }

    public HistoryEntry(String medicineName, LocalDate date, LocalTime scheduledTime, String status) {
        this.medicineName = medicineName;
        this.date = date;
        this.scheduledTime = scheduledTime;
        this.status = status;
    }

    public HistoryEntry(int id, String medicineName, LocalDate date, LocalTime scheduledTime, String status) {
        this.id = id;
        this.medicineName = medicineName;
        this.date = date;
        this.scheduledTime = scheduledTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
