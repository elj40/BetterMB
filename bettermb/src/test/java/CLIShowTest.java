import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.ArrayList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.FileNotFoundException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import java.io.FileReader;
import java.io.BufferedReader;

@Tag("CLI")
@Tag("Manual")
class CLIShowTest
{

    CLI cli;
    String filename;

    @BeforeEach
    void setup()
    {
        filename = "";
        //CLI.debugging = false;
    };
    @Test
    @Disabled
    void testShow()
    {
        filename = "data/test/CLIshow.txt";
        Path path = Paths.get(filename).toAbsolutePath();

        FileInputStream fileStream = null;
        try { fileStream = new FileInputStream(new File(path.toString())); }
        catch (FileNotFoundException ex) { fail("File " + filename + " not found"); }

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
        cli = new CLI(fileStream);
        cli.setClient(client);

        String[] args = new String[0];
        assertDoesNotThrow(() -> cli.show(args));
    }
}
