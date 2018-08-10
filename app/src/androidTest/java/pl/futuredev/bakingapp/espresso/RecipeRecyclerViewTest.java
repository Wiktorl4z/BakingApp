package pl.futuredev.bakingapp.espresso;

import android.content.res.Resources;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.ui.activities.RecipeActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class RecipeRecyclerViewTest {

    public static final String RECIPE_NAME = "Nutella Pie";

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(
            RecipeActivity.class);


    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();

        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void testClickAtPosition() {
        if (isTablet())
            onView(withId(R.id.my_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //   onView(withId(R.id.tv_recipe_name)).check(matches(withText(RECIPE_NAME)));
    }

    @Test
    public void clickRecipeActivityRecyclerViewOpensStepsActivity() {

        if (isTablet())
            onView(withId(R.id.my_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onData(withId(R.id.my_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(RECIPE_NAME)), click()));

/*
        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(RECIPE_NAME))));*/
    }

    private static Matcher<Object> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(Toolbar item) {
                return textMatcher.matches(item.getTitle());
            }
        };
    }

    private boolean isTablet() {
        Resources resources = getInstrumentation().getTargetContext().getResources();
        return resources.getBoolean(R.bool.isTablet);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
