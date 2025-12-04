package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

class ButtonCancel extends Button implements ActionListener
{
    JPanel form;
    List<ActionListener> listeners = new ArrayList<>();
    ButtonCancel(String text, JPanel form)
    {
        super(text);
        this.form = form;
        addActionListener(this);
    }
    public void addListener(ActionListener a)
    {
        listeners.add(a);
    }
    public void actionPerformed(ActionEvent e)
    {
        for (ActionListener l : listeners) l.actionPerformed(e);

        form.removeAll();
        form.add(new JLabel("TODO: cancel form"));
        form.revalidate();
        form.repaint();
    }
};
