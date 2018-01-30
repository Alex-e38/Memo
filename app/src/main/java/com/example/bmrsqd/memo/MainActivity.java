package com.example.bmrsqd.memo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    Button btn;
    EditText editText;
    File ordner;
    File ordnerAudio;
    Button RecordBtn;
    Button PlayBtn;
    Button SaveBtn;
    MediaPlayer mPlayer;
    private MediaRecorder mRecorder;
    private String mFileName =null;
    private static final String LOG_TAG = "Record_log";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.removeItem(R.id.action_delete);
        menu.removeItem(R.id.action_share);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);


        int check = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (check == PackageManager.PERMISSION_DENIED) {

            Toast.makeText(getApplicationContext(), "Zugriff auf den Speicher wird benötigt.", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
        }



        ordner = new File(Environment.getExternalStorageDirectory(), "Memos");


        if (!ordner.exists()) {
            ordner.mkdirs();
        }


        editText = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int check = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (check == PackageManager.PERMISSION_DENIED) {

                    Toast.makeText(getApplicationContext(), "Zugriff auf den Speicher wird benötigt.", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
                }

                if (!ordner.exists()) {
                    ordner.mkdirs();
                }

                if (editText.getText().length() > 0) {

                    File notitzdatei = new File(ordner, "Text_" + System.currentTimeMillis() + ".txt");
                    try {
                        OutputStream outputStream = new FileOutputStream(notitzdatei);
                        outputStream.write(editText.getText().toString().getBytes());
                        outputStream.close();
                        editText.setText(null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Kein Text", Toast.LENGTH_SHORT).show();
                }

            }
        });




        RecordBtn = (Button) findViewById(R.id.button2);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/Memos/Audio/audio.mp4";

        PlayBtn = (Button) findViewById(R.id.button3);
        SaveBtn = (Button) findViewById(R.id.button4);
        ordnerAudio = new File(Environment.getExternalStorageDirectory(), "Memos/Audio");

        RecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (!ordnerAudio.exists()){
                    ordnerAudio.mkdirs();
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                    int check = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO);

                    if (check == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1025);
                    } else {
                        startRecording();
                    }



                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP){

                    int check = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO);
                    if (mRecorder!=null){

                        try {
                            stopRecording();
                        }catch(RuntimeException ex){
                            //kek
                        }
                    }


                }

                return false;
            }
        });

        RecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        PlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mPlayer!=null && mPlayer.isPlaying()){
                    stopPlaying();
                }

                startPlaying();

            }
        });


        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPlayer!=null && mPlayer.isPlaying()){
                    stopPlaying();
                }

                String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Memos/Audio/";
                File file = new File(dir, "audio.mp4");
                File file2 = new File(dir, "audio_"+System.currentTimeMillis()+".mp4");
                file.renameTo(file2);
            }
        });


    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //mRecorder.setAudioEncoder(MediaRecorder.getAudioSourceMax());
        mRecorder.setAudioEncodingBitRate(128000);
        mRecorder.setAudioSamplingRate(44100);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    class PlayButton extends android.support.v7.widget.AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (!ordner.exists()) {
            ordner.mkdirs();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_memo) {

            int check = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (check == PackageManager.PERMISSION_GRANTED) {

                if (ordner.listFiles().length > 0) {
                    startActivity(new Intent(MainActivity.this, ViewNotes_Activity.class));
                    //Intent startNewActivity = new Intent(this, ViewNotes_Activity.class);
                } else {
                    Toast.makeText(getApplicationContext(), "Keine Notizen", Toast.LENGTH_SHORT).show();
                }

                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Zugriff auf den Speicher wird benötigt.", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
            }

        }
        if (id == R.id.action_about) {
            Toast.makeText(getApplicationContext(), "Bimmer 4 Life", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
