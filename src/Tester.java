import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Assertions;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import java.net.URI;
import java.net.ConnectException;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import com.google.gson.Gson;

class Tester {
    @Test
    void ClientConnectToServer()
    {
        try {
        URI uri = URI.create("http://127.0.0.1:8080");
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .timeout(Duration.ofSeconds(1))
            .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        }
        catch (ConnectException e) { e.printStackTrace(); }
        //catch (TimeoutException e) { System.out.println("[CLIENT] Timed out"); }
        catch (IOException e) { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    };
    @Test
    void ClientGetAvailableMealSlots()
    {
        String base_url = "http://127.0.0.1:8080";
        Client client = new Client(base_url);
        Gson gson = new Gson();
        List<MealSlot> mealSlots;
        String date = "2025-01-01";
        Meal meal;

        client.flushServer();
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertEquals(3, mealSlots.size());

        meal = new Meal(date, MealSlot.BREAKFAST);
        client.book(meal);
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertEquals(2, mealSlots.size());

        meal = new Meal(date, MealSlot.LUNCH);
        client.book(meal);
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertEquals(1, mealSlots.size());

        meal = new Meal(date, MealSlot.DINNER);
        client.book(meal);
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertEquals(0, mealSlots.size());
    };
    @Test
    void ClientGetAvailableMealFacilities() { Assertions.assertTrue(false); };
    @Test
    void ClientGetAvailableMeals() { Assertions.assertTrue(false); };
    @Test
    void ClientBookMeal() { Assertions.assertTrue(false); };
    @Test
    void ClientGetMealsBookedThisMonth() { Assertions.assertTrue(false); };
    @Test
    void ClientCancelMeal() { Assertions.assertTrue(false); };
};
