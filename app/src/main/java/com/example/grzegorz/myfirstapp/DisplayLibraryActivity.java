package com.example.grzegorz.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gaurav.cdsrecyclerview.CdsItemTouchCallback;
import com.gaurav.cdsrecyclerview.CdsRecyclerView;
import com.gaurav.cdsrecyclerview.CdsRecyclerViewAdapter;

import org.parceler.Parcels;

import java.util.Comparator;
import java.util.List;

public class DisplayLibraryActivity extends AppCompatActivity {


    public static final String EXTRA_BOOK = "com.example.grzegorz.myfirstapp.BOOK";
    public  static  final  int orderByTitle = 0;
    public  static  final  int orderByAuthor = 1;

    public static int orderBy = orderByTitle;
    public static int scrollPos;


    private CdsRecyclerView mRecyclerView;
    private CdsRecyclerViewAdapter mAdapter;
    private CdsRecyclerView.LayoutManager mLayoutManager;

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

        mRecyclerView = (CdsRecyclerView) findViewById(R.id.recyclerView);

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
        mRecyclerView.enableItemSwipe();
        mRecyclerView.setItemClickListener(new CdsRecyclerView.ItemClickListener() {
            @Override
            public void onItemClick(int i) {

                Book selectedBook =  (Book) mAdapter.getItem(i);
                Intent intent = new Intent(DisplayLibraryActivity.this, DisplaySingleBookActivity.class);
                intent.putExtra(DisplayLibraryActivity.EXTRA_BOOK, Parcels.wrap(selectedBook));
                Context context = DisplayLibraryActivity.this;
                //Find out if searching books to add or displaying one already tracked
                if(context instanceof DisplaySearchResultActivity)
                    intent.putExtra(DisplaySearchResultActivity.EXTRA_ADDING, true);


                context.startActivity(intent);
            }
        });
        mRecyclerView.setItemSwipeCompleteListener(new CdsItemTouchCallback.ItemSwipeCompleteListener() {
            @Override
            public void onItemSwipeComplete(int i) {
                Book book = (Book) mAdapter.getItem(i);
                Toast.makeText(DisplayLibraryActivity.this, "Book was deleted from library:"
                                + book.getTitle() + " by " + book.getAuthor() ,
                        Toast.LENGTH_SHORT).show();
                MySQLiteHelper db = new MySQLiteHelper(DisplayLibraryActivity.this);
                db.deleteBook(book);

            }
        });

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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt("orderBy", orderBy);
        Log.v("orderBy","numbr: " + orderBy);

        LinearLayoutManager layoutManager = ((LinearLayoutManager)mRecyclerView.getLayoutManager());
        savedInstanceState.putInt("position", layoutManager.findFirstVisibleItemPosition());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        int clickedItemPos = item.getOrder();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)  item.getMenuInfo();
        int listPosition = info.position;
        Book book = (Book) mAdapter.getItem(listPosition);
        Log.v("clicked pos",""+ listPosition);
        return super.onContextItemSelected(item);
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
        Log.v("orderByResuuuuume","numbr: " + orderBy);
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
        Log.v("orderByRestore","numbr: " + orderBy);

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
        Log.v("Books","numbr: " + books.size());

        Book[] array = new Book[books.size()];
        books.toArray(array);

        mAdapter = new MyAdapter(array, this);
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
