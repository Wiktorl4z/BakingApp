package pl.futuredev.bakingapp.service;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpConnector {

    private static Retrofit retrofit;

    //https://pastebin.com/raw/WjKgw7Dv
    //https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
//    public static final String BASE_URL = "https://pastebin.com/";

    private HttpConnector() {
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            return createRetrofit();
        }
        return retrofit;
    }

    public static <T> T getService(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }

    @NonNull
    private static Retrofit createRetrofit() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClientBuilder.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(HttpConnector.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
