package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
public class GUI
{
    final int FACTOR = 40;
    final int WIDTH_ASPECT = 16;
    final int HEIGHT_ASPECT = 9;
    public static void main(String[] args)
    {
        System.out.println("Hello GUI!");
        GUI gui = new GUI();
        gui.main();
    };
    void main()
    {

        JFrame frame = new JFrame("bettermb-gui");
        frame.setSize(WIDTH_ASPECT*FACTOR, HEIGHT_ASPECT*FACTOR);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Header bar
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        {
            JLabel title = new JLabel();
            // title.setIcon(); TODO
            title.setText("Better Meal Booking");
            headerPanel.add(title, BorderLayout.WEST);

            JLabel profile = new JLabel();
            profile.setText("12345678");
            headerPanel.add(profile, BorderLayout.EAST);
            // Student no. text input
            // Reload button
        }
        mainPanel.add(headerPanel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        {
            JPanel pagePanel = new JPanel();
            pagePanel.setLayout(new BoxLayout(pagePanel, BoxLayout.Y_AXIS));
            {
                pagePanel.add(new JLabel("Home"));
                pagePanel.add(new JLabel("Quota"));
                pagePanel.add(new JLabel("Menu"));
                pagePanel.add(new JLabel("About"));
            }
            contentPanel.add(pagePanel);
        }
        mainPanel.add(contentPanel);
        // Page list
        //
        // Calendar
        //      Header
        //          today
        //          month
        //          next
        //          prev
        //      Body
        //          Sun->Fri
        //          weeks from 1->30/31

        frame.add(mainPanel);
        frame.setVisible(true);
    };
};
