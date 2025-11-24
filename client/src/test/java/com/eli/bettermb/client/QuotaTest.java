import com.eli.bettermb.client.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.ConnectException;

// summary
// https://web-apps.sun.ac.za/student-meal-booking/spring/api/get-quota-summary/en
// {
//    "quotaPendingMessage": null,
//    "currentQuotaDesc": "R21000.00",
//    "cobQuotaDesc": "R0",
//    "balanceDesc": "R1730.86",
//    "mealUsageDesc": null,
//    "quotaIncreaseDTO": [
//        {
//            "quotaIncreaseCode": 50000,
//            "quotaIncreaseAmount": "R500.00"
//        },
//        {
//            "quotaIncreaseCode": 100000,
//            "quotaIncreaseAmount": "R1000.00"
//        },
//        {
//            "quotaIncreaseCode": 200000,
//            "quotaIncreaseAmount": "R2000.00"
//        },
//        {
//            "quotaIncreaseCode": 400000,
//            "quotaIncreaseAmount": "R4000.00"
//        }
//    ],
//    "quotaDecreaseDTO": [
//        {
//            "quotaDecreaseCode": 50000,
//            "quotaDecreaseAmount": "R500.00"
//        },
//        {
//            "quotaDecreaseCode": 100000,
//            "quotaDecreaseAmount": "R1000.00"
//        },
//        {
//            "quotaDecreaseCode": 173086,
//            "quotaDecreaseAmount": "R1730.86"
//        }
//    ],
//    "cobIncreaseDTO": [
//        {
//            "cobIncreaseCode": 10000,
//            "cobIncreaseAmount": "R100.00"
//        },
//        {
//            "cobIncreaseCode": 20000,
//            "cobIncreaseAmount": "R200.00"
//        },
//        {
//            "cobIncreaseCode": 40000,
//            "cobIncreaseAmount": "R400.00"
//        },
//        {
//            "cobIncreaseCode": 80000,
//            "cobIncreaseAmount": "R800.00"
//        },
//        {
//            "cobIncreaseCode": 160000,
//            "cobIncreaseAmount": "R1600.00"
//        },
//        {
//            "cobIncreaseCode": 173086,
//            "cobIncreaseAmount": "R1730.86"
//        }
//    ],
//    "cobDecreaseDTO": [
//        {
//            "cobDecreaseCode": 0,
//            "cobDecreaseAmount": "R0"
//        }
//    ]
//}
class QuotaTest
{
    Client client;
    CustomStubHttpClient stub;
    QuotaSummary quota;
    @BeforeEach
    void setup()
    {
        client = new Client();
        stub = new CustomStubHttpClient();
        quota = new QuotaSummary();
        client.setCookies(Common.securityCookies);
        client.setUrlBase("http://127.0.0.1");
        Client.debugging = true;
        stub.shouldThrowException = false;
    };
    @Test
    void testGetQuotaSummary()
    {
        String responseString = "{" +
            "\"quotaPendingMessage\": null," +
            "\"currentQuotaDesc\": \"R21000.00\"," +
            "\"cobQuotaDesc\": \"R0\"," +
            "\"balanceDesc\": \"R1730.86\"," +
            "\"mealUsageDesc\": null," +
            "\"quotaIncreaseDTO\": []," +
            "\"quotaDecreaseDTO\": []," +
            "\"cobIncreaseDTO\": []," +
            "\"cobDecreaseDTO\": []" +
            "}";
        stub.setResponseFromString(200, responseString);
        client.setHttpClient(stub);
        assertDoesNotThrow(() -> { quota = client.getQuotaSummary(); });
        assertNull(quota.quotaPendingMessage);
        assertEquals(quota.quotaPendingMessage, null);
        assertEquals(quota.currentQuotaDesc, "R21000.00");
        assertEquals(quota.cobQuotaDesc, "R0");
        assertEquals(quota.balanceDesc, "R1730.86");
        assertEquals(quota.mealUsageDesc, null);
    };
    @Test
    void testSingleSignOnResponse()
    {
        stub.setResponseFromString(200,
                "<!DOCTYPE html><html><body>Single Sign-on</body></html>");
        client.setHttpClient(stub);
        assertThrows(SecurityFailedException.class, () -> client.getQuotaSummary());
    };
    @Test
    void testResourceMovedResponse()
    {
        stub.setResponseFromString(403,
                "<!DOCTYPE html><html><body>403: resource is no longer here</body></html>");
        client.setHttpClient(stub);
        assertThrows(SecurityFailedException.class, () -> client.getQuotaSummary());
    };

}
