package com.medicinemanager.ui;

import com.medicinemanager.model.Medicine;
import com.medicinemanager.service.MedicineService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddMedicinePanel extends JPanel {

    private final MedicineService service;
    private final Runnable onSaved;

    private final JTextField nameField = new JTextField(20);
    private final JTextField dosageField = new JTextField(20);
    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"Tablet", "Capsule", "Syrup", "Injection", "Drops", "Cream", "Other"});
    private final JTextField startDateField = new JTextField("yyyy-MM-dd", 20);
    private final JTextField endDateField = new JTextField("yyyy-MM-dd", 20);
    private final JTextField timeField = new JTextField("HH:mm", 20);
    private final JTextField notesField = new JTextField(20);

    public AddMedicinePanel(MedicineService service, Runnable onSaved) {
        this.service = service;
        this.onSaved = onSaved;
        setBackground(new Color(248, 244, 244));
        JLabel title = new JLabel("Add New Medicine");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(128, 0, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setLayout(new GridBagLayout());

        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.gridwidth = 2;
        titleGbc.insets = new Insets(0, 6, 16, 6);
        titleGbc.anchor = GridBagConstraints.WEST;
        add(title, titleGbc);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        addRow(gbc, 1, "Medicine Name:", nameField);
        addRow(gbc, 2, "Dosage (e.g. 500mg):", dosageField);
        addRow(gbc, 3, "Medicine Type:", typeBox);
        addRow(gbc, 4, "Start Date (yyyy-MM-dd):", startDateField);
        addRow(gbc, 5, "End Date (yyyy-MM-dd):", endDateField);
        addRow(gbc, 6, "Reminder Time (HH:mm):", timeField);
        addRow(gbc, 7, "Notes:", notesField);

        JButton save = new JButton("Save Medicine");
        JButton clear = new JButton("Clear");
        save.addActionListener(e -> save());
        clear.addActionListener(e -> clear());

        gbc.gridx = 1;
        gbc.gridy = 8;
        JPanel buttons = new JPanel();
        buttons.add(save);
        buttons.add(clear);
        add(buttons, gbc);
    }

    private void addRow(GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }

    private void save() {
        try {
            String name = nameField.getText().trim();
            String dosage = dosageField.getText().trim();
            if (name.isEmpty() || dosage.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and dosage are required.");
                return;
            }
            LocalDate start = LocalDate.parse(startDateField.getText().trim());
            LocalDate end = LocalDate.parse(endDateField.getText().trim());
            LocalTime time = LocalTime.parse(timeField.getText().trim());
            if (end.isBefore(start)) {
                JOptionPane.showMessageDialog(this, "End date cannot be before start date.");
                return;
            }
            Medicine m = new Medicine(0, name, dosage, (String) typeBox.getSelectedItem(), start, end, time, notesField.getText().trim());
            service.addMedicine(m);
            JOptionPane.showMessageDialog(this, "Medicine added successfully.");
            clear();
            onSaved.run();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date/time format. Use yyyy-MM-dd and HH:mm.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clear() {
        nameField.setText("");
        dosageField.setText("");
        startDateField.setText("yyyy-MM-dd");
        endDateField.setText("yyyy-MM-dd");
        timeField.setText("HH:mm");
        notesField.setText("");
    }
}
