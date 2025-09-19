import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.function.Function;
import java.util.function.Supplier;

class CLI
{
    static boolean debugging = true;
    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    ScannerCLI scannerCLI;
    Client client;
    User user;

    String signin_entry = "https://my.sun.ac.za/tracker?linkID=239&lang=en";
    String signin_target = "https://web-apps.sun.ac.za";

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
            else if (action.equals("cookie")) this.cookie(args.toArray(argsArray));
            else if (action.equals("student")) this.student(args.toArray(argsArray));
            else if (action.equals("help")) this.help();
            else if (action.isEmpty()) action = "";
            else this.help();
        }
    };
    public void show(String[] args)
    {
        String today = LocalDate.now().format(dateFormatter);
        String date = null;
        boolean checkArg = false;
        if (args.length > 0)
        {
            String arg = args[0].trim();
            if (scannerCLI.checkDate(arg)) {
                checkArg = true;
                date = arg;
            } else {
                System.out.println("[show] Invalid argument:        " + arg);
                System.out.println("[show] Expected date in format: yyyy-mm-dd");
            }
        }

        if (!checkArg) {
            do {
                System.out.print("[show] Enter a date (default=["+today+"] (today)): ");
                date = scannerCLI.nextDateWithDefault(today);
            } while(date == null);
        }

        System.out.println("[show] Requesting booked meals from " + date + "...");
        System.out.println();
        CompletableFuture<List<Meal>> futureMeals = client.getMealsBookedInMonthAsync(date);

        List<Meal> meals = new ArrayList<>();
        if (user.meals.size() > 0)
        {
            System.out.println("[show] Cached bookings from " + date + ":");
            System.out.println(mealBookingHeadersString());
            meals = user.getMealsBookedFrom(date);
            for (int i = 0; i < meals.size(); i++)
            {
                Meal meal = meals.get(i);
                System.out.println(String.format("%02d. %s", i, mealBookingString(meal)));
            }
            System.out.println("");
        }
        System.out.print("[show] Wait for server result? [Y/n] ");
        char choice;
        do {
            choice = scannerCLI.nextCharFromWithDefault("Yyn", 'y');
        } while (choice == 0);
        if (choice == 'n')
        {
            futureMeals.thenAccept(result -> user.mergeMeals(result));
            return;
        }

        try { meals = futureMeals.get(); }
        catch (Exception ex) { handleException(ex); }

        if (meals == null) {
            System.out.println("[show] Failed to get meals from server. If you have already done \"signin\", please report");
            return;
        } else {
            user.overwriteMealsFromDate(meals, date);
        }

        System.out.println("[show] Server bookings from " + date + ":");
        System.out.println(mealBookingHeadersString());

        meals = user.getMealsBookedFrom(date);
        for (int i = 0; i < meals.size(); i++)
        {
            Meal meal = meals.get(i);
            System.out.println(String.format("%02d. %s", i, mealBookingString(meal)));
        }
        System.out.println("");
    };
    public void cancel(String[] args)
    {
        System.out.println("[cancel] At any option, enter -1 to cancel");
        int id = -1;
        boolean checkArg = false;
        if (args.length > 0)
        {
            String arg = args[0].trim();
            try {
                id = Integer.parseInt(arg);
                checkArg = true;
            } catch (java.lang.NumberFormatException ex) {
                System.out.println("[cancel] Invalid argument:        " + arg);
                System.out.println("[cancel] Expected id in format: 1234567");
            }
        }

        if (!checkArg) {
            System.out.println("Enter an id: ");
            id = scannerCLI.nextIntRanged(-1, (int) 1e8);
            if (id == -1) return;
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

        if (mcr.success) user.cancelByID(id);

        System.out.println("[cancel] " + mealCancelResponseString(mcr));
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

        if (!checkArg) {
            System.out.println("Enter an SU number to save bookings (default=anonymous): ");
            su = scannerCLI.nextIntRangedWithDefault((int) 2e7, (int) 10e7, 0);
        }

        user.studentNumber = su;
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
        System.out.println("[book] At any option, enter -1 to cancel");
        String today = LocalDate.now().format(dateFormatter);
        String date = null;
        int choice = 0;
        boolean checkArg = false;
        if (args.length > 0)
        {
            String arg = args[0].trim();
            if (scannerCLI.checkDate(arg)) {
                checkArg = true;
                date = arg;
            } else {
                System.out.println("[book] Invalid argument:        " + arg);
                System.out.println("[book] Expected date in format: yyyy-mm-dd");
            }
        }

        if (!checkArg) {
            do {
                System.out.print("[book] Enter a date (default=["+today+" (today)]): ");
                date = scannerCLI.nextDateWithDefault(today);
            } while(date == null);
        }

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
        if (mbo.advanceBookingDays == -1) return;

        System.out.println();

        System.out.println("[book] Sending request to book meal");
        List<MealBookingResponse> mbrs = null;
        try { mbrs = client.book(mbo); }
        catch (Exception e) { bookHandleExceptions(e); }

        if (mbrs == null) return;
        bookPrintResponseList(mbrs, this::mealBookingResponseString);
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
            return;
        }
        System.out.println(client.getCookies());
    }
    void help()
    {
        System.out.println("[help] Commands:");
        System.out.println("\tsignin - sign in to pass security checks");
        System.out.println("\tbook   - make a new meal booking");
        System.out.println("\tshow   - show meal bookings in from a date");
        System.out.println("\tcancel - cancel a meal booking using a meal ID");
        System.out.println("\tquit   - quits the application");
        System.out.println("\tcookie - [string] sets cookie if arg is passed, gets cookie if not");
        System.out.println("\t         Copy to cookie to avoid signin for every session, cookies do expire");
        System.out.println("\tstudent- [int] sets student number if arg is passed, gets student number if not");
        System.out.println("\thelp   - displays this help command");
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
    String mealBookingHeadersString()
    {
        return String.format("##. %-7s [%s] %-10s %-20s %s",
                "ID",
                "yyyy-mm-dd Day",
                "Title",
                "Facility",
                "Description");
    };
    String mealBookingString(Meal meal)
    {
        String idString;
        if (meal.id == 0) idString = "?".repeat(7);
        else idString = String.valueOf(meal.id);

        String date = "?".repeat(10);
        String day  = "???";
        if (meal.start != null) {
            date = meal.start.substring(0, "yyyy-mm-dd".length());
            day = LocalDate.parse(date)
                .getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        }
        return String.format("%-7s [%s %s] %-10s %-20s %s",
                idString,
                date,
                day,
                meal.title,
                meal.facility,
                meal.description);
    }
    private void handleException(Exception ex)
    {
        System.out.println("YOOO, something went really wrong! Damn...");
        System.out.println(ex.getMessage());
        if (CLI.debugging) ex.printStackTrace();
        System.exit(1);
    }
};
