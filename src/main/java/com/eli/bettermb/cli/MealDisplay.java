package com.eli.bettermb.cli;

import com.eli.bettermb.client.Meal;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

class MealDisplay
{
    public int id;
    public String date;
    public String day;
    public String title;
    public String facility;
    public String description;
    static MealDisplay fromMeal(Meal meal)
    {
        MealDisplay M = new MealDisplay();
        M.id = meal.id;

        M.date = "?".repeat(10);
        M.day  = "???";
        if (meal.start != null) {
            M.date = meal.start.substring(0, "yyyy-mm-dd".length());
            M.day = LocalDate.parse(M.date)
                .getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        }
        M.title = meal.title;
        M.facility = meal.facility;
        M.description = meal.description;
        return M;
    };
    @Override
    public String toString()
    {
        return String.format("%-7d [%s %s] %-10s %-20s %s",
                id,
                date,
                day,
                title,
                facility,
                description);
    }
    public static String headers()
    {
        return String.format("##. %-7s [%s] %-10s %-20s %s",
                "ID",
                "yyyy-mm-dd Day",
                "Title",
                "Facility",
                "Description");
    }
};
