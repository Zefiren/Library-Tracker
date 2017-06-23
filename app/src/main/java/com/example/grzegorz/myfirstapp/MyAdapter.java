package com.example.grzegorz.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.gaurav.cdsrecyclerview.CdsRecyclerView;
import com.gaurav.cdsrecyclerview.CdsRecyclerViewAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Grzegorz on 10/05/2017.
 */

public class MyAdapter extends CdsRecyclerViewAdapter<Book,MyAdapter.BookViewHolder> {

    private static Book[] mDataset;
    private static Context packageContext;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class BookViewHolder extends CdsRecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, View.OnClickListener {
        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mTextView;
        public TextView mATextView;

        public BookViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cardView);
            mTextView = (TextView) itemView.findViewById(R.id.titleText);
            mATextView = (TextView) itemView.findViewById(R.id.authorText);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick (View v) {

            int mSelectedItemPosition = getAdapterPosition();
            Log.v("selectedPosition","numbr: " + mSelectedItemPosition+ "; selected book: "+MyAdapter.mDataset[getAdapterPosition()].getTitle());

            Book selectedBook = MyAdapter.mDataset[getAdapterPosition()];
            Intent intent = new Intent(MyAdapter.packageContext, DisplaySingleBookActivity.class);
            intent.putExtra(DisplayLibraryActivity.EXTRA_BOOK, Parcels.wrap(selectedBook));

            //Find out if searching books to add or displaying one already tracked
            if(packageContext instanceof DisplaySearchResultActivity)
                intent.putExtra(DisplaySearchResultActivity.EXTRA_ADDING, true);


            packageContext.startActivity(intent);
            //Your other handling in onclick
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            AdapterView.AdapterContextMenuInfo mi =(AdapterView.AdapterContextMenuInfo) menuInfo;

            menu.setHeaderTitle("Select Action");
            if(packageContext instanceof DisplayLibraryActivity){
                MenuItem item = menu.add(Menu.NONE, v.getId(),
                        Menu.NONE, R.string.menuRemove);
                item.setOnMenuItemClickListener(this);
            }
            /*else
                menu.add(Menu.NONE, v.getId(),
                    Menu.NONE, R.string.menuAdd);*/
        }



    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Book[] myDataset, Context packageContext) {
        super(packageContext, new ArrayList<Book>(Arrays.asList(myDataset)) );
        mDataset = myDataset;
        this.packageContext = packageContext;
    }

    public Book getItemAtPosition(int position) {
        return mDataset[position];
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_card_view, parent, false);

        // set the view's size, margins, paddings and layout parameters

        BookViewHolder vh = new BookViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void bindHolder(BookViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        Log.v("title of book",mDataset[position].getTitle());

        holder.mTextView.setText(mDataset[position].getTitle());
        holder.mATextView.setText(mDataset[position].getAuthor());
        if(mDataset[position].isHave_book()) {
//            Log.v("have book","title:"+mDataset[position].getTitle()+"; have:"+mDataset[position].isHave_book());
            holder.mCardView.setBackgroundColor(packageContext.getResources().getColor(R.color.colorAccent));
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
