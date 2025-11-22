import com.eli.bettermb.client.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
class User
{
    static boolean debugging = true;
    int studentNumber = 0;
    List<Meal> meals = new ArrayList<>();
    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    public User()
    {
        studentNumber = 0;
    };
    public User(int su)
    {
        studentNumber = su;
    };
    static void debug(String s)
    {
        if (User.debugging) System.out.println(s);
    }
    List<Meal> loadFromFile(String pathString)
            throws FileNotFoundException, JsonIOException, JsonSyntaxException, IOException
    {
        Path path = Paths.get(pathString).toAbsolutePath();
        User.debug(path.toString());
        BufferedReader in = new BufferedReader(new FileReader(path.toString()));

        Type mealListType = new TypeToken<List<Meal>>() {}.getType();
        List<Meal> meals = gson.fromJson(in, mealListType);

        if (meals == null) return new ArrayList<Meal>();

        return meals;
    };
    void saveToFile(List<Meal> meals, String pathString) throws IOException
    {
        Path path = Paths.get(pathString).toAbsolutePath();
        User.debug(path.toString());
        Files.createDirectories(path.getParent());

        String content = gson.toJson(meals);

        FileWriter fw = new FileWriter(path.toString());
        fw.write(content, 0, content.length());
        fw.close();
    }
    public List<Meal> getMealsBookedFrom(String date)
    {
        List<Meal> filtered = new ArrayList<Meal>();
        for (Meal meal: this.meals)
        {
            if (meal.start == null) { filtered.add(meal); continue; };
            int    end      = "yyyy-mm-dd".length();
            if (meal.start.length() < end) { filtered.add(meal); continue; };
            String mealDate = meal.start.substring(0, end);
            if (mealDate.compareTo(date) >= 0) filtered.add(meal);
        }
        return filtered;
    }
    public boolean cancelByID(int id)
    {
        int removeIndex = -1;
        for (int i = 0; i < meals.size(); i++)
        {
            if (meals.get(i).id == id)
            {
                removeIndex = i;
                break;
            }
        };
        if (removeIndex > 0) {
            meals.remove(removeIndex);
            return true;
        }
        return false;
    };
    public void overwriteMealsFromDate(List<Meal> newMeals, String date)
    {
        for (int i = this.meals.size()-1; i >= 0; i--)
        {
            boolean remove = false;
            Meal oldMeal = this.meals.get(i);

            int    end      = "yyyy-mm-dd".length();
            if (oldMeal.start.length() < end) remove = true;
            else {
                String oldMealDate = oldMeal.start.substring(0,end);
                if (oldMealDate.compareTo(date) >= 0) remove = true;;
            }

            if (remove) this.meals.remove(i);
        }
        for (Meal newMeal : newMeals)
        {
            this.meals.add(newMeal);
        }
    };
    public void mergeMeals(List<Meal> newMeals)
    {
        if (newMeals == null) return;
        for (Meal newMeal : newMeals)
        {
            mergeMeal(newMeal);
        }
    };
    public int mergeMeal(Meal newMeal)
    {
        boolean replaced = false;
        for (int i = 0; i < this.meals.size(); i++)
        {
            Meal oldMeal = this.meals.get(i);

            if (oldMeal.mealSlot != newMeal.mealSlot) continue;
            if (!oldMeal.facility.equals(newMeal.facility)) continue;
            int    end      = "yyyy-mm-dd".length();
            if (oldMeal.start.length() < end) continue;
            if (newMeal.start.length() < end) continue;
            String oldMealDate = oldMeal.start.substring(0,end);
            String newMealDate = newMeal.start.substring(0,end);
            if (!oldMealDate.equals(newMealDate)) continue;
            if (oldMeal.id != 0 && newMeal.id == 0) continue;

            this.meals.set(i, newMeal);
            replaced = true;
            break;
        }
        if (!replaced) this.meals.add(newMeal);
        return this.meals.size();
    };
};
