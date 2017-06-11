package com.example.grzegorz.myfirstapp;


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import java.util.ArrayList;
        import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bookTracker";

    // Books table name
    private static final String TABLE_BOOKS = "books";

    // Book Table Columns names
    private static final String KEY_ID = "book_id";
    private static final String KEY_TITLE = "name";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_ISBN = "isbn_10";
    private static final String KEY_HAVE = "haveBook";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_TITLE + "  VARCHAR(200),"
                + KEY_AUTHOR + " VARCHAR(100),"  + KEY_HAVE + " BIT," + KEY_ISBN + " VARCHAR(10)" + ")";
        db.execSQL(CREATE_BOOKS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);

        // Create tables again
        onCreate(db);
    }

    // Adding new book
    public void addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, book.getTitle());
        values.put(KEY_AUTHOR, book.getAuthor());
        values.put(KEY_ISBN, book.getIsbn_code());
        values.put(KEY_HAVE, book.isHave_book() ? 1 : 0);


        // Inserting Row
        db.insert(TABLE_BOOKS, null, values);
        db.close(); // Closing database connection}
    }
    // Getting single book with the library int id
    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOKS, new String[] { KEY_ID,
                        KEY_TITLE, KEY_AUTHOR, KEY_ISBN, KEY_HAVE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Book book = new Book(cursor.getString(1), cursor.getString(2), cursor.getString(4), cursor.getInt(3) == 1,cursor.getInt(0));
        cursor.close();

        // return book
        return book;
    }

    // Getting single book with the given String ISBN code
    public Book getBookByISBN(String isbn) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, new String[] { KEY_ID,
                        KEY_TITLE, KEY_AUTHOR, KEY_ISBN, KEY_HAVE }, KEY_ISBN + "=?",
                new String[] { isbn }, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            Book book = new Book(cursor.getString(1), cursor.getString(2), cursor.getString(4), cursor.getInt(3) == 1, cursor.getInt(0));
            cursor.close();
            // return book
            return book;

        }
        return null;
    }

    // Getting All books
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<Book>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setIsbn_code(cursor.getString(4));
                book.setHave_book(cursor.getInt(3) == 1);
                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }

        // return book list
        return bookList;
    }

    // Getting books Count
    public int getBooksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BOOKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating single book
    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, book.getTitle());
        values.put(KEY_AUTHOR, book.getAuthor());
        values.put(KEY_ISBN, book.getIsbn_code());
        values.put(KEY_HAVE, book.isHave_book());
        // updating row
        return db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(book.getLibraryID()) });

    }

    // Deleting single book
    public void deleteBook(Book book) {    SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKS, KEY_ID + " = ?",
                new String[] { String.valueOf(book.getLibraryID()) });
        db.close();}
}