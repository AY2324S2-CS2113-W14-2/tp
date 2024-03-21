package recipeio.commands;

import recipeio.recipe.Recipe;
import recipeio.ui.UI;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
public class DeleteRecipeCommand {

    /**
     * Deletes the Recipe from the recipe list.
     *
     * @param recipeNumber The recipe number from the user.
     */
    private static final Logger logr = Logger.getLogger("DeleteRecipeCommand");
    public static void execute(int recipeNumber, ArrayList<Recipe> recipes) {
        recipeNumber = recipeNumber - 1;
        if (recipeNumber >= recipes.size() || recipeNumber < 0) {
            System.out.println("Sorry, there were no recipes with that number.");
            logr.log(Level.WARNING, "recipeNumber out of bound");
        } else {
            Recipe selectedRecipe = recipes.get(recipeNumber);
            recipes.remove(recipeNumber);
            UI.printDeleteMessage(selectedRecipe, recipes.size());
            logr.log(Level.INFO, "recipe deleted");
        }
    }
}
