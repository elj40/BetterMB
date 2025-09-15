import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.time.Year;
import java.time.LocalDate;

import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

import com.google.gson.Gson;
//Cookie: NSC_MC_WT_xfc-bqqt.tvo.bd.ab_IUUQ=ffffffff91e0096a45525d5f4f58455e445a4a422d6c; jsessionid=CHw1X8Fgf4CPz1naOPPjRWCZ-ofXwFQSGXbXw5Rvm6yZANBvOKCF!-517264473
class Main
{
    static Gson gson = new Gson();

    static String test_url = "http://127.0.0.1:8080";
    static String test_server_cookies = Common.securityCookies;
    static String test_entry_url = test_url + "/sign-in";
    static String test_target_url = test_url + "/target";

    static String sun_url = "https://web-apps.sun.ac.za";
    static String mysun_link = "https://my.sun.ac.za/tracker?linkID=239&lang=en";
    static String sun_entry_url = "https://my.sun.ac.za/tracker?linkID=239&lang=en";
    static String sun_target_url = sun_url;

    static String cookies = "jsessionid=tyZIITSsnd-c2FNMhuhlU8B2rNOa_EQyZwu2X8RR-r-LP7r-5U-c!2013719654;_pk_id.325.";

    public static void main(String[] args) throws IOException,InterruptedException
    {
        ClientGetMealsBookedThisMonth();
        //Scanner scanner = new Scanner(System.in);

        //if (args.length > 0)
        //{
        //    File file = new File(args[0]);
        //    try {scanner = new Scanner(file);}
        //    catch (FileNotFoundException e) { scanner = new Scanner(System.in); }
        //    catch (Exception e) { e.printStackTrace(); };
        //}
        //Client  client  = new Client(sun_url, cookies);
        //String command = null;
        //boolean success = true;

        //help();
        //while (!"quit".equals(command))
        //{
        //    System.out.print("> ");
        //    command = scanner.nextLine();
        //    command = command.trim().toLowerCase();
        //    if (command.equals("book"))
        //    {
        //        success = book(scanner, client);
        //        if (!success) System.out.println("Security check failed. See \"signin\" command");
        //    }
        //    else if (command.equals("show"))
        //    {
        //        success = show(scanner, client);
        //        if (!success) System.out.println("Security check failed. See \"signin\" command");
        //    }
        //    else if (command.equals("cancel"))
        //    {
        //        success = cancel(scanner, client);
        //        if (!success) System.out.println("Security check failed. See \"signin\" command");
        //    }
        //    else if (command.equals("signin"))
        //    {
        //        success = signin(client, sun_entry_url, sun_target_url);
        //        if (!success) System.out.println("Sign in failed, please report this.");
        //    }
        //    else if (command.equals("help"))   help();
        //};
    };
    static void help()
    {
        System.out.println("Commands:");
        System.out.println("\tsignin - sign in to pass security checks");
        System.out.println("\tbook   - make a new meal booking");
        System.out.println("\tshow   - show meal bookings in a certain month");
        System.out.println("\tcancel - cancel a meal booking using a meal ID");
        System.out.println("\tquit   - quits the application");
        System.out.println("\thelp   - displays this help command");
    };
    static public boolean cancel(Scanner scanner, Client client) throws IOException,InterruptedException
    {
        int id = nextIntRanged(scanner, "Enter a meal ID:", 0, 9999999);
        System.out.println("Sending cancel request for meal ID: " + id);
        MealCancelResponse mcr = client.cancel(id);
        if (mcr == null) return false;
        System.out.println(mcr.cliDisplayString());
        return true;
    };
    static public boolean signin(Client client, String entryUrl, String targetUrl) throws IOException,InterruptedException
    {
        cookies = Client.getSecurityCookiesBySignIn(entryUrl, targetUrl);
        if (cookies == null)
        {
            System.out.println("Something went wrong on sign in. Please report.");
            System.exit(0);
        };
        client.setCookies(cookies);
        return true;
    };
    static public boolean show(Scanner scanner, Client client) throws IOException,InterruptedException
    {
        int currentYear = Year.now().getValue();
        int currentMonth = LocalDate.now().getMonthValue();

        String currentYearString = String.valueOf(currentYear);
        String currentMonthString = String.format("%02d", currentMonth);

        System.out.println(
                "Enter the month (yyyy-mm) " +
                "[Default: " + currentYearString + "-" + currentMonthString +
                "]:");
        String date = null;
        while (date == null)
        {
            String line = scanner.nextLine();
            line = line.strip();
            if (line.length() == 0) { line = currentYearString + "-" + currentMonthString; };
            if      (!Character.isDigit(line.charAt(0))) date = null; // y
            else if (!Character.isDigit(line.charAt(1))) date = null; // y
            else if (!Character.isDigit(line.charAt(2))) date = null; // y
            else if (!Character.isDigit(line.charAt(3))) date = null; // y
            else if (line.charAt(4) != '-')              date = null; // -
            else if (!Character.isDigit(line.charAt(5))) date = null; // m
            else if (!Character.isDigit(line.charAt(6))) date = null; // m
            else date = line;
        };
        date = date + "-01";

        // TODO: make this async and cache the results
        System.out.println("Sending request for meals in month of: " + date);
        List<Meal> meals = client.getMealsBookedInMonth(date);
        if (meals == null) return false;

        meals.sort((m1, m2) -> m1.start.compareTo(m2.start));

        System.out.println(Meal.cliDisplayHeaders());
        for (int i = 0; i < meals.size(); i++)
        {
            System.out.print(String.format("%02d. ",i));
            System.out.println(meals.get(i).cliDisplayString());
        };

        return true;
    };
    static public boolean book(Scanner scanner, Client client) throws IOException,InterruptedException
    {
        int choice = -1;
        String date = null;
        String today = LocalDate.now().format(client.dateFormatter);
        MealBookingOptions mbo = new MealBookingOptions();

        while (date == null)
        {
            System.out.println("Enter a date to book on (yyyy-mm-dd) [Default: "+today+"]:");
            date = nextDateWithDefault(scanner.nextLine(), today);
        }
        mbo.mealDate = date;

        List<MealSlot> slots = client.getAvailableMealSlots(mbo.mealDate);
        if (slots == null) return false;
        for (int i = 0; i < slots.size(); i++)
        {
            System.out.print(i);
            System.out.print(". ");
            System.out.println(slots.get(i).cliDisplayString());
        };
        choice = nextIntRanged(scanner, "Select a meal slot: ", 0, slots.size());
        mbo.mealSlot = slots.get(choice).code;

        List<MealFacility> facilities =
            client.getAvailableMealFacilities(mbo.mealDate, mbo.mealSlot);
        if (facilities == null) return false;
        for (int i = 0; i < facilities.size(); i++)
        {
            System.out.print(i);
            System.out.print(". ");
            System.out.println(facilities.get(i).cliDisplayString());
        };
        choice = nextIntRanged(scanner, "Select a meal facility: ", 0, facilities.size());
        mbo.mealFacility = facilities.get(choice).code;

        List<MealInfo> options =
            client.getAvailableMeals(mbo.mealDate, mbo.mealSlot, mbo.mealFacility);
        if (options == null) return false;
        for (int i = 0; i < options.size(); i++)
        {
            System.out.print(i);
            System.out.print(". ");
            System.out.println(options.get(i).cliDisplayString());
        };
        choice = nextIntRanged(scanner, "Select a meal option: ", 0, options.size());
        mbo.mealOption = options.get(choice).code;
        mbo.mealSession = options.get(choice).sessionId;

        mbo.advanceBookingDays = nextIntRanged(scanner, "How many days to book ahead? ", 0, 31);
        System.out.println();

        System.out.println("Sending request to book meal");
        List<MealBookingResponse> mbrs = client.book(mbo);
        if (options == null) return false;
        for (int i = 0; i < mbrs.size(); i++)
        {
            System.out.print(i);
            System.out.print(". ");
            System.out.println(mbrs.get(i).cliDisplayString());
        };

        System.out.println("======================================================================");
        System.out.println("FINISHED");
        System.out.println("======================================================================");
        return true;
    };
    static public String nextDateWithDefault(String d, String default_date)
    {
        String date = d.strip();
        if (date.length() == 0) return default_date;
        if (!Character.isDigit(date.charAt(0))) return null; // y
        if (!Character.isDigit(date.charAt(1))) return null; // y
        if (!Character.isDigit(date.charAt(2))) return null; // y
        if (!Character.isDigit(date.charAt(3))) return null; // y
        if (date.charAt(4) != '-')              return null; // -
        if (!Character.isDigit(date.charAt(5))) return null; // m
        if (!Character.isDigit(date.charAt(6))) return null; // m
        if (date.charAt(7) != '-')              return null; // -
        if (!Character.isDigit(date.charAt(8))) return null; // d
        if (!Character.isDigit(date.charAt(9))) return null; // d
        return date;
    };
    static public String parseDate(String d)
    {
        String date = d.strip();
        if (!Character.isDigit(date.charAt(0))) return null; // y
        if (!Character.isDigit(date.charAt(1))) return null; // y
        if (!Character.isDigit(date.charAt(2))) return null; // y
        if (!Character.isDigit(date.charAt(3))) return null; // y
        if (date.charAt(4) != '-')              return null; // -
        if (!Character.isDigit(date.charAt(5))) return null; // m
        if (!Character.isDigit(date.charAt(6))) return null; // m
        if (date.charAt(7) != '-')              return null; // -
        if (!Character.isDigit(date.charAt(8))) return null; // d
        if (!Character.isDigit(date.charAt(9))) return null; // d
        return date;
    };
    // Written by ChatGPT
    static public int nextIntWithDefault(Scanner scanner, int default_int)
    {
        String input = scanner.nextLine();

        int value;
        if (input.isEmpty()) {
            value = default_int; // Use default value if input is empty
        } else {
            try {
                value = Integer.parseInt(input); // Try to parse the input to an integer
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Using default value: " + default_int);
                value = default_int; // Use default value if parsing fails
            }
        }
        return value;
    };
    static public int nextIntRanged(Scanner scanner, String msg, int min, int max)
    {
        int choice = min-1;
        while (choice < min || choice > max)
        {
            System.out.println(msg);
            try { choice = scanner.nextInt(); }
            catch (InputMismatchException e) { scanner.next(); continue; }
            catch (Exception e) { e.printStackTrace(); }
        };
        return choice;
    };
    static void SeleniumGetSecurityCookies()
    {
        String entryUrl = test_url + "/sign-in";
        String targetUrl = test_url + "/target";
        String securityCookies = Client.getSecurityCookiesBySignIn(entryUrl, targetUrl);
        Assertions.assertTrue(securityCookies != null);
        Assertions.assertTrue(securityCookies.contains("urmom"));
        Assertions.assertTrue(securityCookies.contains("sixseven"));
    };
    static void ClientGetAvailableMealSlots() throws IOException,InterruptedException
    {
        Client client = new Client(test_url, test_server_cookies);
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

        client.setCookies("incorrect cookie");
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertNull(mealSlots);
    };
    static void ClientGetAvailableMealFacilities() throws IOException, InterruptedException
    {
        Client client = new Client(test_url, test_server_cookies);
        String date = "2025-01-01";
        char   slot = MealSlot.BREAKFAST;
        client.flushServer();

        List<MealFacility> mealFacs = client.getAvailableMealFacilities(date, slot);
        Assertions.assertEquals(7, mealFacs.size());

        client.setCookies("incorrect cookie");
        mealFacs = client.getAvailableMealFacilities(date,slot);
        Assertions.assertNull(mealFacs);
    };
    static void ClientGetAvailableMeals()
    {
        Client client = new Client(test_url, test_server_cookies);
        String date  = "2025-01-01";
        char slot    = MealSlot.BREAKFAST;
        int facility = MealFacility.MAJUBA;
        client.flushServer();

        List<MealInfo> mealOptions = client.getAvailableMeals(date, slot, facility);
        Assertions.assertEquals(25159, mealOptions.getLast().code);

        client.setCookies("incorrect cookie");
        mealOptions = client.getAvailableMeals(date,slot,facility);
        Assertions.assertNull(mealOptions);
    };
    static void ClientSignInAndBook() throws IOException
    {
        Client client = new Client(test_url, "");
        String date = "2025-01-01";
        List<MealSlot> mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertNull(mealSlots);

        client.setCookies(Common.securityCookies);
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertNotNull(mealSlots);

        String newCookies = Client.getSecurityCookiesBySignIn(test_entry_url, test_target_url);
        client.setCookies(newCookies);
        mealSlots = client.getAvailableMealSlots(date);
        Assertions.assertNotNull(mealSlots);
    };
    static void ClientBookMeal()
    {
        Client client = new Client(test_url, test_server_cookies);
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

        client.setCookies("incorrect cookie");
        mbr = client.book(mbo);
        Assertions.assertNull(mbr);

        client.setCookies(test_server_cookies);
        mbr = client.book(mbo);
        Assertions.assertNotNull(mbr);
    };
    static void ClientGetMealsBookedThisMonth()
    {
        Client client = new Client(test_url, test_server_cookies);
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

        client.setCookies("incorrect cookie");
        meals = client.getMealsBookedInMonth(date);
        Assertions.assertNull(meals);
    };
    static void ClientCancelMeal()
    {
        Client client = new Client(test_url, test_server_cookies);
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

        mcr = client.cancel(9999);
        meals = client.getMealsBookedInMonth(date);
        Assertions.assertEquals(numMealsBooked-1, meals.size());
        Assertions.assertFalse(mcr.success);

        client.setCookies("incorrect cookie");
        mcr = client.cancel(meals.getLast().id);
        Assertions.assertNull(mcr);
    };
};
