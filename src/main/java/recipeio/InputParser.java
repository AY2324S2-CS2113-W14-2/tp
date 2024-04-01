package recipeio;

import recipeio.exceptions.InvalidMealCategory;
import recipeio.recipe.Recipe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import recipeio.enums.MealCategory;

import static recipeio.Constants.INDEX_COMMAND;
import static recipeio.Constants.INDEX_ID;
import static recipeio.Constants.INTEGER_NEEDED_ERROR_MESSAGE;
import static recipeio.Constants.INVALID_TASK_FORMAT_ERROR_MESSAGE;
import static recipeio.Constants.MEAL_CATEGORY_ERROR_MESSAGE;
import static recipeio.Constants.RECIPE_DELIMETER;


/**
 * Methods to parse input by the user.
 */
public class InputParser {

    /**
     * Returns command entered by the user, expected to be at beginning of string.
     * Error handling in TaskList.java
     *
     * @param userInput input from the user in the command line.
     * @return the command keyword. e.g. add, delete.
     */
    public static String parseCommand(String userInput) {
        return userInput.trim().split(" ")[INDEX_COMMAND];
    }

    /**
     * Returns index entered by the user, expected to be after the command.
     *
     * @param userInput input from the user in the command line.
     * @return the part of the user input after the command. e.g. 1
     */
    public static Integer parseID(String userInput) {
        String id = "";
        try {
            id = userInput.trim().split(" ")[INDEX_ID];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\tIndex not given, please enter an index");
            return null;
        }
        if (!CommandValidator.isParsableAsInteger(id)) {
            return null;
        }
        return Integer.parseInt(id);
    }

    /**
     * Splits the description into components, such as name and time
     *
     * @param userInput input from the user in the command line.
     */
    public static String[] parseDetails(String userInput){
        String[] words = userInput.trim().split(" ");
        // Ignore the first word and join the remaining words into a string
        String remainingInput = String.join(" ", Arrays.copyOfRange(words, 1, words.length));
        return remainingInput.trim().split(" ");
    }

    public static String parseFindType(String userInput) {
        return parseDetails(userInput)[0];
    }

    public static String parseFindCriteria(String userInput) {
        return parseDetails(userInput)[1];
    }

    public static String parseAllergyCriteria(String userInput) {
        return parseDetails(userInput)[0];
    }

    public static MealCategory parseMealCriteria(String userInput) {
        MealCategory mealCategory;
        switch (userInput) {
        case Constants.MEAL_CAT_GENERAL:
            mealCategory = MealCategory.GENERAL;
            break;
        case Constants.MEAL_CAT_DINNER:
            mealCategory = MealCategory.DINNER;
            break;
        case Constants.MEAL_CAT_LUNCH:
            mealCategory = MealCategory.LUNCH;
            break;
        case Constants.MEAL_CAT_BREAKFAST:
            mealCategory = MealCategory.BREAKFAST;
            break;
        case Constants.MEAL_CAT_APPETIZER:
            mealCategory = MealCategory.APPETIZER;
            break;
        case Constants.MEAL_CAT_DESSERT:
            mealCategory = MealCategory.DESSERT;
            break;
        default:
            mealCategory = MealCategory.GENERAL;
        }
        return mealCategory;
    }

    public static Recipe parseAdd(String userInput) throws Exception{
        String[] words = userInput.trim().split(" ");
        // Ignore the first word and join the remaining words into a string
        //add pizza/34/340/eggs/dinner/www.food.com
        String[] remainingInput = words[1].trim().split(RECIPE_DELIMETER);
        assert remainingInput.length > 0;
        if (remainingInput.length != 6) {
            throw new Exception(INVALID_TASK_FORMAT_ERROR_MESSAGE);
        }
        String recipeName = remainingInput[0].trim();
        try {
            Integer.parseInt(remainingInput[1].trim());
            Integer.parseInt(remainingInput[2].trim());
        } catch (NumberFormatException e){
            throw new Exception(INTEGER_NEEDED_ERROR_MESSAGE);
        }
        int cookTime = Integer.parseInt(remainingInput[1].trim());
        int calories = Integer.parseInt(remainingInput[2].trim());
        String[] allergies = remainingInput[3].trim().split(", ");
        ArrayList<String> allergiesList = new ArrayList<>();
        Collections.addAll(allergiesList, allergies);
        try {
            MealCategory.valueOf(remainingInput[4].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception(MEAL_CATEGORY_ERROR_MESSAGE);
        }
        MealCategory category = MealCategory.valueOf(remainingInput[4].trim().toUpperCase());
        String url = remainingInput[5].trim();
        return new Recipe(recipeName, cookTime, calories, allergiesList, category, LocalDate.now(), url);
    }
}
