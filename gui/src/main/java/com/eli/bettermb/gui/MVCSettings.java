package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

class SettingsView extends JPanel
{
    JLabel title = new JLabel("Settings");
    JLabel cookiesLabel = new JLabel("Cookies");
    JTextField cookiesInput = new JTextField();
    SettingsView()
    {
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createSetting(cookiesLabel, cookiesInput));

        add(content, BorderLayout.CENTER);
    }
    JPanel createSetting(JComponent desc, JComponent field)
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(desc, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }
}

class SettingsModel
{
    String cookies = "default=cookies";
}
