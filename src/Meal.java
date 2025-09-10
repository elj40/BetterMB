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
}
class MealCancelResponse
{
    boolean success;
    String message;
}
class MealBookingResponse
{
    String bookingDate;
    String bookingMessage;
}
class MealInfo
{
    int code;
    String description;
    String cost;
    int sessionId;
    String sessionStart;
    String sessionEnd;
    boolean bookable;
    String reason;
};
class MealFacility
{
    int code;
    String description;

    static int MAJUBA = 22;
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

    static Meal fromMBO(MealBookingOptions mbo, int id)
    {
        Meal meal = new Meal();
        meal.id = id;
        meal.mealSlot = mbo.mealSlot;
        //meal.facility = Meal.CodeFacilityMap.get(mbo.mealFacility);
        //meal.description = Meal.CodeDescriptionMap.get(mbo.mealOption);
        //meal.mealCost = Meal.CodeCostMap.get(mbo.mealOption);
        //meal.title = Meal.CodeTitleMap.get(mbo.mealOption, mbo.mealSession);

        return meal;
    };
}
