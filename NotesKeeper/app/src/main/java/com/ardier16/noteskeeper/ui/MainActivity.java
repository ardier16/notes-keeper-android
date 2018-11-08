package com.ardier16.noteskeeper.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.ardier16.noteskeeper.SQLite.DBAssistant;
import com.ardier16.noteskeeper.notes.Note;
import com.ardier16.noteskeeper.adapters.NoteAdapter;
import com.ardier16.noteskeeper.notes.NotesListHelper;
import com.ardier16.noteskeeper.notes.NotePriority;
import com.ardier16.noteskeeper.R;
import com.ardier16.noteskeeper.notes.SortType;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT_ID = 2;

    private static final int ADD_NOTE = 1;
    private static final int EDIT_NOTE = 2;

    private static SortType sortType = SortType.NONE;

    private int editedNoteIndex = 0;

    public static ArrayList<Note> notes = new ArrayList<>();
    private static ArrayList<Note> notesFiltered;
    private DBAssistant db;

    MenuItem lowPriorityItem;
    MenuItem mediumPriorityItem;
    MenuItem highPriorityItem;

    private static boolean lowPriorityChecked = true;
    private static boolean mediumPriorityChecked = true;
    private static boolean highPriorityChecked = true;

    EditText searchField;
    ListView notesList;

    NoteAdapter noteAdapter;
    AlertDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBAssistant(this);

        setNotesList();
        setSearchField();
        registerForContextMenu(notesList);
    }

    private void setNotesList() {
        // Uncomment if you need some data to debug
        //NotesListHelper.fillData(notes, 100, this);

        notes = db.getNotes();
        notesFiltered = new ArrayList<>(notes);

        noteAdapter = new NoteAdapter(this, notesFiltered);
        notesList = findViewById(R.id.notesList);

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Note note = notesFiltered.get(position);
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                putIntentExtras(intent, note);
                startActivity(intent);
            }
        });

        notesList.setAdapter(noteAdapter);
    }

    private void setSearchField() {
        searchField = findViewById(R.id.searchField);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterNotes();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }
        }

        lowPriorityItem = menu.findItem(R.id.lowPriorityFilter);
        mediumPriorityItem = menu.findItem(R.id.mediumPriorityFilter);
        highPriorityItem = menu.findItem(R.id.highPriorityFilter);

        setPrioritiesMenuChecked();
        filterNotes();

        return true;
    }

    private void setPrioritiesMenuChecked() {
        lowPriorityItem.setChecked(lowPriorityChecked);
        mediumPriorityItem.setChecked(mediumPriorityChecked);
        highPriorityItem.setChecked(highPriorityChecked);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.addItem:
                addNote(null);
                break;
            case R.id.sortByTitleItem:
                sortType = sortType == SortType.SORT_BY_TITLE_ASC ?
                        SortType.SORT_BY_TITLE_DESC : SortType.SORT_BY_TITLE_ASC;
                break;
            case R.id.sortByPriorityItem:
                sortType = sortType == SortType.SORT_BY_PRIORITY_ASC ?
                        SortType.SORT_BY_PRIORITY_DESC : SortType.SORT_BY_PRIORITY_ASC;
                break;
            case R.id.lowPriorityFilter:
                lowPriorityChecked = !lowPriorityChecked;
                break;
            case R.id.mediumPriorityFilter:
                mediumPriorityChecked = !mediumPriorityChecked;
                break;
            case R.id.highPriorityFilter:
                highPriorityChecked = !highPriorityChecked;
                break;
            default:
                break;
        }

        setPrioritiesMenuChecked();
        filterNotes();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        switch (item.getItemId()) {
            case CM_DELETE_ID:
                showDeleteDialog(acmi.position);
                return true;
            case CM_EDIT_ID:
                editedNoteIndex = acmi.position;
                editNote(notesFiltered.get(editedNoteIndex));
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void showDeleteDialog(int notePosition) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle(R.string.delete_title);
        dialogBuilder.setMessage(R.string.delete_message);
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Note note = notesFiltered.get(notePosition);
                        notesFiltered.remove(note);
                        notes.remove(note);
                        db.deleteNote(note.getId());
                        noteAdapter.notifyDataSetChanged();
                    }
                });

        dialogBuilder.setNegativeButton(
                R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        deleteDialog = dialogBuilder.create();
        deleteDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        notes = db.getNotes();
        notesFiltered = new ArrayList<>(notes);

        db.close();
    }


    public void addNote(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        startActivityForResult(intent, ADD_NOTE);
    }

    private void editNote(Note note) {
        Intent intent = new Intent(this, EditActivity.class);
        putIntentExtras(intent, note);
        startActivityForResult(intent, EDIT_NOTE);
    }

    private void putIntentExtras(Intent intent, Note note) {
        intent.putExtra("title", note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("priority", note.getPriority().name());
        intent.putExtra("imagePath", note.getImagePath());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE && data != null) {
            db.addNote(convertResultDataToNote(data));
            notes = db.getNotes();
        } else if (requestCode == EDIT_NOTE && data != null) {
            replaceNoteDataFromIntent(data);
        }

        refreshActivity();
    }

    private Note convertResultDataToNote(Intent data) {
        String title = data.getStringExtra("title");
        String description = data.getStringExtra("description");
        NotePriority priority = NotePriority.valueOf(data.getStringExtra("priority"));
        String imageUri = data.getStringExtra("imagePath");

        return new Note(title, description, priority, imageUri);
    }

    private void replaceNoteDataFromIntent(Intent data) {
        String title = data.getStringExtra("title");
        String description = data.getStringExtra("description");
        NotePriority priority = NotePriority.valueOf(data.getStringExtra("priority"));
        String imagePath = data.getStringExtra("imagePath");

        Note note = notes.get(notes.indexOf(notesFiltered.get(editedNoteIndex)));

        note.setTitle(title);
        note.setDescription(description);
        note.setPriority(priority);
        note.setDateCreated(new Date());

        if (imagePath != null) {
            note.setImagePath(imagePath);
        }

        db.updateNote(note.getId(), note);
    }

    private void refreshActivity() {
        searchField.setText("");

        lowPriorityItem.setChecked(true);
        mediumPriorityItem.setChecked(true);
        highPriorityItem.setChecked(true);

        sortType = SortType.NONE;

        notesFiltered = new ArrayList<>(notes);
        noteAdapter.refresh(notesFiltered);
    }

    private void filterNotes() {
        if (lowPriorityItem != null) {
            boolean lowPriority = lowPriorityItem.isChecked();
            boolean mediumPriority = mediumPriorityItem.isChecked();
            boolean highPriority = highPriorityItem.isChecked();

            notesFiltered = NotesListHelper.filterNotesByTitle(notes, searchField.getText().toString());
            notesFiltered = NotesListHelper.filterNotesByPriorities(notesFiltered, lowPriority,
                    mediumPriority, highPriority);

            NotesListHelper.sortNotes(notesFiltered, sortType);
            noteAdapter.refresh(notesFiltered);
        }
    }

    public void clearSearchField(View view) {
        searchField.setText("");

        boolean lowPriority = lowPriorityItem.isChecked();
        boolean mediumPriority = mediumPriorityItem.isChecked();
        boolean highPriority = highPriorityItem.isChecked();

        notesFiltered = NotesListHelper.filterNotesByPriorities(notes, lowPriority,
                mediumPriority, highPriority);

        noteAdapter.refresh(notesFiltered);
    }
}
