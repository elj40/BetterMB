import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

class Tester {
    private static MockServer server;
    @BeforeAll
    public static void initAll()
    {
        server = new MockServer();
        server.start();
    };
    @AfterAll
    public static void endAll()
    {
        server.stop();
    };
    @Test
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
