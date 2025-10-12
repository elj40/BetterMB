import org.junit.jupiter.api.Test;
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
import java.io.FileWriter;

import java.io.IOException;
import java.io.FileNotFoundException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

@Tag("User")
class UserSaveBookedMealsTest
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
    void testSaveToFile()
    {
        filename = "data/test/testSaveFile.json";
        String loadFilename = "data/test/loadTest.json";
        assertDoesNotThrow(() -> {list = user.loadFromFile(loadFilename);});
        assertDoesNotThrow(() -> user.saveToFile(list, filename));

        File file = new File(filename);
        assertTrue(file.exists());
        assertDoesNotThrow(() -> {list = user.loadFromFile(filename);});
        assertEquals(6, list.size());
    }
}
