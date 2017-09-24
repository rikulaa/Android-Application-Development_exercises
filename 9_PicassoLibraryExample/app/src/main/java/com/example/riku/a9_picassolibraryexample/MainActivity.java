package com.example.riku.a9_picassolibraryexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView imageview;
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageview = (ImageView) findViewById(R.id.imageView);
        loader = (ProgressBar) findViewById(R.id.progressBar);

        loadRandomImage();
    }

    public void handleLoadRandomButtonClick(View view) {
        loadRandomImage();
    }

    public void loadRandomImage() {
        // creates random integer between 1-3
        int random = new Random().nextInt(3) + 1;
        Log.e("random", String.valueOf(random));

        loader.setVisibility(View.VISIBLE);
        Picasso.with(getApplicationContext()).
                load("http://student.labranet.jamk.fi/~H3410/android-application-development/res/imageviewexercise/" + random + ".jpeg")
                .into(imageview, new Callback() {
                    @Override
                    public void onSuccess() {
                        loader.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        showToast("Error downloading image!");
                    }
                });
    }

    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
