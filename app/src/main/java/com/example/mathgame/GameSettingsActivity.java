package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.Color;

public class GameSettingsActivity extends AppCompatActivity {

    private EditText timerInput;
    private Button[] timerButtons = new Button[3];
    private int[] timerValues = {30, 15, 5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        timerInput = findViewById(R.id.timerInput);
        timerButtons[0] = findViewById(R.id.timerInput1);
        timerButtons[1] = findViewById(R.id.timerInput2);
        timerButtons[2] = findViewById(R.id.timerInput3);

        findViewById(R.id.submitButton).setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("timer", Integer.parseInt(timerInput.getText().toString()));
            resultIntent.putExtra("bounds", Integer.parseInt(((EditText) findViewById(R.id.boundsInput)).getText().toString()));
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        for (int i = 0; i < timerButtons.length; i++) {
            int finalI = i;
            timerButtons[i].setOnClickListener(v -> {
                resetButtons();
                timerInput.setText(String.valueOf(timerValues[finalI]));
                timerButtons[finalI].setEnabled(false);
                timerButtons[finalI].setBackgroundColor(Color.GRAY);
            });
        }
    }

    private void resetButtons() {
        for (Button timerButton : timerButtons) {
            timerButton.setEnabled(true);
            timerButton.setBackgroundColor(Color.parseColor("#6200EE"));
        }
    }
}