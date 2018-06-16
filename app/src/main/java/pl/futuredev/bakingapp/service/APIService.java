package pl.futuredev.bakingapp.service;

import java.util.List;

import pl.futuredev.bakingapp.models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<Recipe> getRecipes();
}
