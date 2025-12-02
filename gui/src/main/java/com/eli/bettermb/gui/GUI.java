package com.eli.bettermb.gui;

import com.eli.bettermb.client.Client;
import com.eli.bettermb.client.HttpClientImpl;

import javax.swing.*;
import java.awt.*;

import java.time.DayOfWeek;

public class GUI
{
    Client client;
    GuiUser user;

    static String sun_url = "https://web-apps.sun.ac.za";
    public static void main(String[] args)
    {
        boolean shouldDebug = false;
        Client.debugging = shouldDebug;
        GuiUser.debugging = shouldDebug;


        Client client = new Client(new HttpClientImpl());
        client.setUrlBase(sun_url);

        System.out.println("Hello GUI!");
        GUI gui = new GUI(client);
        gui.main();
    };
    GUI(Client _client)
    {
        client = _client;
        user = new GuiUser();
    }
    public void main() {
        final int frameSizeFactor = 50;
        JFrame frame = new JFrame("BetterMB-GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(16*frameSizeFactor, 9*frameSizeFactor);
        frame.setLayout(new BorderLayout());

        JPanel header = createDebugPanel();
        {
            header.setLayout(new BorderLayout());

            JLabel title = new JLabel("BetterMB-GUI");
            addDebugFeatures(title);
            title.setBackground(Color.BLUE);

            JLabel username = new JLabel("28178564");
            username.setBackground(Color.RED);
            addDebugFeatures(username);

            header.add(title, BorderLayout.CENTER);
            header.add(username, BorderLayout.EAST);
        }

        JPanel sidebar = createDebugPanel();
        {
            sidebar.setLayout(new BorderLayout());
            JPanel pages = createDebugPanel();
            {
                pages.setLayout(new BoxLayout(pages, BoxLayout.Y_AXIS));
                pages.add(new Button("HOME"));
                pages.add(new Button("HOME"));
            }
            sidebar.add(pages, BorderLayout.NORTH);

            JPanel menuArea = createDebugPanel();
            {
                menuArea.setLayout(new BorderLayout());
                menuArea.add(new JLabel("menu here please"));
            }
            sidebar.add(menuArea);
        }

        JPanel calendar = createDebugPanel();
        {
            calendar.setLayout(new BorderLayout());
            JPanel calHeader = createDebugPanel();
            {
                calHeader.setLayout(new BorderLayout());
                calHeader.add(new Button("today"), BorderLayout.WEST);
                calHeader.add(new Label("TODO: <Current Month>"), BorderLayout.CENTER);

                JPanel arrows = createDebugPanel();
                arrows.setLayout(new BorderLayout());
                arrows.add(new Button("<"), BorderLayout.WEST);
                arrows.add(new Button(">"), BorderLayout.EAST);
                calHeader.add(arrows, BorderLayout.EAST);
            }
            calendar.add(calHeader, BorderLayout.NORTH);

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

                JPanel calendarDays = createDebugPanel();
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
                        JPanel cell = createDebugPanel();
                        {
                            //cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                            cell.setLayout(new GridLayout(1+3,1));
                            JLabel cellDay = new JLabel(Integer.toString(day));
                            addDebugFeatures(cellDay);
                            cell.add(cellDay);

                            JLabel cellBreakfast = new JLabel("BReakfst right now");
                            addDebugFeatures(cellBreakfast);
                            JLabel cellLunch = new JLabel("Lunch soon");
                            addDebugFeatures(cellLunch);
                            JLabel cellDinner = new JLabel("Dinner later");
                            addDebugFeatures(cellDinner);

                            if (day % 2 ==0) cell.add(cellBreakfast);
                            if (day % 3 ==0) cell.add(cellLunch);
                            if (day % 5 ==0) cell.add(cellDinner);
                        }
                        calendarDays.add(cell);
                    }
                }
                calBody.add(calendarDays);
            }
            calendar.add(calBody, BorderLayout.CENTER);
        }

        frame.add(header, BorderLayout.NORTH);
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(calendar, BorderLayout.CENTER);

        frame.setVisible(true);
    }
    void addDebugFeatures(JComponent c)
    {
        c.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        c.setOpaque(true);
    }
    JPanel createDebugPanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }
};
