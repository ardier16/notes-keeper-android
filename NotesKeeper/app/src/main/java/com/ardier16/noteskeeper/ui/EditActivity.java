package com.ardier16.noteskeeper.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ardier16.noteskeeper.R;

public class EditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    Intent intent;

    EditText titleField;
    EditText descField;

    TextView tvAdd;
    TextView tvImage;
    TextView lowPriority;
    TextView mediumPriority;
    TextView highPriority;

    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        findViews();
        intent = getIntent();
        setIntentData();
    }

    private void findViews() {
        titleField = findViewById(R.id.titleField);
        descField = findViewById(R.id.descField);

        tvAdd = findViewById(R.id.tvAdd);
        tvImage = findViewById(R.id.tvImage);
        lowPriority = findViewById(R.id.lowPriority);
        mediumPriority = findViewById(R.id.mediumPriority);
        highPriority = findViewById(R.id.highPriority);

        saveBtn = findViewById(R.id.btnSave);
    }

    private void setIntentData() {
        ActionBar actionBar = getSupportActionBar();

        if (intent.getExtras() != null) {
            setNoteData(intent);

            if (actionBar != null) {
                actionBar.setTitle(R.string.edit_note);
            }
        } else {
            if (actionBar != null) {
                actionBar.setTitle(R.string.add_note);
            }
        }
    }

    private void setNoteData(Intent intent) {
        titleField.setText(intent.getStringExtra("title"));
        descField.setText(intent.getStringExtra("description"));

        if (intent.getStringExtra("imagePath") != null) {
            tvImage.setText(intent.getStringExtra("imagePath"));
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

        tvAdd.setText(R.string.edit_note);
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


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public void addNote(View view) {
        if (!checkInput())
            return;

        Intent intent = new Intent();
        intent.putExtra("title", titleField.getText().toString());
        intent.putExtra("description", descField.getText().toString());
        intent.putExtra("priority", getPriority());

        if (!tvImage.getText().toString()
                .equals(getString(R.string.no_image))) {
            intent.putExtra("imagePath", tvImage.getText().toString());
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean checkInput() {
        if (titleField.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_title_error), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String getPriority() {
        if (highPriority.getText().toString().equals("★"))
            return "HIGH";
        if (mediumPriority.getText().toString().equals("★"))
            return "MEDIUM";
        return "LOW";
    }


    public void chooseFromGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && data != null) {
            String realPath;

            if (data.getData().getPath().contains("storage")) {
                realPath = data.getData().getPath().substring(5);
            } else {
                realPath = getRealPathFromURI(this, data.getData());
            }

            tvImage.setText(realPath);
        }
    }

    public String getRealPathFromURI(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();

        return filePath;
    }
}
