package pl.futuredev.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import pl.futuredev.bakingapp.adapter.RecipeAdapter;
import pl.futuredev.bakingapp.adapter.SecondActivityAdapter;
import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Recipe;
import pl.futuredev.bakingapp.models.Step;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        Recipe recipe = getIntent().getParcelableExtra("recipe");
        int position = getIntent().getIntExtra("id", 0);

        replaceFragment(recipe, position);
    }

    private void replaceFragment(Recipe recipe, int itemId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, SecondActivityAdapter.getInstance(recipe, itemId));
        ft.commit();
    }
}
