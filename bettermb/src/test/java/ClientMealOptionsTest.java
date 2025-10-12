import java.net.ConnectException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import java.io.IOException;

class ClientMealOptionsTest
{
    Client client;
    CustomStubHttpClient stub;
    List<MealOption> list;
    String date = "2025-01-01";
    char slot;
    int facility;
    @BeforeEach
    void setup()
    {
        client = new Client();
        stub = new CustomStubHttpClient();
        client.setCookies(Common.securityCookies);
        client.setUrlBase("http://127.0.0.1");
        Client.debugging = false;

        stub.setResponseFromString(200, "[ " +
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

        slot    = MealSlot.BREAKFAST;
        facility = MealFacility.MAJUBA;
    };
    @Test
    void testGetAvailableMealOptions()
    {
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getMealOptions(date, slot, facility); });
        assertEquals(11, list.size());
        assertEquals(25159, list.getLast().code);
    };
    @Test
    void testFailedConnection()
    {
        stub.setThrowException(new ConnectException("Connection failed!"));
        client.setHttpClient(stub);
        assertThrows(ConnectException.class, () -> client.getMealOptions(date, slot, facility));
    };
    @Test
    void testSingleSignOnResponse()
    {
        stub.setResponseFromString(200,
                "<!DOCTYPE html><html><body>Single Sign-on</body></html>");
        client.setHttpClient(stub);
        assertThrows(SecurityFailedException.class, () -> client.getMealOptions(date, slot, facility));
    };
    @Test
    void testResourceMovedResponse()
    {
        stub.setResponseFromString(403,
                "<!DOCTYPE html><html><body>403: resource is no longer here</body></html>");
        client.setHttpClient(stub);
        assertThrows(SecurityFailedException.class, () -> client.getMealOptions(date, slot, facility));
    };
}
