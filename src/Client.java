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
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

class Client
{
    HttpClient httpClient = HttpClient.newBuilder().build();
    Gson gson = new Gson();
    String urlBase = "http://127.0.0.1:8080";

    Client(String _url_base)
    {
        urlBase = _url_base;
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
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBase + urlSuffix))
                .timeout(Duration.ofSeconds(1))
                .build();

            System.out.println("[GetAvailableMealSlots] Sending " + urlSuffix);
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[GetAvailableMealSlots] Status code: " + response.statusCode());
            System.out.println("[GetAvailableMealSlots] Body       : " + response.body());

            Type mealSlotListType = new TypeToken<List<MealSlot>>() {}.getType();
            List<MealSlot> mealSlots = gson.fromJson(response.body(), mealSlotListType);
            if (mealSlots.getFirst().code == '0') mealSlots.removeFirst();

            return mealSlots;
        }
        catch (Exception e) { e.printStackTrace(); }

        return new ArrayList<MealSlot>();
    }
    public boolean book(Meal meal)
    {
        String mealJson = gson.toJson(meal);
        String urlSuffix = "/new-booking";
        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBase + urlSuffix))
                .POST(BodyPublishers.ofString(mealJson))
                .timeout(Duration.ofSeconds(1))
                .build();

            System.out.println("[CLIENT] Sending " + urlSuffix+": " + mealJson);
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[CLIENT] Status code: " + response.statusCode());
            System.out.println("[CLIENT] Body       : " + response.body());

            if  (response.body().equals("no")) return false;
        }
        catch (ConnectException e) { e.printStackTrace(); }
        //catch (TimeoutException e) { System.out.println("[CLIENT] Timed out"); }
        catch (IOException e) { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return true;
    };
    // Flushes the test server
    // DO NOT USE OUTSIDE OF TESTING!
    public void flushServer()
    {
        String urlSuffix = "/flush";
        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBase + urlSuffix))
                .timeout(Duration.ofSeconds(1))
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
