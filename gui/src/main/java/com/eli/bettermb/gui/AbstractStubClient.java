package com.eli.bettermb.gui;
import com.eli.bettermb.client.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.io.IOException;

import java.util.Random;
import java.util.Iterator;

// TODO: this should definitely not be here from a good practice point of view.
// Its just hard to do this cleanly without changing client code that could
// mess with cli code

abstract class AbstractClient
{
    abstract void setCookies(String newCookies);
    abstract String getCookies();
    abstract String getSecurityCookiesBySignIn(String entryUrl, String targetUrlPrefix);
    abstract QuotaSummary getQuotaSummary();
    abstract List<MealSlot> getAvailableMealSlots(String date);
    abstract List<MealFacility> getAvailableMealFacilities(String date, char slot);
    abstract List<MealOption> getMealOptions(String date, char slot, int facility) throws IOException;
    abstract List<MealBookingResponse> book(MealBookingOptions meal) throws IOException;
    abstract MealCancelResponse cancel(int mealId) throws IOException;
    abstract CompletableFuture<List<Meal>> getMealsBookedInMonthAsync(String date);
    abstract List<Meal> getMealsBookedInMonth(String date) throws IOException;
}

class StubClient extends AbstractClient
{
    final int sleepTime = 500;
    void sleepTight(int ms)
    {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { e.printStackTrace(); };
    }
    void setCookies(String newCookies) {};
    String getCookies() { return null; };
    String getSecurityCookiesBySignIn(String entryUrl, String targetUrlPrefix) { return null; };

    QuotaSummary getQuotaSummary() { return null; };

    List<MealSlot> getAvailableMealSlots(String date)
    {
        List<MealSlot> slots = new ArrayList<>();
        var b = new MealSlot(); b.code = 'B'; b.description = "Breakfast";
        var l = new MealSlot(); l.code = 'L'; l.description = "Lunch";
        var d = new MealSlot(); d.code = 'D'; d.description = "Dinner";
        slots.add(b);
        slots.add(l);
        slots.add(d);

        sleepTight(sleepTime);
        return slots;
    };

    List<MealFacility> getAvailableMealFacilities(String date, char slot)
    {
        List<MealFacility> list = new ArrayList<>();
        var f1 = new MealFacility(); f1.code = 0; f1.description = "Majuba";
        var f2 = new MealFacility(); f2.code = 1; f2.description = "Minerva";
        var f3 = new MealFacility(); f3.code = 2; f3.description = "Huis ten Bosch";
        var f4 = new MealFacility(); f4.code = 3; f4.description = "Lydia";
        var f5 = new MealFacility(); f5.code = 4; f5.description = "Sagbreek";
        list.add(f1);
        list.add(f2);
        list.add(f3);
        list.add(f4);
        list.add(f5);

        sleepTight(sleepTime);
        return list;
    };

    List<MealOption> getMealOptions(String date, char slot, int facility) throws IOException
    {
        List<MealOption> list = new ArrayList<>();
        var o1 = new MealOption(); o1.code = 0; o1.description = "Standard Meal";
        var o2 = new MealOption(); o2.code = 1; o2.description = "Extra Protein";
        var o3 = new MealOption(); o3.code = 2; o3.description = "Chicken option";
        var o4 = new MealOption(); o4.code = 3; o4.description = "Ribs and wings basket";
        var o5 = new MealOption(); o5.code = 4; o5.description = "MajuGeel";
        list.add(o1);
        list.add(o2);
        list.add(o3);
        list.add(o4);
        list.add(o5);

        sleepTight(sleepTime);
        return list;
    };

    List<MealBookingResponse> book(MealBookingOptions meal) throws IOException 
    {
        List<MealBookingResponse> list = new ArrayList<>();
        for (int i = 0; i < meal.advanceBookingDays+1; i++)
        {
            var r = new MealBookingResponse();
            r.bookingDate = LocalDate.parse(meal.mealDate).plusDays(i).toString();
            r.bookingMessage = (i % 2 == 0) ? "success" : "failed for some reason";
            list.add(r);
        }
        sleepTight(sleepTime);
        return list;
    };

    MealCancelResponse cancel(int mealId) throws IOException
    {
        var mcr = new MealCancelResponse();
        // TODO: somehow need to test if it fails too
        mcr.success = true;
        mcr.message = "success";
        return mcr;
    };

    CompletableFuture<List<Meal>> getMealsBookedInMonthAsync(String date)
    {
        return CompletableFuture.supplyAsync(() -> {
            try { return getMealsBookedInMonth(date); }
            catch (Exception ex) { return null; }
        });
    };

    List<Meal> getMealsBookedInMonth(String date) throws IOException
    {
        Iterator<Integer> random = (new Random()).ints().iterator();
        List<Meal> meals = new ArrayList<>();

        char[] SlotCodes = new char[] {'B','L','D'};

        LocalDate ld = LocalDate.parse(date);
        int dayNow = ld.getDayOfMonth();
        int dayEnd = ld.getMonth().length(false);
        int days = dayEnd-dayNow;
        // Temporary stuff to set up for testing
        for (int i = 0; i < days; i++)
        {
            Meal meal = new Meal();
            meal.canModify = ((random.next() % 2) == 0);

            var time = LocalDateTime.of(ld.plusDays(i), LocalTime.now());

            meal.title = "Meal";
            meal.start = time.toString();
            meal.description = "Food";
            meal.facility = "Majubs";
            meal.mealTime = time.toLocalTime().toString();
            meal.mealCost = "R99.99";
            meal.mealSlot = SlotCodes[java.lang.Math.abs(random.next()) % 3];
            meal.backgroundColor = "#123456";
            meal.borderColor = "#123456";
            meal.id = java.lang.Math.abs(random.next() % 10000000);
            meals.add(meal);
        }

        return meals;
    }
}
