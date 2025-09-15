import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.net.URI;
import java.net.InetSocketAddress;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;

class UnimplementedHandler implements HttpHandler
{
    public void handle(HttpExchange exchange)
    {
        try
        {

            URI uri = exchange.getRequestURI();
            System.out.println("[Unimplemented] " + uri.getPath());

            exchange.sendResponseHeaders(200, 0);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write("UNIMPLEMENTED ".getBytes());
            outputStream.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
};
class EchoHandler implements HttpHandler
{
    public void handle(HttpExchange exchange)
    {
        try
        {
            System.out.println("[SERVER] Echo");
            String      method      = exchange.getRequestMethod();
            URI         uri         = exchange.getRequestURI();
            Headers     headers     = exchange.getRequestHeaders();
            InputStream inputStream = exchange.getRequestBody();

            byte[] uri_bytes = uri.toString().getBytes();
            byte[] headers_bytes = headers.toString().getBytes();
            byte[] body_bytes = inputStream.readAllBytes();

            exchange.sendResponseHeaders(200, 0);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(uri_bytes);
            outputStream.write(headers_bytes);
            outputStream.write(body_bytes);
            outputStream.close();

        } catch (IOException e) { e.printStackTrace(); }
    }
};
class StaticTextHandler implements HttpHandler
{
    String text;
    boolean addHeaders = false;
    StaticTextHandler(String _text)
    {
        text = _text;
    };
    StaticTextHandler(String _text, boolean _headers)
    {
        text = _text;
        addHeaders = _headers;
    };
    public final void handle(HttpExchange exchange) throws IOException
    {
        if (addHeaders)
        {
            Headers headers = exchange.getResponseHeaders();
            String cookies[] = Common.securityCookies.split(";");
            for (String cookie : cookies)
            {
                headers.add("Set-Cookie", cookie);
            }
        };
        exchange.sendResponseHeaders(200, text.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(text.getBytes());
        outputStream.close();
        return;
    };
};
abstract class SecurityHandler implements HttpHandler
{
    String securityCookie = Common.securityCookies;
    @Override
    public final void handle(HttpExchange exchange) throws IOException
    {
        if (!ensureSecurityCookies(exchange))
        {
            exchange.sendResponseHeaders(200, 0);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(
                    "<!DOCTYPE html><html><body><h1>Single Sign-on</h1></body></html>"
                    .getBytes());
            outputStream.close();
            return;
        }
        handler(exchange);
    };

    private boolean ensureSecurityCookies(HttpExchange exchange)
    {
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookieHeader == null) return false;

        System.out.println("[Security] " + cookieHeader);
        String[] cookies = cookieHeader.split(";");
        if (cookies.length == 0) return false;
        if (cookies[0].length() == 0) return false;
        for (String cookie : cookies)
        {
            System.out.println("[Security] " + cookie);
            if (!securityCookie.contains(cookie)) return false;
        }
        return true;
    };

    abstract void handler(HttpExchange exchange);
};
class MockServer {
    static HttpServer server;
    static Gson       gson   = new Gson();
    static HashMap<String, HashMap<Character, Meal>> bookings = new HashMap<String, HashMap<Character, Meal>>();

    static int serverMealCounter = 0;

    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static String getMealSlotsPath = "/student-meal-booking/spring/api/get-meal-slots-dto/";
    static String getMealFacilitiesPath = "/student-meal-booking/spring/api/get-meal-slot-facilities/en";
    static String getMealOptionsPath = "/student-meal-booking/spring/api/get-meal-slot-facility-options/en";
    static String getMealBookingsInMonthPath = "/student-meal-booking/spring/api/get-meal-bookings-dto/en/";
    static String bookMealPath = "/student-meal-booking/spring/api/store-meal-booking/en";
    static String cancelMealPath = "/student-meal-booking/spring/api/cancel-meal-booking/en/";

    public static void main(String[] args)
    {
        start();
    };
    public static void start()
    {
        try
        {
            // This represents https://web-apps.sun.ac.za/
            String serverAddress = "127.0.0.1";
            InetSocketAddress address = new InetSocketAddress(serverAddress, 8080);
            server = HttpServer.create();
            server.bind(address, 0);

            server.createContext("/", new UnimplementedHandler());
            server.createContext("/echo", new EchoHandler());
            server.createContext(getMealSlotsPath, new GetMealSlotsHandler());
            server.createContext(getMealFacilitiesPath, new GetMealFacilitiesHandler());
            server.createContext(getMealOptionsPath, new GetMealOptionsHandler());
            server.createContext(getMealBookingsInMonthPath, new GetMealBookingsInMonthHandler());
            server.createContext(bookMealPath, new BookingHandler());
            server.createContext(cancelMealPath, new CancelHandler());
            server.createContext("/flush", new FlushHandler());
            server.createContext("/sign-in", new StaticTextHandler(
                        "<!DOCTYPE html><html><body><a href=\"/target\">Go to target</a></body></html>"
                        ));
            server.createContext("/target", new StaticTextHandler(
                        "<!DOCTYPE html><html><body><h1>WELCOME!</h1></body></html>",
                        true
                        ));

            server.start();
            System.out.println("[SERVER] Serving at: " + server.getAddress());
        } catch (Exception e) { e.printStackTrace(); };
    };
    public void stop()
    {
        System.out.println("[SERVER] Stopping");
        server.stop(0);
    };
    static class GetMealOptionsHandler extends SecurityHandler
    {
        @Override
        public void handler(HttpExchange exchange)
        {
            try
            {
                StringBuilder responseSB = new StringBuilder();
                responseSB.append("[ ");
                responseSB.append("{\"code\":\"0\",\"description\":\"Select\",\"cost\":null,\"sessionId\":0,\"sessionStart\":\"\",\"sessionEnd\":\"\",\"bookable\":false,\"reason\":null}, ");
                responseSB.append("{\"code\":\"159523\",\"description\":\"Take Away Prepacked Meal\",\"cost\":\"R44.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, ");
                responseSB.append("{\"code\":\"160095\",\"description\":\"Vegetarion\",\"cost\":\"R21.79\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, ");
                responseSB.append("{\"code\":\"160667\",\"description\":\"Chicken Meal\",\"cost\":\"R18.79\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, ");
                responseSB.append("{\"code\":\"161239\",\"description\":\"Halal Friendly Meal\",\"cost\":\"R18.79\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, ");
                responseSB.append("{\"code\":\"161811\",\"description\":\"Lactose Free Meal\",\"cost\":\"R18.79\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, ");
                responseSB.append("{\"code\":\"195628\",\"description\":\"Farmhouse Breakfast\",\"cost\":\"R71.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, ");
                responseSB.append("{\"code\":\"195968\",\"description\":\"Breakfast Wrap Egg Bacon Tomat\",\"cost\":\"R58.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, ");
                responseSB.append("{\"code\":\"196308\",\"description\":\"Omelet Option\",\"cost\":\"R60.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, ");
                responseSB.append("{\"code\":\"196648\",\"description\":\"Cheesegriller Option\",\"cost\":\"R67.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, ");
                responseSB.append("{\"code\":\"196988\",\"description\":\"Breakfast Waffle Option\",\"cost\":\"R58.00\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"}, ");
                responseSB.append("{\"code\":\"25159\",\"description\":\"Standard Meal\",\"cost\":\"R18.79\",\"sessionId\":39,\"sessionStart\":\"07:15\",\"sessionEnd\":\"08:30\",\"bookable\":true,\"reason\":\"\"} ");
                responseSB.append("] ");

                exchange.sendResponseHeaders(200, 0);
                OutputStream out = exchange.getResponseBody();
                out.write(responseSB.toString().getBytes());
                out.close();
            } catch (Exception e) { e.printStackTrace(); };
        };
    }
    static class GetMealFacilitiesHandler extends SecurityHandler
    {
        @Override
        public void handler(HttpExchange exchange)
        {
            try
            {
                StringBuilder responseSB = new StringBuilder();
                responseSB.append("[ ");
                responseSB.append("{\"code\":\"0\",\"description\":\"Select\"}, ");
                responseSB.append("{\"code\":\"8\",\"description\":\"Minerva\"}, ");
                responseSB.append("{\"code\":\"9\",\"description\":\"Dagbreek\"}, ");
                responseSB.append("{\"code\":\"11\",\"description\":\"Huis Ten Bosch\"}, ");
                responseSB.append("{\"code\":\"20\",\"description\":\"Lydia\"}, ");
                responseSB.append("{\"code\":\"22\",\"description\":\"Majuba\"}, ");
                responseSB.append("{\"code\":\"27\",\"description\":\"VictoriaHub\"} ");
                responseSB.append("] ");

                exchange.sendResponseHeaders(200, 0);
                OutputStream out = exchange.getResponseBody();
                out.write(responseSB.toString().getBytes());
                out.close();
            } catch (Exception e) { e.printStackTrace(); };
        };
    }
    static class GetMealBookingsInMonthHandler extends SecurityHandler
    {
        @Override
        public void handler(HttpExchange exchange)
        {
            try
            {
                URI requestURI = exchange.getRequestURI();
                String requestPath = requestURI.getPath();
                if (!isValidPath(requestPath))
                {
                    exchange.sendResponseHeaders(400, 0);
                    OutputStream out = exchange.getResponseBody();
                    out.write("[GetMealBookingsInMonthHandler] Bad request:\n".getBytes());
                    out.write(("Expected: " + getMealBookingsInMonthPath + "yyyy-mm-dd\n").getBytes());
                    out.write(("Got     : " + requestPath).getBytes());
                    out.close();
                    return;
                } else
                {
                    int start = getMealBookingsInMonthPath.length();
                    String date = requestPath.substring(start);
                    System.out.println("[GetMealBookingsInMonthHandler] " + date);

                    StringBuilder responseSB = new StringBuilder();
                    responseSB.append("[ ");

                    boolean first = true;
                    for (HashMap<Character, Meal> day : bookings.values())
                    {
                        for (Meal meal : day.values())
                        {
                            if (first) first = false;
                            else responseSB.append(", ");
                            String mealJson = gson.toJson(meal);
                            responseSB.append(mealJson);
                        };
                    };

                    responseSB.append(" ]");

                    exchange.sendResponseHeaders(200, 0);
                    OutputStream out = exchange.getResponseBody();
                    out.write(responseSB.toString().getBytes());
                    out.close();
                }
            } catch (Exception e) { e.printStackTrace(); };
        };
        private boolean isValidPath(String path)
        {
            if (!path.startsWith(getMealBookingsInMonthPath)) return false;
            int dateLength = "yyyy-mm-dd".length();
            if (path.length() != (getMealBookingsInMonthPath.length() + dateLength))
                return false;
            return true;
        };
    }
    static class GetMealSlotsHandler extends SecurityHandler
    {
        @Override
        public void handler(HttpExchange exchange)
        {
            try
            {
                URI requestURI = exchange.getRequestURI();
                String requestPath = requestURI.getPath();
                if (!isValidPath(requestPath))
                {
                    exchange.sendResponseHeaders(400, 0);
                    OutputStream out = exchange.getResponseBody();
                    out.write("[GetMealSlotsHandler] Bad request:\n".getBytes());
                    out.write(("Expected: " + getMealSlotsPath + "yyyy-mm-dd/en\n").getBytes());
                    out.write(("Got     : " + requestPath).getBytes());
                    out.close();
                    return;
                } else
                {
                    int start = getMealSlotsPath.length();
                    int end   = start + "yyyy-mm-dd".length();
                    String date = requestPath.substring(start, end);
                    System.out.println("[GetMealSlotsHandler] " + date);

                    HashMap<Character, Meal> day = bookings.get(date);
                    boolean noBooking = day == null;
                    StringBuilder responseSB = new StringBuilder();
                    responseSB.append("[ ");
                    responseSB.append("{\"code\":\"0\",\"description\":\"Select\"}");

                    if (noBooking)
                    {
                        responseSB.append(", {\"code\":\"B\",\"description\":\"Breakfast\"}");
                        responseSB.append(", {\"code\":\"L\",\"description\":\"Lunch\"}");
                        responseSB.append(", {\"code\":\"D\",\"description\":\"Dinner\"}");
                    } else
                    {
                        if (!day.containsKey(MealSlot.BREAKFAST))
                            responseSB.append(", {\"code\":\"B\",\"description\":\"Breakfast\"}");

                        if (!day.containsKey(MealSlot.LUNCH))
                            responseSB.append(", {\"code\":\"L\",\"description\":\"Lunch\"}");

                        if (!day.containsKey(MealSlot.DINNER))
                            responseSB.append(", {\"code\":\"D\",\"description\":\"Dinner\"}");
                    };

                    responseSB.append(" ]");

                    exchange.sendResponseHeaders(200, 0);
                    OutputStream out = exchange.getResponseBody();
                    out.write(responseSB.toString().getBytes());
                    out.close();
                }
            } catch (Exception e) { e.printStackTrace(); };
        };
        private boolean isValidPath(String path)
        {
            if (!path.startsWith(getMealSlotsPath)) return false;
            if (!path.endsWith("/en")) return false;
            int dateLength = 4 + 1 + 2 + 1 + 2;
            int langLength = 3;
            if (path.length() != (getMealSlotsPath.length() + dateLength + langLength))
                return false;
            return true;
        };
    }
    static class BookingHandler extends SecurityHandler
    {
        @Override
        public void handler(HttpExchange exchange)
        {
            try
            {
                if(!"POST".equals(exchange.getRequestMethod()))
                {
                    String message = "[BookingHandler] Non-POST method made in booking";
                    System.out.println(message);
                    exchange.sendResponseHeaders(405, message.length());
                    OutputStream out = exchange.getResponseBody();
                    out.write(message.getBytes());
                    out.close();
                    return;
                };
                InputStream in = exchange.getRequestBody();
                String requestBody = new String(in.readAllBytes());
                MealBookingOptions mbo = gson.fromJson(requestBody, MealBookingOptions.class);

                if ("BLD".indexOf(mbo.mealSlot) == -1)
                {
                    System.out.println("[BookingHandler] Incorrect mealSlot: " + mbo.mealSlot);
                    exchange.sendResponseHeaders(400, 0);
                    OutputStream out = exchange.getResponseBody();
                    out.write("[BookingHandler] Bad request:\n".getBytes());
                    out.write(("Expected mbo.mealSlot: [BLD]\n").getBytes());
                    out.write(("Got: " + mbo.mealSlot).getBytes());
                    out.close();
                    return;
                };
                if (mbo.mealDate == null)
                {
                    System.out.println("[BookingHandler] Bad date");
                    exchange.sendResponseHeaders(400, 0);
                    OutputStream out = exchange.getResponseBody();
                    out.write("[BookingHandler] Bad request:\n".getBytes());
                    out.write(("Expected mbo.mealDate: [yyyy-mm-dd]\n").getBytes());
                    out.write(("Got: <null>").getBytes());
                    out.close();
                    return;
                };
                HashMap<Character, Meal> day = bookings.get(mbo.mealDate);
                LocalDate localDate = LocalDate.parse(mbo.mealDate);

                StringBuilder sb = new StringBuilder();

                sb.append("[ ");
                for (int i = 0; i <= mbo.advanceBookingDays; i++) {
                    String date = localDate.plusDays(i).format(dateFormatter);
                    System.out.println(date);
                    if (i > 0) sb.append(", ");

                    if (isBookingOpen(date, mbo.mealSlot)) {
                        Meal meal = Meal.fromMBO(mbo, serverMealCounter++);
                        meal.canModify = i % 2 == 0;
                        meal.start = date;
                        if (!bookings.containsKey(date)) bookings.put(date, new HashMap<>());
                        bookings.get(date).put(mbo.mealSlot, meal);

                        String message = "[BookingHandler] Booking on " + date + " " + mbo.mealSlot + " successful";
                        System.out.println(message);
                        sb.append("{\"bookingDate\":\"" + date + "\",\"bookingMessage\":\"Booking successful\"}");
                    } else
                    {
                        String message = "[BookingHandler] Booking on " + date + " " + mbo.mealSlot + " reserved already";
                        System.out.println(message);
                        sb.append("{\"bookingDate\":\"" + date + "\",\"bookingMessage\":\"Reservation already exists\"}");
                    }
                }
                sb.append(" ]");

                exchange.sendResponseHeaders(200, 0);
                OutputStream out = exchange.getResponseBody();
                out.write(sb.toString().getBytes());
                out.close();
            } catch (Exception e) { e.printStackTrace(); };
        };
        private boolean isBookingOpen(String date, char slot)
        {
            HashMap<Character, Meal> day = bookings.get(date);
            if (day == null) return true;
            return !day.containsKey(slot);
        };
    };
    static class CancelHandler extends SecurityHandler
    {
        @Override
        public void handler(HttpExchange exchange)
        {
            try
            {
                URI requestURI = exchange.getRequestURI();
                String requestPath = requestURI.getPath();
                int start = cancelMealPath.length();
                String idString = requestPath.substring(start);
                int id = Integer.parseInt(idString);

                System.out.println("[CancelHandler] " + idString);


                boolean foundBookingToRemove = false;
                String dateToRemove = "";
                char   slotToRemove = 'B';
                for (String date : bookings.keySet())
                {
                    for (char slot : bookings.get(date).keySet())
                    {
                        Meal meal = bookings.get(date).get(slot);
                        if (meal.id == id)
                        {
                            foundBookingToRemove = true;
                            slotToRemove = slot;
                            dateToRemove = date;
                            break;
                        }
                    };
                    if (foundBookingToRemove) break;
                };

                if (foundBookingToRemove)
                {
                    bookings.get(dateToRemove).remove(slotToRemove);
                    System.out.println("[CancelHandler] Removing " + dateToRemove + " " + slotToRemove);
                };

                StringBuilder responseSB = new StringBuilder();
                responseSB.append("{\"success\":");
                responseSB.append(foundBookingToRemove);
                responseSB.append(",\"message\":null}");

                exchange.sendResponseHeaders(200, 0);
                OutputStream out = exchange.getResponseBody();
                out.write(responseSB.toString().getBytes());
                out.close();
            } catch (Exception e) { e.printStackTrace(); };
        };
        private boolean isValidPath(String path)
        {
            if (!path.startsWith(getMealBookingsInMonthPath)) return false;
            int dateLength = "yyyy-mm-dd".length();
            if (path.length() != (getMealBookingsInMonthPath.length() + dateLength))
                return false;
            return true;
        };
    }
    static class FlushHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange exchange)
        {
            try
            {
                bookings.clear();
                String responseString = "[FlushHandler] Server bookings cleared";
                System.out.println(responseString);

                exchange.sendResponseHeaders(200, responseString.length());
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(responseString.getBytes());
                responseBody.close();
            } catch (Exception e) { e.printStackTrace(); };
        };
    };
};
