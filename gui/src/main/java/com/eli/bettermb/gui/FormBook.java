package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

import java.text.SimpleDateFormat;
import java.util.Date;

class FormBook
    extends JPanel
{
    PanelLFTI dateInput;
    PanelLCBI slotInput;
    PanelLCBI faclInput;
    PanelLCBI optnInput;
    PanelNSPI daysInput;
    Button bookButton;
    Button backButton;

    FormBook()
    {
        setLayout(new GridLayout(0,1));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        dateInput = new PanelLFTI("[Book] Enter date: ", ">", dateFormatter);
        dateInput.textField.setValue(new Date());

        slotInput = new PanelLCBI("[Book] Select slot: ", ">");
        faclInput = new PanelLCBI("[Book] Select facility: ", ">");
        optnInput = new PanelLCBI("[Book] Select option: ", ">");
        daysInput = new PanelNSPI("[Book] Enter days: ", ">");

        dateInput.setActionCommand("BOOK_DATE");
        slotInput.setActionCommand("BOOK_SLOT");
        faclInput.setActionCommand("BOOK_FACL");
        optnInput.setActionCommand("BOOK_OPTN");
        daysInput.setActionCommand("BOOK_DAYS");

        slotInput.setEnabled(false);
        faclInput.setEnabled(false);
        optnInput.setEnabled(false);
        daysInput.setEnabled(false);

        add(dateInput);
        add(slotInput);
        add(faclInput);
        add(optnInput);
        add(daysInput);

        bookButton = new Button("Book");
        bookButton.setActionCommand("BOOK_SUBMIT");
        add(bookButton);

        backButton = new Button("Back");
        backButton.setActionCommand("BACK");
        add(backButton);
    }

    void addActionListener(ActionListener a)
    {
        dateInput.addActionListener(a);
        slotInput.addActionListener(a);
        faclInput.addActionListener(a);
        optnInput.addActionListener(a);
        daysInput.addActionListener(a);

        backButton.addActionListener(a);
    }
    void removeActionListener(ActionListener a)
    {
        dateInput.removeActionListener(a);
        slotInput.removeActionListener(a);
        faclInput.removeActionListener(a);
        optnInput.removeActionListener(a);
        daysInput.removeActionListener(a);

        backButton.removeActionListener(a);
    }
}
