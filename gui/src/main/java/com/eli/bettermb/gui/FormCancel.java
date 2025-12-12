package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

class FormCancel
    extends JPanel
{
    PanelLFTI inputID;
    Button backButton;
    FormCancel()
    {
        setLayout(new GridLayout(0,1));

        MaskFormatter fmt = new MaskFormatter();
        try { fmt.setMask("#######"); }
        catch (Exception e) { e.printStackTrace(); }
        fmt.setPlaceholderCharacter('＿');

        inputID = new PanelLFTI("[Cancel] Enter meal ID:", ">", fmt);
        inputID.setActionCommand("CANCEL_ID");
        add(inputID);

        backButton = new Button("Back");
        backButton.setActionCommand("BACK");
        add(backButton);
    }

    void addActionListener(ActionListener a)
    {
        inputID.addActionListener(a);
        backButton.addActionListener(a);
    }
    void removeActionListener(ActionListener a)
    {
        inputID.removeActionListener(a);
        backButton.removeActionListener(a);
    }
}
