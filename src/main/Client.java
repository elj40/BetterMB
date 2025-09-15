import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
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

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

class Client
{
    static boolean debugging = false;

    HttpClient httpClient = HttpClient.newBuilder().build();
    IHttpClient ihttpClient;
    HttpRequest.Builder requestBuilder;
    Gson gson = new Gson();
    String urlBase = "http://127.0.0.1:8080";

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    Client()
    {
        requestBuilder = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(15));
    };
    Client(String _url_base, String cookies)
    {
        urlBase = _url_base;

        requestBuilder = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(15))
                .setHeader("Cookie", cookies);
    };
    public void setCookies(String newCookies)
    {
        requestBuilder = requestBuilder.setHeader("Cookie", newCookies);
    };
    public void setHttpClient(IHttpClient newHttpClient)
    {
        ihttpClient = newHttpClient;
    };
    static void debug(String msg)
    {
        if (Client.debugging) System.out.println(msg);
    };
    boolean ensureGoodResponse(HttpResponse<String> response) throws IOException
    {
        if (response.body().contains("Single Sign-on")) throw new SecurityFailedException();
        if (response.body().contains("html")) throw new SecurityFailedException();
        if (response.statusCode() != 200) throw new IOException();

        return true;
    };
    static public String getSecurityCookiesBySignIn(String entryUrl, String targetUrlStart)
            throws IOException, InterruptedException
    {
        WebDriver driver = new ChromeDriver();
        String securityCookies = "";

        driver.get(entryUrl);
        Thread.sleep(500);
        String currentUrl = driver.getCurrentUrl();
        while (true)
        {
            Thread.sleep(5000);
            if (driver.getCurrentUrl().startsWith(targetUrlStart))
            {
                Client.debug("[getSecurityCookiesBySignIn] On target page");
                break;
            };
        };

        Set<Cookie> cookies = driver.manage().getCookies();
        boolean first = true;
        for (Cookie cookie : cookies) {
            if (!first) securityCookies = securityCookies + ";";
            else first = false;
            securityCookies = securityCookies + cookie.getName();
            securityCookies = securityCookies + "=";
            securityCookies = securityCookies + cookie.getValue();
        }
        Client.debug("[getSecurityCookiesBySignIn] Security cookies: " + securityCookies);
        return securityCookies;
    };
    public List<MealSlot> getAvailableMealSlots(String date)
            throws IOException
    {
        StringBuilder suffixSB = new StringBuilder();
        // TODO: assert that date is in correct format
        suffixSB.append("/student-meal-booking/spring/api/get-meal-slots-dto/");
        suffixSB.append(date);
        suffixSB.append("/en");
        String urlSuffix = suffixSB.toString();

        HttpRequest request = requestBuilder
            .uri(URI.create(urlBase + urlSuffix))
            .build();

        Client.debug("[GetAvailableMealSlots] Sending " + urlSuffix);
        HttpResponse<String> response = ihttpClient.send(request, BodyHandlers.ofString());
        Client.debug("[GetAvailableMealSlots] Status code: " + response.statusCode());
        Client.debug("[GetAvailableMealSlots] Response   : " + response.body());

        ensureGoodResponse(response);

        Type mealSlotListType = new TypeToken<List<MealSlot>>() {}.getType();
        List<MealSlot> mealSlots = gson.fromJson(response.body(), mealSlotListType);

        if (mealSlots.getFirst().code == '0') mealSlots.removeFirst();

        return mealSlots;
    }
    public List<MealFacility> getAvailableMealFacilities(String date, char slot)
            throws IOException, InterruptedException
    {
        StringBuilder suffixSB = new StringBuilder();
        suffixSB.append("/student-meal-booking/spring/api/get-meal-slot-facilities/en/");
        suffixSB.append(date);
        suffixSB.append("/");
        suffixSB.append(slot);
        String urlSuffix = suffixSB.toString();

        HttpRequest request = requestBuilder
            .uri(URI.create(urlBase + urlSuffix))
            .build();

        Client.debug("[GetAvailableMealFacilities] Sending " + urlSuffix);
        HttpResponse<String> response = ihttpClient.send(request, BodyHandlers.ofString());
        Client.debug("[GetAvailableMealFacilities] Status code: " + response.statusCode());
        Client.debug("[GetAvailableMealFacilities] Response   : " + response.body());

        ensureGoodResponse(response);

        Type mealFacListType = new TypeToken<List<MealFacility>>() {}.getType();
        List<MealFacility> mealFacs = gson.fromJson(response.body(), mealFacListType);

        if (mealFacs.getFirst().code == 0) mealFacs.removeFirst();

        return mealFacs;
    }
    public List<MealOption> getMealOptions(String date, char slot, int facility) throws IOException
    {
        StringBuilder suffixSB = new StringBuilder();
        suffixSB.append("/student-meal-booking/spring/api/get-meal-slot-facility-options/en/");
        suffixSB.append(date);
        suffixSB.append("/"); suffixSB.append(slot);
        suffixSB.append("/");
        suffixSB.append(facility);
        String urlSuffix = suffixSB.toString();

        HttpRequest request = requestBuilder
            .uri(URI.create(urlBase + urlSuffix))
            .build();

        Client.debug("[GetAvailableMeals] Sending " + urlSuffix);
        HttpResponse<String> response = ihttpClient.send(request, BodyHandlers.ofString());
        Client.debug("[GetAvailableMeals] Status code: " + response.statusCode());
        Client.debug("[GetAvailableMeals] Response   : " + response.body());

        ensureGoodResponse(response);

        Type mealOptionListType = new TypeToken<List<MealOption>>() {}.getType();
        List<MealOption> mealOptions = gson.fromJson(response.body(), mealOptionListType);

        if (mealOptions.getFirst().code == 0) mealOptions.removeFirst();

        return mealOptions;
    }
    public List<MealBookingResponse> book(MealBookingOptions meal) throws IOException
    {
        String mealJson = gson.toJson(meal);
        String urlSuffix = "/student-meal-booking/spring/api/store-meal-booking/en";

        HttpRequest request = requestBuilder
            .uri(URI.create(urlBase + urlSuffix))
            .POST(BodyPublishers.ofString(mealJson))
            .setHeader("Content-Type", "application/json;charset=utf-8")
            .build();

        Client.debug("[Book] Sending: " + mealJson);
        HttpResponse<String> response = ihttpClient.send(request, BodyHandlers.ofString());
        Client.debug("[Book] Status code: " + response.statusCode());
        Client.debug("[Book] Response   : " + response.body());

        ensureGoodResponse(response);

        Type mealBookingResponseListType = new TypeToken<List<MealBookingResponse>>() {}.getType();
        List<MealBookingResponse> mealBookingResponses = gson.fromJson(response.body(), mealBookingResponseListType);

        return mealBookingResponses;
    };
    public MealCancelResponse cancel(int mealId) throws IOException
    {
        StringBuilder suffixSB = new StringBuilder();
        suffixSB.append("/student-meal-booking/spring/api/cancel-meal-booking/en/");
        suffixSB.append(mealId);
        String urlSuffix = suffixSB.toString();

        MealCancelResponse mealCancelResponse = new MealCancelResponse();

        HttpRequest request = requestBuilder
            .uri(URI.create(urlBase + urlSuffix))
            .build();

        Client.debug("[Cancel] Sending " + urlSuffix);
        HttpResponse<String> response = ihttpClient.send(request, BodyHandlers.ofString());
        Client.debug("[Cancel] Status code: " + response.statusCode());
        Client.debug("[Cancel] Response   : " + response.body());

        ensureGoodResponse(response);

        mealCancelResponse = gson.fromJson(response.body(), MealCancelResponse.class);

        return mealCancelResponse;
    }
    public List<Meal> getMealsBookedInMonth(String date) throws IOException
    {
        StringBuilder suffixSB = new StringBuilder();
        suffixSB.append("/student-meal-booking/spring/api/get-meal-bookings-dto/en/");
        suffixSB.append(date);
        String urlSuffix = suffixSB.toString();

        HttpRequest request = requestBuilder
            .uri(URI.create(urlBase + urlSuffix))
            .GET()
            .build();

        Client.debug("[GetMealsBookedInMonth] Sending " + urlSuffix);
        HttpResponse<String> response = ihttpClient.send(request, BodyHandlers.ofString());
        Client.debug("[GetMealsBookedInMonth] Status code: " + response.statusCode());
        Client.debug("[GetMealsBookedInMonth] Response   : " + response.body());

        ensureGoodResponse(response);

        Type mealListType = new TypeToken<List<Meal>>() {}.getType();
        List<Meal> meals = gson.fromJson(response.body(), mealListType);

        return meals;
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
            Client.debug("[CLIENT] Flushing server: " + response.statusCode());
        }
        catch (ConnectException e) {
            System.out.println("ERROR: Failed to connect, exiting");
            System.exit(1);
        }
        //catch (TimeoutException e) { Client.debug("[CLIENT] Timed out"); }
        catch (IOException e) { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    };
};
