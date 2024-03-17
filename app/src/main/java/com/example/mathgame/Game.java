package com.example.mathgame;

public class Game {
    private String name;
    private int imageResId;

    public Game(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getImageResourceId() {
        return imageResId;
    }
}
