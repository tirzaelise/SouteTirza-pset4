/*
 * Native App Studio: Assignment 4
 * To Do List
 * Tirza Soute
 *
 * This file shows the user's to do list.
 */

package com.example.tirza.soutetirza_pset4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowContent extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> toDoList;
    ContentUpdater contentUpdater;

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
        setToDoList();
        setCheckBoxes();
    }


    /** Creates a ListView that shows the to do list*/
    public void setToDoList() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        toDoList = databaseHelper.read();
        ArrayList<String> toDoArray = createToDoList();

        adapter = new ArrayAdapter<>(this, R.layout.row_view, R.id.listViewItem, toDoArray);
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

    /** Updates the database */
    public void checkBoxChanged(View view) {
        // Get the item that was clicked
        RelativeLayout layout = (RelativeLayout) view.getParent();
        TextView textView = (TextView) layout.getChildAt(1);
        String clickedItem = textView.getText().toString();
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        if (checkBox.isChecked()) {
            contentUpdater.updateStatus(clickedItem, 1);
        } else {
            contentUpdater.updateStatus(clickedItem, 0);
        }
    }


    /** Sets the checkboxes according to the status in the database */
    public void setCheckBoxes() {
        HashMap<String, Integer> statusHashMap = statusMap();
        View view;
        CheckBox checkBox;

        for (int i = 0; i < listView.getCount(); i++) {
            // Find the checkbox and item on this row
            view = listView.getAdapter().getView(i, null, null);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            TextView textView = (TextView) view.findViewById(R.id.listViewItem);
            String item = textView.getText().toString();
            // Use the status HashMap to find the status of each item in the to do list
            int status = statusHashMap.get(item);

            // Set the checkbox to checked if the status is 1 (done)
            if (status == 1) {
                checkBox.setChecked(true);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /** Creates a HashMap that holds an item as key and its status as value */
    HashMap<String, Integer> statusMap() {
        HashMap<String, Integer> statusHashMap = new HashMap<>();

        // Loop through ArrayList
        for (HashMap<String, String> map : toDoList) {
            int status = Integer.valueOf(map.get("status"));
            String item = map.get("item");
            statusHashMap.put(item, status);
        }
        return statusHashMap;
    }
}