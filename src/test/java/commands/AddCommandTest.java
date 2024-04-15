package commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static recipeio.enums.MealCategory.DINNER;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import recipeio.InputParser;
import recipeio.commands.AddRecipeCommand;
import recipeio.enums.MealCategory;
import recipeio.recipe.Recipe;
import recipeio.recipe.RecipeList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


public class AddCommandTest {
    public static final String MULTIPLE_ALLERGY_PARAM_INPUT = "add pizza, 34, 340, dairy/egg, dinner, www.url.com";
    public static final String MISSING_NAME_PARAM_INPUT = "add 34, 340, dairy/egg, dinner, www.url.com";
    public static final String MISSING_TIME_PARAM_INPUT = "add pizza, 340, dairy/egg, dinner, www.url.com";
    public static final String MISSING_KCAL_PARAM_INPUT = "add pizza, 34, dairy/egg, dinner, www.url.com";
    public static final String MISSING_ALLERGY_PARAM_INPUT = "add pizza, 34, 340, dinner, www.url.com";
    public static final String MISSING_MEAL_CAT_PARAM_INPUT = "add pizza, 34, 340, dairy/egg, www.url.com";
    public static final String MISSING_URL_PARAM_INPUT = "add pizza, 34, 340, dairy / egg, dinner";
    ArrayList<Recipe> recipes = new ArrayList<>();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testMultipleAllergyParseAdd() {
        recipes.clear();
        Recipe testRecipe = InputParser.parseAdd(MULTIPLE_ALLERGY_PARAM_INPUT);
        ArrayList<String> allergies = new ArrayList<>();
        allergies.add("dairy");
        allergies.add("egg");
        Recipe expectedParsedRecipe = new Recipe(
                "pizza",
                34,
                340,
                allergies,
                DINNER,
                LocalDate.now(),
                "www.url.com");
        AddRecipeCommand.execute(testRecipe, recipes);
        assertEquals(recipes.size(), 1);
        assertEquals(testRecipe.allergies.size(), 2);
        assertEquals(testRecipe.toString(), expectedParsedRecipe.toString());
        assertEquals(testRecipe.name, expectedParsedRecipe.name);
        assertEquals(testRecipe.cookTime, expectedParsedRecipe.cookTime);
        assertEquals(testRecipe.calories, expectedParsedRecipe.calories);
        assertEquals(testRecipe.allergies, expectedParsedRecipe.allergies);
        assertEquals(testRecipe.category, expectedParsedRecipe.category);
        assertEquals(testRecipe.dateAdded, expectedParsedRecipe.dateAdded);
        assertEquals(testRecipe.url, expectedParsedRecipe.url);
    }

    @Test
    public void testAddCommandWithCorrectInput() {
        recipes.clear();
        String input = "add sandwich, 10, 250, nuts, lunch, www.example.com";
        Recipe expectedRecipe = new Recipe("sandwich", 10, 250,
                new ArrayList<>(Arrays.asList("nuts")), MealCategory.LUNCH, LocalDate.now(), "www.example.com");
        Recipe actualRecipe = InputParser.parseAdd(input);
        AddRecipeCommand.execute(actualRecipe, recipes);
        assertEquals(1, recipes.size());
        assertEquals(expectedRecipe.toString(), actualRecipe.toString());
    }

    @Test
    public void testAddCommandWithExtraSpaces() {
        recipes.clear();
        String input = "add sandwich,  10, 250,  nuts, lunch,  www.example.com ";
        Recipe actualRecipe = InputParser.parseAdd(input);
        Recipe expectedRecipe = new Recipe("sandwich", 10, 250,
                new ArrayList<>(Arrays.asList("nuts")), MealCategory.LUNCH, LocalDate.now(), "www.example.com");
        AddRecipeCommand.execute(actualRecipe, recipes);
        assertEquals(1, recipes.size());
        assertEquals(expectedRecipe.toString(), actualRecipe.toString());
    }

    @Test
    public void testAddCommandWithIncorrectTypes() {
        recipes.clear();
        String input = "add sandwich, ten, two fifty, nuts, lunch, www.example.com";
        assertThrows(NumberFormatException.class, () -> InputParser.parseAdd(input));
    }

    @Test
    public void testAddCommandWithBoundaryValues() {
        recipes.clear();
        String input = "add massive sandwich, 0, 10000, nuts, dinner, www.bigrecipe.com";
        Recipe expectedRecipe = new Recipe("massive sandwich", 0, 10000,
                new ArrayList<>(Arrays.asList("nuts")), MealCategory.DINNER, LocalDate.now(), "www.bigrecipe.com");
        Recipe actualRecipe = InputParser.parseAdd(input);
        AddRecipeCommand.execute(actualRecipe, recipes);
        assertEquals(1, recipes.size());
        assertEquals(expectedRecipe.toString(), actualRecipe.toString());
    }

