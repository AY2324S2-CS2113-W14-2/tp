package recipeio;

import recipeio.enums.SortType;
import recipeio.recipe.Recipe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import recipeio.enums.MealCategory;
import recipeio.constants.InputParserConstants;

import static recipeio.constants.InputParserConstants.INDEX_COMMAND;
import static recipeio.constants.InputParserConstants.ARRAY_START_INDEX;
import static recipeio.constants.InputParserConstants.FIND_TYPE_INDEX;
import static recipeio.constants.InputParserConstants.FIND_CRITERIA_INDEX;
import static recipeio.constants.InputParserConstants.MEAL_CATEGORY_INDEX;
import static recipeio.constants.InputParserConstants.RECIPE_DELIMETER;
import static recipeio.constants.InputParserConstants.USER_INPUT_INDEX;

/**
 * Methods to parse input by the user.
 */
public class InputParser {

    /**
     * Returns command entered by the user, expected to be at beginning of string.
     * Most error handling in TaskList.java, with an exception for the list command.
     * Checks are made for any trailing characters following the list command and returns an invalid command
     * if any of such characters are found.
     *
     * @param userInput input from the user in the command line.
     * @return the command keyword. e.g. add, delete.
     */
    public static String parseCommand(String userInput) {
        return userInput.trim().split("\\s+")[INDEX_COMMAND].toLowerCase();
    }

    /**
     * Returns index entered by the user, expected to be after the command.
     *
     * @param userInput input from the user in the command line.
     * @return the part of the user input after the command. e.g. 1.
     */
    public static Integer parseID(String userInput) {
        String id = "";
        try {
            id = userInput.trim().split("\\s+")[InputParserConstants.INDEX_ID];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(InputParserConstants.PARSE_ID_ERROR);
            return null;
        }
        if (!CommandValidator.isParsableAsInteger(id)) {
            return null;
        }
        return Integer.parseInt(id);
    }

    /**
     * Splits the description into components, such as name and time.
     *
     * @param userInput input from the user in the command line.
     * @return an array of description extracted from the user input.
     */
    public static String[] parseDetails(String userInput){
        String[] words = userInput.trim().split("\\s+");
        // Ignore the first word and join the remaining words into a string
        String remainingInput = String.join(" ", Arrays.copyOfRange(words, ARRAY_START_INDEX, words.length));
        return remainingInput.trim().split(" ");
    }

    /**
     * Return the type of find the user is using from selection of keyword and date.
     *
     * @param userInput input from the user in the command line.
     * @return String of the type of find command.
     */
    public static String parseFindType(String userInput) {
        return parseDetails(userInput)[FIND_TYPE_INDEX];
    }

    /**
     * Return the criteria the user is searching with after processing.
     *
     * @param userInput input from the user in the command line.
     * @return String of appropriate criteria.
     */
    public static String parseFindCriteria(String userInput) {
        return parseDetails(userInput)[FIND_CRITERIA_INDEX].trim().toLowerCase();
    }

    /**
     * Return the description of allergies from the user input.
     *
     * @param userInput input from the user in the command line.
     * @return String of allergies.
     */
    public static String parseAllergyCriteria(String userInput) {
        int firstSpaceIndex = userInput.indexOf(' ');
        if (firstSpaceIndex == -1) {
            return "";
        }

        return userInput.substring(firstSpaceIndex + 1).trim().toLowerCase();
    }

    /**
     * Return the description of allergies from the user input.
     *
     * @param userInput input from the user in the command line.
     * @return String of allergies.
     */
    public static MealCategory parseMealCriteria(String userInput) {
        MealCategory mealCategory;
        switch (userInput) {
        case InputParserConstants.MEAL_CAT_GENERAL:
            mealCategory = MealCategory.GENERAL;
            break;
        case InputParserConstants.MEAL_CAT_DINNER:
            mealCategory = MealCategory.DINNER;
            break;
        case InputParserConstants.MEAL_CAT_LUNCH:
            mealCategory = MealCategory.LUNCH;
            break;
        case InputParserConstants.MEAL_CAT_BREAKFAST:
            mealCategory = MealCategory.BREAKFAST;
            break;
        case InputParserConstants.MEAL_CAT_APPETIZER:
            mealCategory = MealCategory.APPETIZER;
            break;
        case InputParserConstants.MEAL_CAT_DESSERT:
            mealCategory = MealCategory.DESSERT;
            break;
        default:
            mealCategory = MealCategory.GENERAL;
        }
        return mealCategory;
    }

