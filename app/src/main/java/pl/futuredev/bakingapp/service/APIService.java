package pl.futuredev.bakingapp.service;

import java.util.List;

import pl.futuredev.bakingapp.models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

//    @GET("raw/WjKgw7Dv/")
    @GET("android-baking-app-json/")
    Call<List<Recipe>> getRecipes();
}
