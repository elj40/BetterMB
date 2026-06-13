package com.eli.bettermb.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import java.io.IOException;
import java.net.ConnectException;

class JsonParseTest
{
    @BeforeEach
    void setup()
    {
    };
    @Test
    void testMealBookingResponseBasic()
    {
        String json =
            """
            [{"bookingDate":"2026-06-20","bookingMessage":"Booking successful"}]
            """;
        assertDoesNotThrow(() -> Json.fromJsonList(json, MealBookingResponse.class));
    }
}
