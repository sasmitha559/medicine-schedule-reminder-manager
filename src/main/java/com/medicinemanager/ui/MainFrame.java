package com.medicinemanager.ui;

import com.medicinemanager.service.MedicineService;
import com.medicinemanager.service.ReminderService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class MainFrame extends JFrame {

    private final MedicineService service = new MedicineService();
    private final ReminderService reminderService = new ReminderService();
    private final CardLayout cards = new CardLayout();
    private final JPanel center = new JPanel(cards);

    public MainFrame() {
        setTitle("Medicine Schedule and Reminder Manager");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DashboardPanel dashboard = new DashboardPanel(service, this::show);
        AddMedicinePanel addPanel = new AddMedicinePanel(service, () -> { dashboard.refresh(); });
        ManageMedicinesPanel managePanel = new ManageMedicinesPanel(service, dashboard::refresh);
        HistoryPanel historyPanel = new HistoryPanel(service);

        center.add(dashboard, "dashboard");
        center.add(addPanel, "add");
        center.add(managePanel, "manage");
        center.add(historyPanel, "history");

        add(buildSidebar(), BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
        getContentPane().setBackground(new Color(248, 244, 244));

        reminderService.start();
        setVisible(true);
    }

    private void show(String name) {
        cards.show(center, name);
    }

    private JPanel buildSidebar() {
        Color maroon = new Color(128, 0, 0);
        JPanel side = new JPanel(new GridLayout(0, 1, 0, 12));
        side.setBackground(maroon);
        side.setPreferredSize(new Dimension(200, 0));
        side.setBorder(BorderFactory.createEmptyBorder(30, 14, 30, 14));

        JLabel title = new JLabel("Medicine Manager", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        side.add(title);

        side.add(navButton("Dashboard", "dashboard"));
        side.add(navButton("Add Medicine", "add"));
        side.add(navButton("Manage Medicines", "manage"));
        side.add(navButton("History", "history"));
        return side;
    }

    private JButton navButton(String text, String card) {
        JButton b = new JButton(text);
        b.setBackground(new Color(96, 0, 0));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.addActionListener(e -> cards.show(center, card));
        return b;
    }
}
