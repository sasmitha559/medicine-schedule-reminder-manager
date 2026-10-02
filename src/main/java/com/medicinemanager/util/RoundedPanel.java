package com.medicinemanager.util;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** A panel with rounded corners and a soft shadow-like border. */
public class RoundedPanel extends JPanel {

    public RoundedPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        g2.setColor(new Color(235, 220, 220));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}
