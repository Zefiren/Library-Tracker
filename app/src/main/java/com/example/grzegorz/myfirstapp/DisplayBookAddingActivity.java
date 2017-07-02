package com.example.grzegorz.myfirstapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Grzegorz on 09/05/2017.
 */

public class DisplayBookAddingActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.example.grzegorz.myfirstapp.TITLE";
    public static final String EXTRA_AUTHOR = "com.example.grzegorz.myfirstapp.AUTHOR";
    public static String authorSave;
    public static String titleSave;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_display, menu);

        MenuItem item = menu.findItem(R.id.helpItem);
        item.setVisible(true);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast t = Toast.makeText(DisplayBookAddingActivity.this,"testing, Help?",Toast.LENGTH_LONG);
                t.show();
                return false;
            }
        });
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editAuthor = (EditText) findViewById(R.id.editAuthor);

        String title = editTitle.getText().toString().trim();
        String author = editAuthor.getText().toString().trim();

        savedInstanceState.putString("author", author);
        savedInstanceState.putString("title", title);
    }


    @Override
    public void onPause()
    {
        super.onPause();
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editAuthor = (EditText) findViewById(R.id.editAuthor);

        String title = editTitle.getText().toString().trim();
        String author = editAuthor.getText().toString().trim();

        authorSave = author;
        titleSave = title;
        //read current recyclerview position

    }


    @Override
    protected void onResume() {
        super.onResume();
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editAuthor = (EditText) findViewById(R.id.editAuthor);

        editTitle.setText(titleSave);
        editAuthor.setText(authorSave);
        Log.d("authorResumeeeeee",": " + authorSave);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editAuthor = (EditText) findViewById(R.id.editAuthor);

        editTitle.setText( savedInstanceState.getString("title"));
        editAuthor.setText( savedInstanceState.getString("author"));
        Log.d("authorRestore",": " +  savedInstanceState.getString("author"));
        Log.d("titleRestore",": " +  savedInstanceState.getString("title"));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);
    }



    public void searchBook(View view) {
        Log.d("number of books", "beginning");
        if (true) {
            Intent intent = new Intent(this, DisplaySearchResultActivity.class);

            EditText editTitle = (EditText) findViewById(R.id.editTitle);
            EditText editAuthor = (EditText) findViewById(R.id.editAuthor);

            String title = editTitle.getText().toString().trim();
            String author = editAuthor.getText().toString().trim();

            intent.putExtra(EXTRA_TITLE, title);
            intent.putExtra(EXTRA_AUTHOR, author);
            startActivity(intent);
        } else

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // All your networking logic
                    // should be here
                    // Create URL
                    URL googleBooksEndpoint = null;
                    try {
//                    googleBooksEndpoint = new URL("https://www.googleapis.com/books/v1/volumes?q=flowers+inauthor:keyes&key="+ MainActivity.googleBooksApiKey);

                        EditText editTitle = (EditText) findViewById(R.id.editTitle);
                        EditText editAuthor = (EditText) findViewById(R.id.editAuthor);

                        String title = editTitle.getText().toString().trim();
                        String author;
                        if(editAuthor.getText().toString().trim().length() > 0)
                            author = "+inauthor:" + editAuthor.getText().toString().trim();
                        else
                            author = "";

                        googleBooksEndpoint = new URL("https://www.googleapis.com/books/v1/volumes?" + "q=" + title + author);

                        // Create connection
                        HttpsURLConnection myConnection =
                                (HttpsURLConnection) googleBooksEndpoint.openConnection();
//                    myConnection.setRequestProperty("User-Agent", "google-books-v1");


                        myConnection.setRequestProperty("key", MainActivity.googleBooksApiKey);

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

                            JSONObject parser = (JSONObject) new JSONTokener(stringBuilder.toString()).nextValue();


                            JSONArray books = parser.getJSONArray("items");
                            List<String> bookList = new ArrayList<String>();
                            for (int i = 0; i < books.length() && i < 50; i++) {
                                JSONObject book = books.getJSONObject(i);
                                JSONObject vol_info = book.getJSONObject("volumeInfo");
                                StringBuilder bookInfo = new StringBuilder();
                                bookInfo.append(vol_info.getString("title") + " (");
                                JSONArray authors = vol_info.getJSONArray("authors");
                                for (int j = 0; j < authors.length(); j++) {
                                    if (j > 0)
                                        bookInfo.append(", ");
                                    bookInfo.append(authors.getString(j));
                                }
                                bookInfo.append(")");
                                bookList.add(bookInfo.toString());
                            }
                            Log.d("number of books", parser.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("title"));

                            String[] array = new String[bookList.size()];
                            bookList.toArray(array);
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(DisplayBookAddingActivity.this, R.layout.text_row_item, R.id.textView, array);
                            DisplayBookAddingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ListView bookListView = (ListView) findViewById(R.id.searchResults);

                                    bookListView.setAdapter(adapter);

                                }
                            });


                        } else {
                            Log.d("what went wrong?", Integer.toString(myConnection.getResponseCode()));
                            // Error handling code goes here
                        }
                        Log.d("number of books", "connection done");
                        Log.d("url was", myConnection.getResponseMessage());
                        Log.d("url was", googleBooksEndpoint.toString());


                        myConnection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
    }

    public void sendBook(View view) {
        Intent intent = new Intent(this, DisplayLibraryActivity.class);
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editAuthor = (EditText) findViewById(R.id.editAuthor);

        String title = editTitle.getText().toString();
        String author = editAuthor.getText().toString();


        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_AUTHOR, author);
        startActivity(intent);
    }
}
