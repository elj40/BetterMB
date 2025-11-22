import com.eli.bettermb.client.*;

import java.net.ConnectException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import java.io.IOException;
import com.google.gson.JsonSyntaxException;

class ClientMealFacilitiesTest
{
    Client client;
    CustomStubHttpClient stub;
    List<MealFacility> list;
    String date = "2025-01-01";
    char mealSlot;
    @BeforeEach
    void setup()
    {
        client = new Client();
        stub = new CustomStubHttpClient();
        client.setCookies(Common.securityCookies);
        client.setUrlBase("http://127.0.0.1");
        Client.debugging = false;

        stub.setResponseFromString(200, "[ " +
        "{\"code\":\"0\",\"description\":\"Select\"}, "           +
        "{\"code\":\"8\",\"description\":\"Minerva\"}, "          +
        "{\"code\":\"9\",\"description\":\"Dagbreek\"}, "         +
        "{\"code\":\"11\",\"description\":\"Huis Ten Bosch\"}, "  +
        "{\"code\":\"20\",\"description\":\"Lydia\"}, "           +
        "{\"code\":\"22\",\"description\":\"Majuba\"}, "          +
        "{\"code\":\"27\",\"description\":\"VictoriaHub\"} "      +
        "]");
    };
    @Test
    void testGetAvailableMealFaciliesB()
    {
        mealSlot = 'B';

        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealFacilities(date, mealSlot); });
        assertEquals(6, list.size());
    }
    @Test
    void testGetAvailableMealFaciliesL()
    {
        mealSlot = 'L';

        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealFacilities(date, mealSlot); });
        assertEquals(6, list.size());
    }
    @Test
    void testGetAvailableMealFaciliesD()
    {
        mealSlot = 'D';

        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.getAvailableMealFacilities(date, mealSlot); });
        assertEquals(6, list.size());
    }
    @Test
    void testFailedConnection()
    {
        stub.setThrowException(new ConnectException("Connection failed!"));
        client.setHttpClient(stub);
        assertThrows(ConnectException.class, () -> client.getAvailableMealFacilities(date, 'B'));
    };
    @Test
    void testSingleSignOnResponse()
    {
        mealSlot = 'D';
        stub.setResponseFromString(200,
                "<!DOCTYPE html><html><body>Single Sign-on</body></html>");
        client.setHttpClient(stub);
        assertThrows(SecurityFailedException.class, () -> client.getAvailableMealFacilities(date, mealSlot));
    };
    @Test
    void testResourceMovedResponse()
    {
        mealSlot = 'D';
        stub.setResponseFromString(403,
                "<!DOCTYPE html><html><body>403: resource is no longer here</body></html>");
        client.setHttpClient(stub);
        assertThrows(SecurityFailedException.class, () -> client.getAvailableMealFacilities(date, mealSlot));
    };
}
