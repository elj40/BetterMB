package com.eli.bettermb.gui;
import com.eli.bettermb.client.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.io.IOException;

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
    void setCookies(String newCookies) {};
    String getCookies() { return null; };
    String getSecurityCookiesBySignIn(String entryUrl, String targetUrlPrefix) { return null; };
    QuotaSummary getQuotaSummary() { return null; };
    List<MealSlot> getAvailableMealSlots(String date) { return null; };
    List<MealFacility> getAvailableMealFacilities(String date, char slot) { return null; };
    List<MealOption> getMealOptions(String date, char slot, int facility) throws IOException { return null; };
    List<MealBookingResponse> book(MealBookingOptions meal) throws IOException { return null; };
    MealCancelResponse cancel(int mealId) throws IOException { return null; };
    CompletableFuture<List<Meal>> getMealsBookedInMonthAsync(String date) { return null; };
    List<Meal> getMealsBookedInMonth(String date) throws IOException { return null; };
}
