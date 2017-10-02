package com.example.riku.a12_shoppinglistexercise;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Riku on 2.10.2017.
 */

  public class AddNewItemFragment extends DialogFragment {
        /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks. */
        public interface AddItemDialogListener {
            public void onDialogPositiveClick(String name, int count, double price);
            public void onDialogNegativeClick();
        }

        // Use this instance of the interface to deliver action events
        AddItemDialogListener mListener;

        // Override the Fragment.onAttach() method to instantiate the TeamDialogListener
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the TeamDialogListener so we can send events to the host
                mListener = (AddItemDialogListener) activity;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(activity.toString() + " must implement AddItemDialogListnere");
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // create a new AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            final View dialogView = inflater.inflate(R.layout.add_new_item_dialog, null);
            builder.setView(dialogView)
                    // Set title
                    .setTitle("Add a new item")
                    // Add action buttons
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // find a team name
                            EditText editTextName = (EditText) dialogView.findViewById(R.id.add_new_product_name);
                            EditText editTextCount = (EditText) dialogView.findViewById(R.id.add_new_product_count);
                            EditText editTextPrice = (EditText) dialogView.findViewById(R.id.add_new_product_price);

                            String name = editTextName.getText().toString();
                            int count  = Integer.parseInt(editTextCount.getText().toString());
                            double price  = Double.parseDouble(editTextPrice.getText().toString());
                            // Send the positive button event back to the host activity
                            mListener.onDialogPositiveClick(name, count, price);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Send the negative button event back to the host activity
                            //mListener.onDialogNegativeClick(AddNewItemFragment.this);
                        }
                    });
            return builder.create();
        }
    }