package com.example.grzegorz.myfirstapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Grzegorz on 02/07/2017.
 */

public  class CustomSpinnerAdapter<T> extends ArrayAdapter<String> {
    public CustomSpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("");
        return view;
    }
}