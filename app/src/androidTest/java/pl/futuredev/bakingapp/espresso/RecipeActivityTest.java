package pl.futuredev.bakingapp.espresso;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.ui.RecipeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import android.support.test.espresso.contrib.RecyclerViewActions;

import static android.support.test.espresso.Espresso.onData;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    public static final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(
            RecipeActivity.class);


    @Test
    public void clickRecipeOpensActivity() {
        onView(withId(R.id.my_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

}
