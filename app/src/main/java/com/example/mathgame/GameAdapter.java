package com.example.mathgame;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

public class GameAdapter extends BaseAdapter {
    private final Context context;
    private final List<Game> games;

    public GameAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for the game card if convertView is null
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.game_card, parent, false);
        }

        // Get the current game
        Game currentGame = (Game) getItem(position);

        // Get the CardView
        CardView cardView = convertView.findViewById(R.id.cardView);

        // Get the DisplayMetrics of the current screen
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        // Calculate the desired width and height
        int width = displayMetrics.widthPixels / 2; // Half of the screen width
        int height = displayMetrics.heightPixels / 3; // One third of the screen height

        // Set the dimensions to the CardView
        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        cardView.setLayoutParams(layoutParams);

        // Set the game image and name to the ImageView and TextView
        ImageView gameImageView = convertView.findViewById(R.id.gameImageView);
        gameImageView.setImageDrawable(context.getResources().getDrawable(currentGame.getImageResourceId()));
        TextView gameNameTextView = convertView.findViewById(R.id.gameNameTextView);
        gameNameTextView.setText(currentGame.getName());

        return convertView;
    }
}
