package com.example.firstgame.Firebase;


public class User {

    private String name;
    private int score=0, timesPlayed=0;

    public User(String name, int score, int timesPlayed){
        this.name = name;
        this.score = score;
        this.timesPlayed = timesPlayed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }

    public void setTimesPlayed(int times) {
        this.timesPlayed = times;
    }
}
