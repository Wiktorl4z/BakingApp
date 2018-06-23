package pl.futuredev.bakingapp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThirdActivity extends AppCompatActivity {

    @BindView(R.id.video_view)
    PlayerView videoView;
    @BindView(R.id.tv_title_description)
    TextView tvTitleDescription;
    @BindView(R.id.tv_step_description)
    TextView tvStepDescription;
    @BindView(R.id.tv_ingredients_left)
    TextView tvIngredientsLeft;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        videoView.setPlayer(player);

        player.setPlayWhenReady(true);
        player.seekTo(0, 0);

        Uri uri = Uri.parse(getString(R.string.url_mp3));
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
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
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
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
