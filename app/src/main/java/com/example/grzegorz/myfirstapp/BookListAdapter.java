package com.example.grzegorz.myfirstapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grzegorz on 09/05/2017.
 */

public class BookListAdapter extends ArrayAdapter<String> {

    BookListAdapter(Context context, int resourceID, List<String> books){
        super(context,resourceID,books);
//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.activity_listview, books);

//        ListView listView = (ListView) findViewById(R.id.messageList);
//        listView.setAdapter(adapter);
//
//
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                           int pos, long id) {
//                // TODO Auto-generated method stub
//                myCursor.moveToPosition(pos);
//
//                Log.d("Book:",Integer.toString(myCursor.getInt(0)) +  " "+ myCursor.getString(1));
//                myDB.delete("books","bookID = ?",new String[]{Integer.toString(myCursor.getInt(0))});
//
//                final Cursor myCursor =
//                        myDB.query("books", new String[]{"bookID","title","author"} ,null, null, null, null, null);
//
//                List<String> books = new ArrayList<String>();
//                while(myCursor.moveToNext()) {
//                    String book = myCursor.getString(1) + " by " + myCursor.getString(2);
//                    books.add(book);
//                }
//
//                ArrayAdapter adapter = new ArrayAdapter<String>(DisplayLibraryActivity.this,
//                        R.layout.activity_listview, books);
//
//                ListView listView = (ListView) findViewById(R.id.messageList);
//                listView.setAdapter(adapter);
//                Log.d("long clicked","pos: " + pos);
//
//                return true;
//            }
//        });
    }

}
