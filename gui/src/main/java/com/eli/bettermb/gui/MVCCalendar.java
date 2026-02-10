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
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;

import com.eli.bettermb.client.Meal;

record SlotMealView(String label, Color color) {};
record CalendarMealView(LocalDate date, int slot, SlotMealView slotMealView) {};

class CellController
{
    CellView view;
    CellController(CalendarController calendarControl, CellView view)
    {
        this.view = view;

        view.onDayPressed(e -> calendarControl.onCellDayPressed(view.getDay()));
        for (int i = 0; i < view.slots.length; i++)
        {
            final int I = i;
            view.onSlotPressed(i, e -> calendarControl.onCellSlotPressed(I, view.getDay()));
        }
    }
    void setSlotDisplay(int slot, SlotMealView mv)
    {
        view.setSlotDisplay(slot, mv);
    }
}
class CellView extends JPanel{
    JButton day;
    JButton slots[];
    CellView(int month_day) {
        final int slots_count = 3;
        setLayout(new GridLayout(0,1));

        String month_day_string = Integer.toString(month_day);
        day = new JButton(month_day_string);

        JDebug.addDebugFeatures(day);
        add(day);

        slots = new JButton[slots_count];

        for (int i = 0; i < slots.length; i++)
        {
            slots[i] = new JButton();
            slots[i].setAlignmentX(LEFT_ALIGNMENT);
            add(slots[i]);
        }
    }
    void setSlotDisplay(int slot, SlotMealView mv)
    {
        JButton s = slots[slot];
        s.setLabel(mv.label());
        s.setBackground(mv.color());
        s.setAlignmentX(LEFT_ALIGNMENT);
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

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("LLLL yyyy");
    CalendarHeaderView() {
        JDebug.addDebugFeatures(this);
        setLayout(new BorderLayout());

        add(today, BorderLayout.WEST);

        month.setHorizontalAlignment(SwingConstants.CENTER);
        add(month, BorderLayout.CENTER);

        JPanel arrows = JDebug.createDebugPanel();
        arrows.setLayout(new BorderLayout());
        arrows.add(prev, BorderLayout.WEST);
        arrows.add(next, BorderLayout.EAST);
        add(arrows, BorderLayout.EAST);
    }
    void setMonth(YearMonth month)
    {
        this.month.setText(month.format(dateFormatter));
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
        attachCellControllersToViews();
    }
    void attachCellControllersToViews()
    {
        for (int i = 0; i < monthView.monthLength; i++)
        {
            CellView c = monthView.cells[i];
            cells[i] = new CellController(this, c);
        }
    }
    private LocalDate getDateFromCurrentMonth(int day)
    {
        return LocalDate.of(
                currentMonth.getYear(),
                currentMonth.getMonthValue(),
                day);
    }
    void onCellDayPressed(int day)
    {
        LocalDate date = getDateFromCurrentMonth(day);
        MControl.onCalendarDayPressed(date);
    };
    void onCellSlotPressed(int slot, int day)
    {
        LocalDate date = getDateFromCurrentMonth(day);
        MControl.onCalendarSlotPressed(date, slot);
    }
    void setMonth(YearMonth month)
    {
        headerView.setMonth(month);
        monthView.setMonth(month);
        currentMonth = month;
        attachCellControllersToViews();
    }
    void clearCalendarMeals()
    {
        for (int i = 0; i < monthView.monthLength; i++)
        {
            cells[i].setSlotDisplay(0, new SlotMealView("", Color.WHITE));
            cells[i].setSlotDisplay(1, new SlotMealView("", Color.WHITE));
            cells[i].setSlotDisplay(2, new SlotMealView("", Color.WHITE));
        }
    }
    void setCalendarMeals(List<CalendarMealView> meals)
    {
        clearCalendarMeals();
        for (CalendarMealView meal : meals)
        {
            LocalDate mealDate = meal.date();
            YearMonth mealMonth = YearMonth.of(mealDate.getYear(), mealDate.getMonth());
            if (!mealMonth.equals(currentMonth)) continue;
            int index = mealDate.getDayOfMonth()-1;
            cells[index]
                .setSlotDisplay(meal.slot(), meal.slotMealView());
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
