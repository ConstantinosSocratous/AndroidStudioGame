package com.example.firstgame.States;

import android.graphics.Canvas;

import com.example.firstgame.Handler;

public abstract class State {

    protected Handler handler;

    public State(Handler handler){
        this.handler = handler;
    }

    public abstract void update();

    public abstract void draw(Canvas canvas);
}
