package recipeio.commands;

import recipeio.Constants;
import recipeio.InputParser;
import recipeio.CommandValidator;
import recipeio.recipe.Recipe;
import recipeio.ui.UI;
import java.time.LocalDate;
import java.util.ArrayList;

public class FindCommand {
    public static void execute(String userInput, ArrayList<Recipe> recipes) {
        if (!CommandValidator.isValidFindCommand(userInput)) {
            return;
        }
        String findType = InputParser.parseFindType(userInput);
        String criteria = InputParser.parseFindCriteria(userInput);
        switch (findType) {
        case (Constants.FIND_BY_KEYWORD):
            if (!CommandValidator.isWord(criteria)) {
                return;
            }
            findKeyword(criteria, recipes);
            break;
        case (Constants.FIND_BY_DATE):
            if (!CommandValidator.isParsableAsDate(criteria)) {
                return;
            }
            LocalDate date = LocalDate.parse(criteria);
            findDate(date, recipes);
            break;
        default:
            System.out.println("\tSorry, please follow one of the find command formats.");
            System.out.println("\tAccepted find parameters are: 'kw' and 'date'.");
        }
    }
    public static void findKeyword(String keyword, ArrayList<Recipe> recipes) {
        ArrayList<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (CommandValidator.splitName(recipe.getName()).contains(keyword)) {
                matches.add(recipe);
            }
        }
        if (matches.isEmpty()) {
            System.out.println(Constants.NO_MATCHES_ERROR_MESSAGE);
            return;
        }
        System.out.println("\tHere are your matches with keyword: " + keyword);
        UI.printRecipes(matches);
    }

    public static void findDate(LocalDate date, ArrayList<Recipe> recipes) {
        ArrayList<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.dateAdded.isEqual(date)) {
                matches.add(recipe);
            }
        }
        if (matches.isEmpty()) {
            System.out.println(Constants.NO_MATCHES_ERROR_MESSAGE);
            return;
        }
        System.out.println("\tHere are your matches with date: " + date);
        UI.printRecipes(matches);
    }

}
