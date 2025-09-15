import java.net.ConnectException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import java.io.IOException;

class ClientMealSlotsTest
{
    Client client;
    CustomStubHttpClient stub;
    List<MealSlot> list;
    String date = "2025-01-01";
    @BeforeEach
    void setup()
    {
        client = new Client();
        stub = new CustomStubHttpClient();
        client.setCookies(Common.securityCookies);
        Client.debugging = false;
        stub.shouldThrowException = false;
    };
    @Test
    void testGetAvailableMealSlots3()
    {

        stub.setResponseFromString(200, "[" +
                "  {\"code\":\"0\",\"description\":\"Select\"}"    +
                ", {\"code\":\"B\",\"description\":\"Breakfast\"}" +
                ", {\"code\":\"L\",\"description\":\"Lunch\"}"     +
                ", {\"code\":\"D\",\"description\":\"Dinner\"}"    +
                "]");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealSlots(date); });
        assertEquals(3, list.size());
    }
    @Test
    void testGetAvailableMealSlots2()
    {
        stub.setResponseFromString(200, "[" +
                "  {\"code\":\"0\",\"description\":\"Select\"}"    +
                ", {\"code\":\"L\",\"description\":\"Lunch\"}"     +
                ", {\"code\":\"D\",\"description\":\"Dinner\"}"    +
                "]");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealSlots(date); });
        assertEquals(2, list.size());

        stub.setResponseFromString(200, "[" +
                "  {\"code\":\"0\",\"description\":\"Select\"}"    +
                ", {\"code\":\"B\",\"description\":\"Breakfast\"}" +
                ", {\"code\":\"D\",\"description\":\"Dinner\"}"    +
                "]");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealSlots(date); });
        assertEquals(2, list.size());

        stub.setResponseFromString(200, "[" +
                "  {\"code\":\"0\",\"description\":\"Select\"}"    +
                ", {\"code\":\"B\",\"description\":\"Breakfast\"}" +
                ", {\"code\":\"L\",\"description\":\"Lunch\"}"     +
                "]");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealSlots(date); });
        assertEquals(2, list.size());
    }
    @Test
    void testGetAvailableMealSlots1()
    {
        stub.setResponseFromString(200, "[" +
                "  {\"code\":\"0\",\"description\":\"Select\"}"    +
                ", {\"code\":\"B\",\"description\":\"Breakfast\"}" +
                "]");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealSlots(date); });
        assertEquals(1, list.size());

        stub.setResponseFromString(200, "[" +
                "  {\"code\":\"0\",\"description\":\"Select\"}"    +
                ", {\"code\":\"L\",\"description\":\"Lunch\"}"     +
                "]");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealSlots(date); });
        assertEquals(1, list.size());

        stub.setResponseFromString(200, "[" +
                "  {\"code\":\"0\",\"description\":\"Select\"}"    +
                ", {\"code\":\"D\",\"description\":\"Dinner\"}"    +
                "]");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealSlots(date); });
        assertEquals(1, list.size());
    };
    @Test
    void testGetAvailableMealSlots0()
    {
        stub.setResponseFromString(200, "[" +
                "  {\"code\":\"0\",\"description\":\"Select\"}"    +
                "]");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealSlots(date); });
        assertEquals(0, list.size());
    }
    @Test
    void testFailedConnection()
    {
        stub.setThrowException(new ConnectException("Connection failed!"));
        client.setHttpClient(stub);
        assertThrows(ConnectException.class, () -> client.getAvailableMealSlots(date));
    };
    @Test
    void testSingleSignOnResponse()
    {
        stub.setResponseFromString(200,
                "<!DOCTYPE html><html><body>Single Sign-on</body></html>");
        client.setHttpClient(stub);
        client.setCookies("incorrect=cookies");
        assertThrows(SecurityFailedException.class, () -> client.getAvailableMealSlots(date));
    };
    @Test
    void testResourceMovedResponse()
    {
        stub.setResponseFromString(403,
                "<!DOCTYPE html><html><body>403: resource is no longer here</body></html>");
        client.setHttpClient(stub);
        client.setCookies("incorrect=cookies");
        assertThrows(SecurityFailedException.class, () -> client.getAvailableMealSlots(date));
    };
};
