package com.tomvandesteene.dictionaryapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import stanford.androidlib.SimpleActivity;
import stanford.androidlib.SimpleList;

public class DictionaryActivity extends SimpleActivity {

    private Map<String, String> dictionary;
    private MediaPlayer mp;
    private int points;
    private List<String> words;


    private void readFileData(){
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.grewords));
        readFileHelper(scan);

        try {
            Scanner scan2 = new Scanner(openFileInput("added_words.txt"));
            readFileHelper(scan2);
        }catch (Exception e){
            //do nothing
        }
    }

    private void readFileHelper(Scanner scan){
        while (scan.hasNextLine()){
            //vrolijk		blij, gelukkig
            String line = scan.nextLine();
            String[] parts = line.split("\t");
            if (parts.length < 2) continue;
            dictionary.put(parts[0], parts[1]);
            words.add(parts[0]);
        }
    }

    //pick the word
    //pick 5 random answers
    //show all of that on screen
    private void chooseWords(){
        Random randy = new Random();
        int randomIndex = randy.nextInt(words.size());
        String theWord = words.get(randomIndex);
        String theDefn = dictionary.get(theWord);

        //pick 4 other (wrong) definitions at random
        List<String> defns = new ArrayList<>(dictionary.values());
        defns.remove(theDefn);
        Collections.shuffle(defns);
        defns = defns.subList(0, 4);
        defns.add(theDefn);
        Collections.shuffle(defns);

        $TV(R.id.the_word).setText(theWord);
        SimpleList.with(this).setItems(R.id.word_list, defns);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTraceLifecycle(true);

        //read file data and choose initial word to display
        dictionary = new HashMap<>();
        words = new ArrayList<>();
        readFileData();
        chooseWords();

        ListView list = $(R.id.word_list);
        points = 0;
        //SimpleList.with(this).setItems(list, dictionary.keySet());

        //this code runs when the user taps items in the list
        //java 8 feature
        list.setOnItemClickListener((parent, view, position, id) -> {

            //Look up definition and display it as a toast
            String defnClicked = parent.getItemAtPosition(position).toString();
            String theWord = $TV(R.id.the_word).getText().toString();
            String correctDefn = dictionary.get(theWord);
            if (defnClicked.equals(correctDefn)){
                points++;
                toast("AWESOME! Your points are: " + points);
            } else {
                points--;
                toast(":-( LOLOLOL duuuh! Your points are: " + points);
            }
            chooseWords();
            }
        );
        mp = MediaPlayer.create(this, R.raw.jeopardy);
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("points", points);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        points = savedInstanceState.getInt("points", /* default */ 0);
    }

    public void addAWordClick(View view){
        //go to the AddWordActivity
        Intent intent = new Intent(this, AddWordActivity.class);
        startActivity(intent);
    }
}
