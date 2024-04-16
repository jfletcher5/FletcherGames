package com.example.mathgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Locale;
import java.util.Random;

public class GameMathGame extends AppCompatActivity {

    private static final int SETTINGS_REQUEST = 1;

    private TextView timerTextView;
    private TextView problemTextView;
    private TextView scoreTextView;
    private Button buttonAnswer1;
    private Button buttonAnswer2;
    private Button buttonAnswer3;

    private int numCorrectAnswers = 0;
    private int timeRemaining;
    private int bounds;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_math_game);

        timerTextView = findViewById(R.id.timerTextView);
        problemTextView = findViewById(R.id.problemTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        buttonAnswer1 = findViewById(R.id.buttonAnswer1);
        buttonAnswer2 = findViewById(R.id.buttonAnswer2);
        buttonAnswer3 = findViewById(R.id.buttonAnswer3);

        findViewById(R.id.closeButton).setOnClickListener(v -> finish());

        // Initialize the timer here
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = (int) (millisUntilFinished / 1000);
                updateTimerText(timeRemaining);
            }

            @Override
            public void onFinish() {
                endGame();
            }
        };

        Intent settingsIntent = new Intent(this, GameSettingsActivity.class);
        startActivityForResult(settingsIntent, SETTINGS_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTINGS_REQUEST && resultCode == RESULT_OK) {
            timeRemaining = data.getIntExtra("timer", 60);
            bounds = data.getIntExtra("bounds", 4);
            generateNewProblem();
            startTimer();
        }
    }

    public void generateNewProblem() {
        Random rand = new Random();
        int num1 = rand.nextInt(bounds) + 1;
        int num2 = rand.nextInt(bounds) + 1;
        String s = num1 + " + " + num2;
        problemTextView.setText(s);
        generateRandomAnswers(num1, num2);
    }

    private void generateRandomAnswers(int num1, int num2) {
        Random rand = new Random();
        int correctAnswer = num1 + num2;
        //generate 2 random answers that are not equal to correct answer
        int[] answers = new int[2];
        for (int i = 0; i < 2; i++) {
            int randomAnswer;
            do {
                randomAnswer = rand.nextInt(9) + 1;
            } while (randomAnswer == correctAnswer || randomAnswer == answers[0]);
            answers[i] = randomAnswer;
        }
        assignAnswersToButtons(answers, correctAnswer);
    }

    private void assignAnswersToButtons(int[] answers, int correctAnswer) {
        Random rand = new Random();
        int correctButton = rand.nextInt(3) + 1;
        switch (correctButton) {
            case 1:
                buttonAnswer1.setText(String.valueOf(correctAnswer));
                buttonAnswer2.setText(String.valueOf(answers[0]));
                buttonAnswer3.setText(String.valueOf(answers[1]));
                break;
            case 2:
                buttonAnswer1.setText(String.valueOf(answers[0]));
                buttonAnswer2.setText(String.valueOf(correctAnswer));
                buttonAnswer3.setText(String.valueOf(answers[1]));
                break;
            case 3:
                buttonAnswer1.setText(String.valueOf(answers[0]));
                buttonAnswer2.setText(String.valueOf(answers[1]));
                buttonAnswer3.setText(String.valueOf(correctAnswer));
                break;
        }
    }

    public void onAnswerButtonClick(View view) {
        Button button = (Button) view;
        int userAnswer = Integer.parseInt(button.getText().toString());
        checkAnswer(userAnswer);
    }

    private void checkAnswer(int userAnswer) {
        int num1 = Integer.parseInt(problemTextView.getText().toString().split(" ")[0]);
        int num2 = Integer.parseInt(problemTextView.getText().toString().split(" ")[2]);
        int correctAnswer = num1 + num2;

        if (userAnswer == correctAnswer) {
            numCorrectAnswers++;
            timeRemaining += 2;
            updateTimerText(timeRemaining);
            updateScoreText();
            generateNewProblem();
            problemTextView.setBackgroundColor(Color.GREEN);
            new Handler().postDelayed(() -> problemTextView.setBackgroundColor(Color.TRANSPARENT), 200);
            restartTimer();
        } else {
            problemTextView.setBackgroundColor(Color.RED);
            new Handler().postDelayed(() -> problemTextView.setBackgroundColor(Color.TRANSPARENT), 200);
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(timeRemaining * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = (int) (millisUntilFinished / 1000);
                updateTimerText(timeRemaining);
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }

    private void restartTimer() {
        timer.cancel();
        startTimer();
    }

    private void updateTimerText(int timeRemaining) {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerTextView.setText(String.format(Locale.getDefault(), "%d:%02d", minutes, seconds));
    }

    private void updateScoreText() {
        scoreTextView.setText("Score:\n");
        scoreTextView.append("1. " + numCorrectAnswers + "\n");
    }

    private void endGame() {
        timer.cancel();

        if (!isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle("Game Over")
                    .setMessage("You got " + numCorrectAnswers + " correct answers!")
                    .setPositiveButton("Start Over", (dialogInterface, i) -> {
                        numCorrectAnswers = 0;
                        timeRemaining = 60;
                        generateNewProblem();
                        timer.start();
                    })
                    .setCancelable(false)
                    .show();
        }

        displayScores(findViewById(R.id.scoresListTextView));
        numCorrectAnswers = 0;
        updateScoreText();
    }

    private void displayScores(View view) {
        EditText editText = (EditText) view;
        editText.setText("Scores:\n");
        editText.append("1. " + numCorrectAnswers + "\n");
    }

}