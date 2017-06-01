package com.example.grzegorz.myfirstapp;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import org.parceler.Parcels;

/**
 * Created by Grzegorz on 09/05/2017.
 */

public class DisplaySingleBookActivity extends AppCompatActivity {

    private boolean changedDetails = false;
    private static Book book;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_single_book);
        book = Parcels.unwrap( getIntent().getParcelableExtra(DisplayLibraryActivity.EXTRA_BOOK));

        TextView titleView = (TextView)  findViewById(R.id.titleText);
        TextView authorView = (TextView)  findViewById(R.id.authorText);
        Button haveBtn = (Button)  findViewById(R.id.haveBook);

        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());
        if(book.isHave_book() == true)
            haveBtn.setText(R.string.have_book);
        else
            haveBtn.setText(R.string.not_have_book);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SQLiteDatabase myDB;
        if(changedDetails == true) {
            myDB = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
            String where = "bookID=" + book.getLibraryID();

            ContentValues row = new ContentValues();
            row.put("title", book.getTitle());
            row.put("author", book.getAuthor());
            row.put("haveBook", book.isHave_book());
            myDB.update("books", row,where,null);
        }

    }
}
