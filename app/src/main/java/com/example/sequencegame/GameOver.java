package com.example.sequencegame;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import java.util.List;


public class GameOver extends AppCompatActivity {

private DBHandler dbHandler;
private String score = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Retrieve the data from the Intent
        Intent intent = getIntent();
        Integer playerScore = intent.getIntExtra("playerScore", 0);
        TextView scoreView = findViewById(R.id.scoreView);
        scoreView.setText("Your score was: " + playerScore);
        score = playerScore.toString();
        //buttons
        Button playAgain = findViewById(R.id.playAgain);
        Button seeHighScores = findViewById(R.id.seeHighScores);
        EditText playerName = findViewById(R.id.enterPlayerName);
        dbHandler = new DBHandler(GameOver.this);

        //button click events
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go back to play game
                Intent intent = new Intent(GameOver.this, MainActivity.class);
                startActivity(intent);
            }
        });
        seeHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to high score view and use database to view scores
                String name = playerName.getText().toString();
                // save the new comment to the database
                dbHandler.addNewHighScore(name, score);
                //small bit of code to output everything in db to console
                Intent intent = new Intent(GameOver.this, SeeHighScores.class);
                startActivity(intent);
            }
        });
    }
}