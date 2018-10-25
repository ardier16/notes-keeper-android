package com.ardier16.noteskeeper.comparators;

import com.ardier16.noteskeeper.notes.Note;

import java.util.Comparator;

public class NoteTitleComparator implements Comparator<Note> {
    public int compare(Note a, Note b) {
        return a.getTitle().compareTo(b.getTitle());
    }
}