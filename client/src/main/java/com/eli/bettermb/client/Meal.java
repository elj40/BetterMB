package com.eli.bettermb.client;
public class Meal
{
    public boolean canModify;
    public String  title;
    public String  start;
    public String  description;
    public String  facility;
    public String  mealTime;
    public String  mealCost;
    public char    mealSlot;
    public String  backgroundColor;
    public String  borderColor;
    public int     id;

    static Meal fromMBO(MealBookingOptions mbo, String start, int id)
    {
        Meal meal = new Meal();
        meal.id = id;
        meal.mealSlot = mbo.mealSlot;
        meal.facility = MealMaps.CodeFacilityMap.get(mbo.mealFacility);
        meal.description = MealMaps.CodeDescriptionMap.get(mbo.mealOption);
        meal.mealCost = MealMaps.CodeCostMap.get(mbo.mealOption);
        meal.title = MealMaps.CodeTitleMap.get(mbo.mealSlot);

        if (mbo.mealDate == null) meal.start = start;
        else meal.start = mbo.mealDate;

        return meal;
    };
}
