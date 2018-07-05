package pl.futuredev.bakingapp.db;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import pl.futuredev.bakingapp.database.RecipeDataBase;
import pl.futuredev.bakingapp.database.RecipePOJO;
import pl.futuredev.bakingapp.models.AppExecutors;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RecipeDaoTest {

    @Rule
    private AppExecutors appExecutors;

    private RecipePOJO recipePOJO;

    private RecipeDataBase recipeDataBase;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        recipeDataBase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RecipeDataBase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        recipePOJO = (RecipePOJO) recipeDataBase.recipeDao();
    }

    @After
    public void closeDb() throws Exception {
        recipeDataBase.close();
    }

    @Test
    public void getRecipeWhenNoRecipeInserted() throws InterruptedException {
        List<RecipePOJO> products = (List<RecipePOJO>) recipeDataBase.recipeDao().loadAllRecipes();

        assertTrue(products.isEmpty());
    }
}
