package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ButtonBook extends Button implements ActionListener
{
    JPanel form;
    ButtonBook(String text, JPanel form)
    {
        super(text);
        this.form = form;
        addActionListener(this);
    }
    public void actionPerformed(ActionEvent e)
    {
        form.removeAll();
        form.add(new JLabel("TODO: book form"));
        form.revalidate();
        form.repaint();
    }
};
