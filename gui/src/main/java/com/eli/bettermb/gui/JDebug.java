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
};
