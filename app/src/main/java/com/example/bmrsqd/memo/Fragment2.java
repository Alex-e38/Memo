package com.example.bmrsqd.memo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


public class Fragment2 extends Fragment {

    View contentView2;

    ListView listView;

    ArrayList<File> dateienliste;
    ArrayList<String> audioliste;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contentView2 = inflater.inflate(R.layout.fragment2_layout, null);

        return contentView2;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) contentView2.findViewById(R.id.listView);

        arrayListSetup();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, audioliste);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent editIntent = new Intent(getActivity(), EditAudioActivity.class);
                editIntent.putExtra("EXTRA_AUDIO_FILE",dateienliste.get(i));
                startActivity(editIntent);

            }
        });
    }

    @Override
    public void onResume() {

        listView = (ListView) contentView2.findViewById(R.id.listView);

        arrayListSetup();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, audioliste);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent editIntent = new Intent(getActivity(), EditAudioActivity.class);
                editIntent.putExtra("EXTRA_AUDIO_FILE",dateienliste.get(i));
                startActivity(editIntent);

            }
        });

        super.onResume();
    }

    private void arrayListSetup() {

        dateienliste = new ArrayList<>();
        audioliste = new ArrayList<>();

        File ordner = new File(Environment.getExternalStorageDirectory(), "Memos/Audio");

        dateienliste.addAll(Arrays.asList(ordner.listFiles()));
        Collections.sort(dateienliste);
        Collections.reverse(dateienliste);


        int dateicounter = 0;

        while (dateicounter < dateienliste.size()){
            audioliste.add(getNameFromFile(dateienliste.get(dateicounter)));
            dateicounter++;

        }

    }

    public String getNameFromFile (File datei){

        String name = datei.getName().toString();

        return name;

    }
}



