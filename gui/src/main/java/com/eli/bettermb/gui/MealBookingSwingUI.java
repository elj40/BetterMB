package com.eli.bettermb.gui;
// ===================== <CHATGPT> ========================================
// First written by ChatGPT
// Modified by Eli Joubert
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

public class MealBookingSwingUI {
    final int TITLE_FONT_SIZE = 22;
    final int HEADER_FONT_SIZE = 20;
    final int LABEL_FONT_SIZE = 14;

    final Color BREAKFAST_COLOR = Color.decode("#e29e20");
    final Color LUNCH_COLOR = Color.decode("0x28793d");
    final Color DINNER_COLOR = Color.decode("0x286679");
    public MealBookingSwingUI() {
        JFrame frame = new JFrame("Meal Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);

        frame.setLayout(new BorderLayout());

        // =========================
        // HEADER (Top bar)
        // =========================
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(230, 230, 230));

        JLabel title = new JLabel("🍽  Meal Booking System");
        title.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));

        JButton userButton = new JButton("Mr EL Joubert (28178564)");
        header.add(title, BorderLayout.WEST);
        header.add(userButton, BorderLayout.EAST);

        // =========================
        // SIDEBAR (Left menu)
        // =========================
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidebar.setBackground(new Color(245, 245, 245));

        sidebar.add(makeMenuButton("Home"));
        sidebar.add(makeMenuButton("Quota"));
        sidebar.add(makeMenuButton("Facility Meal Menu"));
        sidebar.add(makeMenuButton("About"));

        // =========================
        // CALENDAR (Center panel)
        // =========================
        JPanel calendarPanel = buildCalendar(YearMonth.of(2025, 11));

        // =========================
        // Add all components
        // =========================
        frame.add(header, BorderLayout.NORTH);
        frame.add(sidebar, BorderLayout.WEST);
        //var calendarScrollPane = new JScrollPane(calendarPanel);
        //calendarScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        //frame.add(calendarScrollPane, BorderLayout.CENTER);
        frame.add(calendarPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JButton makeMenuButton(String name) {
        JButton btn = new JButton(name);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        return btn;
    }

    private JPanel buildCalendar(YearMonth month) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel(month.getMonth() + " " + month.getYear(), SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, HEADER_FONT_SIZE));
        header.setBorder(new EmptyBorder(10, 0, 0, 0));
        panel.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(7, 7)); // days + weekday headers
        grid.setBorder(new EmptyBorder(-20, 0, 0, 0));

        // Weekday headers
        for (DayOfWeek dow : DayOfWeek.values()) {
            JLabel lbl = new JLabel(dow.toString().substring(0,3), SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
            grid.add(lbl);
        }

        // Calendar cell generation
        LocalDate first = month.atDay(1);
        int shift = first.getDayOfWeek().getValue() % 7;

        // Empty cells before start of month
        for (int i = 0; i < shift; i++) {
            grid.add(new JLabel(""));
        }

        int days = month.lengthOfMonth();
        // Days with mock meal events
        for (int day = 1; day <= days; day++) {
            JPanel cell = buildCalenderCell(day);
            grid.add(cell);
        }

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }
    private JPanel buildCalenderCell(int day)
    {
            JPanel cell = new JPanel();
            cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
            cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            cell.add(new JLabel(String.valueOf(day)));

            // Example: Add sample meal events
            if (day % 2 == 0) cell.add(buildMealPanel("Breakfast", BREAKFAST_COLOR));
            //else cell.add(buildMealPanel(null, new Color(0, true)));
            if (day % 3 == 0) cell.add(buildMealPanel("Lunch", LUNCH_COLOR));
            //else cell.add(buildMealPanel(null, new Color(0, true)));
            if (day % 5 == 0) cell.add(buildMealPanel("Dinner", DINNER_COLOR));
            //else cell.add(buildMealPanel(null, new Color(0, true)));
            return cell;
    }

    private JPanel buildMealPanel(String text, Color bg_color)
    {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(text);

        label.setMinimumSize(new Dimension(0,0));

        if (text == null)
        {
            label.setText("None");
            label.setForeground(new Color(0,0,0,0));
            panel.setForeground(new Color(0,0,0,0));
            panel.setBackground(new Color(0,0,0,0));
        } else
        {
            panel.setOpaque(true);
            panel.setBackground(bg_color);
        }

        panel.add(label);
        return panel;
    }
}

// ===================== <\CHATGPT> ========================================
