package com.ardier16.noteskeeper.comparators;

import com.ardier16.noteskeeper.notes.Note;

import java.util.Comparator;

public class NotePriorityComparator implements Comparator<Note> {
    public int compare(Note a, Note b) {
        return a.getPriority().compareTo(b.getPriority());
    }
}