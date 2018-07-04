package pl.futuredev.bakingapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.futuredev.bakingapp.models.Ingredient;

@Entity(tableName = "recipe")
public class RecipePOJO extends BaseObservable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "recipe_name")
    private String name;
    @TypeConverters(IngredientConverter.class)
    @ColumnInfo(name = "ingredients")
    private List<Ingredient> ingredient;

    public RecipePOJO(int id, String name, List<Ingredient> ingredient) {
        this.id = id;
        this.name = name;
        this.ingredient = ingredient;
    }

    @Ignore
    public RecipePOJO(String name, List<Ingredient> ingredient) {
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
}
