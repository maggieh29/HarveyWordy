package com.example.harveywordy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String randomWord;
    private DatabaseReference mDatabase;
    String currentWord;

    Button toAddPage;
    Button submit;
    Button clear;
    Button restart;
    GridLayout grid;


    int row = 0;
    int round = 0;
    LinkedList<Character> entry;
    LinkedList<Character> winner;
    LinkedList<Integer> cellState;



    View.OnClickListener changeScreen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), AddWordScreen.class);
            startActivity(intent);
        }
    };
    View.OnClickListener submitL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            entry.clear();
            checkWord();
            round++;

            Intent intent = new Intent(getApplicationContext(), FinalScreen.class);
            intent.putExtra("word", currentWord);
            if(testWin() == true){
                intent.putExtra("win", true);
                startActivity(intent);
            }
            if(testWin() == false && round == 6){
                intent.putExtra("win", false);
                startActivity(intent);
            }

            cellState.clear();
            showNext();

        }
    };
    View.OnClickListener clearL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearAll();
            round = 0;
        }
    };
    View.OnClickListener restartL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearAll();
            round = 0;
            setRandomWord();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toAddPage = findViewById(R.id.addWordButton);
        toAddPage.setOnClickListener(changeScreen);
        submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(submitL);
        clear = findViewById(R.id.clearButton);
        clear.setOnClickListener(clearL);
        restart = findViewById(R.id.restartButton);
        restart.setOnClickListener(restartL);

        grid = findViewById(R.id.gridLayout);
        entry = new LinkedList<>();
        winner = new LinkedList<>();
        cellState = new LinkedList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Words");

        currentWord = "";
        setRandomWord();
        hideAll();

    }

    public void setRandomWord(){
        //DataSnapshot temp = mDatabase.get().getResult();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalWords = (int) dataSnapshot.getChildrenCount();

                Random gen = new Random();


                int randomIndex = (gen.nextInt(totalWords));

                int currentIndex = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (currentIndex == randomIndex) {
                        randomWord = snapshot.getValue().toString();
                        storeWord(randomWord);
                        break;
                    }
                    currentIndex++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void storeWord(String word){
        currentWord = word;
        for(int i = 0; i < currentWord.length(); i++){
            winner.add(currentWord.charAt(i));
        }
    }


    public void checkWord(){
        String ent = "";

        for (int i = 0; i < 5; i++) {
           TextView textBox = null;
            switch(round){
                case 0:
                    textBox = (TextView) grid.getChildAt(i);
                    break;
                case 1:
                     textBox = (TextView) grid.getChildAt(i+5);
                     break;
                case 2:
                     textBox = (TextView) grid.getChildAt(i+10);
                    break;
                case 3:
                     textBox = (TextView) grid.getChildAt(i+15);
                    break;
                case 4:
                     textBox = (TextView) grid.getChildAt(i+20);
                    break;
                case 5:
                    textBox = (TextView) grid.getChildAt(i+25);
                    break;

            }
            if (textBox != null) {
                String input = textBox.getText().toString().toLowerCase();
                char add = input.charAt(0);
                if(Character.isAlphabetic(add)){
                 //if the char is a letter
                    entry.add(add);
                }else{
                    Toast.makeText(this, "Please only enter letters", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please fill all five letters", Toast.LENGTH_SHORT).show();

            }
        }
        loadCellState();
        colorRow();
    }

    public void loadCellState(){
        for (int i = 0; i < 5; i++){
            if(entry.get(i) == winner.get(i)){
                cellState.add(2);
            }else if(currentWord.contains(entry.get(i).toString())){
                cellState.add(1);
            }else{
                cellState.add(0);
            }
        }
    }

    public String getStateColor(int x){
        if(x == 2){
            return "green";
        }else if(x == 1){
            return "yellow";
        }
        return "grey";
    }

    public void colorRow(){
        for (int i = 0; i < 5; i++) {
            TextView textBox = null;
            String cellColor = getStateColor(cellState.get(i));
            switch (round) {
                case 0:
                    textBox = (TextView) grid.getChildAt(i);
                    textBox.setBackgroundColor(getColorByName(cellColor));
                    break;
                case 1:
                    textBox = (TextView) grid.getChildAt(i + 5);
                    textBox.setBackgroundColor(getColorByName(cellColor));
                    break;
                case 2:
                    textBox = (TextView) grid.getChildAt(i + 10);
                    textBox.setBackgroundColor(getColorByName(cellColor));
                    break;
                case 3:
                    textBox = (TextView) grid.getChildAt(i + 15);
                    textBox.setBackgroundColor(getColorByName(cellColor));
                    break;
                case 4:
                    textBox = (TextView) grid.getChildAt(i + 20);
                    textBox.setBackgroundColor(getColorByName(cellColor));
                    break;
                case 5:
                    textBox = (TextView) grid.getChildAt(i + 25);
                    textBox.setBackgroundColor(getColorByName(cellColor));
                    break;

            }

        }
    }
    private int getColorByName(String colorName) {
        switch (colorName) {
            case "green":
                return ContextCompat.getColor(this, R.color.green);
            case "yellow":
                return ContextCompat.getColor(this, R.color.yellow);
            case "grey":
                return ContextCompat.getColor(this, R.color.grey);
            default:
                return Color.TRANSPARENT;
        }
    }

    public void showNext(){
        for (int i = 0; i < 5; i++) {
            TextView textBox = null;
            switch (round) {
                case 0:
                    textBox = (TextView) grid.getChildAt(i);
                    break;
                case 1:
                    textBox = (TextView) grid.getChildAt(i + 5);
                    textBox.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    textBox = (TextView) grid.getChildAt(i + 10);
                    textBox.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    textBox = (TextView) grid.getChildAt(i + 15);
                    textBox.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    textBox = (TextView) grid.getChildAt(i + 20);
                    textBox.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    textBox = (TextView) grid.getChildAt(i + 25);
                    textBox.setVisibility(View.VISIBLE);
                    break;

            }
        }
    }

    public void hideAll(){
        for(int i = 5; i < 30; i++){
            TextView textBox = (TextView) grid.getChildAt(i);
            textBox.setVisibility(View.INVISIBLE);
        }
    }

    public void clearAll(){
        for(int i = 0; i < 35; i++){
            TextView textBox = (TextView) grid.getChildAt(i);
            if (textBox != null) {
                textBox.setBackgroundColor(getColor(R.color.white));
                textBox.setText("");
            }
        }
        hideAll();
    }

    public boolean testWin(){
        boolean win = true;
        for(int i = 0; i < 5; i++){
            if(cellState.get(i) == 1||cellState.get(i) == 0){
                win = false;
            }
        }
      return win;
    }

    }
