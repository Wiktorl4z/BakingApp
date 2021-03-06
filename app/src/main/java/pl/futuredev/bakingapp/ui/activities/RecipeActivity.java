package pl.futuredev.bakingapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Recipe;
import pl.futuredev.bakingapp.models.Step;
import pl.futuredev.bakingapp.service.APIService;
import pl.futuredev.bakingapp.service.HttpConnector;
import pl.futuredev.bakingapp.service.InternetReceiver;
import pl.futuredev.bakingapp.ui.adapter.RecipeAdapter;
import pl.futuredev.bakingapp.ui.interfaces.IOnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity implements IOnClick {

    private static final String RECIPE = "recipe";

    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    private InternetReceiver internetReceiver;
    private APIService service;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter adapter;
    private Step step;
    private List<Recipe> recipes;
    private List<Ingredient> ingredient;
    private boolean tablet;
    private GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        setSupportActionBar(toolBar);

        internetReceiver = new InternetReceiver();
        service = HttpConnector.getService(APIService.class);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(this, 2);

        if (findViewById(R.id.activity_main_tablet) != null) {
            tablet = true;
        }

        service.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                settingUpView(response);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(RecipeActivity.this, t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Log.d(getLocalClassName(), t.getMessage());
            }
        });
    }

    private void settingUpView(Response<List<Recipe>> response) {
        if (response.isSuccessful()) {
            recipes = response.body();
            if (tablet) {
                adapter = new RecipeAdapter(recipes, getApplicationContext(), RecipeActivity.this::onClick);
                myRecyclerView.setLayoutManager(gridLayoutManager);
                myRecyclerView.setAdapter(adapter);
            } else {
                adapter = new RecipeAdapter(recipes, getApplicationContext(), RecipeActivity.this::onClick);
                myRecyclerView.setLayoutManager(linearLayoutManager);
                myRecyclerView.setAdapter(adapter);
            }
        }
    }

    ;

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, RecipeStepsActivity.class);
        intent.putExtra(RECIPE, recipes.get(position));
        startActivity(intent);
    }
}

