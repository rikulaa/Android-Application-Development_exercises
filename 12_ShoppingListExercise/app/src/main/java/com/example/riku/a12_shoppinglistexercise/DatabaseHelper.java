package com.example.riku.a12_shoppinglistexercise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riku on 30.9.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ShoppingList.db";
    // Contacts table name
    private static final String TABLE_NAME = "shopping_list";
    // column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_PRICE = "price";


    // queries to create and delete database
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "
                    + TABLE_NAME
                    + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_AMOUNT + " INT,"
                    + COLUMN_PRICE  + "DOUBLE"
                    + ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public  void onCreate(SQLiteDatabase database) {
       database.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // returs cursor which can be easily appended to listview etc
    public Cursor getAllShoppingListItems() {
        List<ShoppingListItem> shoppingList =  new ArrayList<>();
        int index = 0;
        // select all
        String select = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(select, null);

        //if (cursor.moveToFirst()) {
            //do {
                //ShoppingListItem item = new ShoppingListItem(
                        //cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        //cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNT)),
                        //cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                //);

                //shoppingList.add(index, item);
                //index++;

            //} while (cursor.moveToNext());
        //}

        //return shoppingList;
    }

    // helper methods for inserting, updating or deleting data in the databse
    public void addShoppingListItem(ShoppingListItem item) {
        // get database access in writable mode
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // add values
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_AMOUNT, item.getAmount());
        values.put(COLUMN_PRICE, item.getPrice());
        db.insert(TABLE_NAME, null, values);
    }

    public void updateShoppingListItem(ShoppingListItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_AMOUNT, item.getAmount());
        values.put(COLUMN_PRICE, item.getPrice());
        // ? will be replaced with whereArgs
        String whereClause = COLUMN_NAME + " LIKE ?";
        String[] whereArgs = {item.getName()};

        db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public void deleteShoppingListItem(ShoppingListItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_NAME + "LIKE ?";
        String[] whereArgs = {item.getName()};

        db.delete(TABLE_NAME, whereClause, whereArgs);
    }


}
