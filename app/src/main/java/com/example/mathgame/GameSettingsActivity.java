package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GameSettingsActivity extends AppCompatActivity {

    private EditText timerInput;
    private int timerValue;
    private EditText boundsInput;
    private int boundsValue;
    Button timerInput1;
    Button timerInput2;
    Button timerInput3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        timerInput = findViewById(R.id.timerInput);
        boundsInput = findViewById(R.id.boundsInput);
        Button submitButton = findViewById(R.id.submitButton);
        timerInput1 = findViewById(R.id.timerInput1);
        timerInput2 = findViewById(R.id.timerInput2);
        timerInput3 = findViewById(R.id.timerInput3);

        submitButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("timer", timerValue);
            resultIntent.putExtra("bounds", Integer.parseInt(boundsInput.getText().toString()));
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        timerInput1.setOnClickListener(v -> {
            resetButtons();
            timerValue = 30;
            timerInput.setText("30");
            timerInput1.setEnabled(false);
            timerInput1.setBackgroundColor(Color.GRAY);
        });

        timerInput2.setOnClickListener(v -> {
            resetButtons();
            timerValue = 15;
            timerInput.setText("15");
            timerInput2.setEnabled(false);
            timerInput2.setBackgroundColor(Color.GRAY);
        });

        timerInput3.setOnClickListener(v -> {
            resetButtons();
            timerValue = 5;
            timerInput.setText("5");
            timerInput3.setEnabled(false);
            timerInput3.setBackgroundColor(Color.GRAY);
        });
    }

    private void resetButtons() {
        //set buttons back to primary button style
        timerInput1.setEnabled(true);
        timerInput1.setBackgroundColor(Color.parseColor("#6200EE"));
        timerInput2.setEnabled(true);
        timerInput2.setBackgroundColor(Color.parseColor("#6200EE"));
        timerInput3.setEnabled(true);
        timerInput3.setBackgroundColor(Color.parseColor("#6200EE"));
    }



}