package com.example.mathgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

public class GameMathGame extends AppCompatActivity {

    private TextView timerTextView;
    private TextView problemTextView;
    private TextView scoreTextView;
    private Button buttonAnswer1;
    private Button buttonAnswer2;
    private Button buttonAnswer3;

    private int numCorrectAnswers = 0;
    private int timeRemaining = 60;
    private CountDownTimer timer;

    @SuppressLint("MissingInflatedId")
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


        // Add a click listener to the closeTextView
        TextView closeTextView = findViewById(R.id.closeButton);
        closeTextView.setOnClickListener(v -> finish());

        generateNewProblem();

        timer = new CountDownTimer(timeRemaining * 200, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = (int) (millisUntilFinished / 1000);
                updateTimerText();
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }

    public void generateNewProblem() {
        Random rand = new Random();
        int num1 = rand.nextInt(4) + 1;
        int num2 = rand.nextInt(4) + 1;
        problemTextView.setText(num1 + " + " + num2);
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
        String buttonText = button.getText().toString();
        try {
            int userAnswer = Integer.parseInt(buttonText);
            checkAnswer(userAnswer);
            //log user answer
            Log.d("useranswer", "User answer: " + userAnswer);
        } catch (NumberFormatException e) {
            // Log the error or show an error message to the user
            Log.e("useranswer", "Invalid number format: " + buttonText, e);
        }
    }

    private void checkAnswer(int userAnswer) {
        int num1 = Integer.parseInt(problemTextView.getText().toString().split(" ")[0]);
        int num2 = Integer.parseInt(problemTextView.getText().toString().split(" ")[2]);
        int correctAnswer = num1 + num2;

        if (userAnswer == correctAnswer) {
            numCorrectAnswers++;
            timeRemaining += 10;
            updateTimerText();
            updateScoreText();
            generateNewProblem();

            // flash green
            problemTextView.setBackgroundColor(Color.GREEN);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    problemTextView.setBackgroundColor(Color.TRANSPARENT);
                }
            }, 200);
        } else {
            // flash red
            problemTextView.setBackgroundColor(Color.RED);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    problemTextView.setBackgroundColor(Color.TRANSPARENT);
                }
            }, 200);
        }

    }

    private void updateTimerText() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        String timeText = String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
        timerTextView.setText(timeText);
    }

    private void updateScoreText() {
        // Create a SpannableStringBuilder
        SpannableStringBuilder ssb = new SpannableStringBuilder("Score: ");

        // Get the drawable
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.score_counter);

        // Set the size of the drawable to match the text size
        int textSize = (int) scoreTextView.getTextSize();
        drawable.setBounds(0, 0, textSize, textSize);

        // Add an ImageSpan for each correct answer
        for (int i = 0; i < numCorrectAnswers; i++) {
            ssb.append(" ");
            ssb.setSpan(new ImageSpan(drawable), ssb.length() - 1, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Set the text of the TextView
        scoreTextView.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    private void endGame() {
        timer.cancel();

        if (!isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle("Game Over")
                    .setMessage("You got " + numCorrectAnswers + " correct answers!")
                    .setPositiveButton("Start Over", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            numCorrectAnswers = 0;
                            timeRemaining = 60;
                            generateNewProblem();
                            timer.start();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

        displayScores(findViewById(R.id.scoresListTextView));
        numCorrectAnswers = 0;
        updateScoreText();
    }

    public void displayScores(View view) {
        view.setVisibility(View.GONE);
        TextView scoresListTextView = findViewById(R.id.scoresListTextView);
        scoresListTextView.setText("Your score: " + numCorrectAnswers);
        scoresListTextView.setVisibility(View.VISIBLE);
    }

}