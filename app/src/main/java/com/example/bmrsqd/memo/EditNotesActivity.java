package com.example.bmrsqd.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    boolean isDeleted;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.removeItem(R.id.action_memo);
        menu.removeItem(R.id.action_about);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText2 = (EditText) findViewById(R.id.editText2);

        if (getIntent().hasExtra("EXTRA_NOTE_TEXT") && getIntent().hasExtra("EXTRA_NOTE_FILE")) {

            notetext = getIntent().getStringExtra("EXTRA_NOTE_TEXT");
            notefile = (File) getIntent().getExtras().get("EXTRA_NOTE_FILE");


            editText2.setText(notetext);

            //set caret/cursor behind the text
            int textLenght = editText2.getText().length();
            editText2.setSelection(textLenght, textLenght);

        }
    }


    @Override
    protected void onPause() {

        if (editText2.getText().length() > 0 && !isDeleted) {

            try {
                OutputStream outputStream = new FileOutputStream(notefile);
                outputStream.write(editText2.getText().toString().getBytes());
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //set caret/cursor behind the text
            int textLenght = editText2.getText().length();
            editText2.setSelection(textLenght, textLenght);

        } else if (editText2.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Text kann nicht leer sein.", Toast.LENGTH_SHORT).show();
        }
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_delete) {
            deleteNote();
            return true;
        }

        if (id == R.id.action_share) {
            share();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void share() {

        String text=editText2.getText().toString();
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareSubject = "Memo:";
        String shareText = text;
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, "Share"));

    }

    private void deleteNote() { //delete memo function


        AlertDialog.Builder dialog = new AlertDialog.Builder(EditNotesActivity.this);
        dialog.setTitle("Memo löschen?");
        dialog.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                notefile.delete();
                Toast.makeText(getApplicationContext(), "Memo wurde gelöscht!", Toast.LENGTH_SHORT).show();
                isDeleted = true;
                finish();
            }
        });

        dialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //cancel
            }
        });

        dialog.show();
    }
}
