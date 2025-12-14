package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.text.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

class BookFormView
    extends FormView
{
    LabelFormatText dateInput;
    LabelComboBox slotInput = new LabelComboBox("[Book] Select slot: ", ">");
    LabelComboBox faclInput = new LabelComboBox("[Book] Select facility: ", ">");
    LabelComboBox optnInput = new LabelComboBox("[Book] Select option: ", ">");
    LabelNumberSpinner daysInput = new LabelNumberSpinner("[Book] Enter days: ", ">");

    LabelInput[] inputs = {
            dateInput,
            slotInput,
            faclInput,
            optnInput,
            daysInput
    };
    JButton bookButton = new JButton("Book");
    JButton backButton = new JButton("Back");

    BookFormView(LocalDate date)
    {
        setLayout(new GridLayout(0,1));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        dateInput = new LabelFormatText("[Book] Enter date: ", ">", dateFormatter);

        Date d = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        dateInput.textField.setValue(d);

        slotInput.setEnabled(false);
        faclInput.setEnabled(false);
        optnInput.setEnabled(false);
        daysInput.setEnabled(false);

        add(dateInput);
        add(slotInput);
        add(faclInput);
        add(optnInput);
        add(daysInput);

        bookButton.setEnabled(false);
        add(bookButton);

        add(backButton);
    }
    void onGoBack(ActionListener listener)
    {
        backButton.addActionListener(listener);
    }
    void onDateEnter(ActionListener listener) { dateInput.addActionListener(listener); };
    void onSlotEnter(ActionListener listener) { slotInput.addActionListener(listener); };
    void onFaclEnter(ActionListener listener) { faclInput.addActionListener(listener); };
    void onOptnEnter(ActionListener listener) { optnInput.addActionListener(listener); };
    void onDaysEnter(ActionListener listener) { daysInput.addActionListener(listener); };

    void clearAllInputs()
    {
        dateInput.textField.setValue(new Date());
        slotInput.clear(); slotInput.setEnabled(false);
        faclInput.clear(); faclInput.setEnabled(false);
        optnInput.clear(); optnInput.setEnabled(false);
        daysInput.clear(); daysInput.setEnabled(false);
    }
    void disableInputsFrom(LabelInput in)
    {
        boolean disabling = false;
        for (LabelInput l : inputs)
        {
            if (l == in) disabling = true;
            if (disabling) l.setEnabled(false);
        }
    }
}

class BookFormController
{
    BookFormController(MainController MControl, BookFormView view)
    {
        view.onGoBack(e -> MControl.setAndClearActionsArea(MControl.DFView));
        view.onDateEnter(e ->  MControl.bookingDateEntered(view.dateInput.getText()) );
        view.onSlotEnter(e ->  MControl.bookingSlotEntered(view.slotInput.getText()) );
        view.onFaclEnter(e ->  MControl.bookingFaclEntered(view.faclInput.getText()) );
        view.onOptnEnter(e ->  MControl.bookingOptnEntered(view.optnInput.getText()) );
        view.onDaysEnter(e ->  MControl.bookingDaysEntered(view.daysInput.getText()) );
    }
}
