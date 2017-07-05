package com.tomvandesteene.dictionaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import stanford.androidlib.SimpleActivity;

public class StartActivity extends SimpleActivity {

    private static final int REQ_CODE_ADD_WORD = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setTraceLifecycle(true);
    }
    public void playTheGameClick(View view){
        //go to the DictionaryActivity
        Intent intent = new Intent(this, DictionaryActivity.class);
        startActivity(intent);
    }
    public void addANewWordClick(View view){
        //go to the AddWordActivity
        Intent intent = new Intent(this, AddWordActivity.class);
        intent.putExtra("initialtext", "Woord");
        startActivityForResult(intent, REQ_CODE_ADD_WORD);

        //i'm back
    }
    //gets called when AddWordActivity finish()es and comes back to me
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQ_CODE_ADD_WORD){
            // we are coming back from AddWordActivity
            //Intent
            //-> "newword", "newdefn"
            String newWord = intent.getStringExtra("newword");
            String newDefn = intent.getStringExtra(("newdefn"));

            toast("You added the word: " + newWord);
        }
    }
}
