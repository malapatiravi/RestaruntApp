package com.example.malz.listview;

import java.util.HashMap;

/**
 * Created by Sonu on 11/02/16.
 */
public interface OnListItemSelectedListener {
    public void onListItemSelected(int id);
    public void onListItemSelected(int id, HashMap<String, ?> movie);
}
