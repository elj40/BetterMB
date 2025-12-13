package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

// Labeled ComboBox Input
class PanelLCBI extends JPanel
{
    JLabel label = JDebug.createDebugLabel("");
    JComboBox comboBox = new JComboBox();
    JButton button = new JButton();

    boolean shouldListenerIgnore = false;

    String actionCommand;
    PanelLCBI(String label_str, String button_label)
    {
        setLayout(new BorderLayout());
        label.setText(label_str);
        add(label, BorderLayout.NORTH);
        add(comboBox, BorderLayout.CENTER);
        button.setLabel(button_label);
        add(button, BorderLayout.EAST);
    }
    String getText()
    {
        return (String) comboBox.getSelectedItem();
    }
    public void addItems(String[] items)
    {
        for (String s : items)
        {
            addItem(s);
        }
    }
    public void addItem(String item)
    {
        comboBox.addItem(item);
    }
    public void setEnabled(boolean enabled)
    {
        comboBox.setEnabled(enabled);
        button.setEnabled(enabled);
    }
    void setActionCommand(String ac)
    {
        comboBox.setActionCommand(ac);
        button.setActionCommand(ac);
    }
    void addActionListener(ActionListener a)
    {
        comboBox.addActionListener(a);
        button.addActionListener(a);
    }
    void removeActionListener(ActionListener a)
    {
        comboBox.removeActionListener(a);
        button.removeActionListener(a);
    }
}
