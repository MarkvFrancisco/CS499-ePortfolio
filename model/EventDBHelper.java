package com.example.cs360_projectthree.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EventDBHelper extends SQLiteOpenHelper {

    // Naming the SQLite .db file for events
    public static final String DBNAME = "Events.db";

    // initializing the database with the DBNAME and version #
    public EventDBHelper(@Nullable Context context) {
        super(context, DBNAME, null, 2);
    }

    // Called when the database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE events(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "title TEXT, " +
                "description TEXT, " +
                "phone TEXT, " +
                "smsSent INTEGER DEFAULT 0)");
    }

    // Called when the database version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }

    // Inserts a new event into the database
    public long insertEvent(String date, String title, String description, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("date", date);
        values.put("title", title);
        values.put("description", description);
        values.put("phone", phone);
        values.put("smsSent", 0);

        return db.insert("events", null, values);
    }

    // Deletes an event from the database
    public boolean deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("events", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Retrieves all events from the database
    public ArrayList<Event> getEventsList() {
        ArrayList<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM events", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                int smsSent = cursor.getInt(cursor.getColumnIndexOrThrow("smsSent"));

                eventList.add(new Event(id, date, title, description, phone, smsSent));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return eventList;
    }

    // Updates an event in the database
    public boolean updateEvent(int id, String date, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("date", date);
        values.put("title", title);
        values.put("description", description);

        int result = db.update("events", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Marks an event as sent via SMS
    public boolean markSmsSent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("smsSent", 1);

        int result = db.update("events", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}