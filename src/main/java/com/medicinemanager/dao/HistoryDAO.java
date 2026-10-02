package com.medicinemanager.dao;

import com.medicinemanager.database.DatabaseManager;
import com.medicinemanager.model.HistoryEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryDAO {

    public void insert(HistoryEntry entry) throws SQLException {
        String sql = "INSERT INTO medicine_history (medicine_name, date, scheduled_time, status) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entry.getMedicineName());
            ps.setString(2, entry.getDate().toString());
            ps.setString(3, entry.getScheduledTime().toString());
            ps.setString(4, entry.getStatus());
            ps.executeUpdate();
        }
    }

    public List<HistoryEntry> findAll() throws SQLException {
        List<HistoryEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM medicine_history ORDER BY date DESC, scheduled_time DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new HistoryEntry(
                        rs.getInt("id"),
                        rs.getString("medicine_name"),
                        LocalDate.parse(rs.getString("date")),
                        LocalTime.parse(rs.getString("scheduled_time")),
                        rs.getString("status")));
            }
        }
        return list;
    }

    /** Checks whether an entry already exists for the same medicine/date/time/status. */
    public boolean exists(String medicineName, LocalDate date, LocalTime time) throws SQLException {
        String sql = "SELECT COUNT(*) FROM medicine_history WHERE medicine_name=? AND date=? AND scheduled_time=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, medicineName);
            ps.setString(2, date.toString());
            ps.setString(3, time.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public int countByStatusAndDate(String status, LocalDate date) throws SQLException {
        String sql = "SELECT COUNT(*) FROM medicine_history WHERE status=? AND date=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, date.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
