package com.eli.bettermb.client;

import java.net.URI;

public record Configuration (
        boolean debugging,
        URI entry,
        URI target,
        URI base,
        URI quotaSummary,
        URI quotaIncreaseRequest,
        URI quotaDecreaseRequest,
        URI cobIncreaseRequest,
        URI cobDecreaseRequest,
        URI bookedQuery,
        URI slotsQuery,
        URI facilitiesQuery,
        URI optionsQuery,
        URI bookRequest,
        URI cancelRequest
        )
{
    public Configuration withDebug(boolean debug)
    {
        return new Configuration(
                debug,
                entry(),
                target(),
                base(),
                quotaSummary(),
                quotaIncreaseRequest(),
                quotaDecreaseRequest(),
                cobIncreaseRequest(),
                cobDecreaseRequest(),
                bookedQuery(),
                slotsQuery(),
                facilitiesQuery(),
                optionsQuery(),
                bookRequest(),
                cancelRequest()
                );
    }
    public static Configuration test = new Configuration(
            true,
            URI.create("http://127.0.0.1:8080/entry"),
            URI.create("http://127.0.0.1:8080/target"),
            URI.create("http://127.0.0.1:8080/"),
            URI.create("quota/how-much-money/"),
            URI.create("quota/increase/"),
            URI.create("quota/decrease/"),
            URI.create("cob/increase/"),
            URI.create("cob/decrease/"),
            URI.create("booked/what/do/I/eat/"),
            URI.create("slots/get-more/"),
            URI.create("facilities/get-more/"),
            URI.create("options/get-more/"),
            URI.create("book/get-more/"),
            URI.create("cancel/I/dont/want/it/")
            );
    public static Configuration release = new Configuration(
            false,
            URI.create("https://my.sun.ac.za/api/tracker?linkID=239&lang=en"),
            URI.create("https://web-apps.sun.ac.za"),
            URI.create("https://web-apps.sun.ac.za/student-meal-booking/spring/api/"),
            URI.create("get-quota-summary/en"),
            URI.create("increase-quota/en/"),
            URI.create("decrease-quota/en/"), // Guess
            URI.create("increase-cob/en/"),
            URI.create("decrease-cob/en/"), // Guess
            URI.create("get-meal-bookings-dto/en/"),
            URI.create("get-meal-slots-dto/"),
            URI.create("get-meal-slot-facilities/en/"),
            URI.create("get-meal-slot-facility-options/en/"),
            URI.create("store-meal-booking/en"),
            URI.create("cancel-meal-booking/en/")
            );
    public static Configuration devLive = Configuration.release.withDebug(true);
}
