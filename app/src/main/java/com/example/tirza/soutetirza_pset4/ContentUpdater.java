/*
 * Native App Studio: Assignment 4
 * To Do List
 * Tirza Soute
 *
 * This file updates the database using the DatabaseHelper class and refreshes the to do list
 * ListView if something changes.
 */

package com.example.tirza.soutetirza_pset4;

import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


class ContentUpdater {
    private ShowContent activity;

    ContentUpdater(ShowContent activity) {
        this.activity = activity;
    }

    /**
     * Adds the item that was given by the user to the database if it is unique and refreshes the
     * list view
     */
    void addItem() {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        EditText userInput = (EditText) activity.findViewById(R.id.addItemText);
        String item = userInput.getText().toString();
        if (!item.isEmpty()) {
            if (databaseHelper.create(item)) {
                refreshListView(databaseHelper);
            } else {
                Toast.makeText(activity, "Please enter a unique item", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "Please enter an item to add", Toast.LENGTH_SHORT).show();
        }
        userInput.setText("");
    }

    /** Update an item's status in the database */
    void updateStatus(String item, int newStatus) {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        // Find the ID
        long _id = findId(item);
        // Update the item with the new name
        databaseHelper.update(item, _id, newStatus);
        refreshListView(databaseHelper);
    }

    /** Deletes an item from the database */
    void deleteItem(String item) {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        long _id = findId(item);
        databaseHelper.delete(_id);
        refreshListView(databaseHelper);
    }

    /** Finds the ID that corresponds to an item */
    private long findId(String item) {
        long _id = 0;

        // Loop through ArrayList
        for (HashMap<String, String> map : activity.toDoList) {
            // Loop through HashMap
            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                if (value.equals(item)) {
                    _id = Long.valueOf(map.get("id"));
                }
            }
        }
        return _id;
    }

    /** Refreshes the list view after a change */
    private void refreshListView(DatabaseHelper databaseHelper) {
        activity.adapter.clear();
        // Get the updated database
        activity.toDoList = databaseHelper.read();
        activity.adapter.addAll(activity.createToDoList());
        activity.adapter.notifyDataSetChanged();
    }
}
