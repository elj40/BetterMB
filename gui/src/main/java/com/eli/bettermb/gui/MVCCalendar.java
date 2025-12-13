package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.format.TextStyle;

class CellView extends JPanel{
    int mday;
    JButton day;
    JButton slots[];
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
    void onDayPressed(ActionListener listener) { day.addActionListener(listener); };
    void onSlotPressed(int i, ActionListener listener) { slots[i].addActionListener(listener); };
}

class CalendarMonthView extends JPanel {
    CellView cells[] = new CellView[31];
    int monthLength;
    CalendarMonthView() {
        JDebug.addDebugFeatures(this);
        setLayout(new GridLayout(0,7));

        setMonth(YearMonth.now());
    }
    void setMonth(YearMonth month)
    {
        removeAll();
        LocalDate first = month.atDay(1);
        int shift = first.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < shift; i++) { add(new JLabel("")); }

        monthLength = month.lengthOfMonth();
        for (int day = 1; day <= monthLength; day++)
        {
            CellView cell = new CellView(day);
            cells[day-1] = cell;
            add(cell);
        }
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

        setMonth(YearMonth.now());

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
    public JPanel month;
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
        add(month = new CalendarMonthView(), BorderLayout.CENTER);
    }
}
class CalendarView extends JPanel
{
    public JPanel header;
    public JPanel body;
    CalendarView()
    {
        setLayout(new BorderLayout());
        add(header = new CalendarHeaderView(), BorderLayout.NORTH);

        add(body = new CalendarBodyView(), BorderLayout.CENTER);
    }
}
