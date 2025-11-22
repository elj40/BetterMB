package com.eli.bettermb.client;
public class MealOption
{
    public int code;
    public String description;
    public String cost;
    public int sessionId;
    public String sessionStart;
    public String sessionEnd;
    public boolean bookable;
    public String reason;

    public String cliDisplayString()
    {
        return String.format("%-40s %s", description, cost);
    }
};
