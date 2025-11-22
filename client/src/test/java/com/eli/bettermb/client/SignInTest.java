import com.eli.bettermb.client.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.lang.InterruptedException;

import org.openqa.selenium.WebDriverException;

class ClientSignInTest
{
    String urlEntry;
    String urlTarget;
    String targetCookies;
    @BeforeEach
    void setup()
    {
        Client.debugging = false;

        urlEntry = "http://127.0.0.1:8080/sign-in";
        urlTarget = "http://127.0.0.1:8080/target";
        targetCookies = "";
    };
    @Test
    @Disabled
    void testGetSecurityCookies()
    {
        try {targetCookies = Client.getSecurityCookiesBySignIn(urlEntry, urlTarget); }
        catch (WebDriverException e)
        {
            if (e.getMessage().contains("ERR_CONNECTION_REFUSED"))
                fail("Connection refused: check if mock server is running");
            else fail(e.getMessage());
        }
        catch (Exception e) { fail(e.getMessage()); }
        String[] targetCookiesSplit = targetCookies.split(";");
        for (String cookie : targetCookiesSplit)
        {
            assertTrue(Common.securityCookies.contains(cookie));
        }
    }
}
