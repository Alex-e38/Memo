package com.example.bmrsqd.memo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
