package com.example.grzegorz.myfirstapp;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.parceler.Parcels;

/**
 * Created by Grzegorz on 09/05/2017.
 */

public class DisplaySingleBookActivity extends AppCompatActivity {
    private boolean addingAllowed;
    private boolean changedDetails = false;
    private static Book book;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_single_book);
        Intent intent = getIntent();

        book = Parcels.unwrap( intent.getParcelableExtra(DisplayLibraryActivity.EXTRA_BOOK));
        addingAllowed = intent.getBooleanExtra(DisplaySearchResultActivity.EXTRA_ADDING,true);
        TextView titleView = (TextView)  findViewById(R.id.titleText);
        TextView authorView = (TextView)  findViewById(R.id.authorText);
        Button haveBtn = (Button)  findViewById(R.id.haveBook);

        checkLibrary();
        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());
        if(addingAllowed) {
            haveBtn.setEnabled(false);
            haveBtn.setText(R.string.bookNotTrackedPrompt);
        }
        else
            if(book.isHave_book())
                haveBtn.setText(R.string.have_book);
            else
                haveBtn.setText(R.string.not_have_book);
    }

    protected void checkLibrary(){
        MySQLiteHelper db = new MySQLiteHelper(this);
        Book bookInLibrary = db.getBookByISBN(book.getIsbn_code());
        if(bookInLibrary != null) {
            Button addBtn = (Button) findViewById(R.id.addBookBtn);
            addBtn.setText(R.string.bookAlreadyTrackedPrompt);
            addBtn.setEnabled(false);
            book = bookInLibrary;
            addingAllowed = false;
        }
    }

    public void toggleHaveBtn(View view){
        if(!addingAllowed){
            Button haveBtn = (Button)  findViewById(R.id.haveBook);
            //changedDetails=true;
            if(book.isHave_book()) {
                book.setHave_book(false);
                haveBtn.setText(R.string.not_have_book);
            }
            else{
                book.setHave_book(true);
                haveBtn.setText(R.string.have_book);
            }
            MySQLiteHelper db = new MySQLiteHelper(this);
            db.updateBook(book);
        }
        else
            return;
    }

    public void addBtnPressed(View view) {
        MySQLiteHelper db = new MySQLiteHelper(this);
        db.addBook(book);
        book = db.getBookByISBN(book.getIsbn_code());

        Button addBtn = (Button) findViewById(R.id.addBookBtn);
        addBtn.setText(R.string.bookAddedPrompt);
        addBtn.setEnabled(false);
        addingAllowed = false;

        Button haveBtn = (Button)  findViewById(R.id.haveBook);
        haveBtn.setEnabled(true);
        haveBtn.setText(R.string.not_have_book);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(changedDetails == true) {
            MySQLiteHelper db = new MySQLiteHelper(this);
            Book updated = new Book( book.getTitle(),book.getAuthor(),book.getIsbn_code(), book.isHave_book(),book.getLibraryID());
            //db.updateBook( updated );
        }

    }
}
