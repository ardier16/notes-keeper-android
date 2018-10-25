package com.ardier16.noteskeeper.notes;

import android.content.Context;

import com.ardier16.noteskeeper.R;

import java.util.ArrayList;

public class NotesListHelper {
    private static final NotePriority[] priorities = new NotePriority[]{
            NotePriority.HIGH,
            NotePriority.LOW,
            NotePriority.LOW,
            NotePriority.MEDIUM,
            NotePriority.HIGH,
            NotePriority.HIGH,
            NotePriority.LOW,
            NotePriority.HIGH,
            NotePriority.MEDIUM,
            NotePriority.LOW
    };

    private static ArrayList<Note> filterNotesByPriority(ArrayList<Note> notes, NotePriority priority) {
        ArrayList<Note> result = new ArrayList<>();

        for (Note note : notes) {
            if (note.getPriority().equals(priority)) {
                result.add(note);
            }
        }

        return result;
    }

    public static ArrayList<Note> filterNotesByPriorities(ArrayList<Note> notes, boolean low, boolean medium, boolean high) {
        ArrayList<Note> result = new ArrayList<>(notes);

        if (!low) {
            result.removeAll(filterNotesByPriority(result, NotePriority.LOW));
        }
        if (!medium) {
            result.removeAll(filterNotesByPriority(result, NotePriority.MEDIUM));
        }
        if (!high) {
            result.removeAll(filterNotesByPriority(result, NotePriority.HIGH));
        }

        return result;
    }

    public static ArrayList<Note> filterNotesByTitle(ArrayList<Note> notes, String titlePart) {
        ArrayList<Note> result = new ArrayList<>();

        for (Note note : notes) {
            if (note.getTitle().toLowerCase().contains(titlePart.toLowerCase())) {
                result.add(note);
            }
        }

        return result;
    }

    public static void fillData(ArrayList<Note> notes, int notesCount, Context ctx) {
        if (notes.size() == 0) {
            for (int i = 0; i < notesCount; i++) {
                notes.add(new Note(ctx.getString(R.string.note) + i,
                        ctx.getString(R.string.some_description), priorities[i % 10], null));
            }
        }
    }
}
