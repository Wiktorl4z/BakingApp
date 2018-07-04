package pl.futuredev.bakingapp.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import pl.futuredev.bakingapp.database.RecipeDataBase;
import pl.futuredev.bakingapp.database.RecipePOJO;

public class AddRecipeViewModel extends ViewModel {

    private LiveData<RecipePOJO> recipe;

    public AddRecipeViewModel(RecipeDataBase recipeDataBase, int recipeID) {
        recipe = recipeDataBase.recipeDao().loadRecipeByID(recipeID);
    }

    public LiveData<RecipePOJO> getRecipe() {
        return recipe;
    }
}
