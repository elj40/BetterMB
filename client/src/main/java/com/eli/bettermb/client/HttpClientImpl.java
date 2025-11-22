package com.eli.bettermb.client;

// CHATGPT
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

public class HttpClientImpl implements IHttpClient {
    private final HttpClient httpClient;

    public HttpClientImpl() {
        this.httpClient = HttpClient.newHttpClient(); // Create a new HttpClient instance
    }

    @Override
    public HttpResponse<String> send(HttpRequest request, BodyHandler<String> bodyHandler) throws IOException {
        try {
            return httpClient.send(request, bodyHandler); // Use the HttpClient to send the request
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            throw new IOException("Request was interrupted", e);
        }
    }
}

