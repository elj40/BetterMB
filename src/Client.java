import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import java.net.URI;

import java.io.IOException;
import java.net.ConnectException;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;

class Client
{
    HttpClient httpClient;
    Gson gson = new Gson();
    String     linkPrefix = "http://127.0.0.1:8080";
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
        String mealJson = gson.toJson(meal);
        String linkSuffix = "/new-booking";
        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(linkPrefix + linkSuffix))
                .POST(BodyPublishers.ofString(mealJson))
                .timeout(Duration.ofSeconds(1))
                .build();

            System.out.println("[CLIENT] Sending " + linkSuffix+": " + mealJson);
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[CLIENT] Status code: " + response.statusCode());
            System.out.println("[CLIENT] Body       : " + response.body());

            if  (response.body().equals("no")) return false;
        }
        catch (ConnectException e) { e.printStackTrace(); }
        //catch (TimeoutException e) { System.out.println("[CLIENT] Timed out"); }
        catch (IOException e) { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return true;
    };
    // Flushes the test server
    // DO NOT USE OUTSIDE OF TESTING!
    public void _flushServer()
    {
        String linkSuffix = "/flush";
        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(linkPrefix + linkSuffix))
                .timeout(Duration.ofSeconds(1))
                .build();

            System.out.println("[CLIENT] Sending " + linkSuffix);
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("[CLIENT] Status code: " + response.statusCode());
            System.out.println("[CLIENT] Body       : " + response.body());
        }
        catch (ConnectException e) { e.printStackTrace(); }
        //catch (TimeoutException e) { System.out.println("[CLIENT] Timed out"); }
        catch (IOException e) { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    };
};
