package pl.futuredev.bakingapp.db;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import pl.futuredev.bakingapp.LiveDataTestUtil;
import pl.futuredev.bakingapp.database.RecipeDao;
import pl.futuredev.bakingapp.database.RecipeDataBase;
import pl.futuredev.bakingapp.database.RecipePOJO;
import pl.futuredev.bakingapp.models.AppExecutors;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static pl.futuredev.bakingapp.db.TestData.PRODUCT_ENTITY;

@RunWith(AndroidJUnit4.class)
public class RecipeDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    public AppExecutors appExecutors;
    private RecipePOJO recipePOJO;
    private RecipeDao recipeDao;
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
        recipeDao = recipeDataBase.recipeDao();
    }

    @After
    public void closeDb() throws Exception {
        recipeDataBase.close();
    }

    @Test
    public void getRecipeWhenNoRecipeInserted() throws InterruptedException {
        List<RecipePOJO> products = LiveDataTestUtil.getValue(recipeDao.loadAllRecipes());
        assertTrue(products.isEmpty());
    }


    @Test
    public void getRecipesAfterInserted() throws InterruptedException {
        recipeDao.insertRecipe(PRODUCT_ENTITY);
        List<RecipePOJO> products = LiveDataTestUtil.getValue(recipeDao.loadAllRecipes());
        assertThat(products.size(), is(1));
    }

    @Test
    public void getRecipesAfterDelete() throws InterruptedException {
        recipeDao.deleteRecipe(PRODUCT_ENTITY);
        List<RecipePOJO> products = LiveDataTestUtil.getValue(recipeDao.loadAllRecipes());
        assertThat(products.size(), is(0));
    }


}
