import com.eli.bettermb.client.*;

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
class Manual
{
    public static void main(String[] args)
    {
        StubBookHttpClient stub = new StubBookHttpClient();
        Client client = new Client(stub);

        CLI cli = new CLI(System.in);
        cli.setClient(client);

        cli.main();
    }
    static void book() throws IOException,InterruptedException
    {
        StubBookHttpClient stub = new StubBookHttpClient();
        Client client = new Client(stub);

        CLI cli = new CLI(System.in);
        cli.setClient(client);

        String[] showArgs;
        showArgs = new String[0];
        cli.book(showArgs);

        showArgs = new String[] { "2025-08-31" };
        cli.book(showArgs);
    }
    static void signin() throws IOException,InterruptedException
    {
        CustomStubHttpClient stub = new CustomStubHttpClient();
        stub.setResponseFromString(200, "{\"success\":true,\"message\":null}");
        Client client = new Client(stub);

        CLI cli = new CLI(System.in);
        cli.setClient(client);

        String test_url = "http://127.0.0.1:8080";
        cli.signin_entry = test_url + "/sign-in";
        cli.signin_target = test_url + "/target";

        cli.signin(new String[0]);
        System.out.println("User student number: " + String.valueOf(cli.user.studentNumber));

        cli.signin(new String[] { "28178564" });
        System.out.println("User student number: " + String.valueOf(cli.user.studentNumber));
    }
    static void cancel() throws IOException,InterruptedException
    {
        CustomStubHttpClient stub = new CustomStubHttpClient();
        stub.setResponseFromString(200, "{\"success\":true,\"message\":null}");
        Client client = new Client(stub);

        CLI cli = new CLI(System.in);
        cli.setClient(client);

        cli.loadStoredMeals("data/test/loadTest.json");
        cli.show(new String[] { "2025-01-01" });

        String[] cancelArgs;
        cancelArgs = new String[0];
        cli.cancel(cancelArgs);

        cancelArgs = new String[] { "2025083" };
        cli.cancel(cancelArgs);

        stub.setResponseFromString(200, "{\"success\":false,\"message\":null}");
        cancelArgs = new String[] { "2025083" };
        cli.cancel(cancelArgs);

        stub.setResponseFromString(200, "{\"success\":false,\"message\":\"Nah gang\"}");
        cancelArgs = new String[] { "2025083" };
        cli.cancel(cancelArgs);
    }
    static void show() throws IOException,InterruptedException
    {
        CustomStubHttpClient stub = new CustomStubHttpClient();
        String responseString = "";
        responseString += "[ ";
        responseString += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2025-08-31T11:00\",\"description\":\"Sunday Roast Beef\",\"facility\":\"Majuba\",\"mealTime\":\"11:00 - 13:00\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8172130}, ";
        responseString += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2025-09-01T07:15\",\"description\":\"Fried Egg Cheese Tomato\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172107}, ";
        responseString += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2025-09-01T12:30\",\"description\":\"Chicken Pie\",\"facility\":\"Majuba\",\"mealTime\":\"12:30 - 13:30\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8186151}, ";
        responseString += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2025-09-02T07:15\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172108}, ";
        responseString += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2025-09-02T12:30\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"12:30 - 13:30\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8186207}, ";
        responseString += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2025-09-03T07:15\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":0}";
        responseString += " ]";
        stub.setResponseFromString(200, responseString);

        Client client = new Client(stub);
        CLI cli = new CLI(System.in);
        cli.setClient(client);

        String[] showArgs;
        showArgs = new String[0];
        cli.show(showArgs);

        showArgs = new String[] { "2025-08-31" };
        cli.show(showArgs);
    }
};
