package com.medicinemanager.ui;

import com.medicinemanager.model.Medicine;
import com.medicinemanager.service.MedicineService;
import com.medicinemanager.util.RoundedPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class DashboardPanel extends JPanel {

    private final MedicineService service;
    private final Consumer<String> navigator;
    private final JLabel takenLabel = new JLabel("0");
    private final JLabel missedLabel = new JLabel("0");
    private final JLabel todayCountLabel = new JLabel("0");
    private final JLabel upcomingCountLabel = new JLabel("0");
    private final JTextArea todayArea = new JTextArea(8, 40);
    private final JTextArea upcomingArea = new JTextArea(8, 40);

    public DashboardPanel(MedicineService service, Consumer<String> navigator) {
        this.service = service;
        this.navigator = navigator;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(248, 244, 244));

        JLabel welcome = new JLabel("Welcome back, Admin");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcome.setForeground(new Color(128, 0, 0));
        JLabel subtitle = new JLabel("Here's an overview of your medicine schedule.");
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        header.add(welcome);
        header.add(subtitle);
        add(header, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 4, 12, 0));
        cards.setOpaque(false);
        cards.add(statCard("Today's Medicines", todayCountLabel, new Color(128, 0, 0)));
        cards.add(statCard("Taken Today", takenLabel, new Color(46, 125, 50)));
        cards.add(statCard("Missed Today", missedLabel, new Color(198, 40, 40)));
        cards.add(statCard("Upcoming (7 days)", upcomingCountLabel, new Color(180, 120, 40)));

        todayArea.setEditable(false);
        upcomingArea.setEditable(false);
        todayArea.setBorder(null);
        upcomingArea.setBorder(null);

        RoundedPanel todayPanel = new RoundedPanel();
        todayPanel.setLayout(new BorderLayout());
        JLabel t = new JLabel("Today's Medicines");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        todayPanel.add(t, BorderLayout.NORTH);
        todayPanel.add(new JScrollPane(todayArea), BorderLayout.CENTER);

        RoundedPanel upcomingPanel = new RoundedPanel();
        upcomingPanel.setLayout(new BorderLayout());
        JLabel u = new JLabel("Upcoming Medicines");
        u.setFont(new Font("Segoe UI", Font.BOLD, 14));
        upcomingPanel.add(u, BorderLayout.NORTH);
        upcomingPanel.add(new JScrollPane(upcomingArea), BorderLayout.CENTER);

        JPanel lists = new JPanel(new GridLayout(1, 2, 12, 0));
        lists.setOpaque(false);
        lists.add(todayPanel);
        lists.add(upcomingPanel);

        JPanel actions = new JPanel(new GridLayout(0, 1, 0, 8));
        actions.setOpaque(false);
        actions.add(new JLabel("Quick Actions"));
        actions.add(quickButton("+ Add Medicine", "add"));
        actions.add(quickButton("Manage Medicines", "manage"));
        actions.add(quickButton("View History", "history"));
        RoundedPanel actionsCard = new RoundedPanel();
        actionsCard.setLayout(new BorderLayout());
        actionsCard.add(actions);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.add(cards, BorderLayout.NORTH);
        center.add(lists, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
        add(actionsCard, BorderLayout.EAST);

        refresh();
    }

    private JButton quickButton(String text, String card) {
        JButton b = new JButton(text);
        b.addActionListener(e -> navigator.accept(card));
        return b;
    }

    private RoundedPanel statCard(String title, JLabel valueLabel, Color accent) {
        RoundedPanel card = new RoundedPanel();
        card.setLayout(new GridLayout(2, 1));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accent);
        JLabel t = new JLabel(title);
        t.setForeground(Color.GRAY);
        card.add(valueLabel);
        card.add(t);
        return card;
    }

    public void refresh() {
        try {
            LocalDate today = LocalDate.now();
            takenLabel.setText(String.valueOf(service.countTakenToday()));
            missedLabel.setText(String.valueOf(service.countMissedToday()));

            StringBuilder todaySb = new StringBuilder();
            StringBuilder upcomingSb = new StringBuilder();
            int todayCount = 0, upcomingCount = 0;
            List<Medicine> all = service.getAllMedicines();
            for (Medicine m : all) {
                if (m.isScheduledOn(today)) {
                    todayCount++;
                    todaySb.append("- ").append(m.getName()).append(" (")
                            .append(m.getDosage()).append(") at ").append(m.getReminderTime()).append('\n');
                }
                boolean upcoming = false;
                for (int i = 1; i <= 7 && !upcoming; i++) {
                    LocalDate d = today.plusDays(i);
                    if (m.isScheduledOn(d)) {
                        upcoming = true;
                        upcomingCount++;
                        upcomingSb.append("- ").append(d.format(DateTimeFormatter.ofPattern("dd MMM")))
                                .append(" ").append(m.getReminderTime()).append(" : ").append(m.getName()).append('\n');
                    }
                }
            }
            todayCountLabel.setText(String.valueOf(todayCount));
            upcomingCountLabel.setText(String.valueOf(upcomingCount));
            todayArea.setText(todaySb.length() == 0 ? "No medicines scheduled today." : todaySb.toString());
            upcomingArea.setText(upcomingSb.length() == 0 ? "No upcoming medicines." : upcomingSb.toString());
        } catch (SQLException e) {
            todayArea.setText("Error: " + e.getMessage());
        }
    }
}
