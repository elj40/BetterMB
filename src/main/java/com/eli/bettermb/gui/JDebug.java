package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;

class JDebug
{
    static void addDebugFeatures(JComponent c)
    {
        c.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        c.setOpaque(true);
    }
    static JPanel createDebugPanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }
    static JLabel createDebugLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }
};
