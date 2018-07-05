package pl.futuredev.bakingapp.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.database.RecipeDataBase;
import pl.futuredev.bakingapp.database.RecipePOJO;

public class ViewModel extends AndroidViewModel{

    private static final String TAG = ViewModel.class.getSimpleName();
    private LiveData<List<RecipePOJO>> listRecipePOJO;

    public ViewModel(@NonNull Application application) {
        super(application);
        RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(this.getApplication());
        Log.d(TAG,application.getString(R.string.retrieving_recipes_from_db));
        listRecipePOJO = recipeDataBase.recipeDao().loadAllRecipes();
    }

    public LiveData<List<RecipePOJO>> getListRecipePOJO() {
        return listRecipePOJO;
    }
}
