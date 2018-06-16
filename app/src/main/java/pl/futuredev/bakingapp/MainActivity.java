package pl.futuredev.bakingapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private Recipe recipe;
    private Step step;
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

        service.getRecipes().enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                settingUpView(response);
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
        private void settingUpView (Response <Recipe> response){
            if (response.isSuccessful()) {
                recipe = response.body();
                adapter = new RecipeAdapter(recipe, MainActivity.this::onClick);
                myRecyclerView.setHasFixedSize(true);
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
        };

        @Override
        public void onClick ( int clickedItemIndex){

        }
    }

