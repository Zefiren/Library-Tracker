package com.example.grzegorz.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;



import org.parceler.Parcels;

import java.util.Comparator;
import java.util.List;

public class DisplayLibraryActivity extends AppCompatActivity {


    public static final String EXTRA_BOOK = "com.example.grzegorz.myfirstapp.BOOK";
    public  static  final  int orderByTitle = 0;
    public  static  final  int orderByAuthor = 1;

    public static int orderBy = orderByTitle;
    public static int scrollPos;


    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
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

        registerForContextMenu(mRecyclerView);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display, menu);
        MenuItem item = menu.findItem(R.id.spinner_library);
        item.setVisible(true);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        CustomSpinnerAdapter<CharSequence> adapter = new CustomSpinnerAdapter<CharSequence>(this, R.layout.text_row_item
                ,getResources().getStringArray(R.array.sort_options));
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt("orderBy", orderBy);

        LinearLayoutManager layoutManager = ((LinearLayoutManager)mRecyclerView.getLayoutManager());
        savedInstanceState.putInt("position", layoutManager.findFirstVisibleItemPosition());
    }


    @Override
    public void onPause()
    {
        super.onPause();
        //read current recyclerview position
        orderBy = orderBy;
        scrollPos = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //set recyclerview position
        showLibrary();
        if(scrollPos != -1)
        {
            ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset( scrollPos, 0);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        orderBy = savedInstanceState.getInt("orderBy");

        showLibrary();
        LinearLayoutManager layoutManager = ((LinearLayoutManager)mRecyclerView.getLayoutManager());
        layoutManager.scrollToPositionWithOffset(savedInstanceState.getInt("position"),0);
    }

    private void addBook(Intent intent) {
        MySQLiteHelper myDB = new MySQLiteHelper(this);

        String title = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_TITLE);
        String author = intent.getStringExtra(DisplayBookAddingActivity.EXTRA_AUTHOR);
        Book book = new Book(title,author,"",true,-1);


        myDB.addBook(book);
    }

    private void initializeList() {
        MySQLiteHelper db = new MySQLiteHelper(this);
        List<Book> books = db.getAllBooks();

        mAdapter = new MyAdapter(books, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void clearLibrary(View view){
        MySQLiteHelper db = new MySQLiteHelper(this);
        db.clearBooks();
        showLibrary();
    }

    private void showLibrary() {
        MySQLiteHelper db = new MySQLiteHelper(this);
        List<Book> books = db.getAllBooks();
        books.sort(new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                int compVal=0;
                if(orderBy == orderByTitle) {
                    compVal = o1.getTitle().compareTo(o2.getTitle());
                    compVal = compVal == 0 ? o1.getAuthor().compareTo(o2.getAuthor()) : compVal;
                    return compVal;
                }
                else {
                    compVal =o1.getAuthor().compareTo(o2.getAuthor());
                    compVal = compVal == 0 ? o1.getTitle().compareTo(o2.getTitle()) : compVal;

                    return compVal;
                }
            }
        });
        mAdapter.swap(books);

    }

}
