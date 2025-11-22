package com.eli.bettermb.client;
public class MealBookingResponse
{
    public String bookingDate;
    public String bookingMessage;
    public String cliDisplayString()
    {
        return String.format("[%1$s] %2$s", bookingDate, bookingMessage);
    }
}
