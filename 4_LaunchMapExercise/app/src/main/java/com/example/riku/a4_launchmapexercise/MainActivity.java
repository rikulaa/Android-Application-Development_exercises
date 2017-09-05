package com.example.riku.a4_launchmapexercise;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showMap(View view) {
        EditText editText1 = (EditText) findViewById(R.id.lat);
        EditText editText2 = (EditText) findViewById(R.id.lng);

        double lat = Double.parseDouble(editText1.getText().toString());
        double lng = Double.parseDouble(editText2.getText().toString());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:" + lat + ", " + lng));
        startActivity(intent);
    }
}
