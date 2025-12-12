package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

// Labeled Formatted Text Input
class PanelLFTI extends JPanel
{
    JLabel label;
    JFormattedTextField textField;
    Button button;

    String actionCommand;
    PanelLFTI(String label_str, String button_label, DefaultFormatter textfield_format)
    {
        setLayout(new BorderLayout());

        label = JDebug.createDebugLabel(label_str);
        add(label, BorderLayout.NORTH);

        textField = new JFormattedTextField(textfield_format);
        add(textField, BorderLayout.CENTER);

        button = new Button(button_label);
        add(button, BorderLayout.EAST);

        setActionCommand("LabeledTextInput");
    }
    String getText()
    {
        return textField.getText();
    }
    public void setEnabled(boolean enabled)
    {
        textField.setEnabled(enabled);
        button.setEnabled(enabled);
    }
    void setActionCommand(String ac)
    {
        textField.setActionCommand(ac);
        button.setActionCommand(ac);
    }
    void addActionListener(ActionListener a)
    {
        textField.addActionListener(a);
        button.addActionListener(a);
    }
    void removeActionListener(ActionListener a)
    {
        textField.removeActionListener(a);
        button.removeActionListener(a);
    }
}
