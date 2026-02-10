package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.text.*;

class CancelFormView
    extends FormView
{
    LabelText inputID;
    JButton backButton = new JButton("Back");
    CancelFormView()
    {
        setLayout(new GridLayout(0,1));

        //MaskFormatter fmt = new MaskFormatter();
        //try { fmt.setMask("#######"); }
        //catch (Exception e) { e.printStackTrace(); }
        //fmt.setPlaceholderCharacter('＿');

        inputID = new LabelText("[Cancel] Enter meal ID:", ">");
        add(inputID);

        add(backButton);
    }
    void onGoBack(ActionListener listener)
    {
        backButton.addActionListener(listener);
    }
    void onIDEnter(ActionListener listener)
    {
        inputID.addActionListener(listener);
    }
    void clearAllInputs()
    {
        inputID.setValue("");
    }
}

class CancelFormController
{
    CancelFormController(MainController MControl, CancelFormView view)
    {
        view.onGoBack(e -> MControl.setAndClearActionsArea(MControl.DFView));
        view.onIDEnter(e -> {
            String text = view.inputID.getText();
            MControl.cancel(text);
        });
    }
}
