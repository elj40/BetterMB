package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Date;
import java.time.YearMonth;
import java.time.LocalDate;

class MainView
    extends JPanel
{
    JPanel header;
    SidebarView sidebar;

    JPanel content;
    CalendarView calendar = new CalendarView();

    MainView()
    {
        setLayout(new BorderLayout());

        content = new JPanel();
        content.setLayout(new BorderLayout());

        header = new PanelHeader("BetterMB-GUI", "28178564");
        sidebar = new SidebarView();

        content.add(calendar, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }
    void setContent(JPanel c)
    {
        content.removeAll();
        content.add(c);
        content.revalidate();
        content.repaint();
    }
}
class MainModel
{
}
class MainController
{
    MainView view = new MainView();
    MainModel model = new MainModel();

    DefaultFormView DFView = new DefaultFormView();
    DefaultFormController DFControl = new DefaultFormController(this, DFView);

    CancelFormView CFView = new CancelFormView();
    CancelFormController CFControl = new CancelFormController(this, CFView);

    BookFormView BFView = new BookFormView(LocalDate.now());
    BookFormController BFControl = new BookFormController(this, BFView);

    CalendarController CalControl;
    boolean suppressEvents;

    MainController(MainView view, MainModel model)
    {
        this.view = view;
        this.model = model;
        CalControl = new CalendarController(this, view.calendar);

        this.view.sidebar.onGoToAbout(e -> onGoToAbout());
        this.view.sidebar.onGoToHome(e -> onGoToHome());

        this.view.sidebar.setActionsArea(DFView);
    }
    void onGoToAbout()
    {
        JPanel about = new JPanel();
        about.setForeground(Color.WHITE);
        about.setBackground(Color.BLACK);
        about.add(new JLabel("ABOUT"));
        view.setContent(about);
    }
    void onGoToHome()
    {
        // Probably will need to do some model stuff here once we use the client
        view.setContent(view.calendar);
    }
    void cancelMeal(String id)
    {
        System.out.println("[STUB] cancel meal " + id);
    }
    void setAndClearActionsArea(FormView panel)
    {
        panel.clearAllInputs();
        view.sidebar.setActionsArea(panel);
    }
    void setActionsArea(FormView panel)
    {
        view.sidebar.setActionsArea(panel);
    }
    void onCalendarDayPressed(YearMonth month, int day)
    {
        System.out.print("TODO: onCalendarDayPressed, ");
        LocalDate date = LocalDate.of(month.getYear(), month.getMonthValue(), day);
        BFView = new BookFormView(date);
        BFControl = new BookFormController(this, BFView);
        setActionsArea(BFView);
        bookingDateEntered(date.toString());
    }
    void onCalendarSlotPressed(YearMonth month, int day, int slot)
    {
        // TODO: I dont wanna know which slot was selected
        // better to know which meal that slot represents (meal title, meal code or something)
        System.out.print("TODO: onCalendarSlotPressed, ");
        System.out.print(day);
        System.out.print(", ");
        System.out.println(slot);
        onCalendarDayPressed(month, day);

        // TODO: this should come from models expected slots (actual slots gets loaded and then gets double checked)
        BFView.slotInput.comboBox.setSelectedIndex(1+slot);
        bookingSlotEntered(BFView.slotInput.getText());

    }
    void bookingDateEntered(String info)
    {
        if (suppressEvents) return;

        // TODO: use model to see if it was valid
        LabelComboBox next = BFView.slotInput;
        // Use model to figure out what data to fill in the next field
        String[] stubReceived= {"", "Breakfast", "Lunch", "Dinner" };

        suppressEvents = true;

        BFView.disableInputsFrom(next);
        next.shouldListenerIgnore = true;
        next.setItems(stubReceived);
        next.setEnabled(true);

        suppressEvents=false;
    };
    void bookingSlotEntered(String info)
    {
        if (suppressEvents) return;
        // TODO: use model to see if it was valid

        String[] stubReceived= { "", "Huis Visser", "Majuba", "Minerva", "Dagbreek" };

        LabelInput curr = BFView.slotInput;
        LabelComboBox next = BFView.faclInput;

        suppressEvents = true;

        BFView.disableInputsFrom(next);

        next.shouldListenerIgnore = true;
        next.setItems(stubReceived);
        next.setEnabled(true);

        suppressEvents=false;
    };
    void bookingFaclEntered(String info)
    {
        if (suppressEvents) return;
        String[] stubReceived= {"", "Standard Meal", "Extra Protein", "Halaal", "Vegetarian" };

        // TODO: use model to see if it was valid
        LabelInput curr = BFView.faclInput;
        LabelComboBox next = BFView.optnInput;

        suppressEvents = true;

        BFView.disableInputsFrom(next);

        next.shouldListenerIgnore = true;
        next.setItems(stubReceived);
        next.setEnabled(true);

        suppressEvents=false;
    };
    void bookingOptnEntered(String info)
    {
        if (suppressEvents) return;

        // TODO: use model to see if it was valid
        LabelInput curr = BFView.optnInput;
        LabelNumberSpinner next = BFView.daysInput;
        // Just ignore first action, this is action from setting combobox
        suppressEvents=true;

        next.shouldListenerIgnore = true;
        next.setValue(0);
        next.setEnabled(true);

        suppressEvents=false;
    };
    void bookingDaysEntered(String info)
    {
        // TODO: use model to see if it was valid
        System.out.println("bookingDaysEntered: " + info);
        // model.setDays(info)
        // model.book()
    };
}
