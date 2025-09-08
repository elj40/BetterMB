import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import java.net.URI;

import java.io.IOException;
import java.net.ConnectException;

class Client
{
    HttpClient httpClient;
    String     linkPrefix = "https://127.0.0.1:8080";
    String     jsessionid = "abcdefg";
    String     NSC        = "1234567";
    Client()
    {
        httpClient = HttpClient.newBuilder().build();
    };
    Client(HttpClient _httpClient)
    {
        httpClient = _httpClient;
    };
    public void setLinkPrefix(String lp)
    {
        linkPrefix = lp;
    }
    public void setJSessionID(String jid)
    {
        jsessionid = jid;
    }
    public void setNSC(String nsc)
    {
        NSC = nsc;
    }
    public Meal[] getAllBookedMeals(String date)
    {
        return null;
    };
    public boolean book(Meal meal)
    {
        String mealJson = "mealJson";
        String linkSuffix = "/new-booking";
        linkSuffix = "/";
        //try
        //{
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(linkPrefix + linkSuffix))
                .POST(BodyPublishers.ofString(mealJson))
                .build();

            System.out.println("[CLIENT] Sending " + linkSuffix+": " + mealJson);
            httpClient.sendAsync(request, BodyHandlers.discarding());
        //}
        //catch (ConnectException e) { e.printStackTrace(); }
        //catch (IOException e) { e.printStackTrace(); }
        //catch (InterruptedException e) { e.printStackTrace(); }
        return true;
    };
    // Flushes the test server
    // DO NOT USE OUTSIDE OF TESTING!
    public void _flushServer()
    {
    };
};
