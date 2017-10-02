package com.example.riku.a12_shoppinglistexercise;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity implements AddNewItemFragment.AddItemDialogListener {
    DatabaseHelper databaseHelper;
    SimpleCursorAdapter cursorAdapter;
    double totalSum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllShoppingListItems();

    }

    public void getAllShoppingListItems() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        //databaseHelper.deleteAllShoppingListItems();


        Cursor values = databaseHelper.getAllShoppingListItems();

        final ListView listView = (ListView) findViewById(R.id.listView);
        String[] fromColumn = {"name", "amount", "price"};
        int[] toView = {R.id.name, R.id.amount, R.id.price};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, values, fromColumn, toView, 0);
        listView.setAdapter(cursorAdapter);
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View list, int pos, long id) {
              Cursor cursor = (Cursor) listView.getAdapter().getItem(pos);
                cursor.moveToPosition(pos);
                //Log.e("curso", pos + String.valueOf(cursor.getLong(cursor.getColumnIndex("_id"))));
                // delete by id in cursor
                handleItemDelete(cursor.getLong(cursor.getColumnIndex("_id")));
                return true;
            }
        });

        showTotalPrice(values);


    }

    public void openAddNewItemDialog(View view) {
        AddNewItemFragment addNewItemFragment = new AddNewItemFragment();
        addNewItemFragment.show(getFragmentManager(), "add");
    }

    @Override
    public void onDialogPositiveClick(String name, int count, double price) {
        ShoppingListItem shoppingListItem = new ShoppingListItem(name, count, price);
        databaseHelper.addShoppingListItem(shoppingListItem);
        getAllShoppingListItems();
    }

    @Override
    public void onDialogNegativeClick() {
    }

    public void handleItemDelete(long index) {
        databaseHelper.deleteShoppingListItemAtIndex(index);
        getAllShoppingListItems();
    }

    public void showTotalPrice(Cursor cursor) {
        totalSum = 0;

         if (cursor.moveToFirst()) {
            do {
                double sum = 0;
                int amount = cursor.getInt(cursor.getColumnIndex("amount"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));

                sum = price * amount;
                totalSum += sum;

            } while (cursor.moveToNext());
        }

        Toast.makeText(getApplicationContext(), "Total sum: " + String.valueOf(totalSum), Toast.LENGTH_SHORT).show();
    }


}


