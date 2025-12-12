package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

// Labeled Number Spinner Input
class PanelNSPI extends JPanel
{
    JLabel label;
    JSpinner numberField;
    Button button;

    String actionCommand;
    PanelNSPI(String label_str, String button_label)
    {
        setLayout(new BorderLayout());

        label = JDebug.createDebugLabel(label_str);
        add(label, BorderLayout.NORTH);

        var spinnerModel = new SpinnerNumberModel(0, 0, 31, 1);
        numberField = new JSpinner(spinnerModel);
        add(numberField, BorderLayout.CENTER);

        button = new Button(button_label);
        add(button, BorderLayout.EAST);

        setActionCommand("NSPI");
    }
    String getText()
    {
        return (String) numberField.getValue();
    }
    public void setEnabled(boolean enabled)
    {
        numberField.setEnabled(enabled);
        button.setEnabled(enabled);
    }
    void setActionCommand(String ac)
    {
        button.setActionCommand(ac);
    }
    void addActionListener(ActionListener a)
    {
        button.addActionListener(a);
    }
    void removeActionListener(ActionListener a)
    {
        button.removeActionListener(a);
    }
}
