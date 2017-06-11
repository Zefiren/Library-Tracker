package com.example.grzegorz.myfirstapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Parcels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DisplayLibraryActivity extends AppCompatActivity {


    public static final String EXTRA_BOOK = "com.example.grzegorz.myfirstapp.BOOK";
    public static final String titleSort = "title ASC";
    public static final String authorSort = "author ASC";
    public  static  final  int orderByTitle = 0;
    public  static  final  int orderByAuthor = 1;

    public static int orderBy = orderByTitle;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_library);
        MySQLiteHelper db = new MySQLiteHelper(this);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Boolean viewOnly = intent.getBooleanExtra(MainActivity.EXTRA_VIEW_ONLY, false);
        String title = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_TITLE);
        String author = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_AUTHOR);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        if (title != null)
            addBook(intent);

        initializeList();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display, menu);
        MenuItem item = menu.findItem(R.id.spinner_library);
        item.setVisible(true);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int oldOrder = orderBy;
                if (position == 0)
                    orderBy = orderByTitle;
                else
                    orderBy = orderByAuthor;
                if(orderBy != oldOrder) {
                    showLibrary();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void addBook(Intent intent) {
        MySQLiteHelper myDB = new MySQLiteHelper(this);

        String title = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_TITLE);
        String author = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_AUTHOR);
        Book book = new Book(title,author,"",true,0);


        myDB.addBook(book);
    }

    private void initializeList() {
        MySQLiteHelper db = new MySQLiteHelper(this);
        List<Book> books = db.getAllBooks();
        Log.v("Books","numbr: " + books.size());

        Book[] array = new Book[books.size()];
        books.toArray(array);

        mAdapter = new MyAdapter(array, this);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void showLibrary() {
        MySQLiteHelper db = new MySQLiteHelper(this);
        List<Book> books = db.getAllBooks();
        books.sort(new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                if(orderBy == orderByTitle)
                    return o1.getTitle().compareTo(o2.getTitle());
                else
                    return o1.getAuthor().compareTo(o2.getAuthor());
            }
        });
        Log.v("Books","numbr: " + books.size());

        Book[] array = new Book[books.size()];
        books.toArray(array);

//        final Cursor myCursor =
//                myDB.query("books", new String[]{"bookID", "title", "author", "haveBook"}, null, null, null, null, orderBy);

//        Book[] books = new Book[myCursor.getCount()];
//        while (myCursor.moveToNext()) {
//            Book book = new Book(myCursor.getString(1), myCursor.getString(2), myCursor.getInt(3) == 1, myCursor.getInt(0));
//            books[myCursor.getPosition()] = book;
//        }

        mAdapter = new MyAdapter(array, this);
        mRecyclerView.setAdapter(mAdapter);
    }

}
