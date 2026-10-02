package com.medicinemanager.ui;

import com.medicinemanager.model.HistoryEntry;
import com.medicinemanager.service.MedicineService;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.util.List;

public class HistoryPanel extends JPanel {

    private final MedicineService service;
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Medicine Name", "Date", "Scheduled Time", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public HistoryPanel(MedicineService service) {
        this.service = service;
        setLayout(new BorderLayout(10, 10));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(248, 244, 244));
        JLabel title = new JLabel("History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(128, 0, 0));
        JPanel top = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        top.setOpaque(false);
        top.add(title);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> load());
        top.add(refresh);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        load();
    }

    private void load() {
        try {
            List<HistoryEntry> list = service.getHistory();
            tableModel.setRowCount(0);
            for (HistoryEntry h : list) {
                tableModel.addRow(new Object[]{h.getId(), h.getMedicineName(), h.getDate(), h.getScheduledTime(), h.getStatus()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
