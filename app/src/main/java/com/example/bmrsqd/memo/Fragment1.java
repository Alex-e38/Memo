package com.example.bmrsqd.memo;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Fragment1 extends Fragment {


    View contentView;


    ListView listView;

    ArrayList<File> dateienliste;
    ArrayList<String> texteliste;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        contentView = inflater.inflate(R.layout.fragment1_layout, null);


        return contentView;
    }

    private void arrayListSetup() {

        dateienliste = new ArrayList<>();
        texteliste = new ArrayList<>();

        File ordner = new File(Environment.getExternalStorageDirectory(), "Memos");

        dateienliste.addAll(Arrays.asList(ordner.listFiles()));
        Collections.sort(dateienliste);
        Collections.reverse(dateienliste);

        int dateicounter = 0;

        while (dateicounter < dateienliste.size()) {
            texteliste.add(getTextFromFile(dateienliste.get(dateicounter)));
            dateicounter++;
        }
    }


    public String getTextFromFile(File datei) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(datei));

            String currentRow;

            try {
                while ((currentRow = bufferedReader.readLine()) != null) {
                    stringBuilder.append(currentRow);
                    stringBuilder.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString().trim();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) contentView.findViewById(R.id.listView);

        arrayListSetup();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, texteliste);
        listView.setAdapter(arrayAdapter);


    }
}



