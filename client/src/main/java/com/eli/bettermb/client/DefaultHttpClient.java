package com.eli.bettermb.client;

// CHATGPT
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

public class DefaultHttpClient implements HttpClientInterface {
    private final HttpClient httpClient;

    public DefaultHttpClient() {
        this.httpClient = HttpClient.newHttpClient(); // Create a new HttpClient instance
    }

    @Override
    public <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> bodyHandler)
        throws IOException, InterruptedException
    {
        return httpClient.send(request, bodyHandler);
    }
}

