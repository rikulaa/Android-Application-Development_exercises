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
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "ShoppingList.db";
    // Contacts table name
    private static final String TABLE_NAME = "shopping_list";
    // column names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_PRICE = "price";

    Cursor cursor;


    // queries to create and delete database
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT," + COLUMN_PRICE  + " DOUBLE," + COLUMN_AMOUNT + " INT);";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public  void onCreate(SQLiteDatabase database) {
        //database.execSQL(SQL_DELETE_ENTRIES);
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
        String select = "SELECT name, amount, price FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        //cursor = db.rawQuery(select, null);
        String[] columns = {"_id", "name", "amount", "price" };
        return db.query(TABLE_NAME, columns, null, null, null, null, null, null);

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

        //cursor.close();

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
        long index = db.insert(TABLE_NAME, null, values);
        item.setIndex(index);
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

    public void deleteShoppingListItemAtIndex(Long index) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(index)};

        db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public void deleteAllShoppingListItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);

    }


}
