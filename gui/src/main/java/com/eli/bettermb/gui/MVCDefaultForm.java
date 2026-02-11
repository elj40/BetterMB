package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.time.LocalDate;

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
    JButton signinButton = new JButton("SIGN IN");
    JButton reloadButton = new JButton("RELOAD");
    DefaultFormView()
    {
        setLayout(new GridLayout(0, 1));
        add(signinButton);
        add(reloadButton);
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
    void onSignInButtonPressed(ActionListener listener)
    {
        signinButton.addActionListener(listener);
    }
    void onReloadButtonPressed(ActionListener listener)
    {
        reloadButton.addActionListener(listener);
    }
    void clearAllInputs() {};
}

class DefaultFormController
{
    DefaultFormController(MainController MControl, DefaultFormView view)
    {
        view.onCancelButtonPressed(e -> MControl.setAndClearActionsArea(MControl.CFView));
        view.onBookButtonPressed(e -> MControl.setAndClearActionsArea(MControl.BFView));
        view.onSignInButtonPressed(e -> MControl.signIn(MControl.model.sun_entry_url, MControl.model.sun_target_url));
        view.onReloadButtonPressed(e -> MControl.reload(LocalDate.now().toString()));
    }
}
