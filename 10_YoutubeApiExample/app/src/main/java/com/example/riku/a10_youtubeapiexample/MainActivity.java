package com.example.riku.a10_youtubeapiexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class MainActivity extends YouTubeBaseActivity {

    private YouTubePlayer player;
    private YouTubePlayer.OnInitializedListener youTubeInitializedListener;
    // listens player actions, start, stop, end etc
    private CustomYoutubePlayerStateListener youTubePlayerStateListener;
    private SeekBar seekbar;
    List<String> playlist = Arrays.asList("55ZBcH_b7rk", "3NbgMu2IVfo", "m9SMG3umU10", "sDepsI_ocQA", "g0ay8mKaYqk");
    private int currentVideo = 0;
    private int currentDuration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        YouTubePlayerView youTubeView = findViewById(R.id.youtube_view);

        youTubeInitializedListener = new YouTubePlayer.OnInitializedListener() {
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;
                player.loadVideo(playlist.get(0));
                player.setPlayerStateChangeListener(new CustomYoutubePlayerStateListener());
                currentVideo = 0;
                showToast("Player initialized!");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                showToast("Error when initializing YouTubePlayer" + youTubeInitializationResult);
                Log.e("error", String.valueOf(youTubeInitializationResult));

            }
        };

        youTubeView.initialize(Config.YOUTUBE_API_KEY, youTubeInitializedListener);
    }

    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
    }

    public void handlePlayButtonClick(View view) {
        if (player.isPlaying()) {
            player.pause();
        } else if (!player.isPlaying()) {
            player.play();
        }
    }

    public void handleChangeSongButtonClick(View view) {
        if (player.isPlaying()) {
            player.pause();
        }

        if (view.getId() == R.id.previousButton) {
            // play previous
            player.loadVideo(playlist.get(getNextSongIndex("previous")));
        } else if (view.getId() == R.id.nextButton) {
            //play next
            player.loadVideo(playlist.get(getNextSongIndex("next")));

        }

    }

    private class CustomYoutubePlayerStateListener implements YouTubePlayer.PlayerStateChangeListener {
        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
            showToast("loaded, start playing");
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
            currentDuration = player.getCurrentTimeMillis();
        }

        @Override
        public void onVideoEnded() {
            // Called when the video end.
            player.loadVideo(playlist.get(getNextSongIndex("next")));
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
        }

        @Override
        public void onAdStarted() {
        }
    }

    // returns the next available song index, depending on the direction
    public int getNextSongIndex(String direction) {
        if (direction == "previous") {
           if (currentVideo - 1 < 0) {
              currentVideo = playlist.size() - 1;
           } else currentVideo = currentVideo - 1;
        } else if (direction == "next") {
            if (currentVideo + 1 > playlist.size() - 1) {
                currentVideo = 0;
            } else currentVideo = currentVideo + 1;
        }

        return currentVideo;
    }
}
