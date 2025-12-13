package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

import java.time.DayOfWeek;

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
            JDebug.addDebugFeatures(slots[i]);
            add(slots[i]);
        }
    }
}

class CalendarDaysView extends JPanel {
    JPanel cells[] = new JPanel[31];
    CalendarDaysView() {
        JDebug.addDebugFeatures(this);
        setLayout(new GridLayout(0,7));

        //LocalDate first = month.atDay(1);
        //int shift = first.getDayOfWeek().getValue() % 7;
        int shift = DayOfWeek.TUESDAY.getValue() % 7 - 1;

        for (int i = 0; i < shift; i++) {
            add(new JLabel(""));
        }

        //int days = month.lengthOfMonth();
        int days = 31;
        // Days with mock meal events

        JButton cellBreakfast = new JButton("BReakfst right now");
        JDebug.addDebugFeatures(cellBreakfast);
        JButton cellLunch = new JButton("Lunch soon");
        JDebug.addDebugFeatures(cellLunch);
        JButton cellDinner = new JButton("Dinner later");
        JDebug.addDebugFeatures(cellDinner);

        for (int day = 1; day <= days; day++)
        {
            CellView cell = new CellView(day);
            if (day % 2 ==0) cell.slots[0].setLabel("Breakfast");
            if (day % 3 ==0) cell.slots[1].setLabel("Lunch");
            if (day % 5 ==0) cell.slots[2].setLabel("Dinner");
            // Dont need to repaint, will do it all one time at the end
            cells[day-1] = cell;
            add(cells[day-1]);
        }
    }
}
class CalendarHeaderView extends JPanel {
    public JButton today;
    public JLabel month;
    public JPanel arrows;
    CalendarHeaderView() {
        JDebug.addDebugFeatures(this);
        setLayout(new BorderLayout());

        add(today = new JButton("today"), BorderLayout.WEST);
        add(month = new JLabel("TODO: <Current Month>"), BorderLayout.CENTER);

        arrows = JDebug.createDebugPanel();
        arrows.setLayout(new BorderLayout());
        arrows.add(new JButton("<-"), BorderLayout.WEST);
        arrows.add(new JButton("->"), BorderLayout.EAST);
        add(arrows, BorderLayout.EAST);
    }
}
class CalendarBodyView extends JPanel {
    public JPanel header;
    public JPanel days;
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

        add(days = new CalendarDaysView(), BorderLayout.CENTER);
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
