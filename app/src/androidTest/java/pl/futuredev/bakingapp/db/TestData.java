package pl.futuredev.bakingapp.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.futuredev.bakingapp.database.RecipePOJO;
import pl.futuredev.bakingapp.models.Ingredient;

public class TestData {

    static final List<? extends Serializable> ingredient = Arrays.asList(12.5, "second", "third");

    static final RecipePOJO PRODUCT_ENTITY = new RecipePOJO(1, 2, "desc",
            (List<Ingredient>) ingredient);

}
