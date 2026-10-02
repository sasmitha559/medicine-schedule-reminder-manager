package com.medicinemanager.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the SQLite database connection and schema creation.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:medicine_manager.db";

    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Creates the database file and required tables if they do not exist.
     */
    public static void initialize() {
        String createMedicines = """
                CREATE TABLE IF NOT EXISTS medicines (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    dosage TEXT NOT NULL,
                    type TEXT NOT NULL,
                    start_date TEXT NOT NULL,
                    end_date TEXT NOT NULL,
                    reminder_time TEXT NOT NULL,
                    notes TEXT
                )""";

        String createHistory = """
                CREATE TABLE IF NOT EXISTS medicine_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    medicine_name TEXT NOT NULL,
                    date TEXT NOT NULL,
                    scheduled_time TEXT NOT NULL,
                    status TEXT NOT NULL
                )""";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createMedicines);
            stmt.execute(createHistory);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
