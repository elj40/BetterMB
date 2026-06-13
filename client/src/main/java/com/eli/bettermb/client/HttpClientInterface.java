package com.eli.bettermb.client;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.io.IOException;
public interface HttpClientInterface
{
    <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> bodyHandler)
            throws IOException, InterruptedException;
};
