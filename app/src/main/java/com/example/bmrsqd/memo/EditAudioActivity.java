package com.example.bmrsqd.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class EditAudioActivity extends AppCompatActivity {

    String audioName;
    TextView label;
    File audioFile;
    Button playBtn;
    Button stopBtn;
    MediaPlayer mPlayer;
    boolean isDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_audio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        playBtn = (Button) findViewById(R.id.button5);
        stopBtn = (Button) findViewById(R.id.button6);
        label = (TextView) findViewById(R.id.textView);


        if (getIntent().hasExtra("EXTRA_AUDIO_FILE")) {

            audioFile = (File) getIntent().getExtras().get("EXTRA_AUDIO_FILE");
            audioName = audioFile.toString();
            label.setText(audioName);

        }

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getIntent().hasExtra("EXTRA_AUDIO_FILE")) {

                    audioFile = (File) getIntent().getExtras().get("EXTRA_AUDIO_FILE");
                    if (mPlayer!=null && mPlayer.isPlaying()){
                        stopPlaying();
                    }

                    startPlaying();

                }

            }
        });


        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPlayer!=null && mPlayer.isPlaying()) {
                    stopPlaying();
                }
            }
        });


    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(String.valueOf(audioFile));
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            //Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.removeItem(R.id.action_memo);
        menu.removeItem(R.id.action_about);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_delete) {
            deleteAudio();
            return true;
        }

        if (id == R.id.action_share) {
            share();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void share() {

        String sharePath=audioName;
        Uri uri = Uri.parse("file://"+sharePath);
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("audio/audio/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share"));
    }

    private void deleteAudio() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(EditAudioActivity.this);
        dialog.setTitle("Memo löschen?");
        dialog.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                audioFile.delete();
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
