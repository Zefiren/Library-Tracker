package com.example.grzegorz.myfirstapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.View;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Grzegorz on 06/06/2017.
 */

public class DisplaySearchResultActivity extends AppCompatActivity{

        public static final String EXTRA_ADDING = "com.example.grzegorz.myfirstapp.ADDING";




        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

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

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSearch);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);


            if (title != null  && author != null)
                if(title.length()+author.length() > 0)
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
            Log.v("Books","numbr: " + books.size());

            Book[] array = new Book[books.size()];
            books.toArray(array);

            mAdapter = new MyAdapter(array, this);
            mRecyclerView.setAdapter(mAdapter);

        }


        private void showLibrary() {
            MySQLiteHelper db = new MySQLiteHelper(this);
            List<Book> books = db.getAllBooks();
            Log.v("Books","numbr: " + books.size());

            Book[] array = new Book[books.size()];
            books.toArray(array);

            mAdapter = new MyAdapter(array, this);
            mRecyclerView.setAdapter(mAdapter);
        }



    private void searchBooks(final String title, final String author) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // All your networking logic
                    // should be here
                    // Create URL
                    URL googleBooksEndpoint = null;
                    try {
                        String inauth = "+inauthor:"+ author;
                        Log.v("number of books",title.length() + " title is "+ author);

                        if (author.length()<1)
                            inauth = "";
                        googleBooksEndpoint = new URL("https://www.googleapis.com/books/v1/volumes?q="+title+inauth+"&key="+ MainActivity.googleBooksApiKey);
                        Log.v("number of books",googleBooksEndpoint.toString()+ "    length =" + author.length());

//                        googleBooksEndpoint = new URL("https://www.googleapis.com/books/v1/volumes?"+ "q="+ title + "+inauthor:"+author);
//
                        // Create connection
                        HttpsURLConnection myConnection =
                                (HttpsURLConnection) googleBooksEndpoint.openConnection();
//                    myConnection.setRequestProperty("User-Agent", "google-books-v1");


                        myConnection.setRequestProperty("key",MainActivity.googleBooksApiKey);

                        if (myConnection.getResponseCode() == 200) {
                            // Success
                            // Further processing here
                            InputStream responseBody = myConnection.getInputStream();
                            BufferedReader responseBodyReader =
                                    new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                            StringBuilder stringBuilder = new StringBuilder();
                            String line;
                            while ((line = responseBodyReader.readLine()) != null) {
                                stringBuilder.append(line).append("\n");
                            }
                            responseBodyReader.close();

                            JSONObject parser=  (JSONObject) new JSONTokener(stringBuilder.toString()).nextValue();


                            JSONArray books = parser.getJSONArray("items");
                            List<Book> bookList=new ArrayList<Book>();
                            String title,author="",isbn;
                            for (int i = 0; i < books.length() && i < 50; i++) {
                                JSONObject book = books.getJSONObject(i);
                                JSONObject vol_info = book.getJSONObject("volumeInfo");
                                title = vol_info.getString("title");
                                JSONArray authors = vol_info.getJSONArray("authors");
                                for (int j = 0; j < 1 /*authors.length()*/; j++) {
                                    author = authors.getString(j);
                                }
                                isbn = vol_info.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier");
                                bookList.add(new Book(title,author,isbn,false));


                            }
                            Log.v("number of books",parser.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("title"));
                            final Book[] array = new Book[bookList.size()];
                            bookList.toArray(array);


                            DisplaySearchResultActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter = new MyAdapter(array, DisplaySearchResultActivity.this);
                                    mRecyclerView.setAdapter(mAdapter);

                                }
                            });


                        } else {
                            Log.v("what went wrong?",Integer.toString(myConnection.getResponseCode()));
                            // Error handling code goes here
                        }
                        Log.v("number of books","connection done");
                        Log.v("url was",myConnection.getResponseMessage());
                        Log.v("url was",googleBooksEndpoint.toString());


                        myConnection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
    }

}
