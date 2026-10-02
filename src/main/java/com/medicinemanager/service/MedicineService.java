package com.medicinemanager.service;

import com.medicinemanager.dao.HistoryDAO;
import com.medicinemanager.dao.MedicineDAO;
import com.medicinemanager.model.HistoryEntry;
import com.medicinemanager.model.Medicine;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MedicineService {

    private final MedicineDAO medicineDAO = new MedicineDAO();
    private final HistoryDAO historyDAO = new HistoryDAO();

    public void addMedicine(Medicine medicine) throws SQLException {
        medicineDAO.insert(medicine);
    }

    public void updateMedicine(Medicine medicine) throws SQLException {
        medicineDAO.update(medicine);
    }

    public void deleteMedicine(int id) throws SQLException {
        medicineDAO.delete(id);
    }

    public List<Medicine> getAllMedicines() throws SQLException {
        return medicineDAO.findAll();
    }

    public List<Medicine> searchMedicines(String keyword) throws SQLException {
        return medicineDAO.search(keyword);
    }

    public List<HistoryEntry> getHistory() throws SQLException {
        return historyDAO.findAll();
    }

    public void recordHistory(HistoryEntry entry) throws SQLException {
        historyDAO.insert(entry);
    }

    public boolean historyExists(String medicineName, LocalDate date, java.time.LocalTime time) throws SQLException {
        return historyDAO.exists(medicineName, date, time);
    }

    public int countTakenToday() throws SQLException {
        return historyDAO.countByStatusAndDate("TAKEN", LocalDate.now());
    }

    public int countMissedToday() throws SQLException {
        return historyDAO.countByStatusAndDate("MISSED", LocalDate.now());
    }
}
