package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Cursor.*;

class PageButton extends Button implements ActionListener
{
    JPanel container;
    protected JPanel page;
    PageButton(String text, JPanel page, JPanel container)
    {
        super(text);
        this.page = page;
        this.container = container;
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addActionListener(this);
    }
    public void actionPerformed(ActionEvent e)
    {
        System.out.println(getLabel());
        container.removeAll();
        container.add(page);
        container.revalidate();
        container.repaint();
    };
};
