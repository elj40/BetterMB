import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
//Cookie: NSC_MC_WT_xfc-bqqt.tvo.bd.ab_IUUQ=ffffffff91e0096a45525d5f4f58455e445a4a422d6c; jsessionid=CHw1X8Fgf4CPz1naOPPjRWCZ-ofXwFQSGXbXw5Rvm6yZANBvOKCF!-517264473
class Main
{
    static Gson gson = new Gson();

    static String test_url = "http://127.0.0.1:8080";
    static String cookies = "urmom;sixseven";
    static String sun_url = "https://web-apps.sun.ac.za";
    //static String cookies = "";

    public static void main(String[] args)
    {
        SeleniumGetSecurityCookies();
    };
    public void cli(String[] args)
    {
        int choice;
        String date = "2025-09-15";
        Scanner scanner = new Scanner(System.in);
        Client client = new Client(sun_url, cookies);
        MealBookingOptions mbo = new MealBookingOptions();

        mbo.mealDate = date;

        List<MealSlot> slots = client.getAvailableMealSlots(mbo.mealDate);
        for (int i = 0; i < slots.size(); i++)
        {
            System.out.print(i);
            System.out.print(". ");
            System.out.println(gson.toJson(slots.get(i)));
        };
        choice = scanner.nextInt();
        mbo.mealSlot = slots.get(choice).code;

        List<MealFacility> facilities =
            client.getAvailableMealFacilities(mbo.mealDate, mbo.mealSlot);
        for (int i = 0; i < facilities.size(); i++)
        {
            System.out.print(i);
            System.out.print(". ");
            System.out.println(gson.toJson(facilities.get(i)));
        };
        choice = scanner.nextInt();
        mbo.mealFacility = facilities.get(choice).code;

        List<MealInfo> options =
            client.getAvailableMeals(mbo.mealDate, mbo.mealSlot, mbo.mealFacility);
        for (int i = 0; i < options.size(); i++)
        {
            System.out.print(i);
            System.out.print(". ");
            System.out.println(gson.toJson(options.get(i)));
        };
        choice = scanner.nextInt();
        mbo.mealOption = options.get(choice).code;
        mbo.mealSession = options.get(choice).sessionId;

        System.out.print("How many days to book ahead?");
        mbo.advanceBookingDays = scanner.nextInt();
        System.out.println();

        List<MealBookingResponse> mbrs = client.book(mbo);
        for (int i = 0; i < mbrs.size(); i++)
        {
            System.out.print(i);
            System.out.print(". ");
            System.out.println(gson.toJson(mbrs.get(i)));
        };

        System.out.println("======================================================================");
        System.out.println("FINISHED");
        System.out.println("======================================================================");
    };
    static void SeleniumGetSecurityCookies()
    {
        String entryUrl = test_url;
        String securityCookies = Client.getSecurityCookiesBySignIn();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        Assertions.assertTrue(securityCookies != null);
        Assertions.assertTrue(securityCookies.contains("urmom"));
        Assertions.assertTrue(securityCookies.contains("sixseven"));
    };
    static void ClientGetAvailableMealSlots()
    {
        Client client = new Client(test_url, cookies);
        List<MealSlot> mealSlots;
        String date = "2025-01-01";
        MealBookingOptions mbo;
        mbo = new MealBookingOptions();
        mbo.mealDate = date;

        client.flushServer();
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertEquals(3, mealSlots.size());

        mbo.mealSlot = MealSlot.BREAKFAST;
        client.book(mbo);
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertEquals(2, mealSlots.size());

        mbo.mealSlot = MealSlot.LUNCH;
        client.book(mbo);
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertEquals(1, mealSlots.size());

        mbo.mealSlot = MealSlot.DINNER;
        client.book(mbo);
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertEquals(0, mealSlots.size());
    };
    static void ClientGetAvailableMealFacilities()
    {
        Client client = new Client(test_url, cookies);
        String date = "2025-01-01";
        char   slot = MealSlot.BREAKFAST;
        client.flushServer();

        List<MealFacility> mealFacs = client.getAvailableMealFacilities(date, slot);
        Assertions.assertEquals(7, mealFacs.size());
    };
    static void ClientGetAvailableMeals()
    {
        Client client = new Client(test_url, cookies);
        String date  = "2025-01-01";
        char slot    = MealSlot.BREAKFAST;
        int facility = MealFacility.MAJUBA;
        client.flushServer();

        List<MealInfo> mealOptions = client.getAvailableMeals(date, slot, facility);
        Assertions.assertEquals(25159, mealOptions.getLast().code);
    };
    static void ClientBookMeal()
    {
        Client client = new Client(test_url, cookies);
        String date  = "2025-01-01";
        char slot    = MealSlot.BREAKFAST;
        int facility = MealFacility.MAJUBA;
        MealBookingOptions mbo = new MealBookingOptions();
        List<MealBookingResponse> mbr;

        client.flushServer();
        mbo.mealDate = date;
        mbo.mealSlot = slot;
        mbo.mealFacility = facility;

        mbr = client.book(mbo);
        Assertions.assertEquals(1, mbr.size());
        Assertions.assertEquals("Booking successful", mbr.getFirst().bookingMessage);
        Assertions.assertEquals(mbo.mealDate, mbr.getFirst().bookingDate);

        mbr = client.book(mbo);
        Assertions.assertEquals(1, mbr.size());
        Assertions.assertEquals("Reservation already exists", mbr.getFirst().bookingMessage);
        Assertions.assertEquals(mbo.mealDate, mbr.getFirst().bookingDate);

        client.flushServer();
        mbo.advanceBookingDays = 3;
        mbr = client.book(mbo);
        Assertions.assertEquals(4, mbr.size());
        for (int i = 0; i < mbr.size(); i++)
        {
            Assertions.assertEquals("Booking successful", mbr.get(i).bookingMessage);
        };

        client.flushServer();
        mbo.advanceBookingDays = 0;
        mbo.mealDate = "2025-01-03";
        mbr = client.book(mbo);

        mbo.advanceBookingDays = 3;
        mbo.mealDate = date;
        mbr = client.book(mbo);
        Assertions.assertEquals(4, mbr.size());
        for (int i = 0; i < mbr.size(); i++)
        {
            if (i == 2) Assertions.assertEquals("Reservation already exists", mbr.get(i).bookingMessage);
            else Assertions.assertEquals("Booking successful", mbr.get(i).bookingMessage);
        };

    };
    static void ClientGetMealsBookedThisMonth()
    {
        Client client = new Client(test_url, cookies);
        String date  = "2025-01-01";
        char slot    = MealSlot.BREAKFAST;
        int facility = MealFacility.MAJUBA;
        MealBookingOptions mbo = new MealBookingOptions();

        client.flushServer();
        mbo.mealDate = "2025-01-01";
        mbo.mealSlot = MealSlot.LUNCH;
        client.book(mbo);
        mbo.mealDate = "2025-01-02";
        mbo.mealSlot = MealSlot.DINNER;
        mbo.advanceBookingDays = 5;
        client.book(mbo);

        List<Meal> meals = client.getMealsBookedInMonth(date);
        Assertions.assertEquals(1 + (1+5), meals.size());
    };
    static void ClientCancelMeal()
    {
        Client client = new Client(test_url, cookies);
        String date  = "2025-01-01";
        char slot    = MealSlot.BREAKFAST;
        int facility = MealFacility.MAJUBA;
        MealBookingOptions mbo = new MealBookingOptions();
        List<Meal> meals;

        client.flushServer();
        mbo.mealDate = "2025-01-01";
        mbo.mealSlot = MealSlot.LUNCH;
        client.book(mbo);
        mbo.mealDate = "2025-01-02";
        mbo.mealSlot = MealSlot.DINNER;
        mbo.advanceBookingDays = 5;
        client.book(mbo);

        meals = client.getMealsBookedInMonth(date);
        Assertions.assertEquals(1 + (1+5), meals.size());
        int numMealsBooked = meals.size();

        MealCancelResponse mcr = client.cancel(meals.getFirst().id);
        meals = client.getMealsBookedInMonth(date);
        Assertions.assertEquals(numMealsBooked-1, meals.size());

        mcr = client.cancel(9999); meals = client.getMealsBookedInMonth(date);
        Assertions.assertEquals(numMealsBooked-1, meals.size());
        Assertions.assertFalse(mcr.success);
    };
};
