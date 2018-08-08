package pl.futuredev.bakingapp.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Recipe;
import pl.futuredev.bakingapp.models.Step;
import pl.futuredev.bakingapp.ui.fragments.DetailFragment;
import pl.futuredev.bakingapp.ui.fragments.StepsFragment;

public class RecipeStepsActivity extends AppCompatActivity {

    private boolean tablet;
    private Step step;
    private List<Ingredient> ingredients;
    private String recipeName;
    private int recipeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_fragment);


       /* step = getIntent().getParcelableExtra("step");
        recipeID = Integer.parseInt(getIntent().getExtras().get("id").toString());
        recipeName = getIntent().getStringExtra("recipeName");
        ingredients = getIntent().getParcelableArrayListExtra("ingredients");*/


        Recipe recipe = getIntent().getParcelableExtra("recipe");

        if (findViewById(R.id.container) != null) {
            tablet = true;
            replaceStepFragment(recipe);
        } else {
            tablet = false;
            replaceStepFragment(recipe);
        }

    }

    private void replaceStepFragment(Recipe recipe) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, StepsFragment.getInstance(recipe));
        ft.commit();
    }

    private void replaceDetailFragment(Step step,List<Ingredient> ingredients,String recipeName,int recipeID) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, DetailFragment.getInstance(step,ingredients,recipeName,recipeID));
        ft.commit();
    }


}
