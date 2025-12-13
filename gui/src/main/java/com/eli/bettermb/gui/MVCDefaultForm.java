package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class DefaultFormView extends JPanel
{
    JButton bookButton = new JButton("BOOK MEAL");
    JButton cancelButton = new JButton("CANCEL MEAL");
    DefaultFormView()
    {
        setLayout(new GridLayout(0, 1));
        add(bookButton);
        add(cancelButton);
    }
    void onBookButtonPressed(ActionListener listener)
    {
        bookButton.addActionListener(listener);
    }
    void onCancelButtonPressed(ActionListener listener)
    {
        cancelButton.addActionListener(listener);
    }
}

class DefaultFormController
{
    DefaultFormController(MainController MControl, DefaultFormView view)
    {
        view.onCancelButtonPressed(e -> MControl.setActionsArea(MControl.CFView));
        view.onBookButtonPressed(e -> MControl.setActionsArea(MControl.BFView));
    }
}
