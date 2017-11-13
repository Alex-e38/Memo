package com.example.bmrsqd.memo;

import android.os.Environment;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

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
        ordner = new File(Environment.getExternalStorageDirectory(), "Memo");
        if(!ordner.exists()) {
            ordner.mkdirs();
        }

        editText = (EditText)findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(editText.getText().length() >0){

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
}
