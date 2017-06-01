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
import java.util.List;

    public class DisplayLibraryActivity extends AppCompatActivity {
        @Parcel
        static public class Book {
            String title;
            String author;
            boolean have_book;
            int libraryID;

            public Book() {
            }

            @ParcelConstructor
            Book(String title, String author, boolean have_book,int libraryID){
                this.title = title;
                this.author = author;
                this.have_book = have_book;
                this.libraryID = libraryID;
            }

            public int getLibraryID() {
                return libraryID;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public boolean isHave_book() {
                return have_book;
            }

            public void setHave_book(boolean have_book) {
                this.have_book = have_book;
            }

            @Override
            public String toString() {
                return title + " by " + author;
            }
        }

        public static final String EXTRA_BOOK = "com.example.grzegorz.myfirstapp.BOOK";
        public static final String titleSort = "title ASC";
        public static final String authorSort = "author ASC";

        public static  String orderBy = titleSort;


        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_library);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Boolean viewOnly = intent.getBooleanExtra(MainActivity.EXTRA_VIEW_ONLY,false);
        String title = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_TITLE);
        String author = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_AUTHOR);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

;

        final SQLiteDatabase myDB =
                openOrCreateDatabase("my.db", MODE_PRIVATE, null);
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS books (bookID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,title VARCHAR(200), author VARCHAR(100), haveBook BIT)"
        );
        if(title != null)
            addBook(intent,myDB);

        initializeList(myDB);

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
                    if(position==0)
                        orderBy = titleSort;
                    else
                        orderBy = authorSort;
                    showLibrary();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            return super.onCreateOptionsMenu(menu);
        }

        private void addBook(Intent intent, SQLiteDatabase myDB){
        String title = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_TITLE);
        String author = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_AUTHOR);


        ContentValues row = new ContentValues();
        row.put("title", title);
        row.put("author", author);
        row.put("haveBook", 1);
        myDB.insert("books", null, row);
    }

    private void initializeList(final SQLiteDatabase myDB){
        final Cursor myCursor =
                myDB.query("books", new String[]{"bookID","title","author","haveBook"} ,null, null, null, null, orderBy);

//        List<Book> books = new ArrayList<Book>();
        Book[] books = new Book[myCursor.getCount()];
        while(myCursor.moveToNext()) {
            Book book = new Book( myCursor.getString(1), myCursor.getString(2), myCursor.getInt(3) == 1, myCursor.getInt(0));
            books[myCursor.getPosition()] = book;
        }

        /*ArrayAdapter adapter = new ArrayAdapter<Book>(this,
                R.layout.activity_listview, books);
*/
//        final ListView listView = (ListView) findViewById(R.id.messageList);
//        listView.setAdapter(adapter);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter( books,this);
        mRecyclerView.setAdapter(mAdapter);
        myCursor.close();
/*
        mRecyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                Book selectedBook = (Book) listView.getItemAtPosition(pos);

                myDB.delete("books","bookID = ?",new String[]{Integer.toString(selectedBook.getLibraryID())});
                Log.v("Book:",Integer.toString(selectedBook.getLibraryID()) +  " "+ selectedBook.toString());

                showLibrary(myDB);
                Log.v("long clicked","pos: " + pos);

                return true;
            }
        });*/

       /* mRecyclerView.setOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book selectedBook = (Book) ((MyAdapter) mAdapter).getItemAtPosition(position);
                Intent intent = new Intent(DisplayLibraryActivity.this,DisplaySingleBookActivity.class);
                intent.putExtra(DisplayLibraryActivity.EXTRA_BOOK, Parcels.wrap(selectedBook));

                startActivity(intent);

            }
        });*/
    }



        private void showLibrary(SQLiteDatabase myDB){
        final Cursor myCursor =
                myDB.query("books", new String[]{"bookID","title","author", "haveBook"} ,null, null, null, null, orderBy);

        Book[] books = new Book[myCursor.getCount()];
        while(myCursor.moveToNext()) {
            Book book = new Book( myCursor.getString(1), myCursor.getString(2), myCursor.getInt(3) == 1, myCursor.getInt(0));
            books[myCursor.getPosition()] = book;
        }

        mAdapter = new MyAdapter( books,this);
        mRecyclerView.setAdapter(mAdapter);
    }

}
