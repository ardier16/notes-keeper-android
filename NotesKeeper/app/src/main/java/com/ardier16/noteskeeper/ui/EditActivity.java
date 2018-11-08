package com.ardier16.noteskeeper.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ardier16.noteskeeper.R;
import com.ardier16.noteskeeper.notes.NotePriority;

public class EditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    Intent intent;
    String currentPriority;

    EditText titleField;
    EditText descField;

    TextView tvAdd;
    TextView tvImage;
    TextView lowPriority;
    TextView mediumPriority;
    TextView highPriority;

    private int READ_EXTERNAL_STORAGE_PERMISSION;

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

            if (currentPriority != null) {
                setPriority(currentPriority);
            }
        }
    }

    private void setNoteData(Intent intent) {
        titleField.setText(intent.getStringExtra("title"));
        descField.setText(intent.getStringExtra("description"));

        if (intent.getStringExtra("imagePath") != null) {
            tvImage.setText(intent.getStringExtra("imagePath"));
        }

        if (currentPriority == null) {
            setPriority(intent.getStringExtra("priority"));
        } else {
            setPriority(currentPriority);
        }

        tvAdd.setText(R.string.edit_note);
    }

    public void setPriority(String priority) {
        switch (priority) {
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


    public void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), PICK_IMAGE);
    }

    public void chooseFromGallery(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    READ_EXTERNAL_STORAGE_PERMISSION);
        } else {
            openImageChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentPriority", getPriority());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            currentPriority = savedInstanceState.getString("currentPriority");
            setPriority(currentPriority);
        }
    }
}
