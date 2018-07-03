package pl.futuredev.bakingapp.database;

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
    List<RecipePOJO> loadRecipeWidgetPOJO();

    @Insert
    void insertRecipeWidget(RecipePOJO recipePOJO);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipeWidget(RecipePOJO recipePOJO);

    @Delete
    void deleteRecipeWidget(RecipePOJO recipePOJO);
}