    @Test
    public void testAddCommandWithInvalidMealCategory() {
        recipes.clear();
        String input = "add sushi, 15, 300, fish, anyday, www.sushilover.com";
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseAdd(input));
    }

    @Test
    public void testAddCommandWithAllCaps() {
        recipes.clear();
        String input = "add PIZZA, 30, 300, CHEESE, DINNER, www.pizzaplace.com";
        Recipe expectedRecipe = new Recipe("pizza", 30, 300,
                new ArrayList<>(Arrays.asList("cheese")), MealCategory.DINNER, LocalDate.now(),
                "www.pizzaplace.com");
        Recipe actualRecipe = InputParser.parseAdd(input);
        AddRecipeCommand.execute(actualRecipe, recipes);
        assertEquals(1, recipes.size());
        assertEquals(expectedRecipe.toString(), actualRecipe.toString());
    }

    @Test
    public void testMissingNameParam() {
        recipes.clear();
        RecipeList recipesList = new RecipeList(recipes);
        String input = MISSING_NAME_PARAM_INPUT;
        assertThrows(NumberFormatException.class, () -> InputParser.parseAdd(input));
        recipesList.add(input);
        assertEquals(0, recipes.size());  // Check no recipes were added
        assertTrue(outContent.toString().contains("The add function accepts 6 parameters"));
        assertTrue(outContent.toString().contains("Tip: make sure you are not missing a comma anywhere!"));
    }

    @Test
    public void testMissingUrlParam() {
        recipes.clear();
        RecipeList recipesList = new RecipeList(recipes);
        String input = MISSING_URL_PARAM_INPUT;
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> InputParser.parseAdd(input));
        recipesList.add(input);
        assertEquals(0, recipes.size());  // Check no recipes were added
        assertTrue(outContent.toString().contains("The add function accepts 6 parameters"));
        assertTrue(outContent.toString().contains("Tip: make sure you are not missing a comma anywhere!"));
    }

    @Test
    public void testMissingCaloriesParam() {
        recipes.clear();
        RecipeList recipesList = new RecipeList(recipes);
        String input = MISSING_KCAL_PARAM_INPUT;
        assertThrows(NumberFormatException.class, () -> InputParser.parseAdd(input));
        recipesList.add(input);
        assertEquals(0, recipes.size());  // Check no recipes were added
        assertTrue(outContent.toString().contains("The add function accepts 6 parameters"));
        assertTrue(outContent.toString().contains("Tip: make sure you are not missing a comma anywhere!"));
    }

    @Test
    public void testMissingCookTimeParam() {
        recipes.clear();
        RecipeList recipesList = new RecipeList(recipes);
        String input = MISSING_TIME_PARAM_INPUT;
        assertThrows(NumberFormatException.class, () -> InputParser.parseAdd(input));
        recipesList.add(input);
        assertEquals(0, recipes.size());  // Check no recipes were added
        assertTrue(outContent.toString().contains("The add function accepts 6 parameters"));
        assertTrue(outContent.toString().contains("Tip: make sure you are not missing a comma anywhere!"));
    }

    @Test
    public void testMissingAllergiesParam() {
        recipes.clear();
        RecipeList recipesList = new RecipeList(recipes);
        String input = MISSING_ALLERGY_PARAM_INPUT;
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseAdd(input));
        recipesList.add(input);
        assertEquals(0, recipes.size());  // Check no recipes were added
        assertTrue(outContent.toString().contains("The add function accepts 6 parameters"));
        assertTrue(outContent.toString().contains("Tip: make sure you are not missing a comma anywhere!"));
    }

    @Test
    public void testMissingMealCatParam() {
        recipes.clear();
        RecipeList recipesList = new RecipeList(recipes);
        String input = MISSING_MEAL_CAT_PARAM_INPUT;
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseAdd(input));
        recipesList.add(input);
        assertEquals(0, recipes.size());  // Check no recipes were added
        assertTrue(outContent.toString().contains("The add function accepts 6 parameters"));
        assertTrue(outContent.toString().contains("Tip: make sure you are not missing a comma anywhere!"));
    }

    @Test
    public void testEmptyAddCommand() {
        recipes.clear();
        RecipeList recipesList = new RecipeList(recipes);
        String input = "add";
        recipesList.add(input);
        assertEquals(0, recipes.size());
        assertTrue(outContent.toString().contains("The add function accepts 6 parameters"));
    }

    @Test
    public void testRepetitiveAddInput() {
        recipes.clear();
        RecipeList recipesList = new RecipeList(recipes);
        String input = "add add add add add add add";
        recipesList.add(input);
        assertEquals(0, recipes.size());
        assertTrue(outContent.toString().contains("The add function accepts 6 parameters"));
    }

    @Test
    public void testInputWithoutCommas() {
        recipes.clear();
        RecipeList recipesList = new RecipeList(recipes);
        String input = "add pizza 30 300 dairy dinner www.url.com";
        recipesList.add(input);
        assertEquals(0, recipes.size());
        assertTrue(outContent.toString().contains("Tip: make sure you are not missing a comma anywhere!"));
    }

    @Test
    public void testTwoWordedAllergies() {
        recipes.clear();
        RecipeList recipesList = new RecipeList(recipes);
        String input = "add pizza, 30, 300, dairy/red meat, dinner, www.url.com";
        recipesList.add(input);
        assertEquals(1, recipes.size(), "One recipe should be added");
        Recipe addedRecipe = recipes.get(0);
        assertTrue(addedRecipe.getAllergies().contains("dairy") &&
                addedRecipe.getAllergies().contains("red meat"));
    }
}
