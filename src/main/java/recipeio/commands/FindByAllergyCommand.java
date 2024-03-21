package recipeio.commands;

import recipeio.recipe.Recipe;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
public class FindByAllergyCommand {

    /**
     * Returns a list of recipes with the allergy included
     *
     * @param allergy The allergy that the user is trying to filter by
     */

    private static final Logger logr = Logger.getLogger("FindByAllergyCommand");
    public static String execute(String allergy, ArrayList<Recipe> recipes) {
        logr.log(Level.INFO, "process started");
        int count = 0;
        String output = "";
        for (Recipe item: recipes) {
            for (String value : item.allergies) {
                logr.log(Level.INFO, "list allergies");
                if (value.contains(allergy)) {
                    output = "List of recipes with " + allergy + " mentioned:\n" + item.name + "\n";
                    count++;
                }
            }
        }
        //if no allergies are found
        if (count == 0) {
            output = "There are no recipes with " + allergy;
        }
        logr.log(Level.INFO, "process exited");
        return output;
    }
}
