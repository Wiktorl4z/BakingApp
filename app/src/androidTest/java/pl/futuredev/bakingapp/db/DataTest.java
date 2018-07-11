package pl.futuredev.bakingapp.db;

import java.util.ArrayList;
import java.util.List;

import pl.futuredev.bakingapp.database.entity.RecipePOJO;
import pl.futuredev.bakingapp.models.Ingredient;

public class DataTest {


    static Ingredient ingredient = new Ingredient(1.0, "onion", "tree");

    static List<Ingredient> ingredientList = new ArrayList<Ingredient>() {{
        add(ingredient);
    }};

    static final RecipePOJO PRODUCT_ENTITY = new RecipePOJO(1, 2, "desc", ingredientList);

}
