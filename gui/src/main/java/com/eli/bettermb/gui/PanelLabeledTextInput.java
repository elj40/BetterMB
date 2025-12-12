package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class PanelLabeledTextInput extends JPanel
{
    JLabel label;
    JTextField textField;
    Button button;

    String actionCommand;
    PanelLabeledTextInput(String label_str, String button_label, String textfield_value)
    {
        setLayout(new BorderLayout());

        label = JDebug.createDebugLabel(label_str);
        add(label, BorderLayout.NORTH);

        textField = new JTextField(textfield_value);
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
