package com.example.grzegorz.myfirstapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by Grzegorz on 22/06/2017.
 */

public class ApiBookSearch extends AsyncTask<String,Void,List<Book>> {

    /*
    private List<Book> searchBooks(final String title, final String author,Context packageContext) {
        final String titleVar = title;
        final String authorVar = author;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // All your networking logic
                // should be here
                // Create URL
                URL googleBooksEndpoint = null;
                try {
                    String inauth = "+inauthor:" + authorVar;
                    List<Book> bookList=new ArrayList<Book>();

                    if (authorVar.length()<1)
                        inauth = "";
                    googleBooksEndpoint = new URL("https://www.googleapis.com/books/v1/volumes?q="+titleVar+inauth+"&key="+ MainActivity.googleBooksApiKey);

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
                        String title,author="",isbn;
                        for (int i = 0; i < books.length() && i < 50; i++) {
                            JSONObject book = books.getJSONObject(i);
                            JSONObject vol_info = book.getJSONObject("volumeInfo");
                            title = vol_info.getString("title");
                            JSONArray authors = vol_info.getJSONArray("authors");
                            for (int j = 0; j < 1 *//*authors.length()*//*; j++) {
                                author = authors.getString(j);
                            }
                            isbn = vol_info.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier");
                            bookList.add(new Book(title,author,isbn,false));


                        }
                        Log.d("number of books",parser.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("title"));



//                        final Book[] array = new Book[bookList.size()];
//                        bookList.toArray(array);
//
//
//                        packageContext.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mAdapter = new MyAdapter(array, packageContext.this);
//                                mRecyclerView.setAdapter(mAdapter);
//
//                            }
//                        });


                    } else {
                        Log.d("what went wrong?",Integer.toString(myConnection.getResponseCode()));
                        // Error handling code goes here
                    }
                    Log.d("number of books","connection done");
                    Log.d("url was",myConnection.getResponseMessage());
                    Log.d("url was",googleBooksEndpoint.toString());


                    myConnection.disconnect();
//                    return bookList;
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }
*/

    @Override
    protected List<Book> doInBackground(String... params) {
        String titleVar = params[0];
        String authorVar = params[1];
        List<Book> bookList=new ArrayList<Book>();

        URL googleBooksEndpoint = null;
        try {
            String inauth = "+inauthor:" + authorVar;

            if (authorVar.length()<1)
                inauth = "";
            googleBooksEndpoint = new URL("https://www.googleapis.com/books/v1/volumes?q="+titleVar+inauth+"&key="+ MainActivity.googleBooksApiKey);

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
                Log.d("number of books",parser.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("title"));



//                        final Book[] array = new Book[bookList.size()];
//                        bookList.toArray(array);
//
//
//                        packageContext.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mAdapter = new MyAdapter(array, packageContext.this);
//                                mRecyclerView.setAdapter(mAdapter);
//
//                            }
//                        });


            } else {
                Log.d("what went wrong?",Integer.toString(myConnection.getResponseCode()));
                // Error handling code goes here
            }
            Log.d("number of books","connection done");
            Log.d("url was",myConnection.getResponseMessage());
            Log.d("url was",googleBooksEndpoint.toString());


            myConnection.disconnect();
//                    return bookList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }
}
