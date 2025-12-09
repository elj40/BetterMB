package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

class ButtonBack extends Button implements ActionListener
{
    List<ActionListener> listeners = new ArrayList<>();
    ButtonBack(String text, ActionListener form)
    {
        super(text);
        addActionListener(form);
        setActionCommand("BACK");
    }
    public void addListener(ActionListener a)
    {
        listeners.add(a);
    }
    public void actionPerformed(ActionEvent e)
    {
        for (ActionListener l : listeners) l.actionPerformed(e);
    }
};
