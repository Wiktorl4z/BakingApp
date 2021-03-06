package pl.futuredev.bakingapp.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import pl.futuredev.bakingapp.database.entity.RecipePOJO;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<RecipePOJO>> loadAllRecipes();

    @Query("SELECT * FROM recipe")
    List<RecipePOJO> loadAllRecipesSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(RecipePOJO recipePOJO);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipePOJO recipePOJO);

    @Delete
    void deleteRecipe(RecipePOJO recipePOJO);

    @Query("SELECT * FROM recipe WHERE recipe_id = :recipeID")
    LiveData<RecipePOJO> loadRecipeByID(int recipeID);

    @Query("SELECT * FROM recipe WHERE recipe_id = :recipeID")
    RecipePOJO loadRecipeByIDRecipePOJO(int recipeID);
}
