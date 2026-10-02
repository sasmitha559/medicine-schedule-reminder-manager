package com.medicinemanager.service;

import com.medicinemanager.model.HistoryEntry;
import com.medicinemanager.model.Medicine;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Background scheduler that checks medicine times and shows reminders.
 */
public class ReminderService {

    private final MedicineService medicineService = new MedicineService();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "reminder-scheduler");
        t.setDaemon(true);
        return t;
    });

    /** Tracks already-notified medicine/date/time keys to avoid duplicates. */
    private final Set<String> notified = new HashSet<>();

    public void start() {
        executor.scheduleAtFixedRate(this::checkReminders, 0, 20, TimeUnit.SECONDS);
    }

    public void stop() {
        executor.shutdownNow();
    }

    private void checkReminders() {
        try {
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now().withSecond(0).withNano(0);
            List<Medicine> medicines = medicineService.getAllMedicines();
            for (Medicine m : medicines) {
                if (m.isScheduledOn(today) && m.getReminderTime().equals(now)) {
                    String key = m.getId() + "-" + today + "-" + m.getReminderTime();
                    boolean alreadyRecorded = medicineService.historyExists(m.getName(), today, m.getReminderTime());
                    if (!notified.contains(key) && !alreadyRecorded) {
                        notified.add(key);
                        SwingUtilities.invokeLater(() -> showReminder(m, today));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showReminder(Medicine m, LocalDate date) {
        Object[] options = {"TAKEN", "MISSED"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Time to take medicine:\n\n" + m.getName() + " - " + m.getDosage() + "\nScheduled at: " + m.getReminderTime(),
                "Medicine Reminder",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        if (choice == 0 || choice == 1) {
            String status = choice == 0 ? "TAKEN" : "MISSED";
            try {
                medicineService.recordHistory(new HistoryEntry(m.getName(), date, m.getReminderTime(), status));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to save history: " + e.getMessage());
            }
        }
    }
}
