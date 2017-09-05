package com.example.riku.a3_basicuicontrolexercise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                new String[] {"Pasi", "Juha", "Kari", "Jouni", "Esa", "Hannu"});

        actv.setAdapter(aa);
    }

    public void onLoginSubmit(View view) {
        Button button = (Button) findViewById(R.id.button);
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        EditText passwordField = (EditText) findViewById(R.id.password);

        // get input field values
        String text = new StringBuilder("User: ").append(actv.getText()).append(" Password: ").append(passwordField.getText()).toString();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

}
