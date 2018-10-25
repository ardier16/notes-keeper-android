package com.ardier16.noteskeeper.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardier16.noteskeeper.R;

public class NoteDetailActivity extends AppCompatActivity {

    Intent intent;

    TextView tvTitle;
    TextView tvDesc;
    ImageView noteImage;
    TextView lowPriority;
    TextView mediumPriority;
    TextView highPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);


        tvTitle = findViewById(R.id.titleField);
        tvDesc = findViewById(R.id.descField);
        noteImage = findViewById(R.id.noteImg);

        lowPriority = findViewById(R.id.lowPriority);
        mediumPriority = findViewById(R.id.mediumPriority);
        highPriority = findViewById(R.id.highPriority);

        intent = getIntent();
        setNoteData(intent);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.note_details));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setNoteData(Intent intent) {
        tvTitle.setText(intent.getStringExtra("title"));
        tvDesc.setText(intent.getStringExtra("description"));

        if (intent.getStringExtra("imagePath") != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(intent.getStringExtra("imagePath"));

            if (bitmap != null) {
                noteImage.setImageBitmap(bitmap);
            }
        } else {
            noteImage.setImageDrawable(getResources().getDrawable(R.mipmap.note_icon));
        }

        switch (intent.getStringExtra("priority")) {
            case "LOW":
                setLowPriority(null);
                break;
            case "MEDIUM":
                setMediumPriority(null);
                break;
            case "HIGH":
                setHighPriority(null);
                break;
        }
    }

    public void setLowPriority(View view) {
        lowPriority.setText("★");
        mediumPriority.setText("☆");
        highPriority.setText("☆");
    }

    public void setMediumPriority(View view) {
        lowPriority.setText("★");
        mediumPriority.setText("★");
        highPriority.setText("☆");
    }

    public void setHighPriority(View view) {
        lowPriority.setText("★");
        mediumPriority.setText("★");
        highPriority.setText("★");
    }
}
