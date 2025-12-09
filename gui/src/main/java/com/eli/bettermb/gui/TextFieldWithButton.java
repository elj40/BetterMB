package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

class TextFieldWithButton
    extends JPanel
    implements ActionListener
{
    JTextField textField;
    Button button;

    List<ActionListener> listeners = new ArrayList<>();

    TextFieldWithButton(String tf_text, String btn_label, String ac)
    {
        create(tf_text, btn_label, ac);
    }
    TextFieldWithButton(String tf_text, String btn_label, String ac, ActionListener al)
    {
        create(tf_text, btn_label, ac);
        addActionListener(al);
    }
    private void create(String tf_text, String btn_label, String ac)
    {
        JDebug.addDebugFeatures(this);
        setLayout(new BorderLayout());

        textField = new JTextField(tf_text);
        textField.addActionListener(this);
        textField.setActionCommand(ac);
        add(textField, BorderLayout.CENTER);

        button = new Button(btn_label);
        button.addActionListener(this);
        button.setActionCommand(ac);
        add(button, BorderLayout.EAST);
    }

    public void addActionListener(ActionListener a)
    {
        listeners.add(a);
    }
    public void removeActionListener(ActionListener a)
    {
        listeners.remove(a);
    }
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == textField || source == button) e.setSource(this);
        for (ActionListener l : listeners) l.actionPerformed(e);
    }
}
