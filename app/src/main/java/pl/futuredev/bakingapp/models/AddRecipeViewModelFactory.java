package pl.futuredev.bakingapp.models;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import pl.futuredev.bakingapp.database.RecipeDataBase;

public class AddRecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeDataBase recipeDataBase;
    private final int recipeID;

    public AddRecipeViewModelFactory(RecipeDataBase recipeDataBase, int recipeID) {
        this.recipeDataBase = recipeDataBase;
        this.recipeID = recipeID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddRecipeViewModel(recipeDataBase, recipeID);
    }
}
