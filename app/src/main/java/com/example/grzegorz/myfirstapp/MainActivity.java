package com.example.grzegorz.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_VIEW_ONLY = "com.example.grzegorz.myfirstapp.VIEW_ONLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void viewLibrary(View view) {
        Intent intent = new Intent(this,DisplayLibraryActivity.class);

        intent.putExtra(EXTRA_VIEW_ONLY, true);

        startActivity(intent);
    }

    public void addBooks(View view) {
        Intent intent = new Intent(this,DisplayBookAddingActivity.class);

        intent.putExtra(EXTRA_VIEW_ONLY, false);

        startActivity(intent);
    }

}
