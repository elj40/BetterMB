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


class Tester {
    @Test
    void ConnectToServer()
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
    @Disabled
    void ClientGetAllBookedMealsThisMonth()
    {
        Client client;
        String date;
        Meal[] meals;

        client = new Client();
        date = "";

        meals = client.getAllBookedMeals(date);
        Assertions.assertNull(meals);

        date = "";
        meals = client.getAllBookedMeals(date);
        Assertions.assertNull(meals);

        date = "2025-01-31";
        Meal meal0 = new Meal("2025-01-01");
        Meal meal1 = new Meal("2025-01-02");
        client.book(meal0);
        client.book(meal1);
        meals = client.getAllBookedMeals(date);
        Assertions.assertEquals(meals.length, 2);
        Assertions.assertEquals(meals[0], meal0);
    };
    @Test
    void ClientBookMeal()
    {
        // How do i test this without the server?
        Client client;
        Meal meal;
        boolean result;

        client = new Client();
        client._flushServer();

        meal = new Meal("2025-01-01");
        result = client.book(meal);
        Assertions.assertTrue(result);

        result = client.book(meal);
        Assertions.assertFalse(result);
    };
};