    /**
     * Returns the sort type of the list command.
     *
     * @param userInput input from the user in the command line.
     * @return the sort type of the list command.
     */
    public static SortType parseListCommand(String userInput) {
        String[] words = userInput.trim().split("\\s+");
        if (words.length == 1) {
            return SortType.NONE;
        }
        if (words[1].equals(InputParserConstants.SORT_NAME)) {
            return SortType.NAME;
        } else if (words[1].equals(InputParserConstants.SORT_DATE_ADDED)) {
            return SortType.DATE;
        } else if (words[1].equals(InputParserConstants.SORT_CALORIES)) {
            return SortType.CALORIES;
        } else if (words[1].equals(InputParserConstants.SORT_COOK_TIME)) {
            return SortType.COOK_TIME;
        }
        return SortType.NONE;
    }

    /**
     * Splits the user input into the recipe name, cook time, calories, allergies, meal category, and url.
     *
     * @param userInput input from the user in the command line.
     * @return an array of the recipe details extracted from the user input.
     */
    public static String[] splitUpAddInput(String userInput) {
        String[] words = userInput.trim().toLowerCase().split("\\s+", 2);
        try {
            String[] remainingInput = words[USER_INPUT_INDEX].trim().split(RECIPE_DELIMETER);
        } catch (ArrayIndexOutOfBoundsException e) {
            return new String[0];
        }
        String[] remainingInput = words[USER_INPUT_INDEX].trim().split(RECIPE_DELIMETER);
        for (int i = 0; i < remainingInput.length; i++) {
            remainingInput[i] = remainingInput[i].trim();
        }
        return remainingInput;
    }

    /**
     * Parses the user input to create a recipe object.
     *
     * @param userInput input from the user in the command line.
     * @return a recipe object.
     */
    public static Recipe parseAdd(String userInput) {
        String[] remainingInput = splitUpAddInput(userInput);
        assert remainingInput.length > 0 : "Add additional parameters to add command";
        return breakUpRemainingInput(remainingInput);
    }

    /**
     * Splits the user input into the recipe name, cook time, calories, allergies, meal category, and url.
     *
     * @param remainingInput input from the user in the command line.
     * @return an array of the recipe details extracted from the user input.
     */
    public static Recipe breakUpRemainingInput(String[] remainingInput) {
        String recipeName = remainingInput[InputParserConstants.RECIPE_NAME_INDEX].trim();
        int cookTime = Integer.parseInt(remainingInput[InputParserConstants.COOK_TIME_INDEX].trim());
        int calories = Integer.parseInt(remainingInput[InputParserConstants.CALORIES_INDEX].trim());
        String[] allergies = remainingInput[InputParserConstants.ALLERGIES_INDEX].trim().split("/");
        ArrayList<String> allergiesList = new ArrayList<>(List.of(allergies));
        allergiesList.replaceAll(String::trim);
        MealCategory category = MealCategory.valueOf(remainingInput[MEAL_CATEGORY_INDEX].trim().toUpperCase());
        String url = remainingInput[InputParserConstants.URL_INDEX].trim();
        return new Recipe(recipeName, cookTime, calories, allergiesList, category, LocalDate.now(), url);
    }

    /**
     * Returns the path of a given url if present, and returns an empty otherwise.
     * If only forward slash is present, return non-empty String.
     *
     * @param userInput User's url in the command line.
     * @return path of url, if any.
     */
    public static String getPath(String userInput) {
        try {
            if (userInput.endsWith("/")) {
                return "/";
            }
            return userInput.split("/")[1];
        } catch (Exception e) {
            return "";
        }
    }
}
