package com.medicinemanager;

import com.medicinemanager.database.DatabaseManager;
import com.medicinemanager.ui.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;

public class Main {

    public static void main(String[] args) {
        DatabaseManager.initialize();
        SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
            } catch (Exception ignored) {
            }
            Color maroon = new Color(128, 0, 0);
            UIManager.put("Button.arc", 18);
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.background", maroon);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.hoverBackground", new Color(160, 30, 30));
            UIManager.put("Component.focusColor", maroon);
            UIManager.put("defaultFont", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
            UIManager.put("Table.background", Color.WHITE);
            UIManager.put("TableHeader.background", new Color(128, 0, 0));
            UIManager.put("TableHeader.foreground", Color.WHITE);
            UIManager.put("Table.selectionBackground", new Color(245, 220, 220));
            UIManager.put("Table.selectionForeground", Color.BLACK);
            new MainFrame();
        });
    }
}
