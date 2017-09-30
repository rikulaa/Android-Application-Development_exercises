package com.example.riku.a12_shoppinglistexercise;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShoppingListItem item1 = new ShoppingListItem("apple", 2, 0.2);
        ShoppingListItem item2 = new ShoppingListItem("orange", 5, 1.2);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.addShoppingListItem(item1);
        databaseHelper.addShoppingListItem(item2);

        Cursor values = databaseHelper.getAllShoppingListItems();

    }
}
