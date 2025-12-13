package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

import javax.swing.text.*;

import java.text.SimpleDateFormat;
import java.util.Date;

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

class CancelFormView extends JPanel
{
    PanelLFTI inputID;
    JButton backButton = new JButton("Back");
    CancelFormView()
    {
        setLayout(new GridLayout(0,1));

        MaskFormatter fmt = new MaskFormatter();
        try { fmt.setMask("#######"); }
        catch (Exception e) { e.printStackTrace(); }
        fmt.setPlaceholderCharacter('＿');

        inputID = new PanelLFTI("[Cancel] Enter meal ID:", ">", fmt);
        inputID.setActionCommand("CANCEL_ID");
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
}

class CancelFormController
{
    CancelFormController(MainController MControl, CancelFormView view)
    {
        view.onGoBack(e -> MControl.setActionsArea(MControl.DFView));
        view.onIDEnter(e -> {
            String text = view.inputID.getText();
            MControl.cancelMeal(text);
        });
    }
}

class BookFormView extends JPanel
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

    BookFormView()
    {
        setLayout(new GridLayout(0,1));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        dateInput = new LabelFormatText("[Book] Enter date: ", ">", dateFormatter);
        dateInput.textField.setValue(new Date());

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
        view.onGoBack(e -> MControl.view.sidebar.setActionsArea(MControl.DFView));
        view.onDateEnter(e ->  MControl.bookingDateEntered(view.dateInput.getText()) );
        view.onSlotEnter(e ->  MControl.bookingSlotEntered(view.slotInput.getText()) );
        view.onFaclEnter(e ->  MControl.bookingFaclEntered(view.faclInput.getText()) );
        view.onOptnEnter(e ->  MControl.bookingOptnEntered(view.optnInput.getText()) );
        view.onDaysEnter(e ->  MControl.bookingDaysEntered(view.daysInput.getText()) );
    }
}

class SidebarView extends JPanel
{
    JPanel actionsArea  = new JPanel();
    JButton aboutButton = new JButton("ABOUT");
    JButton homeButton  = new JButton("HOME");
    SidebarView() {

        setLayout(new BorderLayout());
        JDebug.addDebugFeatures(this);
        JPanel pages = JDebug.createDebugPanel();
        {
            pages.setLayout(new GridLayout(0, 1));
            pages.add(homeButton);
            pages.add(aboutButton);
        }
        add(pages, BorderLayout.NORTH);

        add(actionsArea, BorderLayout.SOUTH);
    }
    void setActionsArea(JPanel actionsAreaView)
    {
        actionsArea.removeAll();
        actionsArea.add(actionsAreaView);
        actionsArea.revalidate();
        actionsArea.repaint();
    };
    void onGoToAbout(ActionListener listener)
    {
        aboutButton.addActionListener(listener);
    }
    void onGoToHome(ActionListener listener)
    {
        homeButton.addActionListener(listener);
    }
}

class PanelSidebar extends JPanel implements ActionListener
{
    List<ActionListener> listeners = new ArrayList<>();
    JPanel actionsArea;
    JButton aboutButton = new JButton("ABOUT");
    JButton homeButton  = new JButton("HOME");
    PanelSidebar(JPanel calendar, JPanel content) {
        setLayout(new BorderLayout());
        JDebug.addDebugFeatures(this);
        JPanel pages = JDebug.createDebugPanel();
        {
            pages.setLayout(new GridLayout(0, 1));
            pages.add(homeButton);
            pages.add(aboutButton);
        }
        add(pages, BorderLayout.NORTH);

        actionsArea = JDebug.createDebugPanel();
        {
            actionsArea.setLayout(new BoxLayout(actionsArea, BoxLayout.Y_AXIS));
            actionsArea.add(new ButtonBook("BOOK MEAL", actionsArea));

            Button buttonCancel = new Button("CANCEL MEAL");
            buttonCancel.setActionCommand("CANCEL");
            buttonCancel.addActionListener(this);

            actionsArea.add(buttonCancel);
        }
        add(actionsArea, BorderLayout.SOUTH);
    }
    public void addListener(ActionListener a)
    {
        listeners.add(a);
    }
    public void actionPerformed(ActionEvent e)
    {
        for (ActionListener l : listeners) l.actionPerformed(e);
    }
    void onGoToAbout(ActionListener listener)
    {
        aboutButton.addActionListener(listener);
    }
    void onGoToHome(ActionListener listener)
    {
        homeButton.addActionListener(listener);
    }
}
