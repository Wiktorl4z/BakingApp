package pl.futuredev.bakingapp;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.futuredev.bakingapp.adapter.IngredientsAdapter;
import pl.futuredev.bakingapp.database.RecipeDao;
import pl.futuredev.bakingapp.database.RecipeDataBase;
import pl.futuredev.bakingapp.database.RecipePOJO;
import pl.futuredev.bakingapp.models.AddRecipeViewModel;
import pl.futuredev.bakingapp.models.AddRecipeViewModelFactory;
import pl.futuredev.bakingapp.models.AppExecutors;
import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Step;

public class ThirdActivity extends AppCompatActivity {

    @BindView(R.id.video_view)
    PlayerView videoView;
    @BindView(R.id.tv_short_description)
    TextView tvTitleDescription;
    @BindView(R.id.tv_step_description)
    TextView tvStepDescription;
    @BindView(R.id.tv_ingredients_left)
    TextView tvIngredientsLeft;
    @BindView(R.id.ingredients_recycler_view)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.toolbarThird)
    Toolbar toolbarThird;
    private ExoPlayer player;
    private LinearLayoutManager linearLayoutManager;
    private Step step;
    private List<Ingredient> ingredients;
    private RecyclerView.Adapter adapter;
    private RecipePOJO recipePOJO;
    private String recipeName;
    boolean widgetChecked;
    private RecipeDataBase recipeDataBase;
    private AddRecipeViewModelFactory addRecipeViewModelFactory;
    private int recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarThird);

        recipeDataBase = RecipeDataBase.getInstance(getApplicationContext());

        step = getIntent().getParcelableExtra("step");
        recipeID = Integer.parseInt(getIntent().getExtras().get("id").toString());
        recipeName = getIntent().getStringExtra("recipeName");
        ingredients = getIntent().getParcelableArrayListExtra("ingredients");

        tvTitleDescription.setText(step.getShortDescription());
        tvStepDescription.setText(step.getDescription());

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ingredientsRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new IngredientsAdapter(ingredients);
        ingredientsRecyclerView.setAdapter(adapter);
    }

    public void removingRecipeFromDatabase() {
        addRecipeViewModelFactory = new AddRecipeViewModelFactory(recipeDataBase, recipeID);
        final AddRecipeViewModel viewModel = ViewModelProviders.of(this, addRecipeViewModelFactory)
                .get(AddRecipeViewModel.class);
        viewModel.getRecipe().observe(this, new Observer<RecipePOJO>() {
                    @Override
                    public void onChanged(@Nullable RecipePOJO recipePOJO) {
                        recipeDataBase.recipeDao().deleteRecipe(recipePOJO);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_third_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                if (!widgetChecked) {
                    addToDatabase();
                    Toast.makeText(this, "Add to Widget", Toast.LENGTH_SHORT).show();
                    widgetChecked = true;
                } else {
                    removeFromDatabase();
                    Toast.makeText(this, "Widget Removed", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addToDatabase() {
        final RecipePOJO recipe = new RecipePOJO(recipeID, recipeName, ingredients);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipeDataBase.recipeDao().insertRecipe(recipe);
                finish();
            }
        });
    }

    public void removeFromDatabase() {
        final RecipePOJO recipe = new RecipePOJO(recipeID, recipeName, ingredients);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipeDataBase.recipeDao().deleteRecipe(recipe);
                finish();
            }
        });
    }

    private void initializePlayer(Step step) {
        Uri uri = Uri.parse(step.getVideoURL());
        if (uri == null || uri.equals(Uri.EMPTY)) {
            videoView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.brownies));
        } else {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            player.setPlayWhenReady(true);
            player.seekTo(0, 0);
            videoView.setPlayer(player);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
        }

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(step);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(step);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            long playbackPosition = player.getCurrentPosition();
            int currentWindow = player.getCurrentWindowIndex();
            boolean playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }
}
