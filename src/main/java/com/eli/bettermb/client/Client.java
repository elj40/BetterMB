package com.eli.bettermb.client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.ConnectException;
import java.net.URI;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.lang.reflect.Type;

import java.io.IOException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.logging.Level;
import java.util.logging.Logger;

class Json
{
    public static final Gson gson = new Gson();
    static <T> List<T> fromJsonList(String json, Class<T> type)
    {
        return gson.fromJson(json, TypeToken.getParameterized(List.class, type).getType());
    }
}
// TODO: quota stuff
public class Client
{
    // NEW API, the changing API goes to show that we can't just hardcode things anymore
    // It will need to be hardcoded SOMEWHERE, but preferrably in some config at runtime

    public Configuration config = Configuration.devLive;

    public HttpClientInterface ihttpClient;
    String securityCookies = "default=cookie";
    public List<Exception> asyncExceptions = new ArrayList<>();

    HttpRequest.Builder requestBuilder =
        HttpRequest.newBuilder()
        .timeout(Duration.ofSeconds(15));


    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Client()
    {
    };
    public Client(HttpClientInterface _ihttpClient)
    {
        ihttpClient = _ihttpClient;
    }
    public void setCookies(String newCookies)
    {
        securityCookies = newCookies;
        requestBuilder = requestBuilder.setHeader("Cookie", newCookies);
    };
    public String getCookies()
    {
        return securityCookies;
    };
    public void setHttpClient(HttpClientInterface newHttpClient)
    {
        ihttpClient = newHttpClient;
    };
    void debug(String msg)
    {
        if (config.debugging()) System.out.println(msg);
    };
    boolean ensureGoodResponse(HttpResponse<String> response)
            throws IOException, SecurityFailedException
    {
        if (response.body().contains("Single Sign-on")) throw new SecurityFailedException();
        if (response.body().contains("html")) throw new SecurityFailedException();
        if (response.statusCode() != 200) throw new IOException("Status code not 200!");

        return true;
    };

    public String getSecurityCookiesBySignIn()
            throws IOException, InterruptedException
    {
        // TODO: IMPORTANT, this usually takes quite a while so the user is just left with
        // a long empty load time. This setup should be handled better
        WebDriverManager.chromedriver().setup();

        if (config.debugging()) Logger.getLogger("org.openqa.selenium").setLevel(Level.INFO);
        else Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);

        ChromeOptions options = new ChromeOptions();
        // TODO: IMPORTANT This profile is hard-coded and wont work anywhere else
        // if (config.debugging()) options.addArguments("user-data-dir=/home/eli/.config/google-chrome/");

        WebDriver driver = new ChromeDriver(options);

        String securityCookies = "";

        try { driver.get(config.entry().toString()); }
        catch (Exception e) { throw e; }
        Thread.sleep(500);
        String currentUrl = driver.getCurrentUrl();
        while (true)
        {
            Thread.sleep(1000);
            if (driver.getCurrentUrl().startsWith(config.target().toString()))
            {
                if (config.debugging()) System.out.println("[getSecurityCookiesBySignIn] On target page");
                break;
            };
        };

