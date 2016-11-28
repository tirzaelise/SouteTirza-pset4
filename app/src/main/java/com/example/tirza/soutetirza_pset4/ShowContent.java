/*
 * Native App Studio: Assignment 4
 * To Do List
 * Tirza Soute
 *
 * This file shows the user's to do list.
 */

package com.example.tirza.soutetirza_pset4;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowContent extends AppCompatActivity {
    ListView listView;
    CustomAdapter adapter;
    ArrayList<HashMap<String, String>> toDoList;
    ContentUpdater contentUpdater;
    HashMap<String, Integer> statusHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_content);
        contentUpdater = new ContentUpdater(this);

        // Set OnItemLongClickListener so that items can be deleted
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                // Find the item that was clicked and delete it
                String clickedItem = listView.getItemAtPosition(position).toString();
                contentUpdater.deleteItem(clickedItem);
                return true;
            }
        });

        // Set OnItemClickListener so that items can be checked off
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the item that was clicked and change its status
                String clickedItem = listView.getItemAtPosition(position).toString();
                TextView clickedTextView = (TextView) view.findViewById(R.id.listViewItem);
                // Find the current status of the clicked item
                statusHashMap = statusMap();
                int status = statusHashMap.get(clickedItem);

                if (status == 1) {
                    contentUpdater.updateStatus(clickedItem, 0);
                    // Revert the text colour back to the original grey colour
                    clickedTextView.setTextColor(Color.parseColor("#808080"));
                } else {
                    contentUpdater.updateStatus(clickedItem, 1);
                    // Set the text colour to green
                    clickedTextView.setTextColor(Color.parseColor("#006400"));
                }
            }
        });
        setToDoList();
    }

    /** Creates a ListView that shows the to do list*/
    public void setToDoList() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        toDoList = databaseHelper.read();
        ArrayList<String> toDoArray = createToDoList();
        statusHashMap = statusMap();

        adapter = new CustomAdapter(this, R.layout.row_view, R.id.listViewItem, toDoArray, toDoList);
        listView.setAdapter(adapter);
    }

    /** Creates ArrayList that holds all items from the database */
    public ArrayList<String> createToDoList() {
        ArrayList<String> toDoArray = new ArrayList<>();

        // Loop through ArrayList
        for (HashMap<String, String> map : toDoList) {
            // Loop through HashMap
            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                String key = mapEntry.getKey();
                // Get all values that have "item" as their key and add them to the ArrayList
                if (key.equals("item")) {
                    String value = mapEntry.getValue();
                    toDoArray.add(value);
                }
            }
        }
        return toDoArray;
    }

    /** Necessary for onClick method, sends to ContentUpdater class*/
    public void addItem(View view) {
        contentUpdater.addItem();
    }

    /** Creates a HashMap that holds an item as key and its status as value */
    HashMap<String, Integer> statusMap() {
        statusHashMap = new HashMap<>();

        // Loop through ArrayList
        for (HashMap<String, String> map : toDoList) {
            int status = Integer.valueOf(map.get("status"));
            String item = map.get("item");
            statusHashMap.put(item, status);
        }
        return statusHashMap;
    }
}