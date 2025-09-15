import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.ConnectException;

class ClientBookTest
{
    Client client;
    CustomStubHttpClient stub;
    List<MealBookingResponse> list;
    MealBookingOptions mbo;
    String date = "2025-01-01";
    char slot    = MealSlot.BREAKFAST;
    int facility = MealFacility.MAJUBA;
    @BeforeEach
    void setup()
    {
        client = new Client();
        stub = new CustomStubHttpClient();
        mbo = new MealBookingOptions();
        client.setCookies(Common.securityCookies);
        Client.debugging = false;
        stub.shouldThrowException = false;

        mbo.mealDate = date;
        mbo.mealSlot = slot;
        mbo.mealFacility = facility;
        mbo.advanceBookingDays = 0;
    };
    @Test
    void testBookSuccess()
    {
        stub.setResponseFromString(200, "[ " +
                "{\"bookingDate\":" + date + " ,\"bookingMessage\":\"Booking successful\"}" +
                " ]");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.book(mbo); });
        assertEquals(1, list.size());
        assertEquals("Booking successful", list.getFirst().bookingMessage);
        assertEquals(mbo.mealDate, list.getFirst().bookingDate);
    }
    @Test
    void testBookReserved()
    {
        stub.setResponseFromString(200, "[ " +
                "{\"bookingDate\":" + date + " ,\"bookingMessage\":\"Reservation already exists\"}" +
                " ]");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.book(mbo); });
        assertEquals(1, list.size());
        assertEquals("Reservation already exists", list.getFirst().bookingMessage);
        assertEquals(mbo.mealDate, list.getFirst().bookingDate);
    }
    @Test
    void testBookMultipleSuccess()
    {
        mbo.advanceBookingDays = 3;
        String responseString = "[ ";
        for (int i = 0; i <= mbo.advanceBookingDays; i++)
        {
            String bookingDate = LocalDate
                .parse(date)
                .plusDays(i)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (i > 0) responseString += ", ";
            responseString += "{\"bookingDate\":" + bookingDate + " ,\"bookingMessage\":\"Booking successful\"}";
        };
        responseString += " ]";
        stub.setResponseFromString(200, responseString);
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { list = client.book(mbo); });
        assertEquals(mbo.advanceBookingDays + 1, list.size());
        for (int i = 0; i < list.size(); i++)
        {
            String bookingDate = LocalDate
                .parse(date)
                .plusDays(i)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            assertEquals("Booking successful", list.get(i).bookingMessage);
        };
    }
    @Test
    void testFailedConnection()
    {
        stub.setThrowException(new ConnectException("Connection failed!"));
        client.setHttpClient(stub);
        assertThrows(ConnectException.class, () -> client.book(mbo));
    };
    @Test
    void testSingleSignOnResponse()
    {
        stub.setResponseFromString(200,
                "<!DOCTYPE html><html><body>Single Sign-on</body></html>");
        client.setHttpClient(stub);
        client.setCookies("incorrect=cookies");
        assertThrows(SecurityFailedException.class, () -> client.book(mbo));
    };
    @Test
    void testResourceMovedResponse()
    {
        stub.setResponseFromString(403,
                "<!DOCTYPE html><html><body>403: resource is no longer here</body></html>");
        client.setHttpClient(stub);
        client.setCookies("incorrect=cookies");
        assertThrows(SecurityFailedException.class, () -> client.book(mbo));
    };
}
