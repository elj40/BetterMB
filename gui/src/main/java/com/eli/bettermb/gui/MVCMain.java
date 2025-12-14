package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Date;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.ArrayList;

import com.eli.bettermb.client.Meal;

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
    List<CalendarMealView> getAllMealsToDisplay()
    {
        //Placeholder till we set up client
        var meals = new ArrayList<CalendarMealView>();
        Color colors[] = { Color.YELLOW, Color.GREEN, Color.BLUE };
        char slots[] = { 'B', 'L', 'D' };
        for (int i = 0; i < 7; i++)
        {
            var meal = new Meal();

            int psrand1 = i * 90134017;
            int psrand2 = i * 34257971;

            int ci = psrand1 % 3;
            int si = psrand2 % 3;
            int di = ((psrand1%5 + psrand2%5) % 7);

            var date = LocalDate.now().plusDays(di);
            var slot = si;
            var label = "CHICKEN";
            var color = colors[ci];
            meals.add(new CalendarMealView(date, slot, new SlotMealView(label, color)));
        }
        return meals;
    };
    boolean isSlotBooked(LocalDate date, int slot)
    {
        System.out.println("TODO: MainModel.isSlotBooked()");
        return true;
    }
    int getMealIDFromSlot(LocalDate date, int slot)
    {
        System.out.println("TODO: MainModel.getMealIDFromSlot()");
        return 1234567;
    }
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

    CalendarController calControl;
    boolean suppressEvents;

    MainController(MainView view, MainModel model)
    {
        this.view = view;
        this.model = model;
        calControl = new CalendarController(this, view.calendar);

        List<CalendarMealView> meals = model.getAllMealsToDisplay();
        calControl.setCalendarMeals(meals);

        this.view.sidebar.onGoToAbout(e -> onGoToAbout());
        this.view.sidebar.onGoToHome(e -> onGoToHome());

        this.view.sidebar.setActionsArea(DFView);
    }

    void setMonth(YearMonth month)
    {
        // TODO: this should be per month
        List<CalendarMealView> meals = model.getAllMealsToDisplay();
        calControl.setMonth(month);
        calControl.setCalendarMeals(meals);
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
    void onCalendarDayPressed(LocalDate date)
    {
        System.out.print("TODO: onCalendarDayPressed, ");
        BFView = new BookFormView(date);
        BFControl = new BookFormController(this, BFView);
        setActionsArea(BFView);
        bookingDateEntered(date.toString());
    }
    void onCalendarSlotPressed(LocalDate date, int slot)
    {
        // TODO: I dont wanna know which slot was selected
        // better to know which meal that slot represents (meal title, meal code or something)
        // ^--> I prefer slots now since I am going to let the model deal with that headache
        System.out.print("TODO: onCalendarSlotPressed, ");
        System.out.println(slot);

        if (model.isSlotBooked(date, slot))
        {
            int mealID = model.getMealIDFromSlot(date, slot);
            setAndClearActionsArea(CFView);
            suppressEvents = true;
            CFView.inputID.setValue(Integer.toString(mealID));
            suppressEvents = false;
        } else
        {
            onCalendarDayPressed(date);
            // TODO: this should come from models expected slots (actual slots gets loaded and then gets double checked)
            BFView.slotInput.comboBox.setSelectedIndex(1+slot);
            bookingSlotEntered(BFView.slotInput.getText());
        };
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
