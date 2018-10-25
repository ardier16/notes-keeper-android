package com.ardier16.noteskeeper.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardier16.noteskeeper.notes.Note;
import com.ardier16.noteskeeper.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class NoteAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private ArrayList<Note> notes;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.ctx = context;
        this.notes = notes;
        this.inflater = (LayoutInflater) this.ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.notes.size();
    }

    @Override
    public Object getItem(int i) {
        return this.notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = this.inflater.inflate(R.layout.notes_list_item, parent, false);
        }

        Note note = getNote(position);

        ((TextView) view.findViewById(R.id.noteTitle)).setText(note.getTitle());
        ((TextView) view.findViewById(R.id.notePriority)).setText(getPriority(note));
        ((TextView) view.findViewById(R.id.noteCreated)).setText(getDateString(note.getDateCreated()));

        ImageView imageView = view.findViewById(R.id.noteImg);

        if (note.getImagePath() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(note.getImagePath());

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                // Photo is deleted from the gallery
                note.setImagePath(null);
            }
        } else {
            imageView.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.note_icon));
        }

        return view;
    }

    private Note getNote(int position) {
        return (Note) getItem(position);
    }

    private String getPriority(Note note) {
        switch (note.getPriority()) {
            case LOW:
                return "★☆☆";
            case MEDIUM:
                return "★★☆";
            case HIGH:
                return "★★★";
            default:
                return "";
        }
    }

    private String getDateString(Date date) {
        SimpleDateFormat formatDate = new SimpleDateFormat(ctx.getString(R.string.date_format), Locale.US);
        return formatDate.format(date);
    }

    public void refresh(ArrayList<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
}
