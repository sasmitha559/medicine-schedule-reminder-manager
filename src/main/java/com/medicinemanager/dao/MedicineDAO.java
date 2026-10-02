package com.medicinemanager.dao;

import com.medicinemanager.database.DatabaseManager;
import com.medicinemanager.model.Medicine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {

    public void insert(Medicine medicine) throws SQLException {
        String sql = "INSERT INTO medicines (name, dosage, type, start_date, end_date, reminder_time, notes) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, medicine);
            ps.executeUpdate();
        }
    }

    public void update(Medicine medicine) throws SQLException {
        String sql = "UPDATE medicines SET name=?, dosage=?, type=?, start_date=?, end_date=?, reminder_time=?, notes=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, medicine);
            ps.setInt(8, medicine.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM medicines WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Medicine> findAll() throws SQLException {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines ORDER BY reminder_time";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public List<Medicine> search(String keyword) throws SQLException {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE name LIKE ? OR dosage LIKE ? OR type LIKE ? ORDER BY reminder_time";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    private void bind(PreparedStatement ps, Medicine m) throws SQLException {
        ps.setString(1, m.getName());
        ps.setString(2, m.getDosage());
        ps.setString(3, m.getType());
        ps.setString(4, m.getStartDate().toString());
        ps.setString(5, m.getEndDate().toString());
        ps.setString(6, m.getReminderTime().toString());
        ps.setString(7, m.getNotes());
    }

    private Medicine map(ResultSet rs) throws SQLException {
        return new Medicine(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("dosage"),
                rs.getString("type"),
                LocalDate.parse(rs.getString("start_date")),
                LocalDate.parse(rs.getString("end_date")),
                LocalTime.parse(rs.getString("reminder_time")),
                rs.getString("notes")
        );
    }
}
