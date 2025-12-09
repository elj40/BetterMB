package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;

import java.time.DayOfWeek;

class PanelCalendar extends JPanel
{
    PanelCalendar()
    {
        setLayout(new BorderLayout());
        JPanel calHeader = JDebug.createDebugPanel();
        {
            calHeader.setLayout(new BorderLayout());
            calHeader.add(new Button("today"), BorderLayout.WEST);
            calHeader.add(new Label("TODO: <Current Month>"), BorderLayout.CENTER);

            JPanel arrows = JDebug.createDebugPanel();
            arrows.setLayout(new BorderLayout());
            arrows.add(new Button("<-"), BorderLayout.WEST);
            arrows.add(new Button("->"), BorderLayout.EAST);
            calHeader.add(arrows, BorderLayout.EAST);
        }
        add(calHeader, BorderLayout.NORTH);

        JPanel calBody = new JPanel();
        {
            calBody.setLayout(new BorderLayout());
            JPanel DOWHeader = new JPanel();
            DOWHeader.setLayout(new GridLayout(1,7));
            for (var day : DayOfWeek.values())
            {
                DOWHeader.add(new JLabel(day.toString().substring(0,3)));
            }
            calBody.add(DOWHeader, BorderLayout.NORTH);

            JPanel calendarDays = JDebug.createDebugPanel();
            {
                calendarDays.setLayout(new GridLayout(0,7));

                //LocalDate first = month.atDay(1);
                //int shift = first.getDayOfWeek().getValue() % 7;
                int shift = DayOfWeek.TUESDAY.getValue() % 7 - 1;

                for (int i = 0; i < shift; i++) {
                    calendarDays.add(new JLabel(""));
                }

                //int days = month.lengthOfMonth();
                int days = 31;
                // Days with mock meal events
                for (int day = 1; day <= days; day++) {
                    JPanel cell = JDebug.createDebugPanel();
                    {
                        //cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                        cell.setLayout(new GridLayout(1+3,1));
                        JLabel cellDay = new JLabel(Integer.toString(day));
                        JDebug.addDebugFeatures(cellDay);
                        cell.add(cellDay);

                        JLabel cellBreakfast = new JLabel("BReakfst right now");
                        JDebug.addDebugFeatures(cellBreakfast);
                        JLabel cellLunch = new JLabel("Lunch soon");
                        JDebug.addDebugFeatures(cellLunch);
                        JLabel cellDinner = new JLabel("Dinner later");
                        JDebug.addDebugFeatures(cellDinner);

                        if (day % 2 ==0) cell.add(cellBreakfast);
                        else cell.add(JDebug.createDebugPanel());
                        if (day % 3 ==0) cell.add(cellLunch);
                        else cell.add(JDebug.createDebugPanel());
                        if (day % 5 ==0) cell.add(cellDinner);
                        else cell.add(JDebug.createDebugPanel());
                    }
                    calendarDays.add(cell);
                }
            }
            calBody.add(calendarDays);
        }
        add(calBody, BorderLayout.CENTER);
    }
}
