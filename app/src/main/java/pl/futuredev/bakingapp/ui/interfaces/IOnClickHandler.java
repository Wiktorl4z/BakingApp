package pl.futuredev.bakingapp.ui.interfaces;

import java.util.List;

import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Step;

public interface IOnClickHandler {

    void onClickStep(int index, String recipeName, Step step, int recipeID, List<Ingredient> ingredients);

}
