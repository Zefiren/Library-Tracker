package com.example.grzegorz.myfirstapp;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Grzegorz on 01/06/2017.
 */
@Parcel
public class Book {

    //first author
    //isbn 10
    String title;
    String author;
    String isbn_code;
    boolean have_book;
    int libraryID;

    public Book() {
    }

    Book(String title, String author,String isbn_code, boolean have_book) {
        this.title = title;
        this.author = author;
        this.isbn_code = isbn_code;
        this.have_book = have_book;
        this.libraryID = 0;
    }

    @ParcelConstructor
    Book(String title, String author, String isbn_code, boolean have_book, int libraryID) {
        this.title = title;
        this.author = author;
        this.isbn_code = isbn_code;
        this.have_book = have_book;
        this.libraryID = libraryID;
    }

    Book(Book book) {
        this.title = book.title;
        this.author = book.author;
        this.have_book = book.have_book;
        this.libraryID = book.libraryID;
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

    public String getIsbn_code() {
        return isbn_code;
    }

    public void setIsbn_code(String isbn_code) {
        this.isbn_code = isbn_code;
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
