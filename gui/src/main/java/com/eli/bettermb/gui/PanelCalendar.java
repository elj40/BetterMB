package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

import java.time.DayOfWeek;

class JPanelListener
    extends JPanel
    implements ActionListener
{
    List listeners = new ArrayList<ActionListener>();
    void actionPerformed(ActionEvent e)
    {
        for (var l : listeners) l.actionPerformed(e);
    }
    void addActionListener(ActionListener a)
    {
        listeners.add(a);
    }
}

class PanelCalendar extends JPanel
{
    public JPanel header;
    public JPanel body;
    PanelCalendar()
    {
        setLayout(new BorderLayout());
        class CalendarHeader extends JPanel {
            public JButton today;
            public JLabel month;
            public JPanel arrows;
            CalendarHeader() {
                JDebug.addDebugFeatures(this);
                setLayout(new BorderLayout());

                add(today = new JButton("today"), BorderLayout.WEST);
                add(month = new JLabel("TODO: <Current Month>"), BorderLayout.CENTER);

                arrows = JDebug.createDebugPanel();
                arrows.setLayout(new BorderLayout());
                arrows.add(new Button("<-"), BorderLayout.WEST);
                arrows.add(new Button("->"), BorderLayout.EAST);
                add(arrows, BorderLayout.EAST);
            }
        }
        add(header = new CalendarHeader(), BorderLayout.NORTH);

        class CalendarBody extends JPanel {
            public JPanel header;
            public JPanel days;
            CalendarBody() {
                setLayout(new BorderLayout());

                // Should stay pretty immutable so ignore for now
                header = new JPanel();
                header.setLayout(new GridLayout(1,7));
                for (var day : DayOfWeek.values())
                {
                    header.add(new JLabel(day.toString().substring(0,3)));
                }
                add(header, BorderLayout.NORTH);

                class CalendarDays extends JPanel {
                    JPanel cells[] = new JPanel[31];
                    CalendarDays() {
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
                        class Cell extends JPanelListener {
                            JButton day;
                            JButton slots[];
                            Cell(int month_day) {
                                final int slots_count = 3;
                                setLayout(new GridLayout(1+slots_count,1));

                                String month_day_string = Integer.toString(month_day);
                                day = new JButton(month_day_string);
                                day.setActionCommand("CALENDAR_CELL_"+month_day_string+"_DAY");
                                day.addActionListener(this);

                                JDebug.addDebugFeatures(day);
                                add(day);

                                slots = new JButton[slots_count];

                                for (int i = 0; i < slots.length; i++)
                                {
                                    slots[i] = new JButton();
                                    JDebug.addDebugFeatures(slots[i]);
                                    slots[i].addActionListener(this);
                                    slots[i].setActionCommand(
                                            "CALENDAR_CELL_"+
                                            month_day_string+
                                            "_SLOT_"+
                                            Integer.toString(i));
                                    add(slots[i]);
                                }
                            }
                        }

                        JButton cellBreakfast = new JButton("BReakfst right now");
                        JDebug.addDebugFeatures(cellBreakfast);
                        JButton cellLunch = new JButton("Lunch soon");
                        JDebug.addDebugFeatures(cellLunch);
                        JButton cellDinner = new JButton("Dinner later");
                        JDebug.addDebugFeatures(cellDinner);

                        for (int day = 1; day <= days; day++)
                        {
                            Cell cell = new Cell(day);
                            if (day % 2 ==0) cell.slots[0].setLabel("Breakfast");
                            if (day % 3 ==0) cell.slots[1].setLabel("Lunch");
                            if (day % 5 ==0) cell.slots[2].setLabel("Dinner");
                            // Dont need to repaint, will do it all one time at the end
                            cells[day-1] = cell;
                            add(cells[day-1]);
                        }
                    }
                }
                add(days = new CalendarDays(), BorderLayout.CENTER);
            }
        }
        add(body = new CalendarBody(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
