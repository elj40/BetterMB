import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpTimeoutException;

class ClientCancelTest
{
    Client client;
    CustomStubHttpClient stub;
    List<MealBookingResponse> list;
    MealCancelResponse mcr;
    int id = 0;
    @BeforeEach
    void setup()
    {
        client = new Client();
        stub = new CustomStubHttpClient();
        mcr = null;
        client.setCookies(Common.securityCookies);
        Client.debugging = false;
        stub.shouldThrowException = false;
    };
    @Test
    void testCancelSuccess()
    {
        stub.setResponseFromString(200, "{\"success\":true,\"message\":null}");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { mcr = client.cancel(id); });
        assertTrue(mcr.success);
        assertNull(mcr.message);
    }
    @Test
    void testCancelSuccessWithMessage()
    {
        stub.setResponseFromString(200, "{\"success\":true,\"message\":\"Well done you managed to cancel!\"}");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { mcr = client.cancel(id); });
        assertTrue(mcr.success);
        assertEquals("Well done you managed to cancel!", mcr.message);
    }
    void testCancelFailure()
    {
        stub.setResponseFromString(200, "{\"success\":false,\"message\":null}");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { mcr = client.cancel(id); });
        assertFalse(mcr.success);
        assertNull(mcr.message);
    }
    @Test
    void testCancelFailureWithMessage()
    {
        stub.setResponseFromString(200, "{\"success\":false,\"message\":\"Well done you failed to cancel!\"}");
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { mcr = client.cancel(id); });
        assertFalse(mcr.success);
        assertEquals("Well done you failed to cancel!", mcr.message);
    }
    @Test
    void testTimeout()
    {
        stub.setThrowException(new HttpTimeoutException("Request timed out!"));
        client.setHttpClient(stub);
        assertThrows(HttpTimeoutException.class, () -> client.cancel(id));
    };
    @Test
    void testFailedConnection()
    {
        stub.setThrowException(new ConnectException("Connection failed!"));
        client.setHttpClient(stub);
        assertThrows(ConnectException.class, () -> client.cancel(id));
    };
    @Test
    void testSingleSignOnResponse()
    {
        stub.setResponseFromString(200,
                "<!DOCTYPE html><html><body>Single Sign-on</body></html>");
        client.setHttpClient(stub);
        client.setCookies("incorrect=cookies");
        assertThrows(SecurityFailedException.class, () -> client.cancel(id));
    };
    @Test
    void testResourceMovedResponse()
    {
        stub.setResponseFromString(403,
                "<!DOCTYPE html><html><body>403: resource is no longer here</body></html>");
        client.setHttpClient(stub);
        client.setCookies("incorrect=cookies");
        assertThrows(SecurityFailedException.class, () -> client.cancel(id));
    };
}