        Set<Cookie> cookies = driver.manage().getCookies();
        boolean first = true;
        for (Cookie cookie : cookies) {
            if (!first) securityCookies = securityCookies + ";";
            else first = false;
            securityCookies = securityCookies + cookie.getName();
            securityCookies = securityCookies + "=";
            securityCookies = securityCookies + cookie.getValue();
        }
        if (config.debugging()) System.out.println("[getSecurityCookiesBySignIn] Security cookies: " + securityCookies);
        driver.quit();
        return securityCookies;
    }

    <T> HttpResponse<T> sendHTTP(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler)
        throws IOException, InterruptedException
    {
        debug("[Client] Sending    : " + request.uri().toString());
        HttpResponse<T> response = ihttpClient.send(request, responseBodyHandler);
        debug("[Client] Status code: " + response.statusCode());
        debug("[Client] Response   : " + response.body());
        return response;
    }

    <T> List<T> requestDataList (URI requestURI, Class<T> dataClass)
            throws IOException, InterruptedException, SecurityFailedException
    {
        HttpRequest request = requestBuilder
            .uri(requestURI) // -> assuming valid URI from this
            .GET()           // -> as of now all calls to this function want GET, only booking wants POST
            .build();
        HttpResponse<String> response = sendHTTP(request, BodyHandlers.ofString());
        ensureGoodResponse(response);
        return Json.fromJsonList(response.body(), dataClass);
    }

    public QuotaSummary getQuotaSummary()
            throws IOException, InterruptedException, SecurityFailedException
    {
        URI uri = config.base().resolve(config.quotaSummary());
        HttpRequest request = requestBuilder
            .uri(uri)
            .build();
        HttpResponse<String> response = sendHTTP(request, BodyHandlers.ofString());
        ensureGoodResponse(response);

        QuotaSummary quotaSummary = Json.gson.fromJson(response.body(), QuotaSummary.class);
        return quotaSummary;
    }

    public SimpleResponse quotaChangeHelper(URI changeEndpoint, int amountInCents)
            throws IOException, InterruptedException, SecurityFailedException
    {
        URI uri = config.base()
            .resolve(changeEndpoint)
            .resolve(String.valueOf(amountInCents));

        HttpRequest request = requestBuilder
            .uri(uri)
            .build();

        HttpResponse<String> response = sendHTTP(request, BodyHandlers.ofString());
        ensureGoodResponse(response);

        SimpleResponse quotaResponse = Json.gson.fromJson(response.body(), SimpleResponse.class);
        return quotaResponse;
    }

    public SimpleResponse quotaIncrease(int amountInCents)
            throws IOException, InterruptedException, SecurityFailedException
    {
        return quotaChangeHelper(config.quotaIncreaseRequest(), amountInCents);
    }

    public SimpleResponse quotaDecrease(int amountInCents)
            throws IOException, InterruptedException, SecurityFailedException
    {
        return quotaChangeHelper(config.quotaDecreaseRequest(), amountInCents);
    }

    public SimpleResponse cobIncrease(int amountInCents)
            throws IOException, InterruptedException, SecurityFailedException
    {
        return quotaChangeHelper(config.cobIncreaseRequest(), amountInCents);
    }

    public SimpleResponse cobDecrease(int amountInCents)
            throws IOException, InterruptedException, SecurityFailedException
    {
        return quotaChangeHelper(config.cobDecreaseRequest(), amountInCents);
    }


    public List<MealSlot> getAvailableMealSlots(String date)
            throws IOException, InterruptedException, SecurityFailedException
    {
        URI uri = config.base()
            .resolve(config.slotsQuery())
            .resolve(date + "/")
            .resolve("en");

        return requestDataList (uri, MealSlot.class)
            .stream()
            .dropWhile(m -> m.code == '0')
            .toList();
    }

    public List<MealFacility> getAvailableMealFacilities(String date, char slot)
            throws IOException, InterruptedException, SecurityFailedException
    {
        URI uri = config.base()
            .resolve(config.facilitiesQuery())
            .resolve(date + "/")
            .resolve(String.valueOf(slot) + "/");

        return requestDataList (
                uri,
                MealFacility.class
            ).stream()
            .dropWhile(m -> m.code == 0)
            .toList();
    }
    public List<MealOption> getMealOptions(String date, char slot, int facility)
            throws IOException, InterruptedException, SecurityFailedException
    {
        URI uri = config.base()
            .resolve(config.optionsQuery())
            .resolve(date + "/")
            .resolve(String.valueOf(slot) + "/")
            .resolve(String.valueOf(facility) + "/");

        return requestDataList (
                uri,
                MealOption.class
            ).stream()
            .dropWhile(m -> m.code == 0)
            .toList();
    }
    public List<MealBookingResponse> book(MealBookingOptions meal)
            throws IOException, InterruptedException, SecurityFailedException
    {
        String mealJson = Json.gson.toJson(meal);
        URI uri = config.base()
            .resolve(config.bookRequest());

        HttpRequest request = requestBuilder
            .uri(uri)
            .POST(BodyPublishers.ofString(mealJson))
            .setHeader("Content-Type", "application/json;charset=utf-8")
            .build();

        HttpResponse<String> response = sendHTTP(request, BodyHandlers.ofString());
        ensureGoodResponse(response);

        return Json.fromJsonList(response.body(), MealBookingResponse.class);
    }

    public MealCancelResponse cancel(int mealId)
            throws IOException, InterruptedException, SecurityFailedException
    {
        URI uri = config.base()
            .resolve(config.cancelRequest())
            .resolve(String.valueOf(mealId));

        HttpRequest request = requestBuilder
            .uri(uri)
            .build();

        HttpResponse<String> response = sendHTTP(request, BodyHandlers.ofString());
        ensureGoodResponse(response);

        return Json.gson.fromJson(response.body(), MealCancelResponse.class);
    }

    public CompletableFuture<List<Meal>> getMealsBookedInMonthAsync(String date)
    {
        return CompletableFuture.supplyAsync(() -> {
            try { return getMealsBookedInMonth(date); }
            catch (Exception ex)
            {
                if (config.debugging()) ex.printStackTrace();
                asyncExceptions.add(ex);
                return null;
            }
        });
    };
    public List<Meal> getMealsBookedInMonth(String date)
            throws IOException, InterruptedException
    {
        // suffixSB.append("/student-meal-booking/spring/api/get-meal-bookings-dto/en/");
        URI uri = config.base()
            .resolve(config.bookedQuery())
            .resolve(date + "/");

        return requestDataList(uri, Meal.class);
    };
};
