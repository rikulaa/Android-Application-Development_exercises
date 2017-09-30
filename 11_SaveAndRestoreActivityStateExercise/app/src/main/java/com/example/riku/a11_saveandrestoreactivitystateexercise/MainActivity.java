package com.example.riku.a11_saveandrestoreactivitystateexercise;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements TextEntryDialogFragment.TextEntryDialogListener {

    public final String TEXTVIEW_STATEKEY = "TEXTVIEW_STATEKEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // comment below to test
        TextView textView = (TextView) findViewById(R.id.textView);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(TEXTVIEW_STATEKEY)) {
                String text = savedInstanceState.getString(TEXTVIEW_STATEKEY);

                textView.setText(text);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        TextView textView = (TextView) findViewById(R.id.textView);
        saveInstanceState.putString(TEXTVIEW_STATEKEY, textView.getText().toString());
    }

    // texentrydialoglisteners
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String text) {
        TextView textView = (TextView) findViewById(R.id.textView);
        Log.e("Main acitive", text);
        textView.setText(text);
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
    }

    public void buttonClicked(View view) {
        TextEntryDialogFragment eDialog = new TextEntryDialogFragment();
        TextView textView = (TextView) findViewById(R.id.textView);
        eDialog.show(getFragmentManager(), textView.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu; this adds items to the action bar if it is present
        //getMenuInflater().inflate(R, menu);
        return true;
    }
}
