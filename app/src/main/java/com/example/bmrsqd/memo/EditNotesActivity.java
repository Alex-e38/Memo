package com.example.bmrsqd.memo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EditNotesActivity extends AppCompatActivity {

    String notetext;
    File notefile;

    EditText editText2;
    boolean isDeleted = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        editText2 = (EditText) findViewById(R.id.editText2);

        if (getIntent().hasExtra("EXTRA_NOTE_NEXT") && getIntent().hasExtra("EXTRA_NOTE_FILE")) {

            notetext = getIntent().getStringExtra("EXTRA_NOTE_NEXT");
            notefile = (File) getIntent().getExtras().get("EXTRA_NOTE_FILE");


            editText2.setText(notetext);

        }
    }

    @Override
    protected void onPause() {

        if (editText2.getText().length() > 0) {

            try {
                OutputStream outputStream = new FileOutputStream(notefile);
                outputStream.write(editText2.getText().toString().getBytes());
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Text kann nicht leer sein.", Toast.LENGTH_SHORT).show();
        }
        super.onPause();
    }
}
