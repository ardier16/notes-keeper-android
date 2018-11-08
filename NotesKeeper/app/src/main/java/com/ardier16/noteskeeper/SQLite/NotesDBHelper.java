package com.ardier16.noteskeeper.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "notesDb";
    public static final String TABLE_NOTES = "notes";

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_IMAGE_PATH = "image_path";
    public static final String KEY_CREATED = "date_created";

    public NotesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NOTES + "(" +
                KEY_ID + " integer primary key," +
                KEY_TITLE + " text," +
                KEY_DESCRIPTION + " text," +
                KEY_PRIORITY + " text," +
                KEY_CREATED + " text," +
                KEY_IMAGE_PATH + " text" + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NOTES);
        onCreate(db);
    }
}
