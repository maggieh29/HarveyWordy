package com.example.harveywordy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class AddWordScreen extends AppCompatActivity {

    EditText input;
    Button addButton;
    LinkedList<String> words = new LinkedList<>();
    TextView tv;
    Button back;


    View.OnClickListener backL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    };

    private DatabaseReference mDatabase;

    View.OnClickListener addWord = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            add();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word_screen);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Words");


        input = findViewById(R.id.addingInput);
        addButton = findViewById(R.id.addToDB);
        addButton.setOnClickListener(addWord);
        tv = findViewById(R.id.typeTV);
        back = findViewById(R.id.backButton);
        back.setOnClickListener(backL);



    }


    public void add(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String word = input.getText().toString().toLowerCase();
                boolean pass = true;

                if(word.length() > 5 || word.length() <= 4){
                    pass = false;
                    tv.setTextColor(getColor(R.color.purple));
                    Toast.makeText(getApplicationContext(), "The word must be five letters.", Toast.LENGTH_SHORT).show();
                }
                for(int i = 0; i < word.length(); i++){
                    Character c = word.charAt(i);
                    if(!Character.isAlphabetic(c)){
                        pass = false;
                        tv.setTextColor(getColor(R.color.purple));
                        Toast.makeText(getApplicationContext(), "The word must be letters only", Toast.LENGTH_SHORT).show();

                    }
                }

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                for (DataSnapshot d: dataSnapshot.getChildren()) {
                    String w = d.getValue().toString().toLowerCase();
                    if (w.equals(word)) {
                        pass = false;
                        tv.setTextColor(getColor(R.color.purple));
                        Toast.makeText(getApplicationContext(), "This word already exists in the base.", Toast.LENGTH_SHORT).show();

                    }
                }
                if(pass == true) {
                    String key = mDatabase.push().getKey();
                    mDatabase.child(key).setValue(word);
                    tv.setTextColor(getColor(R.color.black));
                    Toast.makeText(getApplicationContext(), "Word added!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

}