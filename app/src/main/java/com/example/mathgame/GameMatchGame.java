package com.example.mathgame;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GameMatchGame extends AppCompatActivity {

    private Button startButton;
    private TextView timerTextView;
    private final ImageButton[] cards = new ImageButton[8];
    private final int[] cardValues = new int[8];
    private final int[] cardOptions = {
            R.drawable.card_daddy,
            R.drawable.card_mommy,
            R.drawable.card_kalli,
            R.drawable.card_velos
    };
    private final long startTimeInMillis = Integer.MAX_VALUE;
    private long timeElapsed = 0;
    private ImageButton flippedCard = null;
    private ImageButton secondCard = null;
    private int matchedPairs = 0; // Add this line
    private CountDownTimer countUpTimer; // Make this a class variable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_match_game);

        startButton = findViewById(R.id.startButton);
        Button closeButton = findViewById(R.id.closeButton);
        timerTextView = findViewById(R.id.timerTextView);
        for (int i = 0; i < 8; i++) {
            String buttonID = "card" + (i + 1);
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            cards[i] = findViewById(resID);
            cards[i].setTag(i); // Set the tag to the index of the card
            cards[i].setEnabled(false); // Deactivate the cards
            cards[i].setOnClickListener(v -> flipCard((ImageButton) v));
        }

        startButton.setOnClickListener(v -> {
            assignCardValues();
            animateCards();
            startButton.setEnabled(false);
            startTimer();
        });

        closeButton.setOnClickListener(v -> finish());
    }

    private void animateCards() {
        int[] cardIds = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7, R.id.card8};
        for (int cardId : cardIds) {
            ImageButton card = findViewById(cardId);
            card.setVisibility(View.VISIBLE);
            card.setTranslationX(-1000f);
            card.setTranslationY(-1000f);
            card.animate().translationXBy(1000f).translationYBy(1000f).rotation(360).setDuration(400);
            card.setEnabled(true);
        }
    }

    private void assignCardValues() {
        List<Integer> cardOptionsList = new ArrayList<>();
        for (int cardOption : cardOptions) {
            cardOptionsList.add(cardOption);
            cardOptionsList.add(cardOption); // Add each card option twice
        }
        Collections.shuffle(cardOptionsList); // Shuffle the card options

        for (int i = 0; i < cards.length; i++) {
            cardValues[i] = cardOptionsList.get(i); // Assign a card option to each card
        }
    }

    private void startTimer() {
        countUpTimer = new CountDownTimer(startTimeInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeElapsed = startTimeInMillis - millisUntilFinished;
                int seconds = (int) (timeElapsed / 1000) % 60;
                int minutes = (int) ((timeElapsed / (1000 * 60)) % 60);
                int hours = (int) ((timeElapsed / (1000 * 60 * 60)) % 24);
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

                timerTextView.setText(timeLeftFormatted);
            }

            public void onFinish() {
                // The game is over
            }
        }.start();
    }

    private void flipCard(ImageButton card) {
        // Get the Vibrator service
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Check if the device has a vibrator
        if (vibrator != null && vibrator.hasVibrator()) {
            // Create a One-shot vibration
            // Vibration for 500 milliseconds with full amplitude (-1 means default amplitude)
            VibrationEffect effect = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                effect = VibrationEffect.createOneShot(100, 10);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(effect);
            }
        }

        if (flippedCard == null) {
            flippedCard = card;
            showCard(flippedCard);
        } else if (secondCard == null) {
            secondCard = card;
            showCard(secondCard);
            checkForMatch();
        }
    }
    private void showCard(ImageButton card) {
        int cardIndex = Integer.parseInt(card.getTag().toString());
        card.setImageResource(cardValues[cardIndex]); // Set the image resource to the card's assigned value

        // Create an animation to flip the card
        ObjectAnimator flip = ObjectAnimator.ofFloat(card, "rotationY", 0f, 180f);
        flip.setDuration(500);
        flip.start();
    }

    private void checkForMatch() {
        if (cardValues[Integer.parseInt(flippedCard.getTag().toString())] ==
                cardValues[Integer.parseInt(secondCard.getTag().toString())]) {
            // The cards match, so disable them
            flippedCard.setEnabled(false);
            secondCard.setEnabled(false);
            // Increment the number of matched pairs
            matchedPairs++;
            //log matched pair count
            System.out.println("Matched Pairs: " + matchedPairs + " out of " + cards.length / 2);
            // Check if the game is over
            if (matchedPairs == cards.length / 2) {
                countUpTimer.cancel(); // Stop the timer
                // Here you can add any other code to indicate that the game is over
                showGameOverDialog();
            }
            // Reset the flipped cards for the next turn
            flippedCard = null;
            secondCard = null;
        } else {
            // The cards don't match, so flip them back after a short delay
            flippedCard.setEnabled(false);
            secondCard.setEnabled(false);
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                flippedCard.setImageResource(R.drawable.card_back);
                secondCard.setImageResource(R.drawable.card_back);
                flippedCard.setEnabled(true);
                secondCard.setEnabled(true);
                // Reset the flipped cards for the next turn
                flippedCard = null;
                secondCard = null;
            }, 1000);
        }
    }
    private void showGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("The game is over. Your final time is: " + timerTextView.getText().toString());
        builder.setPositiveButton("Play again?", (dialog, which) -> resetGame());
        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void resetGame() {
        // Reset the game state
        for (ImageButton card : cards) {
            card.setEnabled(true);
            card.setImageResource(R.drawable.card_back);
        }
        assignCardValues();
        matchedPairs = 0;
        startButton.setEnabled(true);
    }
}