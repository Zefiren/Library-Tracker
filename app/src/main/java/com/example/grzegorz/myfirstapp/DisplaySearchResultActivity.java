package com.example.grzegorz.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gaurav.cdsrecyclerview.CdsRecyclerView;
import com.gaurav.cdsrecyclerview.CdsRecyclerViewAdapter;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by Grzegorz on 06/06/2017.
 */

public class DisplaySearchResultActivity extends AppCompatActivity {

    public static final String EXTRA_ADDING = "com.example.grzegorz.myfirstapp.ADDING";


    private CdsRecyclerView mRecyclerView;
    private CdsRecyclerViewAdapter mAdapter;
    private CdsRecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);


        final TextView descriptionText = (TextView) findViewById(R.id.detail_description_content);
        final TextView showAll = (TextView) findViewById(R.id.detail_read_all);
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAll.setVisibility(View.INVISIBLE);

                descriptionText.setMaxLines(Integer.MAX_VALUE);
            }
        });
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String title = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_TITLE);
        String author = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_AUTHOR);

        mRecyclerView = (CdsRecyclerView) findViewById(R.id.recyclerViewSearch);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        if (title != null && author != null)
            if (title.length() + author.length() > 0)
                searchBooks(title, author);

//            initializeList();

    }



/*        private void addBook(Intent intent) {
            MySQLiteHelper myDB = new MySQLiteHelper(this);

            String title = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_TITLE);
            String author = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_AUTHOR);
            Book book = new Book(title,author,true,0);


            myDB.addBook(book);
        }*/

    private void initializeList() {
        MySQLiteHelper db = new MySQLiteHelper(this);
        List<Book> books = db.getAllBooks();
        Log.v("Books", "numbr: " + books.size());

        Book[] array = new Book[books.size()];
        books.toArray(array);

        mAdapter = new MyAdapter(array, this);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void showLibrary() {
        MySQLiteHelper db = new MySQLiteHelper(this);
        List<Book> books = db.getAllBooks();
        Log.v("Books", "numbr: " + books.size());

        Book[] array = new Book[books.size()];
        books.toArray(array);

        mAdapter = new MyAdapter(array, this);
        mRecyclerView.setAdapter(mAdapter);
    }


    private void searchBooks(final String title, final String author) {
        final String titleVar = title;
        final String authorVar = author;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ApiBookSearch bookSearch = new ApiBookSearch();
                List<Book> bookList = bookSearch.doInBackground(title, author);
                final Book[] array = new Book[bookList.size()];
                bookList.toArray(array);


                DisplaySearchResultActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new MyAdapter(array, DisplaySearchResultActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setItemClickListener(new CdsRecyclerView.ItemClickListener() {
                            @Override
                            public void onItemClick(int i) {

                                Book selectedBook =  (Book) mAdapter.getItem(i);
                                Context context = DisplaySearchResultActivity.this;
                                Intent intent = new Intent(context, DisplaySingleBookActivity.class);
                                intent.putExtra(DisplayLibraryActivity.EXTRA_BOOK, Parcels.wrap(selectedBook));
                                //Find out if searching books to add or displaying one already tracked
                                if(context instanceof DisplaySearchResultActivity)
                                    intent.putExtra(DisplaySearchResultActivity.EXTRA_ADDING, true);


                                context.startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }

}
