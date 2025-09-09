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

import com.google.gson.Gson;

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
class Day
{
    public Meal breakfast;
    public Meal lunch;
    public Meal dinner;
};
class MockServer {
    static HttpServer server;
    static Gson       gson   = new Gson();
    static HashMap<String, Day> bookings = new HashMap<String, Day>();

    static String getMealSlotsPath = "/student-meal-booking/spring/api/get-meal-slots-dto/";
    public static void main(String[] args)
    {
        start();
    };
    public static void start()
    {
        try
        {
            // This represents https://web-apps.sun.ac.za/student-meal-booking/spring/api
            String serverAddress = "127.0.0.1";
            InetSocketAddress address = new InetSocketAddress(serverAddress, 8080);
            server = HttpServer.create();
            server.bind(address, 0);

            server.createContext("/",            new EchoHandler());
            server.createContext(getMealSlotsPath, new GetMealSlotsHandler());
            server.createContext("/flush",       new FlushHandler());

            server.start();
            System.out.println("[SERVER] Serving at: " + server.getAddress());
        } catch (Exception e) { e.printStackTrace(); };
    };
    public void stop()
    {
        System.out.println("[SERVER] Stopping");
        server.stop(0);
    };
    static class GetMealSlotsHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange exchange)
        {
            try
            {
                URI requestURI = exchange.getRequestURI();
                String requestPath = requestURI.getPath();
                System.out.println("[GetMealSlotsHandler] " + requestPath);
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
                    int start = getMealSlotsPath.length()-1;
                    int end   = start + "yyyy-mm-dd".length();
                    String date = requestPath.substring(start, end);

                    Day day = bookings.get(date);
                    boolean noBooking = day == null;
                    StringBuilder responseSB = new StringBuilder();
                    responseSB.append("[ ");
                    responseSB.append("{\"code\":\"0\",\"description\":\"Select\"}");
                    if (noBooking || day.breakfast == null)
                        responseSB.append(", {\"code\":\"B\",\"description\":\"Breakfast\"}");
                    if (noBooking || day.lunch == null)
                        responseSB.append(", {\"code\":\"L\",\"description\":\"Lunch\"}");
                    if (noBooking || day.dinner == null)
                        responseSB.append(", {\"code\":\"D\",\"description\":\"Dinner\"}");
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
    static class BookingHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange exchange)
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
            } catch (Exception e) { e.printStackTrace(); };
        };
    };
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
