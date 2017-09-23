package com.example.riku.a8_mp3playerexercise;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.example.riku.a8_mp3playerexercise.R.attr.colorPrimary;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    //  is mediaPlayer initialized
    private boolean isInitialised = false;
    private TextView textView;
    // list view
    private ListView listview;
    private TextView playingText;
    // path to mp3-files
    private String mediaPath;
    // List of Strings to hold mp3-filenames
    private List<String> songs = new ArrayList<String>();
    private List<String> songTitles = new ArrayList<String>();
    // use AsyncTask to load filenames
    private LoadSongsTask task;
    // current song, if user clicks next button, the first song (index = 0) will be played
    private int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listView);
        // item listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                playSong(position);
                }
            });

        //play(); // doesn't work, you need to check runtime permissions to read external storage
        if (isReadStoragePermissionGranted()) {
            //loadSongs();
            //play();
            mediaPath = Environment.getExternalStorageDirectory() + "/Music/";
            task = new LoadSongsTask();
            task.execute();
        } else {
            Toast.makeText(getBaseContext(), "No permission to read from disk", Toast.LENGTH_SHORT).show();
        }

    }

    public void highlightCurrentSong() {
        for (int i = 0; i < songs.size(); i++ ) {
            int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
            if (i == currentPosition) {
                listview.getChildAt(i).setBackgroundColor(color);
            } else listview.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }



    public void handleToggleButtonClick(View view) {
        if (mediaPlayer.isPlaying() && isInitialised) {
            mediaPlayer.pause();
        } else if (!mediaPlayer.isPlaying() && isInitialised) {
            mediaPlayer.start();
        } else if (!isInitialised) {
            playSong(0);
        }

    }

    public void handleScrollSongs(View view) {
        int pos = currentPosition;
        switch (view.getId()) {
            // loop songs
            case R.id.previousButton:
                // play previous song
                if (pos - 1 < 0) {
                    pos = songs.size() - 1;
                } else pos = pos - 1;
                break;
            case R.id.nextButton:
                // play next song
                if (pos + 1 > songs.size() - 1) {
                    pos = 0;
                } else pos = pos + 1;
        }
        playSong(pos);
        updatePlayingSongName(subtractSongName(songs.get(pos)));
    }

        public void playSong(int position) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();
            }
            // in recursive version
            mediaPlayer.setDataSource(songs.get(position));
            // set current position so we can check later when scrolling backwards/forwards
            currentPosition = position;
            mediaPlayer.prepare();
            mediaPlayer.start();

            currentPosition = position;
            highlightCurrentSong();
            updatePlayingSongName(subtractSongName(songs.get(position)));
            isInitialised = true;

            //showToast(subtractSongName(currentPosition));
        } catch(IOException e) {
            Toast.makeText(getBaseContext(), "Cannot start audio!", Toast.LENGTH_SHORT).show();
        }
    }

    // Use AsyncTask to read all mp3 file names
    private class LoadSongsTask extends AsyncTask<Void, String, Void> {
        private List<String> loadedSongs = new ArrayList<String>();
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(),"Loading...",
                    Toast.LENGTH_LONG).show();
        }
        protected Void doInBackground(Void... url) {
            updateSongListRecursive(new File(mediaPath));
            return null;
        }
        public void updateSongListRecursive(File path) {
            if (path.isDirectory()) {
                for (int i = 0; i < path.listFiles().length; i++) {
                    File file = path.listFiles()[i];
                    updateSongListRecursive(file);
                }
            } else {
                String name = path.getAbsolutePath();
                publishProgress(name);
                if (name.endsWith(".mp3")) {
                    loadedSongs.add(name);
                    Log.e("aSDFASDF", name);
                }
            }
        }
        protected void onPostExecute(Void args) {
            // strip song name from the full path, for UI
            for (String currentSong : loadedSongs) {
                Log.e("csong", currentSong);
                songTitles.add(subtractSongName(currentSong));
            }

            ArrayAdapter<String> songList = new
                    ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1, songTitles);

            listview.setAdapter(songList);
            songs = loadedSongs;

            Toast.makeText(getApplicationContext(),
                    "Songs="+songs.size(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    public String subtractSongName(String song) {
        return song.substring(song.lastIndexOf("/") + 1, song.lastIndexOf("."));
    }

    public void updatePlayingSongName (String song) {
        playingText = (TextView) findViewById(R.id.playingText);
        playingText.setText("Playing: " + song);
    }


}