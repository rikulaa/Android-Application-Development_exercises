package com.example.riku.a2_basicuicontrolexercise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // show toast when user clicks select-button
    public void selectButtonClick(View view) {
        // radiogroup
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        int id = rg.getCheckedRadioButtonId();

        // checked radiobutton
        RadioButton radioButton = (RadioButton) findViewById(id);

        String text = (String) radioButton.getText();
        // show message
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
