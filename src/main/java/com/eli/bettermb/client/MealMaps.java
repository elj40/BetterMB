package com.eli.bettermb.client;
import java.util.HashMap;
public class MealMaps
{
    static HashMap<Integer, String> CodeFacilityMap = new HashMap<>();
    static HashMap<Integer, String> CodeDescriptionMap = new HashMap<>();
    static HashMap<Integer, String> CodeCostMap = new HashMap<>();
    static HashMap<Character, String> CodeTitleMap = new HashMap<>();

    static {
        // Populate CodeFacilityMap
        CodeFacilityMap.put(0, "Select");
        CodeFacilityMap.put(8, "Minerva");
        CodeFacilityMap.put(9, "Dagbreek");
        CodeFacilityMap.put(11, "Huis Ten Bosch");
        CodeFacilityMap.put(20, "Lydia");
        CodeFacilityMap.put(22, "Majuba");
        CodeFacilityMap.put(27, "VictoriaHub");

        CodeDescriptionMap.put(0     ,"Select");
        CodeDescriptionMap.put(159523,"Take Away Prepacked Meal");
        CodeDescriptionMap.put(160095,"Vegetarion");
        CodeDescriptionMap.put(160667,"Chicken Meal");
        CodeDescriptionMap.put(161239,"Halal Friendly Meal");
        CodeDescriptionMap.put(161811,"Lactose Free Meal");
        CodeDescriptionMap.put(195628,"Farmhouse Breakfast");
        CodeDescriptionMap.put(195968,"Breakfast Wrap Egg Bacon Tomato");
        CodeDescriptionMap.put(196308,"Omelet Option");
        CodeDescriptionMap.put(196648,"Cheesegriller Option");
        CodeDescriptionMap.put(196988,"Breakfast Waffle Option");
        CodeDescriptionMap.put(25159 ,"Standard Meal");

        CodeCostMap.put(0     ,null);
        CodeCostMap.put(159523,"R44.00");
        CodeCostMap.put(160095,"R21.79");
        CodeCostMap.put(160667,"R18.79");
        CodeCostMap.put(161239,"R18.79");
        CodeCostMap.put(161811,"R18.79");
        CodeCostMap.put(195628,"R71.00");
        CodeCostMap.put(195968,"R58.00");
        CodeCostMap.put(196308,"R60.00");
        CodeCostMap.put(196648,"R67.00");
        CodeCostMap.put(196988,"R58.00");
        CodeCostMap.put(25159 ,"R18.79");

        CodeTitleMap.put('B', "Breakfast");
        CodeTitleMap.put('L', "Lunch");
        CodeTitleMap.put('D', "Dinner");
    };
};
