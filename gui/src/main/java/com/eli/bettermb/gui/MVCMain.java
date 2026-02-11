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
import java.util.concurrent.CompletableFuture;

import java.lang.IllegalArgumentException;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

record ExpectedOptionsResult(int index, String[] options) {};

class MainView
    extends JPanel
{
    JPanel header;
    SidebarView sidebar;

    JPanel content;
    CalendarView calendar = new CalendarView();
    SettingsView settings = new SettingsView();

    MainView()
    {
        setLayout(new BorderLayout());

        content = new JPanel();
        content.setLayout(new BorderLayout());

        sidebar = new SidebarView();

        content.add(calendar, BorderLayout.CENTER);

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

record BookAsyncResult(
        List<MealBookingResponse> responses,
        CompletableFuture<List<Meal>> futureMeals) {};

class MainModel
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Client client;
    SettingsModel settings = new SettingsModel();

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
    final String[] SlotDescriptions = {"Breakfast", "Lunch", "Dinner"};

    final String sun_url = "https://web-apps.sun.ac.za";
    final String mysun_link = "https://my.sun.ac.za/tracker?linkID=239&lang=en";
    final String sun_entry_url = mysun_link;
    final String sun_target_url = sun_url;

    MainModel(Client client)
    {
        this.client = client;
    }

    // TODO:
    // these functions save and load the entirety of all meals ever booked
    // this means the cache file can get very large and slow to parse eventually
    // Need to consider caching strategies that make sense
    void saveMealsToFile(String filename)
    {
        String json = gson.toJson(meals);
        Path path = Paths.get(filename);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, json.getBytes());
            System.out.println("Meals saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }

    }

    void loadMealsFromFile(String filename)
    {
        Type mealListType = new TypeToken<List<Meal>>(){}.getType();

        // Read the JSON file and convert it to List<MyObject>
        try {
            String json = new String(Files.readAllBytes(Paths.get(filename)));
            List<Meal> loadedMeals = gson.fromJson(json, mealListType);
            meals = loadedMeals;
        } catch (IOException e) {
            System.out.println("[loadMealsFromFile] failed: " + e.getMessage());
        }
    }

    void tryGetMealsBookedInMonth(String date)
    {
        try { meals = client.getMealsBookedInMonth(date); }
        catch (IOException e) { System.out.println("[tryGetMealsBookedInMonth] failed: " + e.toString()); }
    }

    List<MealBookingResponse> bookSync()
        throws IOException
    {
        List<MealBookingResponse> responses = client.book(mealBookingOptions);
        List<Meal> newMeals = client.getMealsBookedInMonth(mealBookingOptions.mealDate);
        meals.addAll(newMeals);
        return responses;
    };

    BookAsyncResult bookAsync()
    {
        List<MealBookingResponse> responses = new ArrayList<>();
        try { responses= client.book(mealBookingOptions); }
        catch (Exception e) { e.printStackTrace(); };
        //List<Meal> newMeals = client.getMealsBookedInMonth(mealBookingOptions.mealDate);
        CompletableFuture<List<Meal>> futureMeals = client.getMealsBookedInMonthAsync(mealBookingOptions.mealDate);
        return new BookAsyncResult(responses, futureMeals);
    };

    MealCancelResponse cancel(int id) throws IOException
    {
        MealCancelResponse mcr = client.cancel(id);
        if (mcr.success) removeMealByID(id);
        return mcr;
    }

    void removeMealByID(int id)
    {
        // Removes meal from the local memory,
        // does nothing if meal already was not in local memory
        Meal toRemove = null;
        for (Meal m: meals)
        {
            if (m.id == id)
            {
                toRemove = m;
                break;
            }
        }
        if (toRemove != null) meals.remove(toRemove);
    }

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

    ExpectedOptionsResult getExpectedSlots(LocalDate date, int slot)
    {
        List<String> options = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < SlotCodes.length; i++)
        {
            if (!isSlotBooked(date,slot))
            {
                if (SlotDescriptions[slot].equals(SlotDescriptions[i])) index = options.size();
                options.add(SlotDescriptions[i]);
            }
        }
        return new ExpectedOptionsResult(index, options.toArray(new String[0]));
    };

    String[] getAvailableMealSlots(String date)
        throws DateTimeParseException
    {
        date = date.trim();
        LocalDate.parse(date); // Just to check the parsing

        List<MealSlot> slots = new ArrayList<>();
        try { slots = client.getAvailableMealSlots(date); }
        catch (Exception e) { e.printStackTrace(); }

        if (slots == null)
        {
            //TODO: throw an exception that tells view to show user that it failed
            return null;
        }

        List<String> descriptions = new ArrayList<>();
        descriptions.add("");
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
        List<MealFacility> facilites = new ArrayList<>();
        try { facilites = client.getAvailableMealFacilities(date, slot); }
        catch (Exception e) { e.printStackTrace(); }

        if (facilites == null)
        {
            //TODO: throw an exception that tells view to show user that it failed
            return null;
        }

        FacilityCodeMap.clear();
        List<String> descriptions = new ArrayList<>();
        descriptions.add("");
        for (MealFacility facility : facilites)
        {
            FacilityCodeMap.put(facility.description, facility.code);
            descriptions.add(facility.description);
            System.out.println(facility.description + ": " + Integer.toString(facility.code));
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
        descriptions.add("");
        for (MealOption option : options)
        {
            OptionCodeMap.put(option.description, option.code);
            SessionCodeMap.put(option.description, option.sessionId);
            descriptions.add(option.description);
            System.out.println(option.description + ": " + Integer.toString(option.code));
        }
        return descriptions.toArray(new String[0]);
    }
    List<CalendarMealView> getAllMealViews()
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
    MainView view;
    MainModel model;

    SettingsModel settingsModel = new SettingsModel();
    SettingsView settingsView = new SettingsView();

    DefaultFormView DFView = new DefaultFormView();
    DefaultFormController DFControl = new DefaultFormController(this, DFView);

    CancelFormView CFView = new CancelFormView();
    CancelFormController CFControl = new CancelFormController(this, CFView);

    BookFormView BFView = new BookFormView(LocalDate.now());
    BookFormController BFControl = new BookFormController(this, BFView);

    CalendarController calControl;
    boolean suppressEvents;

    final String settingsFilePath = "."+File.separator+"bettermb-settings.json";
    final String cachedMealsFilePath = "."+File.separator+"bettermb-meals.json";

    MainController(MainView view, MainModel model)
    {
        this.view = view;
        this.model = model;
        calControl = new CalendarController(this, view.calendar);

        setMonth(YearMonth.now());

        this.view.sidebar.onGoToAbout(e -> onGoToAbout());
        this.view.sidebar.onGoToHome(e -> onGoToHome());
        this.view.sidebar.onGoToSettings(e -> onGoToSettings());

        this.view.sidebar.setActionsArea(DFView);
    }

    // Init logic we would rather do once window is opened
    void start()
    {
        settingsModel.loadFromFile(settingsFilePath);
        // NOTE: doing it like this prevents us from changing the path at runtime
        settingsView.onSaveButtonPressed(e -> {
            settingsModel.cookies = settingsView.cookiesInput.getText();
            settingsModel.saveToFile(settingsFilePath); });

        settingsView.cookiesInput.setText(settingsModel.cookies);
        model.client.setCookies(settingsModel.cookies);

        String date = LocalDate.now().toString();
        model.loadMealsFromFile(cachedMealsFilePath);
        setMonth(YearMonth.parse(date.substring(0,date.length()-3))); // TODO: this is jank

        tryGetMealsBookedInMonth(date);
    }

    // TODO: clean up, this has bad practices
    void signIn(String signin_entry, String signin_target)
    {
        String cookies = null;
        System.out.println("dddd");
        if (!Client.debugging || true) // TODO: remove this horrid code
        {
            try { cookies = Client.getSecurityCookiesBySignIn(signin_entry, signin_target); }
            catch (Exception ex)
            {
                System.err.println("[signin] Failed to sign in: " + ex.getMessage());
                return;
            }
        }

        if (cookies != null)
        {
            settingsModel.cookies = cookies;
            model.client.setCookies(settingsModel.cookies);

            settingsView.cookiesInput.setText(settingsModel.cookies);
            settingsModel.saveToFile(settingsFilePath);
        }
    }

    void reload(String date)
    {
        settingsModel.cookies = settingsView.cookiesInput.getText();
        model.client.setCookies(settingsModel.cookies);
        tryGetMealsBookedInMonth(date);
        model.saveMealsToFile(cachedMealsFilePath);
    };

    void tryGetMealsBookedInMonth(String date)
    {
        model.tryGetMealsBookedInMonth(date);
        setMonth(YearMonth.parse(date.substring(0,date.length()-3))); // TODO: this is jank
    }

    void setMonth(YearMonth month)
    {
        // TODO: this should be per month
        List<CalendarMealView> meals = model.getAllMealViews();
        calControl.setMonth(month);
        calControl.setCalendarMeals(meals);
    }
    void onGoToAbout()
    {
        JPanel about = new JPanel();
        about.setLayout(new BoxLayout(about, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("ABOUT");
        about.add(title);

        JTextArea description = new JTextArea(""+
                "BetterMB (Better Meal Booking, name only somewhat justified) " +
                "is a student-written frontend for the universities meal booking API. " +
                "It aims to surpass the very low bar that the Stellenbosch university " +
                "has set for user experience. The hope is that students will collaborate " +
                "and contribute to improving this software.");
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        about.add(description);

        JLabel contr = new JLabel("Contributions");
        about.add(contr);
        JTextArea contributors = new JTextArea("Eli Joubert (elaijoubert@gmail.com): Programmer");
        contributors.setEditable(false);
        about.add(contributors);

        view.setContent(about);
    }
    void onGoToSettings()
    {
        view.setContent(settingsView);
    }
    void onGoToHome()
    {
        // Probably will need to do some model stuff here once we use the client
        view.setContent(view.calendar);
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
    void setInfoAreaWithCancelResults(MealCancelResponse mcr)
    {
        JPanel responsePanel = JDebug.createDebugPanel();
        responsePanel.setLayout(new BorderLayout());
        responsePanel.add(new JLabel("Cancel response:"), BorderLayout.NORTH);
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.append(mcr.cliDisplayString());

        JScrollPane scrollPane = new JScrollPane(ta);
        responsePanel.add(scrollPane);
        //setInfoArea(scrollPane);
    }
    void setInfoAreaWithBookingResults(List<MealBookingResponse> responses)
    {
        JPanel responsePanel = JDebug.createDebugPanel();
        responsePanel.setLayout(new BorderLayout());
        responsePanel.add(new JLabel("Booking responses:"), BorderLayout.NORTH);
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        for (MealBookingResponse mbr : responses)
        {
            ta.append(String.format("[%1$s] %2$s\n", mbr.bookingDate, mbr.bookingMessage));
        };

        responsePanel.add(ta);
        JScrollPane scrollPane = new JScrollPane(responsePanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        setInfoArea(responsePanel);
    }
    void onCalendarDayPressed(LocalDate date)
    {
        suppressEvents = true;
        BFView = new BookFormView(date);
        BFControl = new BookFormController(this, BFView);
        setActionsArea(BFView);
        suppressEvents = false;
        bookingDateEntered(date.toString());
    }
    void onCalendarSlotPressed(LocalDate date, int slot)
    {
        // better to know which meal that slot represents (meal title, meal code or something)
        // ^--> I prefer slots now since I am going to let the model deal with that headache

        if (model.isSlotBooked(date, slot))
        {
            int mealID = model.getMealIDFromSlot(date, slot);
            setAndClearActionsArea(CFView);
            suppressEvents = true;
            CFView.inputID.setValue(Integer.toString(mealID));
            suppressEvents = false;
        }
        else
        {
            onCalendarDayPressed(date);
            ExpectedOptionsResult expected = model.getExpectedSlots(date, slot);
            suppressEvents = true;
            BFView.slotInput.setItems(expected.options());
            suppressEvents = false;
            BFView.slotInput.setSelectedIndex(expected.index());

            CompletableFuture validateFuture = CompletableFuture.runAsync(
                    () -> { System.out.println("TODO: async check expected slots."); });
            try { validateFuture.get(); }
            catch (Exception e) { e.printStackTrace(); };
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
        model.setMealBookingFacility(facility);
        try { options = model.getAvailableMealOptions(facility); }
        catch (Exception e) { e.printStackTrace(); return; }

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
        int days = BFView.daysInput.getValue();
        try { model.setMealBookingAhead(days); }
        catch (IllegalArgumentException e) { e.printStackTrace(); }
        // TODO: need to set the advancebooking days here as well
        model.endMealBooking();
        book();
    };
    void book()
    {
        List<MealBookingResponse> results = new ArrayList<>();
        //try { results = model.bookSync(); }
        //catch (IOException e) { e.printStackTrace(); };
        BookAsyncResult bar = model.bookAsync();

        setInfoAreaWithBookingResults(bar.responses());

        bar.futureMeals().thenAccept(result -> {
            if (result == null) return;
            model.meals.addAll(result);
            model.saveMealsToFile(cachedMealsFilePath);
            List<CalendarMealView> meals = model.getAllMealViews();
            calControl.setCalendarMeals(meals);
        });
    }
    void cancel(String id)
    {
        MealCancelResponse mcr = new MealCancelResponse();
        try { mcr = model.cancel(Integer.parseInt(id)); } // Make sure it is a valid int
        catch (IOException e) { e.printStackTrace(); };
        setInfoAreaWithCancelResults(mcr);

        List<CalendarMealView> meals = model.getAllMealViews();
        calControl.setCalendarMeals(meals);
        model.saveMealsToFile(cachedMealsFilePath);
    }
}
