import com.eli.bettermb.client.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import javax.net.ssl.SSLSession;
import java.net.URI;
import java.util.Optional;
import java.io.IOException;

class StubBookHttpClient implements IHttpClient
{
    HttpResponse<String> response;
    boolean shouldThrowException = false;
    IOException exception;
    int responseTimeMs = 0;
    @Override
    public HttpResponse<String> send(HttpRequest request, BodyHandler bodyHandler) throws IOException
    {
        String uri = request.uri().toString();
        if (uri.contains("get-meal-slots-dto"))
        {
            this.setResponseFromString(200, "[" +
                    "  {\"code\":\"0\",\"description\":\"Select\"}"    +
                    ", {\"code\":\"B\",\"description\":\"Breakfast\"}" +
                    ", {\"code\":\"L\",\"description\":\"Lunch\"}"     +
                    ", {\"code\":\"D\",\"description\":\"Dinner\"}"    +
                    "]");
        }
        else if (uri.contains("facilities/"))
        {
            this.setResponseFromString(200, "[ " +
                    "{\"code\":\"0\",\"description\":\"Select\"}, "           +
                    "{\"code\":\"8\",\"description\":\"Minerva\"}, "          +
                    "{\"code\":\"9\",\"description\":\"Dagbreek\"}, "         +
                    "{\"code\":\"11\",\"description\":\"Huis Ten Bosch\"}, "  +
                    "{\"code\":\"20\",\"description\":\"Lydia\"}, "           +
                    "{\"code\":\"22\",\"description\":\"Majuba\"}, "          +
                    "{\"code\":\"27\",\"description\":\"VictoriaHub\"} "      +
                    "]");
        }
        else if (uri.contains("options/"))
        {
            this.setResponseFromString(200, "[ " +
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
        }
        else if (uri.contains("booking/"))
        {
            this.setResponseFromString(200, "[ " +
                    "{\"bookingDate\":\"2025-01-01\" ,\"bookingMessage\":\"Reservation already exists\"}" +
                    " ]");
        }

        return response;
    };
    public void setResponseFromString(int statusCode, String msg)
    {
        response = new HttpResponse<String>() {
            @Override
            public int statusCode() { return statusCode; }
            @Override
            public String body() { return msg; };
            @Override
            public HttpRequest request() { return null; };
            @Override
            public HttpHeaders headers() { return null; };
            @Override
            public URI uri() { return null; };
            @Override
            public Optional<HttpResponse<String>> previousResponse() { return null; };
            @Override
            public HttpClient.Version version() { return null; };
            @Override
            public Optional<SSLSession> sslSession() { return null; };
        };
    };
};
