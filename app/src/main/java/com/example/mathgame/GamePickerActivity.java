package com.example.mathgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GamePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_picker);

        GridView gameGridView = findViewById(R.id.gameGridView);
        final List<Game> games = new ArrayList<>();
        games.add(new Game("Math Game", R.drawable.math_game_icon));
        games.add(new Game("Match Game", R.drawable.match_game_icon));
        // Add more games to the list here
        GameAdapter gameAdapter = new GameAdapter(this, games);
        gameGridView.setAdapter(gameAdapter);

        gameGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game clickedGame = games.get(position);
                if (clickedGame.getName().equals("Math Game")) {
                    Intent intent = new Intent(GamePickerActivity.this, GameMathGame.class);
                    startActivity(intent);
                }
                if (clickedGame.getName().equals("Match Game")) {
                    Intent intent = new Intent(GamePickerActivity.this, GameMatchGame.class);
                    startActivity(intent);
                }
                // Add more if statements here for other games
            }
        });
    }
}