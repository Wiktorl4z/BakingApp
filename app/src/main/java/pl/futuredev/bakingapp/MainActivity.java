package pl.futuredev.bakingapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Recipe;
import pl.futuredev.bakingapp.models.Step;
import pl.futuredev.bakingapp.service.APIService;
import pl.futuredev.bakingapp.service.HttpConnector;
import pl.futuredev.bakingapp.service.InternetReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements IOnClickHandler {

    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    private InternetReceiver internetReceiver;
    private APIService service;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter adapter;
    private Step step;
    private List<Recipe> recipes;
    private List<Ingredient> ingredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        internetReceiver = new InternetReceiver();
        service = HttpConnector.getService(APIService.class);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

/*        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(linearLayoutManager);
        myRecyclerView.setAdapter(adapter);*/

        service.getRecipes().enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                settingUpView(response);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Log.d(getLocalClassName(), t.getMessage());
            }
        });
    }

    private void settingUpView(Response<List<Recipe>> response) {
        if (response.isSuccessful()) {
            recipes = response.body();
            adapter = new RecipeAdapter(recipes,getApplicationContext(), MainActivity.this::onClick);
            myRecyclerView.setLayoutManager(linearLayoutManager);
            myRecyclerView.setAdapter(adapter);
        } else {
            try {
                Toast.makeText(MainActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT)
                        .show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ;

    @Override
    public void onClick(int clickedItemIndex) {
        Toast.makeText(this, "Dziala czy nie dziala"+ clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}

