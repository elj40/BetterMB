package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

abstract class FormView
    extends JPanel
{
    abstract void clearAllInputs();
}
class DefaultFormView
    extends FormView
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
    void clearAllInputs() {};
}

class DefaultFormController
{
    DefaultFormController(MainController MControl, DefaultFormView view)
    {
        view.onCancelButtonPressed(e -> MControl.setAndClearActionsArea(MControl.CFView));
        view.onBookButtonPressed(e -> MControl.setAndClearActionsArea(MControl.BFView));
    }
}
