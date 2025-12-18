package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Date;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.lang.IllegalArgumentException;

import com.eli.bettermb.client.Meal;
import com.eli.bettermb.client.MealBookingOptions;

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
    List meals = new ArrayList<Meal>();
    MealBookingOptions mealBookingOptions;
    private boolean mboDateSet = false;
    private boolean mboSlotSet = false;
    private boolean mboFacilitySet = false;
    private boolean mboOptionSet = false;
    private boolean mboAheadSet = false;

    private Map SlotCodeMap     = new HashMap<String, Character>();
    private Map FacilityCodeMap = new HashMap<String, Integer>();
    private Map OptionCodeMap   = new HashMap<String, Integer>();
    // Session map should have exact, same keys. Consider merging into one map
    private Map SessionCodeMap  = new HashMap<String, Integer>();

    void startMealBooking()
    {
        mealBookingOptions = new MealBookingOptions();
    }
    void setMealBookingDate(String date)
    {
        //TODO setMealBookingDate: consider if date should be passed as LocalDate
        mealBookingOptions.mealDate = date;
        mboDateSet = true;
        mboSlotSet = false;
        mboFacilitySet = false;
        mboOptionSet = false;
        mboAheadSet = false;
    }
    void setMealBookingSlot(String slot)
    {
        if (!SlotCodeMap.containsKey(slot))
            throw new IllegalArgumentException("No slot code for " + slot);
        char slotCode = (char) SlotCodeMap.get(slot);
        mealBookingOptions.mealSlot = slotCode;
        mboSlotSet = true;
        mboFacilitySet = false;
    }
    void setMealBookingFacility(String facility)
    {
        if (!FacilityCodeMap.containsKey(facility))
            throw new IllegalArgumentException("No facility code for " + facility);
        int facilityCode = (int) FacilityCodeMap.get(facility);
        mealBookingOptions.mealFacility = facilityCode;
        mboFacilitySet = true;
        mboOptionSet = false;
    }
    void setMealBookingOption(String option)
    {
        if (!OptionCodeMap.containsKey(option))
            throw new IllegalArgumentException("No Option code for " + option);
        if (!SessionCodeMap.containsKey(option))
            throw new IllegalArgumentException("No session code for " + option);
        int optionCode = (int) OptionCodeMap.get(option);
        int sessionCode = (int) SessionCodeMap.get(option);
        mealBookingOptions.mealOption = optionCode;
        mealBookingOptions.mealSession = sessionCode;
        mboOptionSet = true;
        mboAheadSet = false;
    }
    void setMealBookingAhead(int count)
    {
        if (count < 0) throw new IllegalArgumentException();
        mealBookingOptions.advanceBookingDays = count;
        mboAheadSet = true;
    }
    void endMealBooking() throws IllegalArgumentException
    {
        if (!mboDateSet) throw new IllegalArgumentException();
        if (!mboSlotSet) throw new IllegalArgumentException();
        if (!mboFacilitySet) throw new IllegalArgumentException();
        if (!mboOptionSet) throw new IllegalArgumentException();
        if (!mboAheadSet)
        {
            mealBookingOptions.advanceBookingDays = 0;
            mboAheadSet = true;
        }
    }
    String[] getAvailableMealSlots(String date)
        throws DateTimeParseException
    {
        System.out.println("TODO: undummy getAvailableMealSlots");
        date = date.trim();
        LocalDate.parse(date); // Just to check the parsing

        // TODO: slot codes should come from client
        SlotCodeMap.clear();
        SlotCodeMap.put("Breakfast", 'B');
        SlotCodeMap.put("Lunch",     'L');
        SlotCodeMap.put("Dinner",    'D');
        // dummy client:
        return new String[]{"", "Breakfast", "Lunch", "Dinner" };
    }
    String[] getAvailableMealFaclities(String slot)
    {
        System.out.println("TODO: undummy getAvailableMealFaclities");
        FacilityCodeMap.clear();
        FacilityCodeMap.put("Majuba", 0);
        FacilityCodeMap.put("Minerva", 0);
        FacilityCodeMap.put("Sagbreek", 0);
        FacilityCodeMap.put("Huis ten Bosch", 0);
        return new String[]{"", "Majuba", "Minerva", "Sagbreek", "Huis ten Bosch"};
    }
    String[] getAvailableMealOptions(String facility)
    {
        System.out.println("TODO: undummy getAvailableMealOptions");
        OptionCodeMap.clear();
        OptionCodeMap.put("Standard Meal", 0);
        OptionCodeMap.put("Extra Protein", 0);
        OptionCodeMap.put("Chicken option", 0);
        OptionCodeMap.put("MajuGeel", 0);
        SessionCodeMap.clear();
        SessionCodeMap.put("Standard Meal", 0);
        SessionCodeMap.put("Extra Protein", 0);
        SessionCodeMap.put("Chicken option", 0);
        SessionCodeMap.put("MajuGeel", 0);
        return new String[]{"", "Standard Meal", "Extra Protein", "Chicken option", "MajuGeel"};
    }
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
    void prepareRestOfBookingForm(LabelComboBox next, String[] options)
    {
        System.out.println("TODO: consider this to be a BookingFormView function");
        suppressEvents = true;

        BFView.disableInputsFrom(next);
        next.shouldListenerIgnore = true;
        next.setItems(options);
        next.setEnabled(true);

        suppressEvents=false;
    }
    void bookingDateEntered(String date)
    {
        if (suppressEvents) return;
        model.startMealBooking();

        String[] options;
        try { options = model.getAvailableMealSlots(date); }
        catch (Exception e) { e.printStackTrace(); return; }
        model.setMealBookingDate(date);

        LabelComboBox next = BFView.slotInput;
        prepareRestOfBookingForm(next, options);
    };
    void bookingSlotEntered(String slot)
    {
        if (suppressEvents) return;

        String[] options;
        try { options = model.getAvailableMealFaclities(slot); }
        catch (Exception e) { e.printStackTrace(); return; }
        model.setMealBookingSlot(slot);

        String[] stubReceived= { "", "Huis Visser", "Majuba", "Minerva", "Dagbreek" };

        LabelComboBox next = BFView.faclInput;
        prepareRestOfBookingForm(next, options);
    };
    void bookingFaclEntered(String facility)
    {
        if (suppressEvents) return;

        String[] options;
        try { options = model.getAvailableMealOptions(facility); }
        catch (Exception e) { e.printStackTrace(); return; }
        model.setMealBookingFacility(facility);

        String[] stubReceived= {"", "Standard Meal", "Extra Protein", "Halaal", "Vegetarian" };

        // TODO: use model to see if it was valid
        LabelComboBox next = BFView.optnInput;
        prepareRestOfBookingForm(next, options);
    };
    void bookingOptnEntered(String option)
    {
        if (suppressEvents) return;

        try { model.setMealBookingOption(option); }
        catch (IllegalArgumentException e) { e.printStackTrace(); }

        LabelNumberSpinner next = BFView.daysInput;
        next.setEnabled(true);
        System.out.println("TODO: set book button to enabled");
    };
    void bookingDaysEntered(int days)
    {
        try { model.setMealBookingAhead(days); }
        catch (IllegalArgumentException e) { e.printStackTrace(); }
        model.endMealBooking();
        book();
    };

    void book()
    {
        System.out.println("TODO: book");
    }
}
