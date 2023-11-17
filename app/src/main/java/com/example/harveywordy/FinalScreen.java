package com.example.harveywordy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalScreen extends AppCompatActivity {

    TextView title;
    TextView word;
    Button back;

    View.OnClickListener backL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);
        title = findViewById(R.id.finalMessage);
        word = findViewById(R.id.winningWord);
        back = findViewById(R.id.backButt);
        back.setOnClickListener(backL);

        Intent i = getIntent();
        boolean win = i.getBooleanExtra("win", true);
        String b = i.getStringExtra("word");

        if(win == true){
            title.setText("You Win!");
        }else{
            title.setText("You Loose!");
        }

        word.setText("Solution: " + b);

    }
}