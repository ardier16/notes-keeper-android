package com.ardier16.noteskeeper.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ardier16.noteskeeper.notes.Note;
import com.ardier16.noteskeeper.notes.NotePriority;

import java.util.ArrayList;
import java.util.Date;

public class DBAssistant {
    private NotesDBHelper dbHelper;
    private SQLiteDatabase db;

    public DBAssistant(Context context) {
        this.dbHelper = new NotesDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addNote(Note note) {
        ContentValues values = new ContentValues();
        putContentValues(values, note);
        db.insert(NotesDBHelper.TABLE_NOTES, null, values);
    }

    public void updateNote(int noteId, Note updated) {
        ContentValues values = new ContentValues();
        putContentValues(values, updated);
        db.update(NotesDBHelper.TABLE_NOTES, values,
                NotesDBHelper.KEY_ID + "= ?",
                new String[]{Integer.toString(noteId)});
    }

    public void deleteNote(int noteId) {
        db.delete(NotesDBHelper.TABLE_NOTES,
                NotesDBHelper.KEY_ID + "= " + noteId, null);
    }

    public ArrayList<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(NotesDBHelper.TABLE_NOTES, null, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(NotesDBHelper.KEY_ID);
            int titleIndex = cursor.getColumnIndex(NotesDBHelper.KEY_TITLE);
            int descIndex = cursor.getColumnIndex(NotesDBHelper.KEY_DESCRIPTION);
            int priorityIndex = cursor.getColumnIndex(NotesDBHelper.KEY_PRIORITY);
            int imagePathIndex = cursor.getColumnIndex(NotesDBHelper.KEY_IMAGE_PATH);
            int dateCreatedIndex = cursor.getColumnIndex(NotesDBHelper.KEY_CREATED);

            do {
                Note note = new Note(
                        cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        cursor.getString(descIndex),
                        NotePriority.valueOf(cursor.getString(priorityIndex)),
                        cursor.getString(imagePathIndex),
                        new Date(cursor.getString(dateCreatedIndex))
                );

                notes.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return notes;
    }

    public void close() {
        dbHelper.close();
    }

    private void putContentValues(ContentValues values, Note note) {
        values.put(NotesDBHelper.KEY_TITLE, note.getTitle());
        values.put(NotesDBHelper.KEY_DESCRIPTION, note.getDescription());
        values.put(NotesDBHelper.KEY_PRIORITY, note.getPriority().name());
        values.put(NotesDBHelper.KEY_IMAGE_PATH, note.getImagePath());
        values.put(NotesDBHelper.KEY_CREATED, note.getDateCreated().toString());
    }
}
