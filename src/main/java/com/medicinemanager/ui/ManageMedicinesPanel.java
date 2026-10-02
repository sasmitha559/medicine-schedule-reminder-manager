package com.medicinemanager.ui;

import com.medicinemanager.model.Medicine;
import com.medicinemanager.service.MedicineService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ManageMedicinesPanel extends JPanel {

    private final MedicineService service;
    private final Runnable onChanged;
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Dosage", "Type", "Start Date", "End Date", "Time", "Notes"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private final JTextField searchField = new JTextField(20);

    public ManageMedicinesPanel(MedicineService service, Runnable onChanged) {
        this.service = service;
        this.onChanged = onChanged;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(248, 244, 244));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        JLabel title = new JLabel("Manage Medicines     ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(128, 0, 0));
        top.add(title);
        JButton search = new JButton("Search");
        JButton showAll = new JButton("Show All");
        search.addActionListener(e -> load(searchField.getText().trim()));
        showAll.addActionListener(e -> {
            searchField.setText("");
            load("");
        });
        top.add(new JLabel("Search:"));
        top.add(searchField);
        top.add(search);
        top.add(showAll);

        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton taken = new JButton("Mark TAKEN");
        JButton missed = new JButton("Mark MISSED");
        edit.addActionListener(e -> editSelected());
        delete.addActionListener(e -> deleteSelected());
        taken.addActionListener(e -> markDose("TAKEN"));
        missed.addActionListener(e -> markDose("MISSED"));
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(edit);
        bottom.add(delete);
        bottom.add(taken);
        bottom.add(missed);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        load("");
    }

    private void load(String keyword) {
        try {
            List<Medicine> list = keyword.isEmpty() ? service.getAllMedicines() : service.searchMedicines(keyword);
            tableModel.setRowCount(0);
            for (Medicine m : list) {
                tableModel.addRow(new Object[]{m.getId(), m.getName(), m.getDosage(), m.getType(),
                        m.getStartDate(), m.getEndDate(), m.getReminderTime(), m.getNotes()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private int selectedId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first.");
            return -1;
        }
        return (int) tableModel.getValueAt(row, 0);
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first.");
            return;
        }
        JTextField name = new JTextField((String) tableModel.getValueAt(row, 1));
        JTextField dosage = new JTextField((String) tableModel.getValueAt(row, 2));
        JComboBox<String> type = new JComboBox<>(new String[]{"Tablet", "Capsule", "Syrup", "Injection", "Drops", "Cream", "Other"});
        type.setSelectedItem(tableModel.getValueAt(row, 3));
        JTextField start = new JTextField(String.valueOf(tableModel.getValueAt(row, 4)));
        JTextField end = new JTextField(String.valueOf(tableModel.getValueAt(row, 5)));
        JTextField time = new JTextField(String.valueOf(tableModel.getValueAt(row, 6)));
        JTextField notes = new JTextField(String.valueOf(tableModel.getValueAt(row, 7)));

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Name:")); panel.add(name);
        panel.add(new JLabel("Dosage:")); panel.add(dosage);
        panel.add(new JLabel("Type:")); panel.add(type);
        panel.add(new JLabel("Start Date:")); panel.add(start);
        panel.add(new JLabel("End Date:")); panel.add(end);
        panel.add(new JLabel("Time (HH:mm):")); panel.add(time);
        panel.add(new JLabel("Notes:")); panel.add(notes);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Medicine", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Medicine m = new Medicine(selectedId(), name.getText().trim(), dosage.getText().trim(),
                        (String) type.getSelectedItem(), LocalDate.parse(start.getText().trim()),
                        LocalDate.parse(end.getText().trim()), LocalTime.parse(time.getText().trim()), notes.getText().trim());
                service.updateMedicine(m);
                load("");
                onChanged.run();
                JOptionPane.showMessageDialog(this, "Medicine updated.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void markDose(String status) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine row first.");
            return;
        }
        String name = String.valueOf(tableModel.getValueAt(row, 1));
        String timeText = String.valueOf(tableModel.getValueAt(row, 6));
        try {
            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalTime time = java.time.LocalTime.parse(timeText);
            if (service.historyExists(name, today, time)) {
                JOptionPane.showMessageDialog(this, "Already recorded for today at " + time);
                return;
            }
            service.recordHistory(new com.medicinemanager.model.HistoryEntry(name, today, time, status));
            onChanged.run();
            JOptionPane.showMessageDialog(this, name + " marked as " + status + ". Check History.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected() {
        int id = selectedId();
        if (id == -1) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this medicine?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.deleteMedicine(id);
                load("");
                onChanged.run();
                JOptionPane.showMessageDialog(this, "Medicine deleted.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}
