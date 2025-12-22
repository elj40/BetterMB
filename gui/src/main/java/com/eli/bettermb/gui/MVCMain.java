package com.eli.bettermb.gui;

import com.eli.bettermb.client.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Date;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Iterator;

import java.lang.IllegalArgumentException;
import java.io.IOException;


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
    AbstractClient client;
    List<Meal> meals = new ArrayList<>();
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

    final char[] SlotCodes = {'B', 'L', 'D'};

    MainModel(AbstractClient client)
    {
        this.client = client;
        Iterator<Integer> random = (new Random()).ints().iterator();
        // Temporary stuff to set up for testing
        for (int i = 0; i < 50; i++)
        {
            Meal meal = new Meal();
            meal.canModify = ((random.next() % 2) == 0);

            var time = LocalDateTime.now().plusDays(random.next()%40);

            meal.title = "Meal";
            meal.start = time.toString();
            meal.description = "Food";
            meal.facility = "Majubs";
            meal.mealTime = time.toLocalTime().toString();
            meal.mealCost = "R99.99";
            meal.mealSlot = SlotCodes[java.lang.Math.abs(random.next()) % 3];
            meal.backgroundColor = "#123456";
            meal.borderColor = "#123456";
            meal.id = java.lang.Math.abs(random.next() % 10000000);
            meals.add(meal);
        }
    }

    List<MealBookingResponse> bookSync()
        throws IOException
    {
        return client.book(mealBookingOptions);
        // Iterator<Integer> random = (new Random()).ints().iterator();
        // List<MealBookingResponse> responses = new ArrayList<>();
        // for (int i = 0; i < mealBookingOptions.advanceBookingDays; i++)
        // {
        //     Meal meal = new Meal();
        //     meal.canModify = ((random.next() % 2) == 0);

        //     var date = LocalDate.parse(mealBookingOptions.mealDate).plusDays(i);
        //     var datetime = LocalDateTime.of(date, LocalTime.now()).withNano(0);

        //     meal.title = "Booked";
        //     meal.start = datetime.toString();
        //     meal.description = "Food";
        //     meal.facility = "Majubs";
        //     meal.mealTime = datetime.toLocalTime().toString();
        //     meal.mealCost = "R99.99";
        //     meal.mealSlot = mealBookingOptions.mealSlot;
        //     meal.backgroundColor = "#654321";
        //     meal.borderColor = "#654321";
        //     meal.id = java.lang.Math.abs(random.next() % 10000000);

        //     if (meal.canModify)
        //     {
        //         meals.add(meal);
        //         var response = new MealBookingResponse();
        //         response.bookingDate = meal.start;
        //         response.bookingMessage = "success";
        //         responses.add(response);
        //     } else
        //     {
        //         var response = new MealBookingResponse();
        //         response.bookingDate = meal.start;
        //         response.bookingMessage = "failed for whatever reason";
        //         responses.add(response);
        //     }
        // }
        // return responses;
    };

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

        List<MealSlot> slots = client.getAvailableMealSlots(date);
        if (slots == null)
        {
            //TODO: throw an exception that tells view to show user that it failed
            return null;
        }

        List<String> descriptions = new ArrayList<>();
        SlotCodeMap.clear();
        for (MealSlot slot : slots)
        {
            SlotCodeMap.put(slot.description, slot.code);
            descriptions.add(slot.description);
        };

        return descriptions.toArray(new String[0]);
    }

    String[] getAvailableMealFaclities(String slotDescription)
    {
        if (!SlotCodeMap.containsKey(slotDescription)) throw new IllegalArgumentException();

        String date = mealBookingOptions.mealDate;
        char   slot = (char) SlotCodeMap.get(slotDescription);
        List<MealFacility> facilites = client.getAvailableMealFacilities(date, slot);
        if (facilites == null)
        {
            //TODO: throw an exception that tells view to show user that it failed
            return null;
        }

        FacilityCodeMap.clear();
        List<String> descriptions = new ArrayList<>();
        for (MealFacility facility : facilites)
        {
            FacilityCodeMap.put(facility.description, facility.code);
            descriptions.add(facility.description);
        }
        return descriptions.toArray(new String[0]);
    }

    String[] getAvailableMealOptions(String facilityDescription)
        throws IOException
    {
        if (!FacilityCodeMap.containsKey(facilityDescription)) throw new IllegalArgumentException();

        String date = mealBookingOptions.mealDate;
        char   slot = mealBookingOptions.mealSlot;
        int facility = mealBookingOptions.mealFacility;
        List<MealOption> options = client.getMealOptions(date, slot, facility);
        if (options == null)
        {
            //TODO: throw an exception that tells view to show user that it failed
            return null;
        }

        OptionCodeMap.clear();
        SessionCodeMap.clear();
        List<String> descriptions = new ArrayList<>();
        for (MealOption option : options)
        {
            OptionCodeMap.put(option.description, option.code);
            SessionCodeMap.put(option.description, option.sessionId);
            descriptions.add(option.description);
        }
        return descriptions.toArray(new String[0]);
    }
    List<CalendarMealView> getAllMealsToDisplay()
    {
        //Placeholder till we set up client
        var mealViews = new ArrayList<CalendarMealView>();
        Color colors[] = { Color.YELLOW, Color.GREEN, Color.BLUE };
        for (Meal m : meals)
        {
            var date = LocalDate.parse(m.start.substring(0,m.start.indexOf('T')));
            int slot = 0;
            for (int i = 0; i < SlotCodes.length; i++) { if (SlotCodes[i] == m.mealSlot) { slot = i; break; }; }

            var label = m.description;
            var color = Color.decode(m.backgroundColor);
            mealViews.add(new CalendarMealView(date, slot, new SlotMealView(label, color)));
        }
        return mealViews;
    };
    boolean isSlotBooked(LocalDate date, int slot)
    {
        String mealDate = date.toString();
        char mealSlot = SlotCodes[slot];
        for (Meal m: meals)
        {
            if (mealSlot != m.mealSlot) continue;
            if (!m.start.startsWith(mealDate)) continue;
            return true;
        }
        return false;
    }
    int getMealIDFromSlot(LocalDate date, int slot)
    {
        String mealDate = date.toString();
        char mealSlot = SlotCodes[slot];
        for (Meal m: meals)
        {
            if (mealSlot != m.mealSlot) continue;
            if (!m.start.startsWith(mealDate)) continue;
            return m.id;
        }
        throw new IllegalArgumentException(
                "No meal found with slot="+mealSlot+" and date="+mealDate);
    }
}
class MainController
{
    MainView view = new MainView();
    MainModel model = new MainModel(new StubClient());

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
        suppressEvents = true;
        panel.clearAllInputs();
        view.sidebar.setActionsArea(panel);
        suppressEvents = false;
    }
    void setActionsArea(FormView panel)
    {
        view.sidebar.setActionsArea(panel);
    }
    void setInfoArea(JPanel panel)
    {
        view.sidebar.setInfoArea(panel);
    };
    void setInfoAreaWithBookingResults(List<MealBookingResponse> responses)
    {
        JPanel responsePanel = JDebug.createDebugPanel();
        responsePanel.setLayout(new BorderLayout());
        responsePanel.add(new JLabel("Booking responses:"), BorderLayout.NORTH);
        JTextArea ta = new JTextArea();
        for (MealBookingResponse mbr : responses)
        {
            ta.append(String.format("[%1$s] %2$s\n", mbr.bookingDate, mbr.bookingMessage));
        };
        responsePanel.add(ta);
        setInfoArea(responsePanel);
    }
    void onCalendarDayPressed(LocalDate date)
    {
        suppressEvents = true;
        BFView = new BookFormView(date);
        BFControl = new BookFormController(this, BFView);
        setActionsArea(BFView);
        bookingDateEntered(date.toString());
        suppressEvents = false;
    }
    void onCalendarSlotPressed(LocalDate date, int slot)
    {
        // TODO: I dont wanna know which slot was selected
        // better to know which meal that slot represents (meal title, meal code or something)
        // ^--> I prefer slots now since I am going to let the model deal with that headache

        if (model.isSlotBooked(date, slot))
        {
            int mealID = model.getMealIDFromSlot(date, slot);
            System.out.println(mealID);
            setAndClearActionsArea(CFView);
            suppressEvents = true;
            CFView.inputID.setValue(Integer.toString(mealID));
            suppressEvents = false;
        }
        else
        {
            onCalendarDayPressed(date);
            // TODO: this should come from models expected slots (actual slots gets loaded and then gets double checked)
            // BFView.slotInput.comboBox.setSelectedIndex(slot);
            // bookingSlotEntered(BFView.slotInput.getText());
        };
    }
    void prepareRestOfBookingForm(LabelComboBox next, String[] options)
    {
        // TODO: consider this to be a BookingFormView function
        // Right now convenient to keep here because of suppressEvents
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
        if (slot.isEmpty()) return;

        String[] options;
        try { options = model.getAvailableMealFaclities(slot); }
        catch (Exception e) { e.printStackTrace(); return; }
        model.setMealBookingSlot(slot);

        LabelComboBox next = BFView.faclInput;
        prepareRestOfBookingForm(next, options);
    };
    void bookingFaclEntered(String facility)
    {
        if (suppressEvents) return;
        if (facility.isEmpty()) return;

        String[] options;
        try { options = model.getAvailableMealOptions(facility); }
        catch (Exception e) { e.printStackTrace(); return; }
        model.setMealBookingFacility(facility);

        // TODO: use model to see if it was valid
        LabelComboBox next = BFView.optnInput;
        prepareRestOfBookingForm(next, options);
    };
    void bookingOptnEntered(String option)
    {
        if (suppressEvents) return;
        if (option.isEmpty()) return;

        try { model.setMealBookingOption(option); }
        catch (IllegalArgumentException e) { e.printStackTrace(); }

        LabelNumberSpinner next = BFView.daysInput;
        next.setEnabled(true);
        BFView.bookButton.setEnabled(true);
    };
    void bookingDaysEntered(int days)
    {
        try { model.setMealBookingAhead(days); }
        catch (IllegalArgumentException e) { e.printStackTrace(); }
        model.endMealBooking();
        book();
    };
    void bookingBookPressed()
    {
        model.endMealBooking();
        book();
    };
    void book()
    {
        List<MealBookingResponse> results = new ArrayList<>();
        try { results = model.bookSync(); }
        catch (IOException e) { e.printStackTrace(); };

        setInfoAreaWithBookingResults(results);
        List<CalendarMealView> meals = model.getAllMealsToDisplay();
        calControl.setCalendarMeals(meals);
    }
}
