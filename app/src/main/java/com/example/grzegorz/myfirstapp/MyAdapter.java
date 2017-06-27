package com.example.grzegorz.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Grzegorz on 10/05/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.BookViewHolder> {

    private static List<Book> mDataset;
    private static Context packageContext;




    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Book[] myDataset, Context packageContext) {
        mDataset = new ArrayList<>( Arrays.asList(myDataset));
        this.packageContext = packageContext;
    }

    public Book getItem(int position) {
        return mDataset.get(position);
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
    public void onBindViewHolder(BookViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        Log.v("title of book",mDataset[position].getTitle());

        holder.mTextView.setText(mDataset.get(position).getTitle());
        holder.mATextView.setText(mDataset.get(position).getAuthor());
        if(mDataset.get(position).isHave_book()) {
//            Log.v("have book","title:"+mDataset[position].getTitle()+"; have:"+mDataset[position].isHave_book());
            holder.mCardView.setBackgroundColor(packageContext.getResources().getColor(R.color.colorAccent));
        }
    }

    public void delete(int position){
        Log.v("book MENU DELETE ",position+"");

        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class BookViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, View.OnClickListener,MenuItem.OnMenuItemClickListener{
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
            Log.v("selectedPosition","numbr: " + mSelectedItemPosition+ "; selected book: "+MyAdapter.mDataset.get(getAdapterPosition()).getTitle());

            Book selectedBook = MyAdapter.mDataset.get(getAdapterPosition());
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

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int pos = item.getOrder();
            int listPosition = getAdapterPosition();
            Book book = MyAdapter.mDataset.get(listPosition);
            if(pos == 0){
                Log.v("book deleted",book.getTitle());
                MySQLiteHelper db = new MySQLiteHelper(packageContext);
                db.deleteBook(book);
                delete(listPosition);
                return  true;
            }
            else
                return  false;
        }

    }



}
