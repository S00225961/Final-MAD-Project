package com.example.sequencegame;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class SeeHighScores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_high_scores);

        Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go back to play game
                Intent intent = new Intent(SeeHighScores.this, MainActivity.class);
                startActivity(intent);
            }
        });
        DBHandler dbHandler = new DBHandler(this.getApplicationContext());
        List<HighScore> highScores = dbHandler.getHighScores();
        // Add some HighScore objects to the list
        Collections.sort(highScores, (score1, score2) -> Integer.compare(score2.getPlayerScore(), score1.getPlayerScore()));

        // Take the top 5 scores
        List<HighScore> top5Scores = highScores.subList(0, Math.min(5, highScores.size()));
        List<String> top5ScoresFormatted = new ArrayList<>();
        for(int i = 0; i < top5Scores.size(); i++){
            top5ScoresFormatted.add("Name: " + top5Scores.get(i).getPlayerName() + " Score: " + String.valueOf(top5Scores.get(i).getPlayerScore()));
        }

        // Create an ArrayAdapter to bind data to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, top5ScoresFormatted);

        // Set the adapter to the ListView
        ListView topScoresListView = findViewById(R.id.topScoresListView);
        topScoresListView.setAdapter(adapter);


    }
}