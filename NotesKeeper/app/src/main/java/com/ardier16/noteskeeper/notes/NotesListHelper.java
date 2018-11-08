package com.ardier16.noteskeeper.notes;

import android.content.Context;

import com.ardier16.noteskeeper.R;
import com.ardier16.noteskeeper.comparators.NotePriorityComparator;
import com.ardier16.noteskeeper.comparators.NoteTitleComparator;

import java.util.ArrayList;
import java.util.Collections;

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

    private static void sortNotesByTitle(ArrayList<Note> notes, boolean sortByTitleAsc) {
        Collections.sort(notes, new NoteTitleComparator());

        if (!sortByTitleAsc) {
            Collections.reverse(notes);
        }
    }

    private static void sortNotesByPriority(ArrayList<Note> notes, boolean sortByPriorityAsc) {
        Collections.sort(notes, new NotePriorityComparator());

        if (!sortByPriorityAsc) {
            Collections.reverse(notes);
        }
    }

    public static void sortNotes(ArrayList<Note> notes, SortType sortType) {
        switch (sortType) {
            case SORT_BY_TITLE_ASC:
                sortNotesByTitle(notes, true);
                return;
            case SORT_BY_TITLE_DESC:
                sortNotesByTitle(notes, false);
                return;
            case SORT_BY_PRIORITY_ASC:
                sortNotesByPriority(notes, true);
                return;
            case SORT_BY_PRIORITY_DESC:
                sortNotesByPriority(notes, false);
                return;
            case NONE:
            default:
        }
    }
}
