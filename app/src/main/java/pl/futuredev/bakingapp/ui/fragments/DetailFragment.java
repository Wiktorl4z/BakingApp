package pl.futuredev.bakingapp.ui.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.database.entity.RecipeDataBase;
import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Step;
import pl.futuredev.bakingapp.ui.adapter.IngredientsAdapter;

public class DetailFragment extends Fragment {

    private static final String PLAYER_POSITION = "exo_position";
    private static final String PLAYER_READY = "exo_ready";

    @BindView(R.id.video_view)
    PlayerView videoView;
    @BindView(R.id.ingredients_recycler_view)
    RecyclerView ingredientsRecyclerView;
    Unbinder unbinder;

    private long playerPosition;
    private boolean playbackReady = true;
    private int recipeID;
    private Step step;
    private String recipeName;
    private List<Ingredient> ingredients;
    private RecipeDataBase recipeDataBase;
    private ExoPlayer player;
    private ImageView ivDetail;
    private TextView tvShortDescription;
    private TextView tvStepDescription;
    private TextView tvIngredient;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter adapter;
    private int currentWindow;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public DetailFragment() {
    }

    public static DetailFragment getInstance(Step step, List<Ingredient> ingredients, String recipeName, int recipeID) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("recipeName", recipeName);
        args.putParcelable("step", step);
        args.putInt("id", recipeID);
        args.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, parent, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playbackReady = savedInstanceState.getBoolean(PLAYER_READY);
        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            step = getArguments().getParcelable("step");
            recipeName = getArguments().getString("recipeName");
            ingredients = getArguments().getParcelableArrayList("ingredients");
            recipeID = getArguments().getInt("id");
        }

        recipeDataBase = RecipeDataBase.getInstance(getContext());
        settingUpView(rootView);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ingredientsRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new IngredientsAdapter(ingredients);
        ingredientsRecyclerView.setAdapter(adapter);

        return rootView;
    }

    private void settingUpView(View rootView) {
        ivDetail = rootView.findViewById(R.id.iv_detail);
        tvShortDescription = rootView.findViewById(R.id.tv_short_description);
        tvStepDescription = rootView.findViewById(R.id.tv_step_description);
        tvIngredient = rootView.findViewById(R.id.tv_ingredients_text);

        tvShortDescription.setText(step.getShortDescription());
        tvStepDescription.setText(step.getDescription());
    }

    private void initializePlayer(Step step) {
        Uri uri = Uri.parse(step.getVideoURL());
        if (uri == null || uri.equals(Uri.EMPTY)) {
            videoView.setVisibility(View.GONE);
            ivDetail.setImageResource(R.drawable.baking);
        } else {
            ivDetail.setVisibility(View.GONE);
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            playerPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playbackReady = player.getPlayWhenReady();
        }
        outState.putLong(PLAYER_POSITION, playerPosition);
        outState.putBoolean(PLAYER_READY, playbackReady);
    }
}