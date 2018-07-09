package pl.futuredev.bakingapp.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import pl.futuredev.bakingapp.models.Ingredient;

public class IngredientConverter implements Serializable {

    @TypeConverter
    public String fromIngredientsValuesList(List<Ingredient> ingredientsPOJOS) {
        if (ingredientsPOJOS == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {
        }.getType();
        String json = gson.toJson(ingredientsPOJOS, type);
        return json;
    }

    @TypeConverter
    public List<Ingredient> toIngredientsList(String ingredientsList) {
        if (ingredientsList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {
        }.getType();
        List<Ingredient> productCategoriesList = gson.fromJson(ingredientsList, type);
        return productCategoriesList;
    }
}
