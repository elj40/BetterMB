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
        Client.debugging = false;
        CLI.debugging = false;
        User.debugging = false;

        Client client = new Client(new HttpClientImpl());
        client.setUrlBase(sun_url);

        CLI cli = new CLI(System.in);
        cli.setClient(client);

        cli.main();
    };
    //static void help()
    //{
    //    System.out.println("Commands:");
    //    System.out.println("\tsignin - sign in to pass security checks");
    //    System.out.println("\tbook   - make a new meal booking");
    //    System.out.println("\tshow   - show meal bookings in a certain month");
    //    System.out.println("\tcancel - cancel a meal booking using a meal ID");
    //    System.out.println("\tquit   - quits the application");
    //    System.out.println("\thelp   - displays this help command");
    //};
    //static public boolean cancel(Scanner scanner, Client client) throws IOException,InterruptedException
    //{
    //    int id = nextIntRanged(scanner, "Enter a meal ID:", 0, 9999999);
    //    System.out.println("Sending cancel request for meal ID: " + id);
    //    MealCancelResponse mcr = client.cancel(id);
    //    if (mcr == null) return false;
    //    System.out.println(mcr.cliDisplayString());
    //    return true;
    //};
    //static public boolean signin(Client client, String entryUrl, String targetUrl) throws IOException,InterruptedException
    //{
    //    cookies = Client.getSecurityCookiesBySignIn(entryUrl, targetUrl);
    //    if (cookies == null)
    //    {
    //        System.out.println("Something went wrong on sign in. Please report.");
    //        System.exit(0);
    //    };
    //    client.setCookies(cookies);
    //    return true;
    //};
    //static public boolean show(Scanner scanner, Client client) throws IOException,InterruptedException
    //{
    //    int currentYear = Year.now().getValue();
    //    int currentMonth = LocalDate.now().getMonthValue();

    //    String currentYearString = String.valueOf(currentYear);
    //    String currentMonthString = String.format("%02d", currentMonth);

    //    System.out.println(
    //            "Enter the month (yyyy-mm) " +
    //            "[Default: " + currentYearString + "-" + currentMonthString +
    //            "]:");
    //    String date = null;
    //    while (date == null)
    //    {
    //        String line = scanner.nextLine();
    //        line = line.strip();
    //        if (line.length() == 0) { line = currentYearString + "-" + currentMonthString; };
    //        if      (!Character.isDigit(line.charAt(0))) date = null; // y
    //        else if (!Character.isDigit(line.charAt(1))) date = null; // y
    //        else if (!Character.isDigit(line.charAt(2))) date = null; // y
    //        else if (!Character.isDigit(line.charAt(3))) date = null; // y
    //        else if (line.charAt(4) != '-')              date = null; // -
    //        else if (!Character.isDigit(line.charAt(5))) date = null; // m
    //        else if (!Character.isDigit(line.charAt(6))) date = null; // m
    //        else date = line;
    //    };
    //    date = date + "-01";

    //    // TODO: make this async and cache the results
    //    System.out.println("Sending request for meals in month of: " + date);
    //    List<Meal> meals = client.getMealsBookedInMonth(date);
    //    if (meals == null) return false;

    //    meals.sort((m1, m2) -> m1.start.compareTo(m2.start));

    //    System.out.println(Meal.cliDisplayHeaders());
    //    for (int i = 0; i < meals.size(); i++)
    //    {
    //        System.out.print(String.format("%02d. ",i));
    //        System.out.println(meals.get(i).cliDisplayString());
    //    };

    //    return true;
    //};
    //static public boolean book(Scanner scanner, Client client) throws IOException,InterruptedException
    //{
    //    int choice = -1;
    //    String date = null;
    //    String today = LocalDate.now().format(client.dateFormatter);
    //    MealBookingOptions mbo = new MealBookingOptions();

    //    while (date == null)
    //    {
    //        System.out.println("Enter a date to book on (yyyy-mm-dd) [Default: "+today+"]:");
    //        date = nextDateWithDefault(scanner.nextLine(), today);
    //    }
    //    mbo.mealDate = date;

    //    List<MealSlot> slots = client.getAvailableMealSlots(mbo.mealDate);
    //    if (slots == null) return false;
    //    for (int i = 0; i < slots.size(); i++)
    //    {
    //        System.out.print(i);
    //        System.out.print(". ");
    //        System.out.println(slots.get(i).cliDisplayString());
    //    };
    //    choice = nextIntRanged(scanner, "Select a meal slot: ", 0, slots.size());
    //    mbo.mealSlot = slots.get(choice).code;

    //    List<MealFacility> facilities =
    //        client.getAvailableMealFacilities(mbo.mealDate, mbo.mealSlot);
    //    if (facilities == null) return false;
    //    for (int i = 0; i < facilities.size(); i++)
    //    {
    //        System.out.print(i);
    //        System.out.print(". ");
    //        System.out.println(facilities.get(i).cliDisplayString());
    //    };
    //    choice = nextIntRanged(scanner, "Select a meal facility: ", 0, facilities.size());
    //    mbo.mealFacility = facilities.get(choice).code;

    //    List<MealOption> options =
    //        client.getMealOptions(mbo.mealDate, mbo.mealSlot, mbo.mealFacility);
    //    if (options == null) return false;
    //    for (int i = 0; i < options.size(); i++)
    //    {
    //        System.out.print(i);
    //        System.out.print(". ");
    //        System.out.println(options.get(i).cliDisplayString());
    //    };
    //    choice = nextIntRanged(scanner, "Select a meal option: ", 0, options.size());
    //    mbo.mealOption = options.get(choice).code;
    //    mbo.mealSession = options.get(choice).sessionId;

    //    mbo.advanceBookingDays = nextIntRanged(scanner, "How many days to book ahead? ", 0, 31);
    //    System.out.println();

    //    System.out.println("Sending request to book meal");
    //    List<MealBookingResponse> mbrs = client.book(mbo);
    //    if (options == mbrs) return false;
    //    for (int i = 0; i < mbrs.size(); i++)
    //    {
    //        System.out.print(i);
    //        System.out.print(". ");
    //        System.out.println(mbrs.get(i).cliDisplayString());
    //    };

    //    System.out.println("======================================================================");
    //    System.out.println("FINISHED");
    //    System.out.println("======================================================================");
    //    return true;
    //};
    //static public String nextDateWithDefault(String d, String default_date)
    //{
    //    String date = d.strip();
    //    if (date.length() == 0) return default_date;
    //    if (!Character.isDigit(date.charAt(0))) return null; // y
    //    if (!Character.isDigit(date.charAt(1))) return null; // y
    //    if (!Character.isDigit(date.charAt(2))) return null; // y
    //    if (!Character.isDigit(date.charAt(3))) return null; // y
    //    if (date.charAt(4) != '-')              return null; // -
    //    if (!Character.isDigit(date.charAt(5))) return null; // m
    //    if (!Character.isDigit(date.charAt(6))) return null; // m
    //    if (date.charAt(7) != '-')              return null; // -
    //    if (!Character.isDigit(date.charAt(8))) return null; // d
    //    if (!Character.isDigit(date.charAt(9))) return null; // d
    //    return date;
    //};
    //static public String parseDate(String d)
    //{
    //    String date = d.strip();
    //    if (!Character.isDigit(date.charAt(0))) return null; // y
    //    if (!Character.isDigit(date.charAt(1))) return null; // y
    //    if (!Character.isDigit(date.charAt(2))) return null; // y
    //    if (!Character.isDigit(date.charAt(3))) return null; // y
    //    if (date.charAt(4) != '-')              return null; // -
    //    if (!Character.isDigit(date.charAt(5))) return null; // m
    //    if (!Character.isDigit(date.charAt(6))) return null; // m
    //    if (date.charAt(7) != '-')              return null; // -
    //    if (!Character.isDigit(date.charAt(8))) return null; // d
    //    if (!Character.isDigit(date.charAt(9))) return null; // d
    //    return date;
    //};
    //// Written by ChatGPT
    //static public int nextIntWithDefault(Scanner scanner, int default_int)
    //{
    //    String input = scanner.nextLine();

    //    int value;
    //    if (input.isEmpty()) {
    //        value = default_int; // Use default value if input is empty
    //    } else {
    //        try {
    //            value = Integer.parseInt(input); // Try to parse the input to an integer
    //        } catch (NumberFormatException e) {
    //            System.out.println("Invalid input. Using default value: " + default_int);
    //            value = default_int; // Use default value if parsing fails
    //        }
    //    }
    //    return value;
    //};
    //static public int nextIntRanged(Scanner scanner, String msg, int min, int max)
    //{
    //    int choice = min-1;
    //    while (choice < min || choice > max)
    //    {
    //        System.out.println(msg);
    //        try { choice = scanner.nextInt(); }
    //        catch (InputMismatchException e) { scanner.next(); continue; }
    //        catch (Exception e) { e.printStackTrace(); }
    //    };
    //    return choice;
    //};
};
