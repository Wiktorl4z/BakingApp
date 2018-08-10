package pl.futuredev.bakingapp.ui.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.database.entity.RecipeDataBase;
import pl.futuredev.bakingapp.database.entity.RecipePOJO;
import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Step;
import pl.futuredev.bakingapp.ui.adapter.IngredientsAdapter;
import pl.futuredev.bakingapp.viewmodel.AddRecipeViewModelFactory;
import pl.futuredev.bakingapp.viewmodel.AppExecutors;

public class StepDetailActivity extends AppCompatActivity {

    private static final String PLAYER_POSITION = "exo_position";
    private static final String PLAYER_READY = "exo_ready";

    @BindView(R.id.video_view)
    PlayerView videoView;
    @BindView(R.id.tv_short_description)
    TextView tvTitleDescription;
    @BindView(R.id.tv_step_description)
    TextView tvStepDescription;
    @BindView(R.id.tv_ingredients_text)
    TextView tvIngredientsLeft;
    @BindView(R.id.ingredients_recycler_view)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    private ExoPlayer player;
    private LinearLayoutManager linearLayoutManager;
    private Step step;
    private List<Ingredient> ingredients;
    private RecyclerView.Adapter adapter;
    private RecipePOJO recipe;
    private String recipeName;
    private static boolean widgetChecked;
    private RecipeDataBase recipeDataBase;
    private AddRecipeViewModelFactory addRecipeViewModelFactory;
    private int recipeID;
    private long playerPosition;
    private boolean playbackReady = true;
    private int currentWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playbackReady = savedInstanceState.getBoolean(PLAYER_READY);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_third_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                checkingObjectInDataBase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkingObjectInDataBase() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                RecipePOJO recipe = recipeDataBase.recipeDao().loadRecipeByIDRecipePOJO(recipeID);
                if (recipe != null) {
                    removeFromDatabase(recipe);
                } else {
                    addToDatabase();
                }
            }
        });
    }

    public void addToDatabase() {
        recipe = new RecipePOJO(recipeID, recipeName, ingredients);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipeDataBase.recipeDao().insertRecipe(recipe);
                showToast(getString(R.string.add_to_widget));
            }
        });
    }

    public void removeFromDatabase(RecipePOJO recipe) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipeDataBase.recipeDao().deleteRecipe(recipe);
                showToast(getString(R.string.widget_removed));
            }
        });
    }

    public void showToast(final String toast) {
        runOnUiThread(() -> Toast.makeText(this, toast, Toast.LENGTH_SHORT).show());
    }

    private void initializePlayer(Step step) {
        Uri uri = Uri.parse(step.getVideoURL());
        if (uri == null || uri.equals(Uri.EMPTY)) {
            videoView.setVisibility(View.GONE);
            ivDetail.setImageResource(R.drawable.baking);
        } else {
            ivDetail.setVisibility(View.GONE);
            if (player == null) {
                player = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(this),
                        new DefaultTrackSelector(), new DefaultLoadControl());
                MediaSource mediaSource = buildMediaSource(uri);
                player.prepare(mediaSource);
                player.setPlayWhenReady(true);
                player.seekTo(currentWindow, playerPosition);
                videoView.setPlayer(player);
            }
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
            playerPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playbackReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            playerPosition = player.getCurrentPosition();
            playbackReady = player.getPlayWhenReady();
        }
        outState.putLong(PLAYER_POSITION, playerPosition);
        outState.putBoolean(PLAYER_READY, playbackReady);
    }
}
