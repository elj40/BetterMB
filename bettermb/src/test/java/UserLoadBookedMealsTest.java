import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.ArrayList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

@Tag("User")
class UserLoadBookedMealsTest
{
    final int studentNumber = 28178564;

    User user;
    List<Meal> list = new ArrayList<>();
    String filename;

    @BeforeEach
    void setup()
    {
        user = new User(studentNumber);
        list.clear();
        filename = "";
        User.debugging = false;
    };
    @Test
    void testLoadFromFileNonExistent()
    {
        filename = "data/test/nonExistant.json";
        assertThrows(FileNotFoundException.class, () -> user.loadFromFile(filename));
    }
    @Test
    void testLoadFromFileCorrupt()
    {
        filename = "data/test/corrupt.json";
        String content = "{ \"incorrect\"=\"json\" }";

        try
        {
            Path path = Paths.get(filename).toAbsolutePath();
            Files.createDirectories(path.getParent());

            FileWriter fw = new FileWriter(path.toString());
            fw.write(content, 0, content.length());
            fw.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
            fail("Could not write test file");
        }

        assertThrows(JsonSyntaxException.class, () -> user.loadFromFile(filename));
    }
    @Test
    void testLoadFromFileEmpty()
    {
        filename = "data/test/empty.json";
        String content = "";

        try
        {
            Path path = Paths.get(filename).toAbsolutePath();
            Files.createDirectories(path.getParent());

            FileWriter fw = new FileWriter(path.toString());
            fw.write(content, 0, content.length());
            fw.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
            fail("Could not write test file");
        }

        assertDoesNotThrow(() -> {list = user.loadFromFile(filename);});
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    @Test
    void testLoadFromFile()
    {
        filename = "data/test/loadTest.json";
        String content = "";
        content += "[ ";
        content += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2025-08-31T11:00\",\"description\":\"Sunday Roast Beef\",\"facility\":\"Majuba\",\"mealTime\":\"11:00 - 13:00\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8172130}, ";
        content += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2025-09-01T07:15\",\"description\":\"Fried Egg Cheese Tomato\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172107}, ";
        content += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2025-09-01T12:30\",\"description\":\"Chicken Pie\",\"facility\":\"Majuba\",\"mealTime\":\"12:30 - 13:30\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8186151}, ";
        content += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2025-09-02T07:15\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172108}, ";
        content += "{\"canModify\":false,\"title\":\"Lunch\",\"start\":\"2025-09-02T12:30\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"12:30 - 13:30\",\"mealCost\":\"R44.00\",\"mealSlot\":\"L\",\"backgroundColor\":\"#28793d\",\"borderColor\":\"#28793d\",\"id\":8186207}, ";
        content += "{\"canModify\":false,\"title\":\"Breakfast\",\"start\":\"2025-09-03T07:15\",\"description\":\"Standard Meal\",\"facility\":\"Majuba\",\"mealTime\":\"07:15 - 08:30\",\"mealCost\":\"R18.79\",\"mealSlot\":\"B\",\"backgroundColor\":\"#e29e20\",\"borderColor\":\"#e29e20\",\"id\":8172109}";
        content += " ]";
        try
        {
            Path path = Paths.get(filename).toAbsolutePath();
            Files.createDirectories(path.getParent());

            FileWriter fw = new FileWriter(path.toString());
            fw.write(content, 0, content.length());
            fw.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
            fail("Could not write test file");
        }
        assertDoesNotThrow(() -> {list = user.loadFromFile(filename);});
        assertNotNull(list);
        assertEquals(6, list.size());
    }
}
