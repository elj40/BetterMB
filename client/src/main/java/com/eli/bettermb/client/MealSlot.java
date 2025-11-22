package com.eli.bettermb.client;
public class MealSlot
{
    public char code; public String description;

    public static char BREAKFAST = 'B';
    public static char LUNCH     = 'L';
    public static char DINNER    = 'D';
    public String cliDisplayString()
    {
        return description;
    }
}
