package com.example.cs360_projectthree.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class LoginDBHelper extends SQLiteOpenHelper {

    // Naming the SQLite .db file
    public static final String DBNAME = "Login.db";

    // initializing the database with the DBNAME and version #
    public LoginDBHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    // Called when the database is first created
    // Creates the "users" table with username as the primary key and password as a field
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT)");
    }

    // Called when the database version changes
    // Drops the existing table and recreates it
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // Inserts a new user into the database
    // Returns true if insertion is successful, otherwise false
    public boolean insertData(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Store username and password into key-value pairs
        values.put("username", username);
        values.put("password", password);

        // Insert data into "users" table
        long result = db.insert("users", null, values);

        // If result is -1, insertion failed
        return result != -1;
    }

    // Checks if a username already exists in the database
    // Returns true if the username is found, false otherwise
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Querying the database for matching username
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username = ?",
                new String[]{username}
        );

        // If cursor count > 0, username exists
        boolean exists = cursor.getCount() > 0;

        // Always close cursor to prevent memory leaks
        cursor.close();

        return exists;
    }

    // LOGIN AUTHENTICATION
    // Verifies that both username and password match a record in the database
    // Returns true if credentials are valid, otherwise false
    public boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Query database for matching username AND password
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username = ? AND password = ?",
                new String[]{username, password}
        );

        // If cursor count > 0, credentials are valid
        boolean exists = cursor.getCount() > 0;

        // Close cursor after use
        cursor.close();

        return exists;
    }
}