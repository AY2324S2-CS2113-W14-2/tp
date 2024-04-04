package recipeio;

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
import static recipeio.constants.InputParserConstants.FIND_ALLERGY_INDEX;
import static recipeio.constants.InputParserConstants.MEAL_CATEGORY_INDEX;
import static recipeio.constants.InputParserConstants.RECIPE_DELIMETER;
import static recipeio.constants.InputParserConstants.USER_INPUT_INDEX;

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
            id = userInput.trim().split(" ")[InputParserConstants.INDEX_ID];
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
        String remainingInput = String.join(" ", Arrays.copyOfRange(words, ARRAY_START_INDEX, words.length));
        return remainingInput.trim().split(" ");
    }

    public static String parseFindType(String userInput) {
        return parseDetails(userInput)[FIND_TYPE_INDEX];
    }

    public static String parseFindCriteria(String userInput) {
        return parseDetails(userInput)[FIND_CRITERIA_INDEX];
    }

    public static String parseAllergyCriteria(String userInput) {
        return parseDetails(userInput)[FIND_ALLERGY_INDEX];
    }

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

    public static String[] splitUpAddInput(String userInput) {
        String[] words = userInput.trim().split(" ", 2);
        String[] remainingInput = words[USER_INPUT_INDEX].trim().split(RECIPE_DELIMETER);
        return remainingInput;
    }
    public static Recipe parseAdd(String userInput) throws Exception {
        String[] remainingInput = splitUpAddInput(userInput);
        assert remainingInput.length > 0 : "Add additional parameters to add command";
        return breakUpRemainingInput(remainingInput);
    }

    public static Recipe breakUpRemainingInput(String[] remainingInput) {
        String recipeName = remainingInput[InputParserConstants.RECIPE_NAME_INDEX].trim();
        int cookTime = Integer.parseInt(remainingInput[InputParserConstants.COOK_TIME_INDEX].trim());
        int calories = Integer.parseInt(remainingInput[InputParserConstants.CALORIES_INDEX].trim());
        String[] allergies = remainingInput[InputParserConstants.ALLERGIES_INDEX].trim().split(" ");
        ArrayList<String> allergiesList = new ArrayList<>(List.of(allergies));
        MealCategory category = MealCategory.valueOf(remainingInput[MEAL_CATEGORY_INDEX].trim().toUpperCase());
        String url = remainingInput[InputParserConstants.URL_INDEX].trim();
        System.out.println(url);
        return new Recipe(recipeName, cookTime, calories, allergiesList, category, LocalDate.now(), url);
    }
}
