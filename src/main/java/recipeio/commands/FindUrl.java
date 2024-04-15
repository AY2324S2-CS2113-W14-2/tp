//@@author chenxk619
package recipeio.commands;

import recipeio.InputParser;
import recipeio.constants.CommandConstants;
import recipeio.recipe.Recipe;
import recipeio.ui.UI;

import java.util.ArrayList;

import static recipeio.constants.CommandConstants.NO_MATCHES_ERROR;

/**
 * Command for finding recipes based on a URL.
 */
public class FindUrl {

    /**
     * This searches in the recipe list for a match in domain of the url,
     * or an exact match if url path is given.
     *
     * @param url User's url to be found.
     * @param recipes List of available recipes to search through.
     */
    public static void execute(String url, ArrayList<Recipe> recipes) {
        url = url.toLowerCase();
        ArrayList<Integer> listNumbers = new ArrayList<>();
        ArrayList<Recipe> urlMatches = new ArrayList<>();
        Integer count = CommandConstants.STARTING_COUNT;

        for (Recipe recipe : recipes) {
            String domain = recipe.getURL();
            // If the recipe contains a path but the url does not
            if (domain.contains("/") && InputParser.getPath(url).isEmpty()) {
                domain = recipe.getURL().split("/")[0];
            }
            if (domain.matches(url)) {
                urlMatches.add(recipe);
                listNumbers.add(count);
            }
            count ++;
        }
        if (urlMatches.isEmpty()) {
            System.out.println(NO_MATCHES_ERROR);
            return;
        }
        System.out.println(CommandConstants.VALID_URL_MATCHES + url + "\n");
        UI.printRecipes(urlMatches, listNumbers);
    }
}
