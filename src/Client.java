import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Type;

import java.io.IOException;
import java.net.ConnectException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

class Client
{
    HttpClient httpClient = HttpClient.newBuilder().build();
    HttpRequest.Builder requestBuilder;
    Gson gson = new Gson();
    String urlBase = "http://127.0.0.1:8080";

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    Client(String _url_base, String cookies)
    {
        urlBase = _url_base;

        requestBuilder = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(5))
                .setHeader("Cookie", cookies);
    };
    public List<MealSlot> getAvailableMealSlots(String date)
    {
        StringBuilder suffixSB = new StringBuilder();
        // TODO: assert that date is in correct format
        suffixSB.append("/student-meal-booking/spring/api/get-meal-slots-dto/");
        suffixSB.append(date);
        suffixSB.append("/en");
        String urlSuffix = suffixSB.toString();

        try {
            HttpRequest request = requestBuilder
                .uri(URI.create(urlBase + urlSuffix))
                .build();

            System.out.println("[GetAvailableMealSlots] Sending " + urlSuffix);
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[GetAvailableMealSlots] Status code: " + response.statusCode());
            System.out.println("[GetAvailableMealSlots] Response   : " + response.body());

            Type mealSlotListType = new TypeToken<List<MealSlot>>() {}.getType();
            List<MealSlot> mealSlots = gson.fromJson(response.body(), mealSlotListType);

            if (mealSlots.getFirst().code == '0') mealSlots.removeFirst();

            return mealSlots;
        }
        catch (Exception e) { e.printStackTrace(); }

        return new ArrayList<MealSlot>();
    }
    public List<MealFacility> getAvailableMealFacilities(String date, char slot)
    {
        StringBuilder suffixSB = new StringBuilder();
        suffixSB.append("/student-meal-booking/spring/api/get-meal-slot-facilities/en/");
        suffixSB.append(date);
        suffixSB.append("/");
        suffixSB.append(slot);
        String urlSuffix = suffixSB.toString();

        try {
            HttpRequest request = requestBuilder
                .uri(URI.create(urlBase + urlSuffix))
                .build();

            System.out.println("[GetAvailableMealFacilities] Sending " + urlSuffix);
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[GetAvailableMealFacilities] Status code: " + response.statusCode());
            System.out.println("[GetAvailableMealFacilities] Response   : " + response.body());

            Type mealFacListType = new TypeToken<List<MealFacility>>() {}.getType();
            List<MealFacility> mealFacs = gson.fromJson(response.body(), mealFacListType);

            if (mealFacs.getFirst().code == '0') mealFacs.removeFirst();

            return mealFacs;
        }
        catch (Exception e) { e.printStackTrace(); }

        return new ArrayList<MealFacility>();
    }
    public List<MealInfo> getAvailableMeals(String date, char slot, int facility)
    {
        StringBuilder suffixSB = new StringBuilder();
        suffixSB.append("/student-meal-booking/spring/api/get-meal-slot-facility-options/en/");
        suffixSB.append(date);
        suffixSB.append("/"); suffixSB.append(slot);
        suffixSB.append("/");
        suffixSB.append(facility);
        String urlSuffix = suffixSB.toString();

        try {
            HttpRequest request = requestBuilder
                .uri(URI.create(urlBase + urlSuffix))
                .build();

            System.out.println("[GetAvailableMeals] Sending " + urlSuffix);
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[GetAvailableMeals] Status code: " + response.statusCode());
            System.out.println("[GetAvailableMeals] Response   : " + response.body());

            Type mealInfoListType = new TypeToken<List<MealInfo>>() {}.getType();
            List<MealInfo> mealInfos = gson.fromJson(response.body(), mealInfoListType);

            if (mealInfos.getFirst().code == '0') mealInfos.removeFirst();

            return mealInfos;
        }
        catch (Exception e) { e.printStackTrace(); }

        return new ArrayList<MealInfo>();
    }
    public List<MealBookingResponse> book(MealBookingOptions meal)
    {
        String mealJson = gson.toJson(meal);
        String urlSuffix = "/student-meal-booking/spring/api/store-meal-booking/en";
        try
        {
            HttpRequest request = requestBuilder
                .uri(URI.create(urlBase + urlSuffix))
                .POST(BodyPublishers.ofString(mealJson))
                .setHeader("Content-Type", "application/json;charset=utf-8")
                .build();

            System.out.println("[Book] Sending: " + mealJson);
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[Book] Status code: " + response.statusCode());
            System.out.println("[Book] Response   : " + response.body());

            Type mealBookingResponseListType = new TypeToken<List<MealBookingResponse>>() {}.getType();
            List<MealBookingResponse> mealBookingResponses = gson.fromJson(response.body(), mealBookingResponseListType);

            return mealBookingResponses;
        }
        catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<MealBookingResponse>();
    };
    public MealCancelResponse cancel(int mealId)
    {
        StringBuilder suffixSB = new StringBuilder();
        suffixSB.append("/student-meal-booking/spring/api/cancel-meal-booking/en/");
        suffixSB.append(mealId);
        String urlSuffix = suffixSB.toString();

        MealCancelResponse mealCancelResponse = new MealCancelResponse();
        try {
            HttpRequest request = requestBuilder
                .uri(URI.create(urlBase + urlSuffix))
                .build();

            System.out.println("[Cancel] Sending " + urlSuffix);
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[Cancel] Status code: " + response.statusCode());
            System.out.println("[Cancel] Response   : " + response.body());

            mealCancelResponse = gson.fromJson(response.body(), MealCancelResponse.class);

            return mealCancelResponse;
        }
        catch (Exception e) { e.printStackTrace(); }

        return mealCancelResponse;
    }
    public List<Meal> getMealsBookedInMonth(String date)
    {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate lastLocalDateOfMonth = localDate.withDayOfMonth(localDate.lengthOfMonth());
        String    lastDateOfMonth = lastLocalDateOfMonth.format(dateFormatter);
        StringBuilder suffixSB = new StringBuilder();
        suffixSB.append("/student-meal-booking/spring/api/get-meal-bookings-dto/en/");
        suffixSB.append(lastDateOfMonth);
        String urlSuffix = suffixSB.toString();

        try {
            HttpRequest request = requestBuilder
                .uri(URI.create(urlBase + urlSuffix))
                .build();

            System.out.println("[GetMealsBookedInMonth] Sending " + urlSuffix);
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[GetMealsBookedInMonth] Status code: " + response.statusCode());
            System.out.println("[GetMealsBookedInMonth] Response   : " + response.body());

            Type mealListType = new TypeToken<List<Meal>>() {}.getType();
            List<Meal> meals = gson.fromJson(response.body(), mealListType);

            return meals;
        }
        catch (Exception e) { e.printStackTrace(); }

        return new ArrayList<Meal>();
    };
    // Flushes the test server
    // DO NOT USE OUTSIDE OF TESTING!
    public void flushServer()
    {
        String urlSuffix = "/flush";
        try
        {
            HttpRequest request = requestBuilder
                .uri(URI.create(urlBase + urlSuffix))
                .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[CLIENT] Flushing server: " + response.statusCode());
        }
        catch (ConnectException e) { e.printStackTrace(); }
        //catch (TimeoutException e) { System.out.println("[CLIENT] Timed out"); }
        catch (IOException e) { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    };
};
