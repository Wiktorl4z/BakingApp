package pl.futuredev.bakingapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import pl.futuredev.bakingapp.models.Ingredient;

@Entity(tableName = "recipe", indices = {@Index(value = "recipe_id", unique = true)})
public class RecipePOJO extends BaseObservable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ColumnInfo(name = "recipe_id")
    private int recipeID;
    @ColumnInfo(name = "recipe_name")
    private String name;
    @TypeConverters(IngredientConverter.class)
    @ColumnInfo(name = "ingredients")
    private List<Ingredient> ingredient;

    public RecipePOJO(int id, int recipeID, String name, List<Ingredient> ingredient) {
        this.id = id;
        this.recipeID = recipeID;
        this.name = name;
        this.ingredient = ingredient;
    }

    @Ignore
    public RecipePOJO(int recipeID, String name, List<Ingredient> ingredient) {
        this.recipeID = recipeID;
        this.name = name;
        this.ingredient = ingredient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredient() {
        if (ingredient == null) {
            ingredient = new ArrayList<>();
            return ingredient;
        }
        return ingredient;
    }

    public void setIngredient(List<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }
}
