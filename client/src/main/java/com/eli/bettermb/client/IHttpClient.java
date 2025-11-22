package com.eli.bettermb.client;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.io.IOException;
public interface IHttpClient
{
    HttpResponse<String> send(HttpRequest request, BodyHandler<String> bodyHandler) throws IOException;
};
