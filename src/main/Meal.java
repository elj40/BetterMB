import java.util.HashMap;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
// {"code":"0","description":"Select"},
// {"code":"B","description":"Breakfast"},
// {"code":"L","description":"Lunch"},
// {"code":"D","description":"Dinner"}
class MealSlot
{
    char code;
    String description;

    static char BREAKFAST = 'B';
    static char LUNCH     = 'L';
    static char DINNER    = 'D';
    public String cliDisplayString()
    {
        return description;
    }
}
class MealCancelResponse
{
    boolean success;
    String message;
    public String cliDisplayString()
    {
        if (success) return "Meal cancelled successfully";
        else return "Meal could not be cancelled: " + message;
    }
}
class MealBookingResponse
{
    String bookingDate;
    String bookingMessage;
    public String cliDisplayString()
    {
        return String.format("[%1$s] %2$s", bookingDate, bookingMessage);
    }
}
class MealOption
{
    int code;
    String description;
    String cost;
    int sessionId;
    String sessionStart;
    String sessionEnd;
    boolean bookable;
    String reason;

    public String cliDisplayString()
    {
        return String.format("%-40s %s", description, cost);
    }
};
class MealFacility
{
    int code;
    String description;

    static int MAJUBA = 22;
    public String cliDisplayString()
    {
        return description;
    }
}
class MealBookingOptions
{
    public String mealDate;
    public char mealSlot;
    public int mealFacility;
    public int mealOption;
    public int mealSession;
    public int advanceBookingDays;
    public boolean excludeWeekends = true;
    public String cliDisplayString()
    {
        return "TODO";
    }
};
class MealMaps
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
class Meal
{
    boolean canModify;
    String  title;
    String  start;
    String  description;
    String  facility;
    String  mealTime;
    String  mealCost;
    char    mealSlot;
    String  backgroundColor;
    String  borderColor;
    int     id;

    static String cliDisplayHeaders()
    {
        return String.format("##. %-6s [%s] %-10s %-20s %s",
                "ID",
                "yyyy-mm-dd Day",
                "Title",
                "Facility",
                "Description");
    }
    public String cliDisplayString()
    {
        String date = start.substring(0, "yyyy-mm-dd".length());
        String day = LocalDate.parse(date)
            .getDayOfWeek()
            .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        return String.format("%-6d [%s %s] %-10s %-20s %s", id, date, day, title, facility, description);
    }

    static Meal fromMBO(MealBookingOptions mbo, int id)
    {
        Meal meal = new Meal();
        meal.id = id;
        meal.mealSlot = mbo.mealSlot;
        meal.facility = MealMaps.CodeFacilityMap.get(mbo.mealFacility);
        meal.description = MealMaps.CodeDescriptionMap.get(mbo.mealOption);
        meal.mealCost = MealMaps.CodeCostMap.get(mbo.mealOption);
        meal.title = MealMaps.CodeTitleMap.get(mbo.mealSlot);

        return meal;
    };
}
