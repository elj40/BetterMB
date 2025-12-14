package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.HashMap;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;

import com.eli.bettermb.client.Meal;

class CellModel
{
    boolean empty;
    static final HashMap<Character, Integer> SLOT_MAP = new HashMap<>();
    static {
        SLOT_MAP.put('B', 0);
        SLOT_MAP.put('L', 1);
        SLOT_MAP.put('D', 2);
    }

    String getDisplayString(Meal meal)
    {
        if (meal == null) return "Error: <null>";
        return meal.title;
    }
    int getSlot(Meal meal)
    {
        if (meal == null) {} //IDK which slot to put the error meal in
        return SLOT_MAP.get(meal.mealSlot);
    }
    Color getColor(Meal meal)
            throws NumberFormatException
    {
        if (meal == null) return Color.RED;
        return Color.decode(meal.backgroundColor);
    }
}
class CellController
{
    CellView view;
    CellModel model = new CellModel();
    CellController(MainController MControl, CellView view)
    {
        this.view = view;
    }
    void setSlotDisplay(Meal meal)
    {
        String title = model.getDisplayString(meal);
        int slot = model.getSlot(meal);
        Color color = model.getColor(meal);

        view.setSlotDisplay(slot, title, color);
        // view.setExtraInfo()
    }
}
class CellView extends JPanel{
    JButton day;
    JButton slots[];
    int mday = 0;
    CellView(int month_day) {
        final int slots_count = 3;
        mday = month_day;
        setLayout(new GridLayout(0,1));

        String month_day_string = Integer.toString(month_day);
        day = new JButton(month_day_string);

        JDebug.addDebugFeatures(day);
        add(day);

        slots = new JButton[slots_count];

        for (int i = 0; i < slots.length; i++)
        {
            slots[i] = new JButton();
            JDebug.addDebugFeatures(slots[i]);
            add(slots[i]);
        }
    }
    void setSlotDisplay(int slot, String label, Color color)
    {
        JButton s = slots[slot];
        s.setLabel(label);
        s.setBackground(color);
    }
    int getDay()
    {
        return Integer.parseInt(day.getLabel());
    }
    void onDayPressed(ActionListener listener) { day.addActionListener(listener); };
    void onSlotPressed(int i, ActionListener listener) { slots[i].addActionListener(listener); };
}

class CalendarMonthView extends JPanel {
    CellView cells[] = new CellView[31];
    int monthLength = 0;
    CalendarMonthView() {
        JDebug.addDebugFeatures(this);
        setLayout(new GridLayout(0,7));
    }
    void setMonth(YearMonth month)
    {
        removeAll();
        LocalDate first = month.atDay(1);
        int shift = (first.getDayOfWeek().getValue() % 7) - 1;

        for (int i = 0; i < shift; i++) { add(new JLabel("")); }

        monthLength = month.lengthOfMonth();
        for (int day = 1; day <= monthLength; day++)
        {
            CellView cell = new CellView(day);
            cells[day-1] = cell;
            add(cell);
        }
        repaint();
    }
}
class CalendarHeaderView extends JPanel {
    public JButton today = new JButton("today");
    public JLabel month = new JLabel();
    public JButton next = new JButton("->");
    public JButton prev = new JButton("<-");
    CalendarHeaderView() {
        JDebug.addDebugFeatures(this);
        setLayout(new BorderLayout());

        add(today, BorderLayout.WEST);
        add(month, BorderLayout.CENTER);

        JPanel arrows = JDebug.createDebugPanel();
        arrows.setLayout(new BorderLayout());
        arrows.add(prev, BorderLayout.WEST);
        arrows.add(next, BorderLayout.EAST);
        add(arrows, BorderLayout.EAST);
    }
    void setMonth(YearMonth month)
    {
        this.month.setText(month.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.UK));
    }
    void onTodayPressed(ActionListener listener) { today.addActionListener(listener); }
    void onPrevPressed(ActionListener listener) { prev.addActionListener(listener); }
    void onNextPressed(ActionListener listener) { next.addActionListener(listener); }
}

class CalendarBodyView extends JPanel {
    public JPanel header;
    public CalendarMonthView body;
    CalendarBodyView() {
        setLayout(new BorderLayout());

        // Header should stay pretty static
        header = new JPanel();
        header.setLayout(new GridLayout(1,7));
        for (var day : DayOfWeek.values())
        {
            header.add(new JLabel(day.toString().substring(0,3)));
        }
        add(header, BorderLayout.NORTH);
        add(body = new CalendarMonthView(), BorderLayout.CENTER);
    }
}
class CalendarController
{
    YearMonth currentMonth = YearMonth.now();
    CalendarView view;
    CalendarMonthView monthView;
    CalendarHeaderView headerView;

    CellController cells[] = new CellController[31];
    MainController MControl;

    CalendarController(MainController MControl, CalendarView view)
    {
        this.view = view;
        this.MControl = MControl;
        monthView = view.body.body;
        headerView = view.header;

        headerView.onTodayPressed(e -> MControl.setMonth(YearMonth.now()));
        headerView.onNextPressed(e -> MControl.setMonth(currentMonth.plusMonths(1)));
        headerView.onPrevPressed(e -> MControl.setMonth(currentMonth.minusMonths(1)));

        setMonth(currentMonth);

        for (int i = 0; i < monthView.monthLength; i++)
        {
            CellView c = monthView.cells[i];
            cells[i] = new CellController(MControl, c);
            // TODO: move into cell controller constructor
            c.onDayPressed(e -> MControl.onCalendarDayPressed(currentMonth, c.getDay()));
            c.onSlotPressed(0, e -> MControl.onCalendarSlotPressed(currentMonth, c.getDay(), 0));
            c.onSlotPressed(1, e -> MControl.onCalendarSlotPressed(currentMonth, c.getDay(), 1));
            c.onSlotPressed(2, e -> MControl.onCalendarSlotPressed(currentMonth, c.getDay(), 2));
        }
    }
    void setMonth(YearMonth month)
    {
        headerView.setMonth(month);
        monthView.setMonth(month);
        currentMonth = month;

        for (int i = 0; i < monthView.monthLength; i++)
        {
            CellView c = monthView.cells[i];
            cells[i] = new CellController(MControl, c);
            // TODO: move into cell controller constructor
            c.onDayPressed(e -> MControl.onCalendarDayPressed(currentMonth, c.getDay()));
            c.onSlotPressed(0, e -> MControl.onCalendarSlotPressed(currentMonth, c.getDay(), 0));
            c.onSlotPressed(1, e -> MControl.onCalendarSlotPressed(currentMonth, c.getDay(), 1));
            c.onSlotPressed(2, e -> MControl.onCalendarSlotPressed(currentMonth, c.getDay(), 2));
        }
    }
    void setCalendarMeals(List<Meal> meals)
    {
        for (Meal meal : meals)
        {
            //TODO: check that date is valid
            int index = LocalDateTime.parse(meal.start).getDayOfMonth()-1;
            cells[index].setSlotDisplay(meal);
        }
    }
}
class CalendarView extends JPanel
{
    public CalendarHeaderView header;
    public CalendarBodyView body;
    CalendarView()
    {
        setLayout(new BorderLayout());
        add(header = new CalendarHeaderView(), BorderLayout.NORTH);

        add(body = new CalendarBodyView(), BorderLayout.CENTER);
    }
}
