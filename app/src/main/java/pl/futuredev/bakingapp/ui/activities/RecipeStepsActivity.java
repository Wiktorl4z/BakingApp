package pl.futuredev.bakingapp.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Recipe;
import pl.futuredev.bakingapp.models.Step;
import pl.futuredev.bakingapp.ui.fragments.DetailFragment;
import pl.futuredev.bakingapp.ui.fragments.StepsFragment;
import pl.futuredev.bakingapp.ui.interfaces.IOnClickHandler;

public class RecipeStepsActivity extends AppCompatActivity implements IOnClickHandler {

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    private boolean tablet;
    private Step step;
    private List<Ingredient> ingredients;
    private String recipeName;
    private int recipeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_fragment);
        ButterKnife.bind(this);

        Recipe recipe = getIntent().getParcelableExtra("recipe");
        recipeName = recipe.getName();
        toolbar.setLogoDescription(recipeName);
        setTitle(recipeName);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.detail_fragment) != null) {
            tablet = true;
            getSupportActionBar().hide();
            replaceStepFragment(recipe, tablet);
        } else {
            tablet = false;
            getSupportActionBar();
            replaceStepFragment(recipe, tablet);
        }
    }

    private void replaceStepFragment(Recipe recipe, boolean tablet) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, StepsFragment.getInstance(recipe, tablet));
        ft.commit();
    }

    @Override
    public void onClickStep(int index, String recipeName, Step step, int recipeID, List<Ingredient> ingredients) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.detail_fragment, DetailFragment.getInstance(step, ingredients, recipeName, recipeID));
        ft.commit();
    }
}
