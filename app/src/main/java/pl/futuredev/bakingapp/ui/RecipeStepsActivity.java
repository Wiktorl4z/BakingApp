package pl.futuredev.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.models.Recipe;
import pl.futuredev.bakingapp.ui.adapter.SecondActivityAdapter;

public class RecipeStepsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        Recipe recipe = getIntent().getParcelableExtra("recipe");
        replaceFragment(recipe);
    }

    private void replaceFragment(Recipe recipe) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, SecondActivityAdapter.getInstance(recipe));
        ft.commit();
    }
}