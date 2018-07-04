package pl.futuredev.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<RecipePOJO>> loadRecipe();

    @Insert
    void insertRecipe(RecipePOJO recipePOJO);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipePOJO recipePOJO);

    @Delete
    void deleteRecipe(RecipePOJO recipePOJO);

    @Query("SELECT * FROM recipe WHERE id = :recipeName")
    LiveData<RecipePOJO> loadRecipeByID(int recipeName);
}
