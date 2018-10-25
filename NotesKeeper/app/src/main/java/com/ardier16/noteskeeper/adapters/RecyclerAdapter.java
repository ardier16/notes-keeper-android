package com.ardier16.noteskeeper.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardier16.noteskeeper.notes.Note;
import com.ardier16.noteskeeper.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";
    private ArrayList<Note> notes;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView noteTitle;
        private final TextView notePriority;
        private final TextView noteCreated;
        private final ImageView imageView;

        ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });

            noteTitle = v.findViewById(R.id.noteTitle);
            notePriority = v.findViewById(R.id.notePriority);
            noteCreated = v.findViewById(R.id.noteCreated);
            imageView = v.findViewById(R.id.noteImg);
        }

        TextView getNoteTitle() {
            return noteTitle;
        }

        TextView getNotePriority() {
            return notePriority;
        }

        TextView getNoteCreated() {
            return noteCreated;
        }

        ImageView getImageView() {
            return imageView;
        }
    }

    public RecyclerAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notes_list_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Note note = notes.get(position);

        viewHolder.getNoteTitle().setText(note.getTitle());
        viewHolder.getNotePriority().setText(getPriority(note));
        viewHolder.getNoteCreated().setText(getDateString(note.getDateCreated()));

        if (note.getImagePath() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(note.getImagePath());

            if (bitmap != null) {
                viewHolder.getImageView().setImageBitmap(bitmap);
            } else {
                // Photo is deleted from the gallery
                note.setImagePath(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
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
        SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
        return formatDate.format(date);
    }
}
