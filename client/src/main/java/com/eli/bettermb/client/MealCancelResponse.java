package com.eli.bettermb.client;
public class MealCancelResponse
{
    public boolean success;
    public String message;
    public String cliDisplayString()
    {
        if (success) return "Meal cancelled successfully";
        else return "Meal could not be cancelled: " + message;
    }
}
