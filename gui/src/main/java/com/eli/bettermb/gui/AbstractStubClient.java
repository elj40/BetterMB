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

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import javax.net.ssl.SSLSession;
import java.net.URI;
import java.util.Optional;
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
    final int sleepTime = 50;
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
        sleepTight(sleepTime);
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
        sleepTight(sleepTime*100);

        return meals;
    }
}


// Duplicate from client text fixtures
// I hate this build system and these "best practices"
class StubBookHttpClient implements IHttpClient
{
    HttpResponse<String> response;
    boolean shouldThrowException = false;
    IOException exception;
    int responseTimeMs = 0;
    @Override
    public HttpResponse<String> send(HttpRequest request, BodyHandler bodyHandler) throws IOException
    {
        String uri = request.uri().toString();
        if (uri.contains("get-meal-slots-dto"))
        {
            this.setResponseFromString(200, "[" +
                    "  {\"code\":\"0\",\"description\":\"Select\"}"    +
                    ", {\"code\":\"B\",\"description\":\"Breakfast\"}" +
                    ", {\"code\":\"L\",\"description\":\"Lunch\"}"     +
                    ", {\"code\":\"D\",\"description\":\"Dinner\"}"    +
                    "]");
        }
        else if (uri.contains("facilities/"))
        {
            this.setResponseFromString(200, "[ " +
                    "{\"code\":\"0\",\"description\":\"Select\"}, "           +
                    "{\"code\":\"8\",\"description\":\"Minerva\"}, "          +
                    "{\"code\":\"9\",\"description\":\"Dagbreek\"}, "         +
                    "{\"code\":\"11\",\"description\":\"Huis Ten Bosch\"}, "  +
                    "{\"code\":\"20\",\"description\":\"Lydia\"}, "           +
                    "{\"code\":\"22\",\"description\":\"Majuba\"}, "          +
                    "{\"code\":\"27\",\"description\":\"VictoriaHub\"} "      +
                    "]");
        }
        else if (uri.contains("options/"))
        {
            this.setResponseFromString(200, "[ " +
                    "{\"code\":\"0\",\"description\":\"Select\",\"cost\":null,\"sessionId\":0,\"sessionStart\":\"\",\"sessionEnd\":\"\",\"bookable\":false,\"reason\":null}, " +
                    "{\"code\":\"159523\",\"description\":\"Take Away Prepacked Meal\",\"cost\":\"R44.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, " +
                    "{\"code\":\"160095\",\"description\":\"Vegetarion\",\"cost\":\"R21.79\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, " +
                    "{\"code\":\"160667\",\"description\":\"Chicken Meal\",\"cost\":\"R18.79\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, " +
                    "{\"code\":\"161239\",\"description\":\"Halal Friendly Meal\",\"cost\":\"R18.79\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, " +
                    "{\"code\":\"161811\",\"description\":\"Lactose Free Meal\",\"cost\":\"R18.79\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, " +
                    "{\"code\":\"195628\",\"description\":\"Farmhouse Breakfast\",\"cost\":\"R71.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, " +
                    "{\"code\":\"195968\",\"description\":\"Breakfast Wrap Egg Bacon Tomat\",\"cost\":\"R58.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, " +
                    "{\"code\":\"196308\",\"description\":\"Omelet Option\",\"cost\":\"R60.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, " +
                    "{\"code\":\"196648\",\"description\":\"Cheesegriller Option\",\"cost\":\"R67.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, " +
                    "{\"code\":\"196988\",\"description\":\"Breakfast Waffle Option\",\"cost\":\"R58.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, " +
                    "{\"code\":\"25159\",\"description\":\"Standard Meal\",\"cost\":\"R18.79\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"} " +
                    "] ");
        }
        else if (uri.contains("cancel-meal-booking/"))
        {
            this.setResponseFromString(200, "{\"success\":true,\"message\":null}");
        }
        else if (uri.contains("get-meal-bookings-dto/"))
        {
            sleepTight(1000);
            String responseString = "";
            responseString += "[ ";
            responseString += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2026-01-31T11:00\",\"description\":\"Sunday Roast Beef\",\"facility\":\"Majuba\",\"mealTime\":\"11:00 - 13:00\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8172130}, ";
            responseString += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2026-01-01T07:15\",\"description\":\"Fried Egg Cheese Tomato\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172107}, ";
            responseString += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2026-01-01T12:30\",\"description\":\"Chicken Pie\",\"facility\":\"Majuba\",\"mealTime\":\"12:30 - 13:30\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8186151}, ";
            responseString += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2026-01-02T07:15\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172108}, ";
            responseString += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2026-01-02T12:30\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"12:30 - 13:30\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8186207}, ";
            responseString += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2026-01-03T07:15\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172109}";
            responseString += " ]";
            this.setResponseFromString(200, responseString);
        }
        else if (uri.contains("booking/"))
        {
            this.setResponseFromString(200, "[ " +
                    "{\"bookingDate\":\"2025-01-01\" ,\"bookingMessage\":\"Reservation already exists\"}" +
                    " ]");
        }

        return response;
    };
    public void setResponseFromString(int statusCode, String msg)
    {
        response = new HttpResponse<String>() {
            @Override
            public int statusCode() { return statusCode; }
            @Override
            public String body() { return msg; };
            @Override
            public HttpRequest request() { return null; };
            @Override
            public HttpHeaders headers() { return null; };
            @Override
            public URI uri() { return null; };
            @Override
            public Optional<HttpResponse<String>> previousResponse() { return null; };
            @Override
            public HttpClient.Version version() { return null; };
            @Override
            public Optional<SSLSession> sslSession() { return null; };
        };
    };
    void sleepTight(int ms)
    {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { e.printStackTrace(); };
    }
};
