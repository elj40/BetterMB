import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.net.URI;
import java.net.InetSocketAddress;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
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
class MockServer {
    HttpServer server;
    Meal[] meals;
    public void start()
    {
        try
        {
            // This represents https://web-apps.sun.ac.za/student-meal-booking/spring/api
            String serverAddress = "127.0.0.1";
            InetSocketAddress address = new InetSocketAddress(serverAddress, 8080);
            server = HttpServer.create();
            server.bind(address, 0);

            server.createContext("/", new EchoHandler());
            server.createContext("/new-booking", new BookingHandler());
            server.createContext("/flush", new EchoHandler());

            server.start();
            System.out.println("[SERVER] Serving at: " + server.getAddress());
        } catch (Exception e) { e.printStackTrace(); };
    };
    static class BookingHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange exchange)
        {
            try
            {
                if(!"POST".equals(exchange.getRequestMethod()))
                {
                    System.out.println("[SERVER; BookingHandler] Non-POST method made in booking");
                    exchange.sendResponseHeaders(405, 0);
                };
                InputStream inputStream = exchange.getRequestBody();
                String requestBody = new String(inputStream.readAllBytes());
                System.out.println("[SERVER] BookingHandler received: " + requestBody);
                exchange.sendResponseHeaders(200, 0);
            } catch (Exception e) { e.printStackTrace(); };
        };
    };
    public void stop()
    {
        System.out.println("[SERVER] Stopping");
        server.stop(0);
    };
};
