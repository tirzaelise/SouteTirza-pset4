/*
 * Native App Studio: Assignment 4
 * To Do List
 * Tirza Soute
 *
 * This file extends the ArrayAdapter<String> in order to keep track of the colours of the tasks so
 * that the user knows when a task has been checked off.
 */

package com.example.tirza.soutetirza_pset4;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;


class CustomAdapter extends ArrayAdapter<String> {
    private ArrayList<String> toDoArray;
    private ArrayList<HashMap<String, String>> toDoList;

    CustomAdapter(Context context, int resource, int textViewResource, ArrayList<String> toDoArray,
                  ArrayList<HashMap<String, String>> toDoList) {
        super(context, resource, textViewResource, toDoArray);
        this.toDoArray = toDoArray;
        this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.listViewItem);
        String item = toDoArray.get(position);
        int status = findStatus(item);

        if (status == 1) textView.setTextColor(Color.parseColor("#006400"));

        return view;
    }

    /** Finds the status that corresponds to an item */
    private int findStatus(String item) {
        int status = 0;

        // Loop through ArrayList
        for (HashMap<String, String> map : toDoList) {
            // Loop through HashMap
            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                if (value.equals(item)) {
                    status = Integer.valueOf(map.get("status"));
                }
            }
        }
        return status;
    }

}
