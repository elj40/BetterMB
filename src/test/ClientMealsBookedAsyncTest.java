import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpTimeoutException;

class ClientMealsBookedAsyncTest
{
    Client client;
    CustomStubHttpClient stub;
    CompletableFuture<List<Meal>> listFuture;
    List<Meal> list;
    String date = "2025-01-01";

    final int testResponseTimeMs = 10;
    @BeforeEach
    void setup()
    {
        client = new Client();
        stub = new CustomStubHttpClient();
        client.setCookies(Common.securityCookies);
        client.setUrlBase("http://127.0.0.1");
        Client.debugging = false;
        stub.shouldThrowException = false;
        stub.setResponseTimeMs(testResponseTimeMs);
    };
    @Test
    void testMealsBookedInMonthEmptyAsync()
    {
        String responseString = "";
        responseString += "[ ";
        responseString += " ]";
        stub.setResponseFromString(200, responseString);
        client.setHttpClient(stub);

        listFuture = client.getMealsBookedInMonthAsync(date);
        assertDoesNotThrow(() -> { list = listFuture.get(); });
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    @Test
    void testMealsBookedInMonthAsync()
    {
        String responseString = "";
        responseString += "[ ";
        responseString += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2025-08-31T11:00\",\"description\":\"Sunday Roast Beef\",\"facility\":\"Majuba\",\"mealTime\":\"11:00 - 13:00\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8172130}, ";
        responseString += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2025-09-01T07:15\",\"description\":\"Fried Egg Cheese Tomato\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172107}, ";
        responseString += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2025-09-01T12:30\",\"description\":\"Chicken Pie\",\"facility\":\"Majuba\",\"mealTime\":\"12:30 - 13:30\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8186151}, ";
        responseString += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2025-09-02T07:15\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172108}, ";
        responseString += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2025-09-02T12:30\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"12:30 - 13:30\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8186207}, ";
        responseString += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2025-09-03T07:15\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172109}";
        responseString += " ]";

        stub.setResponseFromString(200, responseString);
        client.setHttpClient(stub);

        listFuture = client.getMealsBookedInMonthAsync(date);
        assertDoesNotThrow(() -> { list = listFuture.get(); });
        assertNotNull(list);
        assertEquals(6, list.size());
    }
    @Test
    void testFailedConnection()
    {
        stub.setThrowException(new ConnectException());

        client.setHttpClient(stub);
        listFuture = client.getMealsBookedInMonthAsync(date);
        assertDoesNotThrow(() -> { list = listFuture.get(); });
        assertNull(list);
    };
    @Test
    void testTimeout()
    {
        stub.setThrowException(new HttpTimeoutException("Request timed out!"));
        client.setHttpClient(stub);

        listFuture = client.getMealsBookedInMonthAsync(date);
        assertDoesNotThrow(() -> { list = listFuture.get(); });
        assertNull(list);
    };
    @Test
    void testSingleSignOnResponse()
    {
        stub.setResponseFromString(200,
                "<!DOCTYPE html><html><body>Single Sign-on</body></html>");
        client.setHttpClient(stub);

        listFuture = client.getMealsBookedInMonthAsync(date);
        assertDoesNotThrow(() -> { list = listFuture.get(); });
        assertNull(list);
    };
    @Test
    void testResourceMovedResponse()
    {
        stub.setResponseFromString(403,
                "<!DOCTYPE html><html><body>403: resource is no longer here</body></html>");
        client.setHttpClient(stub);

        listFuture = client.getMealsBookedInMonthAsync(date);
        assertDoesNotThrow(() -> { list = listFuture.get(); });
        assertNull(list);
    };
}
