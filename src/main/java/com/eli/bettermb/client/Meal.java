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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("Meal{");
        sb.append("id=").append(id);
        sb.append(", canModify=").append(canModify);
        sb.append(", title=").append(title == null ? "null" : "\"" + title + "\"");
        sb.append(", start=").append(start == null ? "null" : "\"" + start + "\"");
        sb.append(", description=").append(description == null ? "null" : "\"" + description + "\"");
        sb.append(", facility=").append(facility == null ? "null" : "\"" + facility + "\"");
        sb.append(", mealTime=").append(mealTime == null ? "null" : "\"" + mealTime + "\"");
        sb.append(", mealCost=").append(mealCost == null ? "null" : "\"" + mealCost + "\"");
        sb.append(", mealSlot=").append(mealSlot);
        sb.append(", backgroundColor=").append(backgroundColor == null ? "null" : "\"" + backgroundColor + "\"");
        sb.append(", borderColor=").append(borderColor == null ? "null" : "\"" + borderColor + "\"");
        sb.append('}');
        return sb.toString();
    }

}
