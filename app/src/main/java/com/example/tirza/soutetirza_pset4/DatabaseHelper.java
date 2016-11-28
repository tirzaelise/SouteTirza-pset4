/*
 * Native App Studio: Assignment 4
 * To Do List
 * Tirza Soute
 *
 * This file implements the database for the to do list.
 */

package com.example.tirza.soutetirza_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TODO_LIST = "TODOLIST";
    private static final int DATABASE_VERSION = 4;
    private static final String ITEM = "item";
    private static final String _ID = "_id";
    private static final String STATUS = "status";

    DatabaseHelper(Context context) {
        super(context, TODO_LIST, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TODO_LIST + " ( " + _ID + " INTEGER PRIMARY KEY" +
                              " AUTOINCREMENT, " + ITEM + " TEXT, " + STATUS + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TODO_LIST);
        onCreate(sqLiteDatabase);
    }

    /** Creates a new item in the database if it does not exist yet */
    boolean create(String item) {
        if (checkItemUnique(item)) {
            SQLiteDatabase database = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            // Generate an ID
            long id = generateId(database);
            contentValues.put(ITEM, item);
            contentValues.put(_ID, id);
            // Status is unchecked at first
            contentValues.put(STATUS, 0);
            // Add new item to database
            database.insert(TODO_LIST, null, contentValues);
            database.close();
            return true;
        }
        return false;
    }

    /** Checks if such an item is already in the database */
    private boolean checkItemUnique(String item) {
        boolean unique = true;
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM " + TODO_LIST + " where " + ITEM + " =?";
        Cursor cursor = database.rawQuery(query, new String[] {item});

        // If a result has been found, it is not unique
        if (cursor.moveToFirst()) unique = false;
        cursor.close();
        return unique;
    }

    /** Updates an item in the database */
    void update(String item, long _id, int status) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM, item);
        contentValues.put(_ID, _id);
        contentValues.put(STATUS, status);
        database.update(TODO_LIST, contentValues, "_ID = ? ",
                        new String[]{String.valueOf(_id)});
        database.close();
    }

    /** Deletes an item from the database using the ID */
    void delete(long id) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TODO_LIST, "_ID = ? ", new String[]{String.valueOf(id)});
        database.close();
    }

    /** Reads the data in the database */
    ArrayList<HashMap<String, String>> read() {
        SQLiteDatabase database = getReadableDatabase();
        // Reads everything that is in the database
        String query = "SELECT _id , " + ITEM + ", " + STATUS + " FROM " + TODO_LIST;
        ArrayList<HashMap<String, String>> toDoList = new ArrayList<>();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                // Creates an empty HashMap that holds "id" as key with its id as value, "item"
                // as key and the item as value and "status" as key with its status as value
                HashMap<String, String> itemData = new HashMap<>();
                itemData.put("id", cursor.getString(cursor.getColumnIndex(_ID)));
                itemData.put("item", cursor.getString(cursor.getColumnIndex(ITEM)));
                itemData.put("status", cursor.getString(cursor.getColumnIndex(STATUS)));
                toDoList.add(itemData);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return toDoList;
    }

    /** Generates an ID for the new item that has to be added to the to do list */
    private long generateId(SQLiteDatabase database) {
        // ID is zero if there isn't any data in the database
        long _id = 0;
        // Finds highest ID in the database
        String query = "SELECT MAX(_id) FROM " + TODO_LIST;
        Cursor cursor = database.rawQuery(query, null);
        // Sets the ID to the highest ID + 1, if there are entries in the database
        if (cursor.moveToFirst()) _id = cursor.getLong(0) + 1;
        cursor.close();
        return _id;
    }
}
