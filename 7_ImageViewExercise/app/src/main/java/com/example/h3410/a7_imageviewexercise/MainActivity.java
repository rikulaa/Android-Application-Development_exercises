package com.example.h3410.a7_imageviewexercise;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // image view object
    private ImageView imageView;
    // text view object
    private TextView textView;
    // progress bar object
    private ProgressBar progressBar;
    // example image's path (change to your own if needed...)
    private final String PATH = "http://student.labranet.jamk.fi/~H3410/android-application-development/res/imageviewexercise/";
    // example image names (change to your own if needed...)
    private String[] images = {"1.jpeg","2.jpeg","3.jpeg"};
    // which image is now visible
    private int imageIndex;
    private int prevIndex;
    // async task to load a new image
    private DownloadImageTask task;
    // swipe down and up values
    private float x1,x2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get views
        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // start showing images
        imageIndex = 0;
        showImage();
    }


    public void showImage() {
        // create a new AsyncTask object
        task = new DownloadImageTask();
        // start asynctask
        task.execute(PATH + images[imageIndex]);
    }

    public boolean onTouchEvent(MotionEvent event) {
        // dont run if start/end of the list

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // user touches screen
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                // user lifts finger up from screen
                x2 = event.getX();

                prevIndex = imageIndex;
                // swipes left to right => go back
                if (x1 < x2) {
                    imageIndex = (imageIndex > 0) ? imageIndex - 1 : 0;
                } else {
                    //swipes right to left => go forward
                    imageIndex = (imageIndex < images.length - 1) ? imageIndex + 1 : images.length - 1;
                }

                // dont update if already at the start/end of the list
                if (prevIndex != imageIndex) {
                    showImage();
                }
                break;


        }
        Log.e("sd", String.valueOf(imageIndex));
        return false;
    }

    // asynctask class
    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {

        // this is done in UI thread, nothing this time
        @Override
        protected void onPreExecute() {
            // show loading progress bar
            progressBar.setVisibility(View.VISIBLE);
        }

        // this is background thread, load image and pass it to onPostExecute
        @Override
        protected Bitmap doInBackground(String... urls) {
            URL imageUrl;
            Bitmap bitmap = null;
            try {
                imageUrl = new URL(urls[0]);
                InputStream in = imageUrl.openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("<<LOADIMAGE>>", e.getMessage());
            }
            return bitmap;
        }

        // this is done in UI thread
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            textView.setText("Image " + (imageIndex + 1) + "/" + images.length);
            // hide loading progress bar
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
