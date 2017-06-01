package com.example.grzegorz.myfirstapp;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Grzegorz on 01/06/2017.
 */
@Parcel
public class Book {

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
