package com.eli.bettermb.cli;

import com.eli.bettermb.client.*;

import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.Comparator;
import java.util.Arrays;
import java.util.ArrayList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.function.Function;
import java.util.function.Supplier;

import com.google.gson.JsonSyntaxException;

class CLI
{
    static boolean debugging = true;
    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    ScannerCLI scannerCLI;
    Client client;
    User user;
    Waiter waiter = new Waiter();

    String signin_entry = "https://my.sun.ac.za/tracker?linkID=239&lang=en";
    String signin_target = "https://web-apps.sun.ac.za";

    static String sun_url = "https://web-apps.sun.ac.za";
    static String mysun_link = "https://my.sun.ac.za/tracker?linkID=239&lang=en";
    static String sun_entry_url = "https://my.sun.ac.za/tracker?linkID=239&lang=en";
    static String sun_target_url = sun_url;

    private boolean asyncSecurityFlag = false;
    private String earliestDate = "9999-99-99";

    public static void main(String[] args) throws IOException,InterruptedException
    {
        boolean shouldDebug = false;
        Client.debugging = shouldDebug;
        CLI.debugging = shouldDebug;
        User.debugging = shouldDebug;

        Client client = new Client(new HttpClientImpl());
        client.setUrlBase(sun_url);

        CLI cli = new CLI(System.in);
        cli.setClient(client);

        cli.main();
    };
    CLI(java.io.InputStream stream)
    {
        scannerCLI = new ScannerCLI(stream);
        user = new User();
    };
    public void setClient(Client c)
    {
        client = c;
    }
    public void main()
    {
        String command;
        String action;
        List<String> args;
        String[] argsArray = new String[0];
        while (true)
        {
            System.out.print("> ");
            command = scannerCLI.nextLine().trim();
            args = new ArrayList<>(Arrays.asList(command.split(" ")));

            if (args.size() > 0) action = args.remove(0).trim();
            else action = "";

            if (action.equals("quit")) this.quit();
            else if (action.equals("show")) this.show(args.toArray(argsArray));
            else if (action.equals("book")) this.book(args.toArray(argsArray));
            else if (action.equals("cancel")) this.cancel(args.toArray(argsArray));
            else if (action.equals("signin")) this.signin(args.toArray(argsArray));
            else if (action.equals("quota")) this.quota(args.toArray(argsArray));
            else if (action.equals("cookie")) this.cookie(args.toArray(argsArray));
            else if (action.equals("student")) this.student(args.toArray(argsArray));
            else if (action.equals("help")) this.help();
            else if (action.isEmpty()) action = "";
            else this.help();
        }
    };
    void updateUserMealsAsync(String date)
    {
        earliestDate = date;
        System.out.println("[cli] Sending request to update meals");
        CompletableFuture<List<Meal>> futureMeals = client.getMealsBookedInMonthAsync(date);
        waiter.markNotDone();
        futureMeals.thenAccept(result -> {
            user.mergeMeals(result);
            waiter.markDone();

            if (result == null) asyncSecurityFlag = true;
            else asyncSecurityFlag = false;
        });
    }
    public void show(String[] args)
    {
        if (asyncSecurityFlag)
        {
            System.out.println("[security] ERROR: could not retrieve meals");
            System.out.println("[security] (hint: try the \"signin\" command)");
            //asyncSecurityFlag = false;
            return;
        }
        String today = LocalDate.now().format(dateFormatter);
        String date = null;
        if (args.length > 0)
        {
            String arg = args[0].trim();
            if (scannerCLI.checkDate(arg)) {
                date = arg;
            } else {
                System.out.println("[show] Invalid argument:        " + arg);
                System.out.println("[show] Expected date in format: yyyy-mm-dd");
                return;
            }
        } else date = today;

        if (date.compareTo(earliestDate) < 0)
        {
            updateUserMealsAsync(date);
        };

        System.out.println("[show] Showing booked meals from " + date + "...");
        System.out.println();

        try { waiter.waitUntilDone(); }
        catch (InterruptedException e)
        {
            System.out.println("[show] Received interrupt, escaping");
            return;
        }

        List<Meal> meals = new ArrayList<>();
        System.out.println(MealDisplay.headers());
        meals = user.getMealsBookedFrom(date);

        meals.sort(Comparator.comparing(m -> m.start));

        MealDisplay mealD;
        MealDisplay pmealD = new MealDisplay();
        for (int i = 0; i < meals.size(); i++)
        {
            mealD = MealDisplay.fromMeal(meals.get(i));

            if (i > 0 && !pmealD.date.equals(mealD.date)) System.out.println("");
            System.out.println(String.format("%02d. %s", i, mealD.toString()));

            pmealD = MealDisplay.fromMeal(meals.get(i));
        }
        System.out.println("");
    };
    public void quota(String[] args)
    {
        System.out.println("[quota] Fetching quota summary");
        QuotaSummary qs = null;
        try { qs = client.getQuotaSummary(); }
        catch (SecurityFailedException ex) {
            System.out.println("[security] Please sign in and try again");
            return; }
        catch (IOException ex) {
            System.out.println("[quota] Exception: " + ex.getMessage());
            return;
        }

        System.out.println("Quota Summary:");
        if (qs.currentQuotaDesc != null) System.out.println("    current quota - " + qs.currentQuotaDesc);
        if (qs.cobQuotaDesc != null) System.out.println("    cob quota     - " + qs.cobQuotaDesc);
        if (qs.balanceDesc != null)  System.out.println("    balance       - " + qs.balanceDesc);
        if (qs.mealUsageDesc != null) System.out.println("    mealUsage     - " + qs.mealUsageDesc);
        if (qs.quotaPendingMessage != null) System.out.println("    quotaPending  - " + qs.quotaPendingMessage);
    };
    public void cancel(String[] args)
    {
        int id = -1;
        if (args.length != 1)
        {
            System.out.println("[cancel] ERROR: incorrect number of arguments");
            System.out.println("[cancel] usage: cancel [meal_id]");
            return;
        };

        String arg = args[0].trim();
        try {
            id = Integer.parseInt(arg);
        } catch (java.lang.NumberFormatException ex) {
            System.out.println("[cancel] Invalid argument:        " + arg);
            System.out.println("[cancel] Expected id in format: 1234567");
            return;
        }

        System.out.println("[cancel] Cancelling meal with ID: " + String.valueOf(id) + "...");
        MealCancelResponse mcr = null;
        try { mcr = client.cancel(id); }
        catch (SecurityFailedException ex) {
            System.out.println("[security] Please sign in and try again");
            return; }
        catch (IOException ex) {
            System.out.println("[cancel] Exception: " + ex.getMessage());
            mcr = new MealCancelResponse();
        }

        System.out.println("[cancel] " + mealCancelResponseString(mcr));

        String today = LocalDate.now().format(dateFormatter);
        updateUserMealsAsync(today);
    };
    public void signin(String args[])
    {
        int su = -1;
        boolean checkArg = false;
        if (args.length > 0)
        {
            String arg = args[0].trim();
            try {
                su = Integer.parseInt(arg);
                checkArg = true;
            } catch (java.lang.NumberFormatException ex) {
                System.out.println("[signin] Invalid argument:        " + arg);
                System.out.println("[signin] Expected SU in format: 12345678");
            }
        }
        String cookies = null;
        try { cookies = Client.getSecurityCookiesBySignIn(signin_entry, signin_target); }
        catch (Exception ex)
        {
            if (CLI.debugging) ex.printStackTrace();
            System.out.println("[signin] Failed to sign in");
            return;
        }
        client.setCookies(cookies);
        String today = LocalDate.now().format(dateFormatter);
        updateUserMealsAsync(today);

        if (!checkArg) {
            System.out.println("Enter an SU number to save bookings (optional): ");
            su = scannerCLI.nextIntRangedWithDefault((int) 2e7, (int) 10e7, 0);
        }

        if (su != 0) user.studentNumber = su;
        loadStoredMeals("data/"+ su + "_meals.json");
    };
    public void quit()
    {
        if (user.studentNumber == 0)
        {
            System.out.println("[quit] Anonymous session: not saving");
        } else
        {
            String pathString = "data/"+Integer.toString(user.studentNumber)+"_meals.json";
            System.out.println("[quit] Saving data to " + pathString);
            try {
            user.saveToFile(user.meals, pathString);
            } catch (IOException ex)
            {
                System.out.println("[quit] Failed to save: " + ex.getMessage());
            }
        }

        System.out.println("[quit] Quitting");
        System.exit(0);
    };
    public void loadStoredMeals(String filename)
    {
        List<Meal> meals = null;
        try { meals = user.loadFromFile(filename); }
        catch (FileNotFoundException ex) {
            System.out.println("[load] Could not find: " + filename);
            System.out.println("[load] Don't worry, its ok");
        }
        catch (IOException ex) {
            System.out.println("[load] Exception: " + ex.getMessage());
        }
        user.mergeMeals(meals);
    };
    private void bookHandleExceptions(Exception e)
    {
        if (e instanceof SecurityFailedException)
        {
            System.out.println("[book] Failed due to security cookies, try \"signin\"");
        }
        System.out.println("[book] Exception message: " + e.getMessage());
    }
    <T> boolean bookPrintResponseList(List<T> list, Function<T, String> getStringFunction) {
        for (int i = 0; i < list.size(); i++)
        {
            System.out.println(String.format("%2d. %s", i, getStringFunction.apply(list.get(i))));
        }
        return true;
    }
    void book(String[] args)
    {
        String today = LocalDate.now().format(dateFormatter);
        String date = null;
        int choice = 0;

        if (args.length != 1)
        {
            System.out.println("[book] ERROR: incorrect number of arguments");
            System.out.println("[book] usage: book [yyyy-mm-dd]");
            return;
        };

        String arg = args[0].trim();
        if (scannerCLI.checkDate(arg)) {
            date = arg;
        } else {
            System.out.println("[book] Invalid argument:        " + arg);
            System.out.println("[book] Expected date in format: yyyy-mm-dd");
            return;
        }
        System.out.println("[book] At any option, enter -1 to cancel");

        MealBookingOptions mbo = new MealBookingOptions();
        mbo.mealDate = date;

        // Slots
        List<MealSlot> slots = null;
        try { slots = client.getAvailableMealSlots(mbo.mealDate); }
        catch (Exception e) { bookHandleExceptions(e); };
        if (slots == null) return;
        bookPrintResponseList(slots, this::mealSlotString);

        System.out.println("Select a meal slot: ");
        choice = scannerCLI.nextIntRanged(-1, slots.size());
        if (choice == -1) return;
        mbo.mealSlot = slots.get(choice).code;

        // Facilities
        List<MealFacility> facilities = null;
        try { facilities = client.getAvailableMealFacilities(mbo.mealDate, mbo.mealSlot); }
        catch (InterruptedException ex)
        { System.out.println("[book] " + ex.getMessage()); return; }
        catch (Exception e) { bookHandleExceptions(e); };
        if (facilities == null) return;
        bookPrintResponseList(facilities, this::mealFacilityString);

        System.out.println("Select a meal faciliy: ");
        choice = scannerCLI.nextIntRanged(-1, facilities.size());
        if (choice == -1) return;
        mbo.mealFacility = facilities.get(choice).code;

        // Options
        List<MealOption> options = null;
        try { options = client.getMealOptions(mbo.mealDate, mbo.mealSlot, mbo.mealFacility); }
        catch (Exception e) { bookHandleExceptions(e); }
        if (options == null) return;

        bookPrintResponseList(options, this::mealOptionsString);
        System.out.println("Select a meal option: ");
        choice = scannerCLI.nextIntRanged(-1, options.size());
        if (choice == -1) return;

        mbo.mealOption = options.get(choice).code;
        mbo.mealSession = options.get(choice).sessionId;

        // Advance booking days
        System.out.println("How many days to book ahead? ");
        mbo.advanceBookingDays = scannerCLI.nextIntRanged(-1, 31);
        scannerCLI.nextLine();
        if (mbo.advanceBookingDays == -1) return;

        System.out.println();

        System.out.println("[book] Sending request to book meal");
        List<MealBookingResponse> mbrs = null;
        try { mbrs = client.book(mbo); }
        catch (Exception e) { bookHandleExceptions(e); }

        if (mbrs == null) return;
        bookPrintResponseList(mbrs, this::mealBookingResponseString);

        updateUserMealsAsync(date);
    };
    void student(String[] args)
    {
        if (args.length > 0)
        {
            boolean validArg = false;
            int number = 0;
            try {
                number = Integer.parseInt(args[0].trim());
                validArg = (number >= 1e7 && number < 10e7);
            }
            catch (Exception e)
            {
                validArg = false;
                if (CLI.debugging) e.printStackTrace();
            }
            if (validArg)
            {
                user.studentNumber = number;
                loadStoredMeals("data/"+ number + "_meals.json");
            }
        } else
        {
            System.out.println(user.studentNumber);
        }
    }
    void cookie(String[] args)
    {
        if (args.length > 0)
        {
            client.setCookies(args[0].trim());
            String today = LocalDate.now().format(dateFormatter);
            updateUserMealsAsync(today);
            return;
        }
        System.out.println(client.getCookies());
    }
    void help()
    {
        System.out.println(String.join("\n",
                "[help] Arguments follow the format of [optional] and <required> (do not use brackets)",
                "[help] Commands:",
                "    signin [student_number]",
                "           Sign in to pass security checks, optional [student_number] to save meals to computer.",
                "    book <yyyy-mm-dd>",
                "           Sake a new meal booking, required date in shown format.",
                "    show [yyyy-mm-dd]",
                "           Show meal bookings from a date.",
                "           If no date provided will use today as default.",
                "    cancel <id>",
                "           Cancel a meal booking using a meal ID.",
                "           Meal ID can be acquired using the \"show\" command.",
                "    quota",
                "           Show summary of quota, cob and balance",
                "    cookie [string]",
                "           Sets cookie used to pass security.",
                "           If no string is passed will display current cookie.",
                "           Copy to cookie to avoid signin across sessions, cookies do expire.",
                "    student [int]",
                "           Sets student number.",
                "           If no int is passed will display current student number.",
                "           Used to load locally stored meals without sign in.",
                "    quit   quits the application",
                "    help   displays this help command"
                ));
    };
    String mealSlotString(MealSlot m)
    {
        return m.description;
    };
    String mealFacilityString(MealFacility m)
    {
        return m.description;
    };
    String mealBookingResponseString(MealBookingResponse m)
    {
        return String.format("[%1$s] %2$s", m.bookingDate, m.bookingMessage);
    }
    String mealOptionsString(MealOption m)
    {
        return String.format("%-40s %s", m.description, m.cost);
    }

    String mealCancelResponseString(MealCancelResponse mcr)
    {
        String s = "Cancel ";
        s += (mcr.success) ? "successful" : "failed";
        s += (mcr.message == null) ? "" : (": " + mcr.message);
        return s;
    };
    private void handleException(Exception ex)
    {
        System.out.println("YOOO, something went really wrong! Damn...");
        System.out.println(ex.getMessage());
        if (CLI.debugging) ex.printStackTrace();
        System.exit(1);
    }
};
