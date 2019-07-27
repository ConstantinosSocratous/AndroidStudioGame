package com.example.firstgame.States;

import android.graphics.Canvas;

import com.example.firstgame.MyHandler;

public abstract class State {

    protected MyHandler myHandler;

    public State(MyHandler myHandler){
        this.myHandler = myHandler;
    }

    public abstract void update();

    public abstract void draw(Canvas canvas);
}
