package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

// Labeled ComboBox Input
class PanelLCBI extends JPanel
{
    JLabel label;
    JComboBox comboBox;
    Button button;

    String actionCommand;
    PanelLCBI(String label_str, String button_label)
    {
        setLayout(new BorderLayout());

        label = JDebug.createDebugLabel(label_str);
        add(label, BorderLayout.NORTH);

        comboBox = new JComboBox();
        add(comboBox, BorderLayout.CENTER);

        button = new Button(button_label);
        add(button, BorderLayout.EAST);

        setActionCommand("LCBI");
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
