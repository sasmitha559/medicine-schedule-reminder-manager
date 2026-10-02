package com.medicinemanager.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Medicine {

    private int id;
    private String name;
    private String dosage;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime reminderTime;
    private String notes;

    public Medicine() {
    }

    public Medicine(int id, String name, String dosage, String type, LocalDate startDate,
                    LocalDate endDate, LocalTime reminderTime, String notes) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reminderTime = reminderTime;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /** True when the medicine is scheduled for the given date. */
    public boolean isScheduledOn(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
