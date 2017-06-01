package com.example.grzegorz.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Grzegorz on 09/05/2017.
 */

public class DisplayBookAddingActivity  extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.example.grzegorz.myfirstapp.TITLE";
    public static final String EXTRA_AUTHOR = "com.example.grzegorz.myfirstapp.AUTHOR";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);
    }


    public void sendBook(View view) {
        Intent intent = new Intent(this,DisplayLibraryActivity.class);
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editAuthor = (EditText) findViewById(R.id.editAuthor);

        String title = editTitle.getText().toString();
        String author = editAuthor.getText().toString();



        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_AUTHOR, author);
        startActivity(intent);
    }
}
