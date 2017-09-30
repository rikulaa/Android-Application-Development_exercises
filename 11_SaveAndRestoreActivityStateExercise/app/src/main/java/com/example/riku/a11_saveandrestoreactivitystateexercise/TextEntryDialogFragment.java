package com.example.riku.a11_saveandrestoreactivitystateexercise;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Riku on 30.9.2017.
 */

public class TextEntryDialogFragment extends DialogFragment {

    // send data to host with listeners
    public interface TextEntryDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String text);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // use the created interface
    TextEntryDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // make sure host has implemented interface
        try {
            mListener = (TextEntryDialogListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement TextEntryDialogInterface");

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        // create new alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // inflate and set the layout  for the dialog
        // pas null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.textentry_dialog, null);

        EditText editText = dialogView.findViewById(R.id.editText);
        editText.setText("");

        builder.setView(dialogView).setTitle("Give me text")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       // find a team name
                        EditText editText = dialogView.findViewById(R.id.editText);
                        String text = editText.getText().toString();

                        // send the positive button event back to the host activity
                        Log.e("click listern", text);
                        mListener.onDialogPositiveClick(TextEntryDialogFragment.this, text);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogNegativeClick(TextEntryDialogFragment.this);
                    }
                });

        return builder.create();
    }
}
